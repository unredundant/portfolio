package io.bkbn.sourdough.api.view.page.auth

import io.bkbn.sourdough.api.model.UserSession
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewCondition
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.component.NavbarComponent
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.label
import kotlinx.html.passwordInput
import kotlinx.html.submitInput
import kotlinx.html.textInput

object LoginView : View {

  override val conditions = listOf(ViewCondition.Unauthenticated)

  context(HTML, UserSession) override fun render() {
    configureHead()
    body {
      div(classes = "container") {
        NavbarComponent()
        h1(classes = "title") {
          +"Sign In"
        }
        form {
          attributes["method"] = "post"
          attributes["action"] = "/api/auth/sign_in"

          div {
            label {
              htmlFor = "username"
              +"Username:"
            }
            textInput {
              name = "username"
              id = "username"
              placeholder = "Enter Username"
            }
          }

          div {
            label {
              htmlFor = "password"
              +"Password:"
            }
            passwordInput {
              name = "password"
              id = "password"
              placeholder = "Enter Password"
            }
          }

          div {
            submitInput {
              value = "Login"
            }
          }
        }
      }
    }
  }
}
