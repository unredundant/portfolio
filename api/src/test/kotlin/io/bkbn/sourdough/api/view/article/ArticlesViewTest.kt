package io.bkbn.sourdough.api.view.article

import io.bkbn.sourdough.api.util.MarkdownExamples.basicMarkdownExample
import io.bkbn.sourdough.api.view.article.ArticleUtils.markdownFlavour
import io.bkbn.sourdough.api.view.article.ArticleUtils.retrieveBlogFrontMatter
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

class ArticlesViewTest : DescribeSpec({
  describe("Markdown Processing") {
    it("can retrieve the metadata attached to a blog post") {
      // Arrange
      val content = basicMarkdownExample
      val rootNode = MarkdownParser(markdownFlavour).buildMarkdownTreeFromString(content)

      // Act
      val result = retrieveBlogFrontMatter(rootNode, content)

      // Assert
      result.title shouldBe "Test Title"
      result.description shouldBe "Test Description"
      result.date shouldBe LocalDate(2069, 4, 20)
    }
  }
})
