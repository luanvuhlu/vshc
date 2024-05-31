package com.luanvv.http.client.vshc.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.luanvv.http.client.vshc.components.models.CollectionItem
import com.luanvv.http.client.vshc.models.Parameter
import com.luanvv.http.client.vshc.models.Request
import com.luanvv.http.client.vshc.models.Response
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

object RequestMakerService {

    private val cookiesMap = mutableMapOf<String, List<String>>()
    private val environment = mutableMapOf<String, String>()

    private val mapper = ObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    private fun buildUrl(url: String, params: List<Parameter>, variables: Map<String, String>): String {
        var newUrl = url
        variables.filter { it.key.isNotBlank() }
            .forEach {
                newUrl = newUrl.replace("{{${it.key}}}", it.value)
            }
        val baseUrl = newUrl.split("?")[0]
        val queries = params
            .stream()
            .filter { it.key.isNotBlank() }
            .map { originalParam ->
                variables.get(originalParam.key)?.let { value ->
                    Pair(originalParam.value, value)
                } ?: Pair(originalParam.key, originalParam.value)
            }
            .map { "${it.first.encode()}=${it.second.encode()}" }
            .collect(Collectors.joining("&"))
        return ("$baseUrl?$queries").also {
            println("URL: $it")
        }
    }

    private fun String.encode(): String =
        URLEncoder.encode(this, StandardCharsets.UTF_8.toString())

    fun makeRequest(request: Request, inputVariables: List<Parameter>): Response {
        println("Request: ${request.url}, Variables: $inputVariables")
        val preRequestResult = runPreRequestScript(request)
        if (preRequestResult?.outEnvironment?.isNotEmpty() == true) {
            preRequestResult.outEnvironment.forEach { this.environment[it.key] = it.value }
        }
        val variables = mutableMapOf<String, String>()
        variables.putAll(environment)
        variables.putAll(inputVariables.associate { it.key to it.value })
        variables.putAll(preRequestResult?.outVariables?.associate { it.key to it.value } ?: emptyMap())
        println("Request: ${request.url}, Variables: $variables")
        val url = buildUrl(request.url, request.parameters, variables)
        val client = HttpClient.newHttpClient()
        val requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(url))
        when (request.method) {
            "GET" -> requestBuilder.GET()
            "POST" -> requestBuilder.POST(HttpRequest.BodyPublishers.ofString(request.body))
            "PUT" -> requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(request.body))
            "DELETE" -> requestBuilder.DELETE()
        }
        request.headers.forEach { requestBuilder.header(it.key, it.value) }
        val response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString())
        if (response.headers().map()["Set-Cookie"]?.isEmpty() != true) {
            // extract domain from url
            val domain = URI(url).host
            cookiesMap[domain] = response.headers().map()["Set-Cookie"] ?: emptyList()
        }
        val responseHeaders = response.headers().map().map {
            Parameter(key = it.key, value = it.value.joinToString(", "))
        }
        val postResponseResult = runPostResponseScript(request, response)
        if (postResponseResult?.outEnvironment?.isNotEmpty() == true) {
            postResponseResult.outEnvironment.forEach { this.environment[it.key] = it.value }
        }
        return Response(response.statusCode(), responseHeaders, response.body())
    }

    private fun runPreRequestScript(request: Request): ScriptService? {
        return request.preRequestScript?.let { script -> ScriptService(script).execute() }
    }

    private fun runPostResponseScript(request: Request, response: HttpResponse<String>): ScriptService? {
        return request.postResponseScript?.let { script -> ScriptService(script, response).execute() }
    }

    fun formatResponse(response: Response): String {
        val responseHeaders = response.headers
        val isJson = responseHeaders.any { it.key.contentEquals("Content-Type", true) && it.value.contains("application/json") }
        return if (isJson) {
            val jsonObject: Any = mapper.readValue<Any>(response.body, Any::class.java)
            mapper.writeValueAsString(jsonObject)
        } else {
            response.body
        }
    }

    fun extractParamsFromUrl(url: String): List<Parameter> {
        return try {
            val query = url.substringAfter("?", "")
            query.split("&")
                .stream()
                .map {
                    val parts = it.split("=")
                    val key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8)
                    val value = if (parts.size > 1) URLDecoder.decode(parts[1], StandardCharsets.UTF_8) else ""
                    Parameter(key = key, value = value)
                }
                .toList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun runFolder(collectionItem: CollectionItem) {
        if (collectionItem.item?.request == null) {
            collectionItem.children.forEach {
                runFolder(it)
            }
        } else {
            val item = collectionItem.item
            val url = item.request?.url?.raw ?: throw IllegalArgumentException("URL is required")
            val request = Request(
                method = item.request?.method ?: "GET",
                url = url,
                headers = item.request?.header?.map { Parameter(key = it.key, value = it.value) } ?: emptyList(),
                parameters = extractParamsFromUrl(url),
                preRequestScript = item.event?.firstOrNull { it.listen == "prerequest" }?.script?.exec?.joinToString("\n") ?: "",
                postResponseScript = item.event?.firstOrNull { it.listen == "test" }?.script?.exec?.joinToString("\n") ?: "",
            )
            val variables = collectionItem.requestCollection?.variable?.map { Parameter(key = it.key, value = it.value) } ?: emptyList()
            val response = makeRequest(request, variables)
            println("Response: $response")
        }
    }
}