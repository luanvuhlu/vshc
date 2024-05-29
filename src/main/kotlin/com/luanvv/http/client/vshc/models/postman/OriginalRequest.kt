package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OriginalRequest (

	var method : String = "",
	var header : List<Header> = listOf(),
	var url : Url = Url(),
)