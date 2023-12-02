package io.bkbn.sourdough.api.view

import io.bkbn.sourdough.api.model.UserSession
import kotlinx.html.HTML

fun interface View {
  context(HTML, UserSession)
  fun render()
}
