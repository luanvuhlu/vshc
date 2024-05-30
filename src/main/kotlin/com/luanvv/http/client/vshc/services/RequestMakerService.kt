package com.luanvv.http.client.vshc.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.luanvv.http.client.vshc.models.Parameter
import com.luanvv.http.client.vshc.models.Request
import com.luanvv.http.client.vshc.models.Response
import com.luanvv.http.client.vshc.services.postman.scripts.Variables
import javafx.collections.ObservableList
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

object RequestMakerService {

    private val cookiesMap = mutableMapOf<String, List<String>>()

    private val mapper = ObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    private fun buildUrl(url: String, params: List<Parameter>, variables: List<Parameter>): String {
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
                variables.firstOrNull { v -> v.key == originalParam.key }?.let { existInVariables ->
                    Pair(originalParam.value, existInVariables.value)
                } ?: Pair(originalParam.key, originalParam.value)
            }
            .map { "${it.first.encode()}=${it.second.encode()}" }
            .collect(Collectors.joining("&"))
        return ("$baseUrl?$queries").also {
            println("URL: $it")
        }
    }

    private fun String.encode(): String {
        return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
    }

    fun makeRequest(request: Request, variables: List<Parameter>): Response {
        println("Request: $request, Variables: $variables")
        // Use http client to make request
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
        // Pre-request script TODO
        // Post-response script TODO
        val response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString())
        if (response.headers().map()["Set-Cookie"]?.isEmpty() != true) {
            cookiesMap.put(url, response.headers().map()["Set-Cookie"] ?: emptyList())
        }
        val responseHeaders = response.headers().map().map {
            Parameter(key = it.key, value = it.value.joinToString(", "))
        }
        return Response(response.statusCode(), responseHeaders, response.body())
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
}