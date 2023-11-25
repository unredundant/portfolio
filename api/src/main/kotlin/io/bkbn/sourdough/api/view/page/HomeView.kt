package io.bkbn.sourdough.api.view.page

import io.bkbn.sourdough.api.model.SessionModels
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.ViewUtils.htmxClick
import io.bkbn.sourdough.api.view.component.NavbarComponent
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.p

object HomeView : View {
  context(HTML) override fun render(session: SessionModels.UserSession?) {
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

        if (session?.authToken != null) {
          p(classes = "subtitle") {
            +"You are logged in!"
          }
        } else {
          p(classes = "subtitle") {
            +"You are not logged in"
          }
        }

        p(classes = "subtitle") {
          +"""
                Occasionally I write things, more often I code things.
              """.trimIndent()
        }
        div(classes = "centered-image-container") {
          a(href = "https://www.youtube.com/watch?v=S8zhnXZdTFM", target = "_blank") {
            img(classes = "centered-image rounded-corners") {
              src = "/static/images/bat-eating-banana.jpg"
              alt = "A bat eating a banana"
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
