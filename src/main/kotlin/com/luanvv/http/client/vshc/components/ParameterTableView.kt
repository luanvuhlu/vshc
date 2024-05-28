package com.luanvv.http.client.vshc

import javafx.collections.FXCollections
import javafx.scene.control.TableView
import javafx.scene.control.cell.TextFieldTableCell
import javafx.util.converter.DefaultStringConverter

class ParameterTableView : TableView<Parameter>() {
    init {
        val numberOfColumns = columns.size
        columns.forEach { column ->
            column.prefWidthProperty().bind(widthProperty().divide(numberOfColumns))
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