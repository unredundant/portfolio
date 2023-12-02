package io.bkbn.sourdough.api.model

class UserSession {

  val authenticated: Boolean
  val authToken: String?

  private constructor() {
    this.authenticated = false
    this.authToken = null
  }

  constructor(authToken: String?) {
    this.authenticated = true
    this.authToken = authToken
  }

  companion object {
    val EMPTY: UserSession = UserSession()
  }
}
