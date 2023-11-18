package io.bkbn.sourdough.api.view.article

import io.bkbn.sourdough.api.model.ArticleModels
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.article.ArticleUtils.getPostMetadata
import io.bkbn.sourdough.api.view.article.ArticleUtils.markdownFlavour
import io.bkbn.sourdough.api.view.component.NavbarComponent
import java.io.File
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.unsafe
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser

class ArticleView(private val slug: String) : View {
  context(HTML) override fun render() {
    val (metadata, content) = loadBlogContent(slug)
    configureHead()
    body {
      div(classes = "container") {
        NavbarComponent()
        h1(classes = "title") {
          +metadata.frontMatter.title
        }
        unsafe {
          raw(content)
        }
      }
    }
  }

  private fun loadBlogContent(slug: String): Pair<ArticleModels.ArticleMetadata, String> {
    val file = File(this.javaClass.getResource("/static/posts/$slug.md")?.toURI() ?: error("Post not found :("))
    val metadata = getPostMetadata(file)
    val rootNode = MarkdownParser(markdownFlavour).buildMarkdownTreeFromString(file.readText())
    val content = HtmlGenerator(file.readText(), rootNode, markdownFlavour).generateHtml()
    return metadata to content
  }
}
