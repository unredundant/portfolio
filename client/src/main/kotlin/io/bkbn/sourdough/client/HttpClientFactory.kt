package io.bkbn.sourdough.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import co.touchlab.kermit.Logger as KermitLogger

object HttpClientFactory {

  val Default = HttpClient(CIO) {
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
        isLenient = true
        encodeDefaults = true
      })
    }
    defaultRequest {
      contentType(ContentType.Application.Json)
    }
    install(Logging) {
      logger = object : Logger {
        override fun log(message: String) {
          KermitLogger.d { message }
        }
      }
      level = LogLevel.INFO
    }
  }
}
