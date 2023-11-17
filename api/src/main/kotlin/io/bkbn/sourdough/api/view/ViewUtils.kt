package io.bkbn.sourdough.api.view

import kotlinx.html.FlowContent
import kotlinx.html.HEAD
import kotlinx.html.HTML
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.script
import kotlinx.html.title

object ViewUtils {

  fun FlowContent.htmxClick(path: String, target: String) {
    attributes["hx-post"] = path
    attributes["hx-trigger"] = "click"
    attributes["hx-swap"] = "outerHTML"
    attributes["hx-target"] = target
  }

  fun HTML.configureHead() {
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

  fun HEAD.loadHighlightJS() {
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

}
