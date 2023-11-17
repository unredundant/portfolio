package io.bkbn.sourdough.api.view

import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.component.NavbarComponent
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p

object ProjectsView : View {
  context(Route) override fun render() {
    get("/projects") {
      call.respondHtml {
        configureHead()
        body {
          div(classes = "container") {
            NavbarComponent()
            h1(classes = "title") {
              +"Projects"
            }
            p(classes = "subtitle") {
              +"""
                Wee little curiosities
              """.trimIndent()
            }
          }
        }
      }
    }
  }
}
