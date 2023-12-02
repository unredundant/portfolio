package io.bkbn.sourdough.api.view

import io.bkbn.sourdough.api.model.UserSession
import kotlinx.html.HTML

interface View {
  context(HTML, UserSession)
  fun render()

  val conditions: List<ViewCondition>
    get() = emptyList()
}
