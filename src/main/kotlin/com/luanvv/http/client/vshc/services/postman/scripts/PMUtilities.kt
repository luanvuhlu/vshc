package com.luanvv.http.client.vshc.services.postman.scripts

class PMUtilities(
    val environment: Environment? = null,
    val cookies: Cookies? = null,
    val variables: Variables? = null
) {
    fun test(msg: String, condition: Boolean) {

    }

    fun expect(obj: Any) = Expect()

    fun function(function: () -> Unit): Boolean {
        function()
        return true
    }
}