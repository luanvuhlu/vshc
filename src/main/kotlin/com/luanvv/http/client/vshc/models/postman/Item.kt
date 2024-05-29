package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
open class Item (

	var name : String = "",
	var item : List<Item> = listOf(),
	var event : List<Event>? = null,
	var request: OriginalRequest? = null,
	var response: List<Response>? = null,
)