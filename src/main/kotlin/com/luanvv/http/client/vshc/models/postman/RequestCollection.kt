package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class RequestCollection (
	var info : Info = Info(),
	var item : List<Item> = listOf(),
	var auth : Auth = Auth(),
	var event : List<Event> = listOf(),
	var variable : List<Variable> = listOf(),
)