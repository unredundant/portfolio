package io.bkbn.sourdough.api

import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.core.routes.redoc
import io.bkbn.kompendium.json.schema.definition.TypeDefinition
import io.bkbn.kompendium.oas.serialization.KompendiumSerializersModule
import io.bkbn.sourdough.api.controller.api.AuthController.authHandler
import io.bkbn.sourdough.api.controller.ViewController.viewHandler
import io.bkbn.sourdough.api.documentation.DocumentationUtils
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.event.Level
import kotlin.reflect.typeOf

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
  routing {
    redoc(pageTitle = "Portfolio Backend Docs")
    staticResources("/static", "static")
    viewHandler()
    route("/api") {
      authHandler()
    }
  }
}
