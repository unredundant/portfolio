package io.bkbn.sourdough.api.controller

import io.bkbn.sourdough.api.view.AboutView
import io.bkbn.sourdough.api.view.ArticlesView
import io.bkbn.sourdough.api.view.HomeView
import io.bkbn.sourdough.api.view.ProjectsView
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.html.body
import kotlinx.html.h1

object WebController {
  fun Route.webHandler() {
    HomeView.render()
    AboutView.render()
    ArticlesView.render()
    ProjectsView.render()
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
}
