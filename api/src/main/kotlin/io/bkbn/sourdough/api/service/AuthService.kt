package io.bkbn.sourdough.api.service

import co.touchlab.kermit.Logger
import io.bkbn.sourdough.api.config.AuthConfig
import io.bkbn.sourdough.api.config.AuthConfig.EdgeDb.BASE_AUTH_EXTENSION_URL
import io.bkbn.sourdough.api.model.AuthModels
import io.bkbn.sourdough.client.HttpClientFactory
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random

/**
 * Responsible for handling authentication-related operations.
 */
object AuthService {

  private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
  private val httpClient = HttpClientFactory.Default

  /**
   * Decodes the provided form data and returns an instance of AuthModels.AuthRequest.
   *
   * @param formData The form data to be decoded. It should be in the format "username=value&password=value".
   * @return Payload containing the decoded username and password.
   */
  fun decodeFormData(formData: String): AuthModels.AuthRequest {
    val (username, password) = formData.split("&").map { it.split("=")[1] }
    val decodedUsername = java.net.URLDecoder.decode(username, "UTF-8")
    val decodedPassword = java.net.URLDecoder.decode(password, "UTF-8")
    return AuthModels.AuthRequest(decodedUsername, decodedPassword)
  }

  /**
   * Executes the sign-in flow using the provided parameters.
   *
   * @param request The sign-in request containing the user's email and password.
   * @return The PKCE code exchange response containing the authentication token.
   */
  suspend fun executeSignInFlow(
    request: AuthModels.AuthRequest,
  ): AuthModels.PkceCodeExchangeResponse {
    val (verifier, challenge) = generatePkceChallenge()

    val verificationRequest = AuthenticationRequest(
      email = request.email,
      password = request.password,
      challenge = challenge,
      provider = AuthConfig.EdgeDb.AUTH_PROVIDER_NAME
    )

    val verificationResponse = httpClient.post("$BASE_AUTH_EXTENSION_URL/authenticate") {
      setBody(verificationRequest)
    }

    when (verificationResponse.status.value) {
      HttpStatusCode.OK.value -> Logger.d { "Successfully verified user" }
      else -> error("Failed to verify user")
    }

    val body = verificationResponse.body<AuthenticationResponse>()
    return exchangePkceCodeForAuthToken(body.code, verifier)
  }

  /**
   * Executes the sign-up flow.
   *
   * @param request The sign-up request containing user credentials.
   * @return The response containing the PKCE code exchange data.
   */
  suspend fun executeSignUpFlow(
    request: AuthModels.AuthRequest
  ): AuthModels.PkceCodeExchangeResponse {
    val (verifier, challenge) = generatePkceChallenge()

    val registrationRequest = RegistrationRequest(
      email = request.email,
      password = request.password,
      challenge = challenge,
      provider = AuthConfig.EdgeDb.AUTH_PROVIDER_NAME,
      verifyUrl = "http://localhost:8080/auth/verify"
    )

    val registrationResponse = httpClient.post("$BASE_AUTH_EXTENSION_URL/register") {
      setBody(registrationRequest)
    }

    when (registrationResponse.status.value) {
      HttpStatusCode.Created.value -> Logger.d { "Successfully registered user" }
      else -> error("Failed to register user")
    }

    val body = registrationResponse.body<AuthenticationResponse>()
    return exchangePkceCodeForAuthToken(body.code, verifier)
  }

  /**
   * Generates a PKCE (Proof Key for Code Exchange) challenge.
   *
   * @return A Pair consisting of the verifier and challenge strings.
   */
  private fun generatePkceChallenge(): Pair<String, String> {
    val verifier = generateBase64EncodedVerifier()
    val challenge = generateBase64EncodedChallenge(verifier)
    return Pair(verifier, challenge)
  }

  /**
   * Exchanges a PKCE code and verifier pair for an authentication token.
   *
   * @param code           The PKCE code to submit.
   * @param verifier       The PKCE verifier to submit.
   * @return               The result of the Pkce code exchange.
   */
  private suspend fun exchangePkceCodeForAuthToken(
    code: String,
    verifier: String
  ): AuthModels.PkceCodeExchangeResponse {
    Logger.d { "Exchanging PKCE code for auth token" }
    val response = httpClient.get("$BASE_AUTH_EXTENSION_URL/token") {
      url {
        parameters.append("code", code)
        parameters.append("verifier", verifier)
      }
    }

    when (response.status.value) {
      HttpStatusCode.OK.value -> Logger.d { "Successfully exchanged PKCE code for auth token" }
      else -> error("Failed to exchange PKCE code for auth token")
    }

    return response.body()
  }

  @OptIn(ExperimentalEncodingApi::class)
  private fun generateBase64EncodedVerifier(): String {
    val verifier = generateRandomPkceCompliantVerifier()
    Logger.d { "Raw Verifier: $verifier" }
    val encodedVerifier = Base64.UrlSafe.encode(verifier.toByteArray())
    return encodedVerifier.replace("=", "").trim()
  }

  @OptIn(ExperimentalEncodingApi::class)
  private fun generateBase64EncodedChallenge(verifier: String): String {
    val challenge = verifier.sha256()
    val encodedChallenge = Base64.UrlSafe.encode(challenge)
    Logger.d { "Encoded Challenge: $encodedChallenge" }
    return encodedChallenge.replace("=", "").trim()
  }

  private fun String.sha256(): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(this.toByteArray())
  }

  @Suppress("MagicNumber")
  private fun generateRandomPkceCompliantVerifier(): String = (1..32)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")
    .trim()

  @Serializable
  private data class AuthenticationRequest(
    val email: String,
    val password: String,
    val challenge: String,
    val provider: String
  )

  @Serializable
  private data class RegistrationRequest(
    val email: String,
    val password: String,
    val challenge: String,
    val provider: String,
    @SerialName("verify_url")
    val verifyUrl: String
  )

  @Serializable
  private data class AuthenticationResponse(
    val code: String
  )
}
