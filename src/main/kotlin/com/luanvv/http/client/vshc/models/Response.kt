package com.luanvv.http.client.vshc.models

data class Response(
    val status: Int,
    val headers: List<Parameter> = emptyList(),
    val body: String = "",
)
