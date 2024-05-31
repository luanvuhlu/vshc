package com.luanvv.http.client.vshc.services

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.net.http.HttpResponse

class ScriptServiceTest {

    class MockHttpResponse(val body: String) : HttpResponse<String> {
        override fun statusCode(): Int = 200
        override fun uri() = null
        override fun version() = null
        override fun body(): String = body
        override fun sslSession() = null
        override fun request() = null
        override fun previousResponse() = null
        override fun headers() = null
    }

    @Test
    fun execute() {
        val script = """
            var jsonData = pm.response.json();
            pm.variables.set("name", jsonData.name);
            pm.environment.set("name", jsonData.name);
            pm.variables.set("age", jsonData.age);
            pm.environment.set("age", jsonData.age);
        """.trimIndent()
        val response = MockHttpResponse("{\"name\":\"John Doe\",\"age\":30}")
        val service = ScriptService(script, response = response)
        service.execute()
        assertEquals(2, service.outVariables.size)
        assertEquals("John Doe", service.outVariables[0].value)
        assertEquals("30", service.outVariables[1].value)
        assertEquals(2, service.outEnvironment.size)
        assertEquals("John Doe", service.outEnvironment[0].value)
        assertEquals("30", service.outEnvironment[1].value)
    }
}