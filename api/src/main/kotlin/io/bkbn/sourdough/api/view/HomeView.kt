package io.bkbn.sourdough.api.view

import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.ViewUtils.htmxClick
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.ul

// PipelineContext<*, ApplicationCall>
object HomeView : View {
  context(Route) override fun render() {
    get("/") {
      call.respondHtml(HttpStatusCode.OK) {
        configureHead()
        body {
          div(classes = "container") {
            nav {
              ul {
                li {
                  mapOf(
                    "Home" to "/",
                    "About" to "/about",
                    "Articles" to "/articles",
                    "Projects" to "/projects",
                  ).forEach {
                    p(classes = "nav-link") {
                      +it.key
                    }
                  }
                }
              }
            }
            h1(classes = "title") {
              +"Unredundant"
            }
            p(classes = "subtitle") {
              +"I’m Ryan"
            }
            p(classes = "subtitle") {
              +"""
                Occasionally I write things, more often I code things.
              """.trimIndent()
            }
            div(classes = "code-block") {
              pre {
                code {
                  // language=kt
                  +"""
                  fun main() {
                    println("Hello World!")
                  }
                """.trimIndent()
                }
              }
            }
            div {
              id = "parent-div"
              +"This is the parent div"
            }
            div {
              button {
                htmxClick("/clicked", "#parent-div")
                +"Click me"
              }
            }
          }
        }
      }
    }
  }
}
