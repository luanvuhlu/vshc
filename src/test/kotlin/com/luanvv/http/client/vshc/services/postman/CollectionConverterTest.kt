package com.luanvv.http.client.vshc.services.postman

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class CollectionConverterTest {

    @Test
    fun importCollection() {
        // Read the collection from a file
        val collectionContent = this::class.java.getResource("/postman_collection.json")?.readText()
        assertNotNull(collectionContent)
        val collection = CollectionConverter.importCollection(collectionContent!!)
        assertNotNull(collection)
        assertEquals("Watson Natural Language Understanding", collection.info.name)
    }

    @Test
    fun importEnvironment() {
        // Read the environment from a file
        val environmentContent =
            this::class.java.getResource("/postman_environment.json")?.readText()
        assertNotNull(environmentContent)
        val environment = CollectionConverter.importEnvironment(environmentContent!!)
        assertNotNull(environment)
        assertEquals("Watson", environment.name)
    }
}