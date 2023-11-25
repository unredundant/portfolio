package io.bkbn.sourdough.api.view

import io.bkbn.sourdough.api.model.SessionModels
import kotlinx.html.HTML

fun interface View {
  context(HTML)
  fun render(session: SessionModels.UserSession?)
}
