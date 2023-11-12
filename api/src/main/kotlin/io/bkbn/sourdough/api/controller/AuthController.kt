package io.bkbn.sourdough.api.controller

import io.bkbn.sourdough.api.model.AuthModels
import io.bkbn.sourdough.api.service.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

/**
 * Responsible for handling authentication-related routes.
 */
object AuthController {
  fun Route.authHandler() {
    route("/auth") {
      route("sign_in") {
        post {
          val body: AuthModels.SignInRequest = call.receive()
          val result = AuthService.executeSignInFlow(body)
          call.response.cookies.append(
            name = "auth_token",
            value = "${result.authToken}; HttpOnly; Path=/; Secure; SameSite=Strict"
          )
          call.respond(HttpStatusCode.NoContent)
        }
      }
      route("sign_up") {
        post {
          val body: AuthModels.SignUpRequest = call.receive()
          val result = AuthService.executeSignUpFlow(body)
          call.response.cookies.append(
            name = "auth_token",
            value = "${result.authToken}; HttpOnly; Path=/; Secure; SameSite=Strict"
          )
          call.respond(HttpStatusCode.NoContent)
        }
      }
    }
  }
}
