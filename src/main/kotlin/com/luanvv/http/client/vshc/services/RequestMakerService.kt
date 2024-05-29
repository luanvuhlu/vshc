package com.luanvv.http.client.vshc.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.luanvv.http.client.vshc.models.Parameter
import com.luanvv.http.client.vshc.models.Request
import com.luanvv.http.client.vshc.models.Response
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

object RequestMakerService {

    private val mapper = ObjectMapper().apply {
        enable(SerializationFeature.INDENT_OUTPUT)
    }

    fun makeRequest(request: Request): Response {
        println("Request: $request")
        // Use http client to make request
        val client = HttpClient.newHttpClient()
        val requestBuilder = HttpRequest.newBuilder()
            .uri(URI.create(request.url))
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
        println(response.statusCode())
        println(response.body())
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