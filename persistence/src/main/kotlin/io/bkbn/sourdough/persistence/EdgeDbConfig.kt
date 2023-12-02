package io.bkbn.sourdough.persistence

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object EdgeDbConfig {

  private val credentials: EdgeDbCredentials

  init {
    val processBuilder = ProcessBuilder("edgedb", "instance", "credentials", "--json").redirectErrorStream(true)
    val process = processBuilder.start()
    val output = process.inputStream.bufferedReader().use { it.readText() }
    credentials = Json.decodeFromString(EdgeDbCredentials.serializer(), output)
  }

  val host: String
    get() = credentials.host

  val port: Int
    get() = credentials.port

  val user: String
    get() = credentials.user

  val password: String
    get() = credentials.password

  val database: String
    get() = credentials.database

  // TODO: Need to switch to TLS for production
  val httpBaseUrl: String
    get() = "http://$host:$port"

  val authProvider: String
    get() = "builtin::local_emailpassword"

  val authExtensionUrl: String
    get() = "$httpBaseUrl/db/edgedb/ext/auth"

  @Serializable
  private data class EdgeDbCredentials(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val database: String,
    @SerialName("tls_cert_data")
    val tlsCertData: String,
    @SerialName("tls_ca")
    val tlsCa: String,
    @SerialName("tls_security")
    val tlsSecurity: String, // TODO: Enum?
  )
}
