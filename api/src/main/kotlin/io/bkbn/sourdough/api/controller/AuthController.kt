package io.bkbn.sourdough.api.controller

import io.bkbn.sourdough.api.config.AuthConfig.EdgeDb.BASE_AUTH_EXTENSION_URL
import io.bkbn.sourdough.api.service.AuthService
import io.ktor.server.application.call
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import java.lang.IllegalStateException

/**
 * AuthController is responsible for handling authentication-related routes.
 */
object AuthController {
  fun Route.authHandler() {
    route("/auth") {
      route("/ui") {
        route("sign_in") {
          get {
            val (verifier, challenge) = AuthService.generatePkceChallenge()
            val redirectUrl = "$BASE_AUTH_EXTENSION_URL/ui/signin"
            val redirectUrlWithChallenge = "$redirectUrl?challenge=$challenge"
            call.response.cookies.append("pkce-verifier", "$verifier; Path=/; HttpOnly")
            call.respondRedirect(redirectUrlWithChallenge)
          }
        }
        route("/callback") {
          route("/sign_in") {
            get {
              val code = call.parameters["code"] ?: error("No code provided")
              val verifier = call.request.cookies["pkce-verifier"] ?: error("No verifier provided")
              val result = AuthService.convertPkceCodeToAuthToken(code, verifier)
              call.response.cookies.append("auth-token", "${result.authToken}; Path=/; HttpOnly")
              call.respondRedirect("http://localhost:3000")
            }
          }
          route("/sign_up") {
            get {
              val code = call.parameters["code"] ?: error("No code provided")
              val verifier = call.request.cookies["pkce-verifier"] ?: error("No verifier provided")
              val result = AuthService.convertPkceCodeToAuthToken(code, verifier)
              call.response.cookies.append("auth-token", "${result.authToken}; Path=/; HttpOnly")
              call.respondRedirect("http://localhost:3000")
            }
          }
        }
      }
    }
  }
}
