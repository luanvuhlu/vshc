package com.luanvv.http.client.vshc.components.models

import com.luanvv.http.client.vshc.models.postman.Item
import com.luanvv.http.client.vshc.models.postman.RequestCollection

data class CollectionItem(
    val item: Item? = null,
    val root: Any? = null,
    val requestCollection: RequestCollection? = null
) {
    override fun toString(): String {
        if (requestCollection != null) {
            return requestCollection.info.name
        }
        return if (item != null) {
            if (item.request != null) {
                "${item.request!!.method.uppercase()} ${item.name}"
            } else {
                item.name
            }
        } else {
            "Untitled"
        }
    }
}