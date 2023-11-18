package io.bkbn.sourdough.api.view

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDate
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

class ArticlesViewTest : DescribeSpec({
  describe("Markdown Processing") {
    it("can retrieve the metadata attached to a blog post") {
      // Arrange

      // language=md
      val src = """
        ---
        title: "Test Title"
        description: "Test Description"
        date: "2069-04-20"
        ---

        # Blah

        This is a test

        ## McBlah

        This is another test
      """.trimIndent()
      val flavour = GFMFlavourDescriptor()
      val rootNode = MarkdownParser(flavour).buildMarkdownTreeFromString(src)

      // Act
      val result = ArticlesView.retrieveBlogFrontMatter(rootNode, src)

      // Assert
      result.title shouldBe "Test Title"
      result.description shouldBe "Test Description"
      result.date shouldBe LocalDate(2069, 4, 20)
    }
  }
})
