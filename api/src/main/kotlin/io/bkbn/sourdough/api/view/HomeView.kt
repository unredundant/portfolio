package io.bkbn.sourdough.api.view

import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.ViewUtils.htmxClick
import io.bkbn.sourdough.api.view.component.NavbarComponent
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.p
import kotlinx.html.pre

object HomeView : View {
  context(Route) override fun render() {
    get("/") {
      call.respondHtml {
        configureHead()
        body {
          div(classes = "container") {
            NavbarComponent()
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
            div(classes = "centered-image-container") {
              img(classes = "centered-image rounded-corners") {
                src = "/static/images/bat-eating-banana.jpg"
                alt = "A bat eating a banana"
              }
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
