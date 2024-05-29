package com.luanvv.http.client.vshc.controllers

import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.layout.HBox

class MainViewController {

    @FXML
    private lateinit var mainTabPane: TabPane

    @FXML
    private lateinit var newTabButton: Button

    @FXML
    fun initialize() {
        newTabButton.setOnAction {
            createNewTab()
        }
        createNewTab()
    }

    private fun createNewTab() {
        val tab = Tab()
        val loader = FXMLLoader(javaClass.getResource("/com/luanvv/http/client/vshc/request-tab.fxml"))
        val content = loader.load<Parent>()
        tab.content = content
        val tabTitle = Label("Tab ${mainTabPane.tabs.size + 1}      ")
        val closeButton = Button("x")
        closeButton.onMouseClicked = EventHandler {
            mainTabPane.tabs.remove(tab)
        }
        val tabHeader = HBox(tabTitle, closeButton)

        tab.graphic = tabHeader
        tab.isClosable = true
        mainTabPane.tabs.add(tab)
        mainTabPane.selectionModel.select(tab)
    }
}
