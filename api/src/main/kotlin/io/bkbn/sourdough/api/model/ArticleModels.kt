package io.bkbn.sourdough.api.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

object ArticleModels {

  @Serializable
  data class ArticleFrontMatter(
    val date: LocalDate,
    val title: String,
    val description: String,
  )

  @Serializable
  data class ArticleMetadata(
    val frontMatter: ArticleFrontMatter,
    val slug: String,
  )

}
