package io.bkbn.sourdough.api.view.page.article

import io.bkbn.sourdough.api.model.ArticleModels
import io.bkbn.sourdough.api.model.UserSession
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.page.article.ArticleUtils.getPostMetadata
import io.bkbn.sourdough.api.view.page.article.ArticleUtils.markdownFlavour
import io.bkbn.sourdough.api.view.component.NavbarComponent
import kotlinx.html.BODY
import java.io.File
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import org.intellij.markdown.parser.MarkdownParser

class ArticleView(private val slug: String) : View {
  context(HTML, UserSession) override fun render() {
    val (metadata, content) = loadBlogContent(slug)
    configureHead()
    body {
      div(classes = "container") {
        NavbarComponent()
        h1(classes = "title") {
          +metadata.frontMatter.title
        }
        renderDocument(content)
      }
    }
  }

  private fun loadBlogContent(slug: String): Pair<ArticleModels.ArticleMetadata, String> {
    val file = File(this.javaClass.getResource("/static/posts/$slug.md")?.toURI() ?: error("Post not found :("))
    val content = file.readText()
    val metadata = getPostMetadata(file)
    return metadata to content
  }

  context(BODY)
  private fun renderDocument(content: String) {
    val rootNode = MarkdownParser(markdownFlavour).buildMarkdownTreeFromString(content)
    MarkdownRenderer.generateHtml(content, rootNode)
  }
}
