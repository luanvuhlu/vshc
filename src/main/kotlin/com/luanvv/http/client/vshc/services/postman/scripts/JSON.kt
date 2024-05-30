package com.luanvv.http.client.vshc.services.postman.scripts

import com.fasterxml.jackson.databind.ObjectMapper

object JSON {
    private val mapper = ObjectMapper()

    fun parse(json: String): Any {
        return json
    }
    fun stringify(obj: Any): String {
        return obj.toString()
    }
}