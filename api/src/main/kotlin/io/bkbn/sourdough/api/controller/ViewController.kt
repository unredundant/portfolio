package io.bkbn.sourdough.api.controller

import io.bkbn.sourdough.api.view.about.AboutView
import io.bkbn.sourdough.api.view.article.ArticleView
import io.bkbn.sourdough.api.view.article.ArticlesView
import io.bkbn.sourdough.api.view.HomeView
import io.bkbn.sourdough.api.view.project.ProjectsView
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.html.body
import kotlinx.html.h1

object ViewController {
  fun Route.viewHandler() {
    route("/") {
      get { call.respondHtml { HomeView.render() } }
      route("/about") {
        get { call.respondHtml { AboutView.render() } }
      }
      route("/articles") {
        get { call.respondHtml { ArticlesView.render() } }
        route("/{slug}") {
          get { call.respondHtml { ArticleView(call.parameters["slug"]!!).render() } }
        }
      }
      route("/projects") {
        get { call.respondHtml { ProjectsView.render() } }
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
}
