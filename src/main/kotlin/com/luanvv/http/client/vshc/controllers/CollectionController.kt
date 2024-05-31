package com.luanvv.http.client.vshc.controllers

import com.luanvv.http.client.vshc.components.models.CollectionItem
import com.luanvv.http.client.vshc.events.ChooseRequestEvent
import com.luanvv.http.client.vshc.models.postman.Item
import com.luanvv.http.client.vshc.models.postman.RequestCollection
import com.luanvv.http.client.vshc.services.postman.CollectionConverter
import javafx.event.Event.fireEvent
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.stage.FileChooser
import javafx.util.Callback
import java.io.File

class CollectionController {

    @FXML
    private lateinit var btnImport: Button

    @FXML
    private lateinit var collectionListView: ListView<TreeView<CollectionItem>>

    @FXML
    fun initialize() {
//        addNewCollection(sampleCollection())
        addNewCollection(anotherSampleCollection())
        btnImport.setOnAction {
            val file = openFilePicker()
            if (file != null) {
                val collectionContent = file.readText()
                val collection = CollectionConverter.importCollection(collectionContent)
                addNewCollection(collection)
            }
        }
    }

    private fun sampleCollection(): RequestCollection {
        val collectionContent = this::class.java.getResource("/sample/postman_collection.json")?.readText()
        return CollectionConverter.importCollection(collectionContent!!)
    }

    private fun anotherSampleCollection(): RequestCollection {
        val collectionContent = this::class.java.getResource("/sample/CoinMarket.postman_collection.json")?.readText()
        return CollectionConverter.importCollection(collectionContent!!)
    }

    private fun openFilePicker(): File? {
        val fileChooser = FileChooser().apply {
            extensionFilters.add(FileChooser.ExtensionFilter("Postman Collection", "*.json"))
        }
        fileChooser.title = "Open Resource File"
        val file = fileChooser.showOpenDialog(btnImport.scene.window)
        return file?.let {
            if (it.exists() && it.isFile) it else null
        }
    }

    private fun addNewCollection(collection: RequestCollection) {
        val rootItem = TreeItem(CollectionItem(requestCollection = collection, root = null)).also {
            it.isExpanded = true
        }
        collection.item.forEach { item ->
            createTreeItems(collection, item, rootItem)
        }
        val treeView = TreeView(rootItem)
        treeView.cellFactory = Callback {
            object : TreeCell<CollectionItem>() {
                init {
                    val contextMenu = ContextMenu()
                    val menuItem1 = MenuItem("Run all without stored cookies")
                    menuItem1.setOnAction {
                        val selectedItem = this.treeItem.value
                        fireEvent(treeView, ChooseRequestEvent(ChooseRequestEvent.CHOOSE_REQUEST_FOLDER_TYPE, selectedItem))
                    }
                    contextMenu.items.addAll(menuItem1)
                    setContextMenu(contextMenu)
                }

                override fun updateItem(item: CollectionItem?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = item?.toString()
                }
            }
        }
        collectionListView.items.add(treeView)
        treeView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            val item = newValue.value
            if (item != null) {
                fireEvent(treeView, ChooseRequestEvent(ChooseRequestEvent.CHOOSE_REQUEST_EVENT_TYPE, item))
            }
        }
    }

    private fun createTreeItems(root: RequestCollection, item: Item, treeItem: TreeItem<CollectionItem>): TreeItem<CollectionItem> {
        val collectionItem = CollectionItem(item = item, root = treeItem.value)
        treeItem.value.children.add(collectionItem)
        return TreeItem(collectionItem).also {
            it.isExpanded = true
            treeItem.children.add(it)
            if (item.item.isNotEmpty()) {
                item.item.forEach { item2 ->
                    createTreeItems(root, item2, it)
                }
            }
        }
    }
}
