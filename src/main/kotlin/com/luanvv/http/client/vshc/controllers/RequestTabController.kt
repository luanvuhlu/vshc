package com.luanvv.http.client.vshc.controllers

import com.luanvv.http.client.vshc.components.models.CollectionItem
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

class RequestTabController {

    @FXML
    private lateinit var txtPostResponse: TextArea

    @FXML
    private lateinit var txtPreRequest: TextArea

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
    private lateinit var variablesTable: TableView<Parameter>

    @FXML
    private lateinit var responseHeadersTable: TableView<Parameter>

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
                val params = RequestMakerService.extractParamsFromUrl(newValue)
                Platform.runLater {
                    paramsTable.items.also {
                        it.clear()
                        it.addAll(params)
                        it.addAll(IntRange(params.size, 10).map { Parameter("", "") })
                    }
                }
            } catch (e: Exception) {
                // Invalid URL, do nothing
            }
        }

//        paramsTable.items.addListener(ListChangeListener {
//            if (it.next() && (it.wasAdded() || it.wasRemoved())) {
//                val params = paramsTable.items.filter { it.key.isNotBlank() }
//                val query = params.joinToString("&") { "${it.key}=${it.value}" }
//                val oldUrl = URI(urlField.text).toURL()
//                val newUrl = URI(
//                    oldUrl.protocol,
//                    null,
//                    oldUrl.host,
//                    oldUrl.port,
//                    oldUrl.path,
//                    query,
//                    null,
//                ).toURL()
//                Platform.runLater {
//                    urlField.text = newUrl.toString()
//                }
//            }
//        })
    }

    fun onSendButtonClick(actionEvent: ActionEvent) {
        val url = urlField.text
        val httpMethod = httpMethodBox.value
//        val authorizationType = authorizationTypeBox.value
        val params = paramsTable.items.stream()
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
            parameters = params,
            preRequestScript = txtPreRequest.text,
            postResponseScript = txtPostResponse.text,
        )
        Platform.runLater {
            val response = RequestMakerService.makeRequest(request, variablesTable.items)
            txtResponseBody.text = RequestMakerService.formatResponse(response)
            responseHeadersTable.items.clear()
            responseHeadersTable.items.addAll(
                response.headers.map { Parameter(key = it.key, value = it.value) }
            )
        }
    }

    private fun addVariables(collectionItem: CollectionItem) {
        collectionItem.requestCollection?.variable?.map { Parameter(key = it.key, value = it.value) }?.let {
            variablesTable.items.addAll(it)
        }
        if (collectionItem.root is CollectionItem) {
            addVariables(collectionItem.root)
        }
    }

    fun updateRequestTab(collectionItem: CollectionItem) {
        if (collectionItem.item != null) {
            val item = collectionItem.item
            urlField.text = item.request?.url?.raw
            httpMethodBox.value = item.request?.method
            variablesTable.items.also {
                it.clear()
            }
            addVariables(collectionItem)
            headersTable.items.also {
                it.clear()
                it.addAll(
                    item.request?.header?.map { Parameter(key = it.key, value = it.value) } ?: emptyList()
                )
            }
            txtPreRequest.text = item.event?.firstOrNull { it.listen == "prerequest" }?.script?.exec?.joinToString("\n") ?: ""
            txtPostResponse.text = item.event?.firstOrNull { it.listen == "test" }?.script?.exec?.joinToString("\n") ?: ""
        }
    }
}
