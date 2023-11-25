package io.bkbn.sourdough.api.view.article

import io.bkbn.sourdough.api.util.MarkdownExamples.basicMarkdownExample
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.page.article.ArticleUtils
import io.bkbn.sourdough.api.view.page.article.MarkdownRenderer
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.html.body
import kotlinx.html.html
import kotlinx.html.stream.createHTML
import org.intellij.markdown.parser.MarkdownParser

class MarkdownRendererTest : DescribeSpec({
  describe("markdown rendering") {
    it("can render a simple markdown document") {
      // arrange
      val content = basicMarkdownExample
      val rootNode = MarkdownParser(ArticleUtils.markdownFlavour).buildMarkdownTreeFromString(content)

      // act
      val result = createHTML().html {
        configureHead()
        body {
          MarkdownRenderer.generateHtml(content, rootNode)
        }
      }

      // language=html
      result.trim() shouldBe """
        <html>
          <head>
            <title>Unredundant</title>
            <script src="/static/scripts/htmx.min.js"></script>
            <link rel="stylesheet" href="/static/styles/style.css" type="text/css">
            <script src="/static/scripts/highlight.min.js"></script>
            <script>hljs.highlightAll();</script>
            <link rel="stylesheet" href="/static/styles/xt256-highlight.css" type="text/css">
          </head>
          <body>
            <h1 class="subtitle">Blah</h1>
            <p class="blog-metadata">This is a test</p>
            <h2 class="subtitle">McBlah</h2>
            <p class="blog-metadata">This is another test</p>
          </body>
        </html>
      """.trimIndent()
    }
  }
})
