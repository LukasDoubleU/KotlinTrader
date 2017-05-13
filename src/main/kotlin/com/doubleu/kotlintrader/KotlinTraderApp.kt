package com.doubleu.kotlintrader

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.view.MainView
import tornadofx.*

// General TODOs:
// TODO implement ViewModels
// TODO implement lazy loading / caching for entity properties
// TODO introduce a stylesheet
// TODO comments
// TODO remove files ignored by .gitignore from GitHub
// TODO figure out how to deploy properly as JAR

class KotlinTraderApp : App(MainView::class) {

    /**
     * Overridden to close the database connection when the application exists.
     */
    override fun stop() {
        super.stop()
        Database.connection?.close()
    }
}