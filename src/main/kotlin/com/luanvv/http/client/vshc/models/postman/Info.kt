package com.luanvv.http.client.vshc.models.postman

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Info (
	var _postman_id : String = "",
	var name : String = "",
	var description : String = "",
	var schema : String = "",
	var _exporter_id : Int = 0
)