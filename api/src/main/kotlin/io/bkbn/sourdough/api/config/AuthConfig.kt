package io.bkbn.sourdough.api.config

object AuthConfig {
  object EdgeDb {
    private const val BASE_HTTP_URL = "http://localhost:10700"
    const val BASE_AUTH_EXTENSION_URL = "$BASE_HTTP_URL/db/edgedb/ext/auth"
  }
}
