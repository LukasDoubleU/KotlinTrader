package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.database.Database
import tornadofx.*

abstract class SuperController : Controller() {

    init {
        Database.connected.onChange {
            if (it) onConnect() else onDisconnect()
        }
    }

    open fun onConnect() {}

    open fun onDisconnect() {}
}