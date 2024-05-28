package com.luanvv.http.client.vshc

import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField

class RequestTabController {
    @FXML
    private lateinit var authorizationTypeBox: ChoiceBox<String>

    @FXML
    private lateinit var urlField: TextField

    @FXML
    private lateinit var httpMethodBox: ChoiceBox<String>

    @FXML
    fun initialize() {
        val options = FXCollections.observableArrayList("GET", "POST", "PUT", "DELETE")
        httpMethodBox.items = options
        httpMethodBox.value = options[0]

        val authorizationTypes = FXCollections.observableArrayList("Inherit auth from parent", "None", "Basic", "Bearer")
        authorizationTypeBox.items = authorizationTypes
        authorizationTypeBox.value = authorizationTypes[0]
    }

    fun onSendButtonClick(actionEvent: ActionEvent) {

    }
}
