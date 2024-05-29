package com.luanvv.http.client.vshc.events

import com.luanvv.http.client.vshc.components.models.CollectionItem
import javafx.event.Event
import javafx.event.EventType

class ChooseRequestEvent(type: EventType<out Event>, val data: CollectionItem) : Event(type) {
    companion object {
        val CHOOSE_REQUEST_EVENT_TYPE: EventType<ChooseRequestEvent> = EventType("CHOOSE_REQUEST")
    }
}