package io.bkbn.sourdough.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object AuthModels {

  /**
   * Represents the response received from a PKCE code exchange request.
   *
   * @property authToken The authentication token associated with the identity.
   * @property identityId The unique identifier of the identity.
   * @property providerToken The provider token obtained during the code exchange process (optional).
   * @property providerRefreshToken The provider refresh token obtained during the code exchange process (optional).
   */
  @Serializable
  data class PkceCodeExchangeResponse(
    @SerialName("auth_token")
    val authToken: String,

    @SerialName("identity_id")
    val identityId: String,

    @SerialName("provider_token")
    val providerToken: String?,

    @SerialName("provider_refresh_token")
    val providerRefreshToken: String?,
  )

  /**
   * Represents a sign in request.
   * @property email The email of the user trying to sign in.
   * @property password The password of the user trying to sign in.
   */
  @Serializable
  data class SignInRequest(
    val email: String,
    val password: String,
  )

  /**
   * Represents a sign up request to create a new user account.
   *
   * @property email The email address of the user.
   * @property password The password for the user account.
   */
  @Serializable
  data class SignUpRequest(
    val email: String,
    val password: String,
  )
}
