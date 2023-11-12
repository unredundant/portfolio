package io.bkbn.sourdough.api.service

import co.touchlab.kermit.Logger
import io.bkbn.sourdough.api.config.AuthConfig.EdgeDb.BASE_AUTH_EXTENSION_URL
import io.bkbn.sourdough.api.model.AuthModels
import io.bkbn.sourdough.client.HttpClientFactory
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import java.security.MessageDigest
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random

object AuthService {

  private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
  private val httpClient = HttpClientFactory.Default

  /**
   * Generates a PKCE (Proof Key for Code Exchange) challenge.
   *
   * @return A Pair consisting of the verifier and challenge strings.
   */
  fun generatePkceChallenge(): Pair<String, String> {
    val verifier = generateBase64EncodedVerifier()
    val challenge = generateBase64EncodedChallenge(verifier)
    return Pair(verifier, challenge)
  }

  /**
   * Converts a PKCE code to an authentication token.
   *
   * @param code           The PKCE code to convert.
   * @param verifierCookie The verifier cookie used to extract the verifier.
   * @return               The result of the Pkce code exchange.
   */
  suspend fun convertPkceCodeToAuthToken(code: String, verifierCookie: String): AuthModels.PkceCodeExchangeResponse {
    Logger.d { "Exchanging PKCE code for auth token" }
    val verifier = verifierCookie.extractVerifierFromCookieString()
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

  private fun String.extractVerifierFromCookieString(): String {
    val tokenPattern = "^[^;]+".toRegex()
    return tokenPattern.find(this)?.value ?: error("No token found")
  }
}
