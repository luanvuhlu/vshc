package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Request (

	var method : String = "",
	var header : List<Header> = listOf(),
	var url : Url = Url(),
	var description : String = "",
)