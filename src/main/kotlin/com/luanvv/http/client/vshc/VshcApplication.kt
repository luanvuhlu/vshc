package com.luanvv.http.client.vshc

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage

class VshcApplication : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(VshcApplication::class.java.getResource("main.fxml"))
        val scene = Scene(fxmlLoader.load(), 1000.0, 240.0)
        stage.title = "Hello!"
        stage.scene = scene
        stage.isMaximized = true
        stage.show()
    }
}

fun main() {
    Application.launch(VshcApplication::class.java)
}