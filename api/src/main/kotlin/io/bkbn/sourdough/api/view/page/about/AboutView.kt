package io.bkbn.sourdough.api.view.page.about

import io.bkbn.sourdough.api.model.SessionModels
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.component.NavbarComponent
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p

object AboutView : View {
  context(HTML)
  override fun render(session: SessionModels.UserSession?) {
    configureHead()
    body {
      div(classes = "container") {
        NavbarComponent()
        h1(classes = "title") {
          +"About Me"
        }
        p(classes = "subtitle") {
          +"""
                Don't worry about it
              """.trimIndent()
        }
      }
    }
  }
}
