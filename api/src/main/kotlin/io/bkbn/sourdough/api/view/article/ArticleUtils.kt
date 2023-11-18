package io.bkbn.sourdough.api.view.article

import io.bkbn.sourdough.api.model.ArticleModels
import java.io.File
import net.mamoe.yamlkt.Yaml
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser

internal object ArticleUtils {

  val markdownFlavour = GFMFlavourDescriptor()

  fun getPostMetadata(file: File): ArticleModels.ArticleMetadata {
    val rootNode = MarkdownParser(markdownFlavour).buildMarkdownTreeFromString(file.readText())
    return ArticleModels.ArticleMetadata(
      retrieveBlogFrontMatter(rootNode, file.readText()),
      file.name.removeSuffix(".md")
    )
  }

  fun retrieveBlogFrontMatter(rootNode: ASTNode, src: String): ArticleModels.ArticleFrontMatter {
    require(rootNode.type == MarkdownElementTypes.MARKDOWN_FILE) { "Root node must be a Markdown file" }

    val frontMatter = rootNode.children.getOrNull(2) ?: error("No front matter found")

    require(frontMatter.type == MarkdownElementTypes.PARAGRAPH) { "Front matter must be a paragraph" }

    val frontMatterText = frontMatter.getTextInNode(src).toString()

    return Yaml.Default.decodeFromString(ArticleModels.ArticleFrontMatter.serializer(), frontMatterText)
  }
}
