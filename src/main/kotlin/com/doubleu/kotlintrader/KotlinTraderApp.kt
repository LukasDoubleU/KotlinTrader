package com.doubleu.kotlintrader

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.view.MainView
import tornadofx.*

// TODO Finish implementing ViewModels and use them
// TODO Implement Default Values inside Entities
// TODO Split Session up into Data package
// TODO implement Trade- and MasterController
// TODO allow for creation of new entity objects
// TODO implement some of dem nice ControlsFX
// TODO ReferenceDelegates can not yet reference RefEntities
// TODO use JavaFX-Gradle-Plugin for JAR creation
// TODO remove files ignored by .gitignore from GitHub

class KotlinTraderApp : App(MainView::class) {

    /**
     * Overridden to close the database connection when the application exists.
     */
    override fun stop() {
        super.stop()
        Database.connection?.close()
    }
}