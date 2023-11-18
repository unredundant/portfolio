package io.bkbn.sourdough.api.view.project

import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.component.NavbarComponent
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p

object ProjectsView : View {
  context(HTML) override fun render() {
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
