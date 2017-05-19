package com.doubleu.kotlintrader

import com.doubleu.kotlintrader.data.Storage
import com.doubleu.kotlintrader.database.DBHelper
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.view.MainView
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import tornadofx.*

// TODO allow for creation of new entity objects (User registration)
// TODO remove files ignored by .gitignore from GitHub
// TODO implement suiting ControlsFX (e.g. lightweight alerts)
// TODO ReferenceDelegates can not yet reference RefEntities (no need?)
// TODO use JavaFX-Gradle-Plugin for JAR creation

class KotlinTraderApp : App(MainView::class) {

    init {
        DBHelper.register()
    }

    /**
     * Overridden to give the background threads a chance to finish properly
     * when the user attempts to close the application
     */
    override fun stop() {
        if (!Storage.anyLoading.get()) {
            Database.forceClose()
            super.stop()
            return
        } else {
            val alert = Alert(Alert.AlertType.CONFIRMATION, "Waiting for background Threads...", ButtonType.CLOSE)
            Storage.anyLoading.onChange {
                Database.forceClose()
                alert.hide()
                super.stop()
            }
            alert.showAndWait()
            System.exit(0)
        }
    }
}