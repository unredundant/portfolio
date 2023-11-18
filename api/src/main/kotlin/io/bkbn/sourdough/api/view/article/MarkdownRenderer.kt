package io.bkbn.sourdough.api.view.article

import kotlinx.html.BODY
import kotlinx.html.br
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.h5
import kotlinx.html.h6
import kotlinx.html.p
import org.intellij.markdown.MarkdownElementTypes.ATX_1
import org.intellij.markdown.MarkdownElementTypes.ATX_2
import org.intellij.markdown.MarkdownElementTypes.ATX_3
import org.intellij.markdown.MarkdownElementTypes.ATX_4
import org.intellij.markdown.MarkdownElementTypes.ATX_5
import org.intellij.markdown.MarkdownElementTypes.ATX_6
import org.intellij.markdown.MarkdownElementTypes.MARKDOWN_FILE
import org.intellij.markdown.MarkdownElementTypes.PARAGRAPH
import org.intellij.markdown.MarkdownTokenTypes.Companion.ATX_HEADER
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.CompositeASTNode
import org.intellij.markdown.ast.LeafASTNode
import org.intellij.markdown.ast.getTextInNode

object MarkdownRenderer {

  private const val POST_FRONT_MATTER_INDEX = 6

  /**
   * Generates HTML content based on the source code and ASTNode.
   *
   * @param content The original markdown content to be rendered.
   * @param rootNode The root ASTNode representing the abstract syntax tree of the source code.
   */
  context(BODY)
  fun generateHtml(content: String, rootNode: ASTNode) {

    require(rootNode.type == MARKDOWN_FILE) {
      "Root node must be a markdown file"
    }

    val childrenWithoutFrontMatter = rootNode.children.subList(POST_FRONT_MATTER_INDEX, rootNode.children.size)

    val trimmedChildren = childrenWithoutFrontMatter
      .dropWhile { it.type == EOL }
      .dropLastWhile { it.type == EOL }

    trimmedChildren.forEach {
      renderNodeFromContent(content, it)
    }
  }

  context(BODY)
  private fun renderNodeFromContent(content: String, node: ASTNode) {
    when (node) {
      is CompositeASTNode -> renderCompositeNode(content, node)
      is LeafASTNode -> renderLeaf(node)
      else -> error("Unhandled node type: ${node.type}")
    }
  }

  context(BODY)
  private fun renderLeaf(node: LeafASTNode) {
    when (node.type) {
      EOL -> br { }
      ATX_HEADER -> TODO()
      else -> error("Unhandled node type: ${node.type}")
    }
  }

  context(BODY)
  private fun renderCompositeNode(content: String, node: CompositeASTNode) {
    when (node.type) {
      ATX_1, ATX_2, ATX_3, ATX_4, ATX_5, ATX_6 -> renderHeader(content, node)
      PARAGRAPH -> renderParagraph(content, node)
      else -> error("Unhandled node type: ${node.type}")
    }
  }

  context(BODY)
  private fun renderHeader(content: String, node: CompositeASTNode) {
    val headerContent = node.children.last().getTextInNode(content).toString().trim()
    when (node.type) {
      ATX_1 -> h1 { +headerContent }
      ATX_2 -> h2 { +headerContent }
      ATX_3 -> h3 { +headerContent }
      ATX_4 -> h4 { +headerContent }
      ATX_5 -> h5 { +headerContent }
      ATX_6 -> h6 { +headerContent }
      else -> error("Unhandled node type: ${node.type}")
    }
  }

  context(BODY)
  private fun renderParagraph(content: String, node: CompositeASTNode) {
    val paragraphContent = node.getTextInNode(content).toString().trim()
    p {
      +paragraphContent
    }
  }
}
