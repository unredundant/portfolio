package io.bkbn.sourdough.api.config

object AuthConfig {
  object EdgeDb {
    private const val BASE_HTTP_URL = "http://localhost:10701"
    const val AUTH_PROVIDER_NAME = "builtin::local_emailpassword" // Needs to be configurable?
    const val BASE_AUTH_EXTENSION_URL = "$BASE_HTTP_URL/db/edgedb/ext/auth"
  }
}
