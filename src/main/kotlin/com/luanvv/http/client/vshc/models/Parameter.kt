package com.luanvv.http.client.vshc.models

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import kotlin.reflect.KProperty

class Parameter(key: String, value: String, description: String = "") {
    val keyProperty = SimpleStringProperty(this, "key", key).observable()
    var key: String by keyProperty

    val valueProperty = SimpleStringProperty(this, "value", value).observable()
    var value: String by valueProperty

    val descriptionProperty = SimpleStringProperty(this, "description", description).observable()
    var description: String by descriptionProperty
}

fun StringProperty.observable() = object : kotlin.properties.ReadWriteProperty<Any?, String> {
    override fun getValue(thisRef: Any?, property: KProperty<*>) = get()
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: String) = set(value)
}