package com.doubleu.kotlintrader

import com.doubleu.kotlintrader.database.DBHelper
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.view.MainView
import tornadofx.*

// TODO implement MasterController
// TODO implement Default Values inside Entities
// TODO allow for creation of new entity objects
// TODO remove files ignored by .gitignore from GitHub
// TODO implement some of dem nice ControlsFX
// TODO ReferenceDelegates can not yet reference RefEntities
// TODO use JavaFX-Gradle-Plugin for JAR creation

class KotlinTraderApp : App(MainView::class) {

    init {
        DBHelper.register()
    }

    /**
     * Overridden to close the database connection when the application exists.
     */
    override fun stop() {
        super.stop()
        Database.forceClose()
    }
}