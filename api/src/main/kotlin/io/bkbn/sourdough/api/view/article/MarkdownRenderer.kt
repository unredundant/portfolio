package io.bkbn.sourdough.api.view.article

import kotlinx.html.BODY
import kotlinx.html.blockQuote
import kotlinx.html.br
import kotlinx.html.classes
import kotlinx.html.code
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.h4
import kotlinx.html.h5
import kotlinx.html.h6
import kotlinx.html.p
import kotlinx.html.pre
import kotlinx.html.unsafe
import org.intellij.markdown.MarkdownElementTypes.ATX_1
import org.intellij.markdown.MarkdownElementTypes.ATX_2
import org.intellij.markdown.MarkdownElementTypes.ATX_3
import org.intellij.markdown.MarkdownElementTypes.ATX_4
import org.intellij.markdown.MarkdownElementTypes.ATX_5
import org.intellij.markdown.MarkdownElementTypes.ATX_6
import org.intellij.markdown.MarkdownElementTypes.BLOCK_QUOTE
import org.intellij.markdown.MarkdownElementTypes.CODE_FENCE
import org.intellij.markdown.MarkdownElementTypes.HTML_BLOCK
import org.intellij.markdown.MarkdownElementTypes.LIST_ITEM
import org.intellij.markdown.MarkdownElementTypes.MARKDOWN_FILE
import org.intellij.markdown.MarkdownElementTypes.ORDERED_LIST
import org.intellij.markdown.MarkdownElementTypes.PARAGRAPH
import org.intellij.markdown.MarkdownTokenTypes.Companion.ATX_HEADER
import org.intellij.markdown.MarkdownTokenTypes.Companion.CODE_FENCE_END
import org.intellij.markdown.MarkdownTokenTypes.Companion.CODE_FENCE_START
import org.intellij.markdown.MarkdownTokenTypes.Companion.EOL
import org.intellij.markdown.MarkdownTokenTypes.Companion.FENCE_LANG
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
      EOL -> Unit
      ATX_HEADER -> TODO()
      else -> error("Unhandled node type: ${node.type}")
    }
  }

  context(BODY)
  private fun renderCompositeNode(content: String, node: CompositeASTNode) {
    when (node.type) {
      ATX_1, ATX_2, ATX_3, ATX_4, ATX_5, ATX_6 -> renderHeader(content, node)
      PARAGRAPH -> renderParagraph(content, node)
      CODE_FENCE -> renderCodeFence(content, node)
      BLOCK_QUOTE -> renderBlockQuote(content, node)
      ORDERED_LIST -> renderOrderedList(content, node)
      HTML_BLOCK -> renderHtmlBlock(content, node)
      else -> error("Unhandled node type: ${node.type}")
    }
  }

  context(BODY)
  private fun renderHeader(content: String, node: CompositeASTNode) {
    val headerContent = node.children.last().getTextInNode(content).toString().trim()
    val classes = "subtitle"
    when (node.type) {
      ATX_1 -> h1(classes = classes) { +headerContent }
      ATX_2 -> h2(classes = classes) { +headerContent }
      ATX_3 -> h3(classes = classes) { +headerContent }
      ATX_4 -> h4(classes = classes) { +headerContent }
      ATX_5 -> h5(classes = classes) { +headerContent }
      ATX_6 -> h6(classes = classes) { +headerContent }
      else -> error("Unhandled node type: ${node.type}")
    }
  }

  context(BODY)
  private fun renderParagraph(content: String, node: CompositeASTNode) {
    val paragraphContent = node.getTextInNode(content).toString().trim() // doesn't properly render things like links
    p(classes = "blog-metadata") {
      +paragraphContent
    }
  }

  context(BODY)
  private fun renderCodeFence(content: String, node: CompositeASTNode) {
    val trimmedChildren = node.children
      .dropWhile { it.type == EOL || it.type == CODE_FENCE_START || it.type == FENCE_LANG }
      .dropLastWhile { it.type == EOL || it.type == CODE_FENCE_END }
    val codeFenceContent = trimmedChildren.joinToString("") { it.getTextInNode(content).toString() }
    div(classes = "code-block") {
      pre {
        code {
          +codeFenceContent
        }
      }
    }
  }

  context(BODY)
  private fun renderBlockQuote(content: String, node: CompositeASTNode) {
    val blockQuoteContent = node.children[1].getTextInNode(content).toString().trim()
    div {
      blockQuote {
        p(classes = "blog-metadata") {
          +blockQuoteContent
        }
      }
    }
  }

  context(BODY)
  private fun renderOrderedList(content: String, node: CompositeASTNode) {
    val listItems = node.children.filter { it.type == LIST_ITEM }
    // Will break on nested lists
    val numberedListItems = listItems.map { astNode ->
      val number = astNode.children.first().getTextInNode(content).toString().trim()
      val text = astNode.children.last().getTextInNode(content).toString().trim()
      "$number $text"
    }
    // Obviously restructure this to use actual list tag
    div(classes = "ordered-list") {
      numberedListItems.forEach {
        p(classes = "blog-metadata") {
          +it
        }
      }
    }
  }

  context(BODY)
  private fun renderHtmlBlock(content: String, node: CompositeASTNode) {
    val htmlBlockContent = node.getTextInNode(content).toString().trim()
    div {
      unsafe {
        raw(htmlBlockContent)
      }
    }
  }
}
