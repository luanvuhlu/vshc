package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Url (

	var raw : String = "",
	var host : List<String> = listOf(),
	var path : List<String> = listOf(),
	var query : List<Query> = listOf(),
)