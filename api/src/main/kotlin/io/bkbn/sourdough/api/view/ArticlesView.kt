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
import kotlinx.html.onLoad
import kotlinx.html.p

object ArticlesView : View {
  context(Route) override fun render() {
    get("/articles") {
      call.respondHtml {
        configureHead()
        body {
          onLoad = "hljs.highlightAll();"
          div(classes = "container") {
            NavbarComponent()
            h1(classes = "title") {
              +"Articles"
            }
            p(classes = "subtitle") {
              +"I’m Ryan"
            }
            p(classes = "subtitle") {
              +"""
                Occasionally I write things, more often I code things.
              """.trimIndent()
            }
          }
        }
      }
    }
  }
}
