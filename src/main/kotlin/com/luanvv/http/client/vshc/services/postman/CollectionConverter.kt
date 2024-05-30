package com.luanvv.http.client.vshc.services.postman

import com.fasterxml.jackson.databind.ObjectMapper
import com.luanvv.http.client.vshc.models.postman.Environment
import com.luanvv.http.client.vshc.models.postman.RequestCollection
import javax.script.ScriptEngineManager

object CollectionConverter {
    private val mapper = ObjectMapper()

    fun importCollection(collection: String): RequestCollection {
        return mapper.readValue(collection, RequestCollection::class.java)
    }

    fun importEnvironment(environment: String): Environment {
        return mapper.readValue(environment, Environment::class.java)
    }

    fun runScript(script: String) {
        val kotlinScriptEngine = ScriptEngineManager().getEngineByExtension("kts")
            ?: throw RuntimeException("Kotlin script engine not found")
        kotlinScriptEngine.eval(script)
    }
}