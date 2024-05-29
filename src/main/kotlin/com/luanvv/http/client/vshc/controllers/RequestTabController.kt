package com.luanvv.http.client.vshc.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.luanvv.http.client.vshc.models.Parameter
import com.luanvv.http.client.vshc.models.Request
import com.luanvv.http.client.vshc.services.RequestMakerService
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TableView
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import java.net.URI
import java.net.URL
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class RequestTabController {

    @FXML
    private lateinit var txtResponseBody: TextArea

    @FXML
    private lateinit var authorizationTypeBox: ChoiceBox<String>

    @FXML
    private lateinit var urlField: TextField

    @FXML
    private lateinit var httpMethodBox: ChoiceBox<String>

    @FXML
    private lateinit var paramsTable: TableView<Parameter>

    @FXML
    private lateinit var headersTable: TableView<Parameter>

    @FXML
    fun initialize() {
        val options = FXCollections.observableArrayList("GET", "POST", "PUT", "DELETE")
        httpMethodBox.items = options
        httpMethodBox.value = options[0]

        val authorizationTypes = FXCollections.observableArrayList("Inherit auth from parent", "None", "Basic", "Bearer")
        authorizationTypeBox.items = authorizationTypes
        authorizationTypeBox.value = authorizationTypes[0]

        urlField.textProperty().addListener { _, _, newValue ->
            try {
                val url = URI(newValue).toURL()
                val query = url.query
                if (query != null) {
                    val params = query.split("&").map {
                        val parts = it.split("=")
                        val key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8)
                        val value = if (parts.size > 1) URLDecoder.decode(parts[1], StandardCharsets.UTF_8) else ""
                        Parameter(key = key, value = value)
                    }
                    Platform.runLater {
                        paramsTable.items.setAll(params)
                    }
                }
            } catch (e: Exception) {
                // Invalid URL, do nothing
            }
        }
    }

    fun onSendButtonClick(actionEvent: ActionEvent) {
        val url = urlField.text
        val httpMethod = httpMethodBox.value
        // TODO
//        val authorizationType = authorizationTypeBox.value
        val params = headersTable.items.stream()
            .filter { it.key.isNotBlank() }
            .map { Parameter(key = it.key, value = it.value) }
            .toList()
        val headers = headersTable.items.stream()
            .filter { it.key.isNotBlank() }
            .map { Parameter(key = it.key, value = it.value) }
            .toList()
        val request = Request(
            method = httpMethod,
            url = url,
            headers = headers,
            parameters = params
        )
        Platform.runLater {
            val response = RequestMakerService.makeRequest(request)
            txtResponseBody.text = RequestMakerService.formatResponse(response)
        }
    }
}
