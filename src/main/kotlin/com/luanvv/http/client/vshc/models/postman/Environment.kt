package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Environment (

	var id : String = "",
	var name : String = "",
	var values : List<Values> = listOf(),
	var _postman_variable_scope : String = "",
	var _postman_exported_at : String = "",
	var _postman_exported_using : String = "",
)