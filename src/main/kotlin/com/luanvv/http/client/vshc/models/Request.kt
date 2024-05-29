package com.luanvv.http.client.vshc.models

data class Request(
    val method: String = "GET",
    val url: String,
    val headers: List<Parameter> = emptyList(),
    val parameters: List<Parameter> = emptyList(),
    val body: String? = null,
    val preRequestScript: String? = null,
    val postResponseScript: String? = null,
)