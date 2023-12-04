package io.bkbn.sourdough.api.view.page

import io.bkbn.sourdough.api.model.UserSession
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.component.NavbarComponent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.img
import kotlinx.html.p
import kotlinx.html.span

object HomeView : View {
  context(HTML, UserSession) override fun render() {
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
          span {
            a(href = "https://www.youtube.com/watch?v=S8zhnXZdTFM", target = "_blank") {
              img(classes = "centered-image rounded-corners") {
                src = "/static/images/bat-eating-banana.jpg"
                alt = "A bat eating a banana"
              }
            }
          }
        }
      }
    }
  }
}
