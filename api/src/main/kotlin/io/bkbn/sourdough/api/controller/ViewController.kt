package io.bkbn.sourdough.api.controller

import io.bkbn.sourdough.api.model.UserSession
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewCondition
import io.bkbn.sourdough.api.view.page.about.AboutView
import io.bkbn.sourdough.api.view.page.article.ArticleView
import io.bkbn.sourdough.api.view.page.article.ArticlesView
import io.bkbn.sourdough.api.view.page.HomeView
import io.bkbn.sourdough.api.view.page.auth.LoginView
import io.bkbn.sourdough.api.view.page.project.ProjectsView
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.body
import kotlinx.html.h1

object ViewController {
  fun Route.viewHandler() {
    route("/") {
      renderStaticRoute(HomeView)
      route("/about") {
        renderStaticRoute(AboutView)
      }
      route("/articles") {
        renderStaticRoute(ArticlesView)
        route("/{slug}") {
          renderDynamicRoute { ArticleView(it.call.parameters["slug"]!!) }
        }
      }
      route("/projects") {
        renderStaticRoute(ProjectsView)
      }
      route("/login") {
        renderStaticRoute(LoginView)
      }
    }
    post("/clicked") {
      call.respondHtml(HttpStatusCode.OK) {
        body {
          h1(classes = "title") {
            +"You clicked me!"
          }
        }
      }
    }
  }

  private fun Route.renderStaticRoute(view: View) = get {
    val userSession = call.sessions.get<UserSession>() ?: UserSession.EMPTY

    evaluateConditions(view, userSession)

    with(userSession) {
      call.respondHtml {
        view.render()
      }
    }
  }

  private fun Route.renderDynamicRoute(block: (PipelineContext<*, ApplicationCall>) -> View) {
    get {
      val userSession = call.sessions.get<UserSession>() ?: UserSession.EMPTY
      val view = block(this@get)

      evaluateConditions(view, userSession)

      with(userSession) {
        call.respondHtml {
          view.render()
        }
      }
    }
  }

  private suspend fun PipelineContext<*, ApplicationCall>.evaluateConditions(view: View, session: UserSession) {
    if (view.conditions.contains(ViewCondition.Unauthenticated) && session.authenticated) {
      call.respondRedirect("/")
    }

    if (view.conditions.contains(ViewCondition.Authenticated) && !session.authenticated) {
      call.respondRedirect("/login")
    }
  }
}
