package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import com.doubleu.kotlintrader.util.Session
import tornadofx.*

abstract class SuperController : Controller() {

    init {
        Database.connected.onChange {
            if (it) onConnect() else onDisconnect()
        }
        Session.isLoggedIn.onChange {
            if (it) onLogin(Session.loggedInUser!!) else onLogout()
        }
    }

    open fun onConnect() {}

    open fun onDisconnect() {}

    open fun onLogin(user: Trader) {}

    open fun onLogout() {}
}