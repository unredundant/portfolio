package io.bkbn.sourdough.api.documentation

import io.bkbn.kompendium.core.metadata.MethodInfo
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.info.Contact
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.info.License
import io.bkbn.kompendium.oas.server.Server
import io.bkbn.sourdough.api.model.ExceptionModels
import io.ktor.http.HttpStatusCode
import java.net.URI

object DocumentationUtils {

  fun applicationSpec() = OpenApiSpec(
    info = Info(
      title = "Sourdough API",
      version = "0.0.1",
      description = "A Delicious Starter API",
      termsOfService = URI("https://example.com"),
      contact = Contact(
        name = "Homer Simpson",
        email = "chunkylover53@aol.com",
        url = URI("https://gph.is/1NPUDiM")
      ),
      license = License(
        name = "MIT",
        url = URI("https://github.com/bkbnio/kompendium/blob/main/LICENSE")
      )
    ),
    servers = mutableListOf(
      Server(
        url = URI("http://localhost:8080"),
        description = "Developer instance"
      ),
    )
  )

  fun MethodInfo.Builder<*>.standardErrors() {
    canRespond {
      description("Indicates that the payload did not match the expected input")
      responseCode(HttpStatusCode.BadRequest)
      responseType<ExceptionModels.Standard>()
    }
    canRespond {
      description("The provided ID did not match any existing author entities")
      responseCode(HttpStatusCode.NotFound)
      responseType<ExceptionModels.Standard>()
    }
  }
}
