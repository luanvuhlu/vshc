package com.luanvv.http.client.vshc.controllers

import com.luanvv.http.client.vshc.components.models.CollectionItem
import com.luanvv.http.client.vshc.events.ChooseRequestEvent
import com.luanvv.http.client.vshc.services.RequestMakerService
import javafx.application.Platform
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
        Platform.runLater {
            val rootNode = mainTabPane.scene.root
            rootNode.addEventFilter(ChooseRequestEvent.CHOOSE_REQUEST_EVENT_TYPE) { event ->
                val data = (event as ChooseRequestEvent).data
                updateRequestTab(data)
            }
            rootNode.addEventFilter(ChooseRequestEvent.CHOOSE_REQUEST_FOLDER_TYPE) { event ->
                val data = (event as ChooseRequestEvent).data
                updateRequestTab(data, true)
            }
        }
    }

    private fun createNewTab(name: String? = null): Tab {
        val tab = Tab()
        val loader = FXMLLoader(javaClass.getResource("/com/luanvv/http/client/vshc/request-tab.fxml"))
        val content = loader.load<Parent>()
        val controller = loader.getController<RequestTabController>()
        tab.userData = controller
        tab.content = content
        val tabName = name ?: "Tab ${mainTabPane.tabs.size + 1}"
        val tabTitle = Label(tabName)
        val closeButton = Button("x")
        closeButton.onMouseClicked = EventHandler {
            mainTabPane.tabs.remove(tab)
        }
        val tabHeader = HBox(tabTitle, closeButton)

        tab.graphic = tabHeader
        tab.isClosable = true
        mainTabPane.tabs.add(tab)
        mainTabPane.selectionModel.select(tab)
        return tab
    }

    private fun updateRequestTab(collectionItem: CollectionItem, folder: Boolean = false) {
        if (folder) {
            RequestMakerService.runFolder(collectionItem)
            return
        }
        val isFolder = collectionItem.requestCollection != null || collectionItem.item?.item?.isNotEmpty() == true
        if (isFolder) {
            return
        }
        val tab = createNewTab(collectionItem.item?.name)
        val controller = tab.userData as? RequestTabController
        controller?.updateRequestTab(collectionItem)
    }
}
