package com.doubleu.kotlintrader

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.view.MainView
import tornadofx.*

class KotlinTraderApp : App(MainView::class) {

    override fun stop() {
        super.stop()
        Database.disconnect()
    }
}