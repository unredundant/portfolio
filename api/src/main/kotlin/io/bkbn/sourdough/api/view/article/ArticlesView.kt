package io.bkbn.sourdough.api.view.article

import io.bkbn.sourdough.api.model.ArticleModels
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.article.ArticleUtils.getPostMetadata
import io.bkbn.sourdough.api.view.component.NavbarComponent
import java.io.File
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr


object ArticlesView : View {
  context(HTML) override fun render() {
    val postMetadata = collectPostMetadata().sortedBy { it.frontMatter.date }.reversed()
    configureHead()
    body {
      div(classes = "container") {
        NavbarComponent()
        h1(classes = "title") {
          +"Ramblings"
        }
        p(classes = "subtitle") {
          +"""
            You probably don't need to read these.
          """.trimIndent()
        }
        table {
          postMetadata.forEach { post ->
            tr {
              td(classes = "blog-column") {
                a(classes = "no-style", href = "/articles/${post.slug}") {
                  p(classes = "blog-title") {
                    +post.frontMatter.title
                  }
                }
              }
              td(classes = "blog-column") {
                p(classes = "blog-metadata") {
                  +post.frontMatter.date.toString()
                }
              }
              td(classes = "blog-column") {
                p(classes = "blog-metadata") {
                  +post.frontMatter.description
                }
              }
            }
          }
        }
      }
    }
  }

  private fun collectPostMetadata(): List<ArticleModels.ArticleMetadata> {
    val postDirectory = File(this.javaClass.getResource("/static/posts")?.toURI() ?: error("Posts not found :("))
    return postDirectory.walk()
      .filter { it.isFile }
      .map { getPostMetadata(it) }
      .toList()
  }
}
