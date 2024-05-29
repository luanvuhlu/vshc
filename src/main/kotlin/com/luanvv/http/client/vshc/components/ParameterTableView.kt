package com.luanvv.http.client.vshc.components

import com.luanvv.http.client.vshc.models.Parameter
import javafx.collections.FXCollections
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.converter.DefaultStringConverter

class ParameterTableView : TableView<Parameter>() {
    init {
        val keyColumn = TableColumn<Parameter, String>("Key")
        keyColumn.cellValueFactory = PropertyValueFactory("key")
        val valueColumn = TableColumn<Parameter, String>("Value")
        valueColumn.cellValueFactory = PropertyValueFactory("value")
        val descriptionColumn = TableColumn<Parameter, String>("Description")
        descriptionColumn.cellValueFactory = PropertyValueFactory("description")
        columns.setAll(keyColumn, valueColumn, descriptionColumn)
        keyColumn.prefWidthProperty().bind(widthProperty().multiply(0.2))
        valueColumn.prefWidthProperty().bind(widthProperty().multiply(0.6))
        descriptionColumn.prefWidthProperty().bind(widthProperty().multiply(0.2))
        columns.forEach { column ->
//            column.prefWidthProperty().bind(widthProperty().divide(numberOfColumns))
            column.isEditable = true
            column.cellFactory = TextFieldTableCell.forTableColumn(DefaultStringConverter())
        }
        isEditable = true
        val rows = FXCollections.observableArrayList<Parameter>()
        for (i in 1..10) {
            rows.add(Parameter("", "", ""))
        }
        items = rows
    }
}