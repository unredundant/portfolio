package io.bkbn.sourdough.api.view.component

import kotlinx.html.SectioningOrFlowContent
import kotlinx.html.li
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.ul

object NavbarComponent {

  context(SectioningOrFlowContent)
  operator fun invoke() {
    nav {
      ul {
        li {
          mapOf(
            "Home" to "/",
            "About" to "/about",
            "Articles" to "/articles",
            "Projects" to "/projects",
          ).forEach { (name, url) ->
            p(classes = "nav-link") {
              attributes["hx-get"] = url
              attributes["hx-push-url"] = "true"
              attributes["hx-swap"] = "outerHTML"
              attributes["hx-target"] = "body"
              +name
            }
          }
        }
      }
    }
  }
}
