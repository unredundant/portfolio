package io.bkbn.sourdough.api.view.page.article

import io.bkbn.sourdough.api.model.ArticleModels
import io.bkbn.sourdough.api.model.UserSession
import io.bkbn.sourdough.api.view.View
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.page.article.ArticleUtils.getPostMetadata
import io.bkbn.sourdough.api.view.page.article.ArticleUtils.markdownFlavour
import io.bkbn.sourdough.api.view.component.NavbarComponent
import io.bkbn.sourdough.client.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.runBlocking
import kotlinx.html.BODY
import java.io.File
import kotlinx.html.HTML
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import org.intellij.markdown.parser.MarkdownParser

class ArticleView(private val slug: String) : View {

  private val httpClient = HttpClientFactory.Default

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

  private fun loadBlogContent(slug: String): Pair<ArticleModels.ArticleMetadata, String> = runBlocking {
    // TODO: This is a hack to get around the fact that the static files aren't available in the jar
    // TODO: Eventually we'll want to move to a database for this
    val loadResource = httpClient.get("http://0.0.0.0:8080/static/posts/$slug.md")
    val content: String = loadResource.body()
    val metadata = getPostMetadata(slug, content)
    metadata to content
  }

  context(BODY)
  private fun renderDocument(content: String) {
    val rootNode = MarkdownParser(markdownFlavour).buildMarkdownTreeFromString(content)
    MarkdownRenderer.generateHtml(content, rootNode)
  }
}
