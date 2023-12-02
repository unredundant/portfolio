package io.bkbn.sourdough.api

import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.core.routes.redoc
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.serialization.KompendiumSerializersModule
import io.bkbn.sourdough.api.controller.ViewController.viewHandler
import io.bkbn.sourdough.api.controller.api.AuthController.authHandler
import io.bkbn.sourdough.api.documentation.DocumentationUtils
import io.bkbn.sourdough.api.model.UserSession
import io.ktor.http.CacheControl
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.cachingheaders.CachingHeaders
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.Routing
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.sessions.SessionTransportTransformerEncrypt
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.util.hex
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import kotlin.reflect.typeOf
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

fun main() {
  embeddedServer(
    CIO,
    host = "0.0.0.0",
    port = 8080,
    module = Application::mainModule,
  ).start(wait = true)
}

@OptIn(ExperimentalSerializationApi::class)
private fun Application.mainModule() {
  install(CachingHeaders)
  install(CallLogging) {
    level = Level.DEBUG
  }
  install(ContentNegotiation) {
    json(Json {
      serializersModule = KompendiumSerializersModule.module
      encodeDefaults = true
      explicitNulls = false
      prettyPrint = true
    })
  }
  install(NotarizedApplication()) {
    spec = DocumentationUtils::applicationSpec
    customTypes = mapOf(
      typeOf<Instant>() to TypeDefinition("string", "date-time")
    )
  }
  install(Sessions) {
    // TODO: Move to env vars
    val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
    val secretSignKey = hex("6819b57a326945c1968f45236589")
    cookie<UserSession>("user_session") {
      cookie.path = "/"
      cookie.extensions["SameSite"] = "Strict"
      cookie.httpOnly = true
      transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
    }
  }
  routing {
    redoc(pageTitle = "Portfolio Backend Docs")
    staticResourceHandler()
    viewHandler()
    route("/api") {
      authHandler()
    }
  }
}

private fun Routing.staticResourceHandler() {
  staticResources("/static", "static") {
    cacheControl { url ->
      when {
        url.file.contains("static/images") || url.file.contains("static/fonts") -> {
          return@cacheControl listOf(CacheControl.MaxAge(365.days.toInt(DurationUnit.SECONDS)))
        }

        else -> {
          return@cacheControl emptyList()
        }
      }
    }
  }
}
