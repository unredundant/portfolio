package io.bkbn.sourdough.api.view.page.article

import io.bkbn.sourdough.api.model.ArticleModels
import io.bkbn.sourdough.api.model.UserSession
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.component.NavbarComponent
import io.bkbn.sourdough.api.view.page.article.ArticleUtils.getPostMetadata
import java.io.File
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr


object ArticlesView : View {
  context(HTML, UserSession) override fun render() {
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
              attributes["hx-get"] = "/articles/${post.slug}"
              attributes["hx-boost"] = "true"
              attributes["hx-push-url"] = "true"
              attributes["hx-swap"] = "outerHTML"
              attributes["hx-target"] = "body"
              td(classes = "blog-column") {
                p(classes = "blog-title") {
                  +post.frontMatter.title
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
//    val postDirectory = File(this.javaClass.getResource("/static/posts")?.toURI() ?: error("Posts not found :("))
//    return postDirectory.walk()
//      .filter { it.isFile }
//      .map { getPostMetadata(it) }
//      .toList()
    return emptyList()
  }
}
