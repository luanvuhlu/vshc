package com.luanvv.http.client.vshc.services.postman

import com.fasterxml.jackson.databind.ObjectMapper
import com.luanvv.http.client.vshc.models.postman.Environment
import com.luanvv.http.client.vshc.models.postman.RequestCollection

object CollectionConverter {
    private val mapper = ObjectMapper()

    fun importCollection(collection: String): RequestCollection {
        return mapper.readValue(collection, RequestCollection::class.java)
    }

    fun importEnvironment(environment: String): Environment {
        return mapper.readValue(environment, Environment::class.java)
    }
}