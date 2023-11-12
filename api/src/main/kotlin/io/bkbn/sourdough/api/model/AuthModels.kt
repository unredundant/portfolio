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

}
