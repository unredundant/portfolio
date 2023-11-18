package io.bkbn.sourdough.api.view

import io.bkbn.sourdough.api.model.ArticleModels
import io.bkbn.sourdough.api.view.ViewUtils.configureHead
import io.bkbn.sourdough.api.view.component.NavbarComponent
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.table
import kotlinx.html.td
import kotlinx.html.tr
import kotlinx.html.unsafe
import net.mamoe.yamlkt.Yaml
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.flavours.gfm.GFMFlavourDescriptor
import org.intellij.markdown.html.HtmlGenerator
import org.intellij.markdown.parser.MarkdownParser
import java.io.File

object ArticlesView : View {
  context(Route) override fun render() {
    route("/articles") {
      get {
        val postMetadata = collectPostMetadata().sortedBy { it.frontMatter.date }.reversed()
        call.respondHtml {
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
                    td(classes = "blog-column") {
                      a(classes = "no-style", href = "/articles/${post.slug}") {
                        p(classes = "blog-title") {
                          +post.frontMatter.title
                        }
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
      }
      route("/{slug}") {
        get {
          val (metadata, content) = loadBlogContent(call.parameters["slug"] ?: error("No slug found"))
          call.respondHtml {
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
        }
      }
    }
  }

  private fun loadBlogContent(slug: String): Pair<ArticleModels.ArticleMetadata, String> {
    val file = File(this.javaClass.getResource("/static/posts/$slug.md")?.toURI() ?: error("Post not found :("))
    val metadata = getPostMetadata(file)
    val flavour = GFMFlavourDescriptor()
    val rootNode = MarkdownParser(flavour).buildMarkdownTreeFromString(file.readText())


    // TODO: Strip front matter... probably write your own generator
    val content = HtmlGenerator(file.readText(), rootNode, flavour).generateHtml()
    return metadata to content
  }

  private fun collectPostMetadata(): List<ArticleModels.ArticleMetadata> {
    val postDirectory = File(this.javaClass.getResource("/static/posts")?.toURI() ?: error("Posts not found :("))
    return postDirectory.walk()
      .filter { it.isFile }
      .map { getPostMetadata(it) }
      .toList()
  }

  private fun getPostMetadata(file: File): ArticleModels.ArticleMetadata {
    val flavour = GFMFlavourDescriptor()
    val rootNode = MarkdownParser(flavour).buildMarkdownTreeFromString(file.readText())
    return ArticleModels.ArticleMetadata(
      retrieveBlogFrontMatter(rootNode, file.readText()),
      file.name.removeSuffix(".md")
    )
  }

  internal fun retrieveBlogFrontMatter(rootNode: ASTNode, src: String): ArticleModels.ArticleFrontMatter {
    require(rootNode.type == MarkdownElementTypes.MARKDOWN_FILE) { "Root node must be a Markdown file" }

    val frontMatter = rootNode.children.getOrNull(2) ?: error("No front matter found")

    require(frontMatter.type == MarkdownElementTypes.PARAGRAPH) { "Front matter must be a paragraph" }

    val frontMatterText = frontMatter.getTextInNode(src).toString()

    return Yaml.Default.decodeFromString(ArticleModels.ArticleFrontMatter.serializer(), frontMatterText)
  }
}
