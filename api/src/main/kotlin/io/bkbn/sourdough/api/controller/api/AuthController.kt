package io.bkbn.sourdough.api.controller.api

import io.bkbn.sourdough.api.model.AuthModels
import io.bkbn.sourdough.api.model.SessionModels
import io.bkbn.sourdough.api.service.AuthService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.sessions.sessions
import io.ktor.util.pipeline.PipelineContext

/**
 * Responsible for handling authentication-related routes.
 */
object AuthController {
  fun Route.authHandler() {
    route("/auth") {
      route("sign_in") {
        post {
          val formData: String = call.receive()
          val body = AuthService.decodeFormData(formData)
          val result = AuthService.executeSignInFlow(body)
          setSessionAndRedirect(result.authToken)
        }
      }
      route("sign_up") {
        post {
          val body: AuthModels.AuthRequest = call.receive()
          val result = AuthService.executeSignUpFlow(body)
          setSessionAndRedirect(result.authToken)
        }
      }
    }
  }

  context(PipelineContext<*, ApplicationCall>)
  private suspend fun setSessionAndRedirect(authToken: String) {
    val session = SessionModels.UserSession(authToken = authToken)
    call.sessions.set("user_session", session)
    call.respondRedirect("/")
  }
}
