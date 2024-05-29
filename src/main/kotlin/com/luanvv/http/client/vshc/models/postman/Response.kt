package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response (

	var name : String = "",
	var originalRequest : OriginalRequest = OriginalRequest(),
	var status : String = "",
	var code : Int = 0,
	var _postman_previewlanguage : String = "",
	var header : List<Header> = listOf(),
	var cookie : List<String> = listOf(),
	var body : String = "",
)