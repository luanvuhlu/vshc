package com.luanvv.http.client.vshc.services

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.luanvv.http.client.vshc.models.Parameter
import java.net.http.HttpResponse
import javax.script.ScriptEngineManager

class ScriptService(
    private val script: String,
//    private val request: Request? = null,
//    private val variables: List<Parameter> = emptyList(),
    private val response: HttpResponse<String>? = null,
){
    private val scriptEngine = ScriptEngineManager().getEngineByName("kotlin")
    private val objectMapper = ObjectMapper()
    private val dataMap = mutableMapOf<String, Any>()
    val outVariables: List<Parameter>
        get() = _outVariables
    private var _outVariables: MutableList<Parameter> = mutableListOf()
    private val outCookies: String?
        get() = _outCookies
    private var _outCookies: String? = null
    val outEnvironment: List<Parameter>
        get() = _outEnvironment
    private var _outEnvironment: MutableList<Parameter> = mutableListOf()

    fun execute(): ScriptService {
        val rawData = response?.body() ?: ""
        script.split("\n").stream()
            // remove all spaces and tabs
            .map { it.replace("\\s".toRegex(), "") }
            .forEach { line ->
                if (line.startsWith("varjsonData=") && rawData.isNotBlank()) {
                    dataMap["jsonData"] = objectMapper.readTree(rawData)
                } else if (dataMap.contains("jsonData") && line.startsWith("pm.variables.set(")) {
                    setParameter(line, dataMap, _outVariables)
                } else if (line.startsWith("pm.environment.set(")) {
                    setParameter(line, dataMap, _outEnvironment)
                }
            }
        return this
    }

    private fun setParameter(line: String, dataMap: Map<String, Any>, out: MutableList<Parameter>) {
        val key = line.substringAfter("set(\"").substringBefore("\",")
        val value = line.substringAfter(",jsonData.").substringBefore(")")
        val valueChain = value.split(".")
        var data: JsonNode? = dataMap["jsonData"] as JsonNode
        for (valueKey in valueChain) {
            if (valueKey.endsWith("]")) {
                // TODO handle array
            } else {
                data = data?.get(valueKey)
            }
        }
        out.add(Parameter(key, data?.asText() ?: ""))
    }

}