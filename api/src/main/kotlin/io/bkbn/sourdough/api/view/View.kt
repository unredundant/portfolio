package io.bkbn.sourdough.api.view

import kotlinx.html.HTML

fun interface View {
  context(HTML)
  fun render()
}
