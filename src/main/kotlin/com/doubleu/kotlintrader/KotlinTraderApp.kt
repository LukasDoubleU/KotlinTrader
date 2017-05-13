package com.doubleu.kotlintrader

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.view.MainView
import tornadofx.*

// General TODOs:
// TODO implement ViewModels
// TODO if not solved with ViewModels: Implement lazy loading / caching for entity properties
// TODO create ToolTips for disabled components
// TODO introduce a stylesheet
// TODO 1-2 Kommentare schaden nicht
// TODO remove files ignored by .gitignore from GitHub
// TODO figure out how to deploy properly as JAR

class KotlinTraderApp : App(MainView::class) {

    override fun stop() {
        super.stop()
        Database.connection?.close()
    }
}