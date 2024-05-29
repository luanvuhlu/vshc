package com.luanvv.http.client.vshc.controllers

import com.luanvv.http.client.vshc.components.models.CollectionItem
import com.luanvv.http.client.vshc.events.ChooseRequestEvent
import com.luanvv.http.client.vshc.models.postman.Item
import com.luanvv.http.client.vshc.models.postman.RequestCollection
import com.luanvv.http.client.vshc.services.postman.CollectionConverter
import javafx.event.Event.fireEvent
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeView

class CollectionController {

    @FXML
    private lateinit var collectionListView: ListView<TreeView<CollectionItem>>

    @FXML
    fun initialize() {
        val collectionContent = this::class.java.getResource("/sample/postman_collection.json")?.readText()
        val collection = CollectionConverter.importCollection(collectionContent!!)
        val rootItem = TreeItem(CollectionItem(requestCollection = collection, root = collection))
        collection.item.forEach { item ->
            createTreeItems(collection, item, rootItem)
        }
        val treeView = TreeView(rootItem)
        collectionListView.items.add(treeView)

        treeView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            val item = newValue.value
            if (item != null) {
                fireEvent(treeView, ChooseRequestEvent(ChooseRequestEvent.CHOOSE_REQUEST_EVENT_TYPE, item))
            }
        }
    }

    private fun createTreeItems(root: RequestCollection, item: Item, treeItem: TreeItem<CollectionItem>): TreeItem<CollectionItem> {
        return TreeItem(CollectionItem(item = item, root = root)).also {
            treeItem.children.add(it)
            if (item.item.isNotEmpty()) {
                item.item.forEach { item2 ->
                    createTreeItems(root, item2, it)
                }
            }
        }
    }
}
