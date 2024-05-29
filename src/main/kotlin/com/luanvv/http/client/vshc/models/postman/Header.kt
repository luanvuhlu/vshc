package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Header (

	var key : String = "",
	var value : String = "",
)