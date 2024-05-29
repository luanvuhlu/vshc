package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Auth (

	var type : String = "",
	var basic : List<Basic> = listOf(),
)