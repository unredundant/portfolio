package io.bkbn.sourdough.api.view

import io.ktor.server.routing.Route

fun interface View {
  context(Route)
  fun render()
}
