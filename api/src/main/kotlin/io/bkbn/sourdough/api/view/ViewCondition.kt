package io.bkbn.sourdough.api.view

sealed interface ViewCondition {
  data object Authenticated : ViewCondition
  data object Unauthenticated : ViewCondition
}
