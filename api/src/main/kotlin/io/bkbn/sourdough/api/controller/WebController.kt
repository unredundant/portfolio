package io.bkbn.sourdough.api.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.html.FlowContent
import kotlinx.html.HEAD
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.script
import kotlinx.html.title
import kotlinx.html.ul

object WebController {
  fun Route.webHandler() {
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

// TODO: Move these to utils

private fun FlowContent.htmxClick(path: String, target: String) {
  attributes["hx-post"] = path
  attributes["hx-trigger"] = "click"
  attributes["hx-swap"] = "outerHTML"
  attributes["hx-target"] = target
}

private fun HTML.configureHead() {
  head {
    title {
      +"Unredundant"
    }
    script {
      src = "/static/scripts/htmx.min.js"
    }
    // Base Styles
    link {
      rel = "stylesheet"
      href = "/static/styles/style.css"
      type = "text/css"
    }
    loadHighlightJS()
  }
}

private fun HEAD.loadHighlightJS() {
  script {
    src = "/static/scripts/highlight.min.js"
  }
  script {
    +"hljs.highlightAll();"
  }
  // HighlightJS Theme
  link {
    rel = "stylesheet"
    href = "/static/styles/xt256-highlight.css"
    type = "text/css"
  }
}
