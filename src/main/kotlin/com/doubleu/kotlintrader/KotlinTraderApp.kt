package com.doubleu.kotlintrader

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.view.MainView
import tornadofx.*

// General TODOs:
// TODO implement ViewModels
// TODO create ToolTips for disabled components
// TODO Sexy GUI
// TODO 1-2 Kommentare schaden nicht

class KotlinTraderApp : App(MainView::class) {

    override fun stop() {
        super.stop()
        Database.connection?.close()
    }
}