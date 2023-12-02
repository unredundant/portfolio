package io.bkbn.sourdough.api.controller

import io.bkbn.sourdough.api.model.UserSession
import io.bkbn.sourdough.api.view.View
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
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.util.pipeline.PipelineContext
import kotlinx.coroutines.withContext
import kotlinx.html.body
import kotlinx.html.h1

object ViewController {
  fun Route.viewHandler() {
    route("/") {
      renderStaticView(HomeView)
      route("/about") {
        renderStaticView(AboutView)
      }
      route("/articles") {
        renderStaticView(ArticlesView)
        route("/{slug}") {
          renderDynamicView { ArticleView(it.call.parameters["slug"]!!) }
        }
      }
      route("/projects") {
        renderStaticView(ProjectsView)
      }
      route("/login") {
        renderStaticView(LoginView)
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

  // TODO: Rethink term "static" vs "dynamic" here... it's not quite right
  private fun Route.renderStaticView(view: View) = get {
    val userSession = call.sessions.get<UserSession>() ?: UserSession.EMPTY
    with(userSession) {
      call.respondHtml {
        view.render()
      }
    }
  }

  private fun Route.renderDynamicView(block: (PipelineContext<*, ApplicationCall>) -> View) {
    get {
      val userSession = call.sessions.get<UserSession>() ?: UserSession.EMPTY
      with(userSession) {
        call.respondHtml {
          block(this@get).render()
        }
      }
    }
  }
}
