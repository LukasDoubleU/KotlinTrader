package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import com.doubleu.kotlintrader.util.FxDialogs
import com.doubleu.kotlintrader.util.Session
import com.doubleu.kotlintrader.view.LoginView
import tornadofx.*

class LoginController : Controller() {

    val view: LoginView by inject()

    fun connect() {
        with(view) {
            Database.connect(ipProperty.get(), dbProperty.get())
            userTable.items = Database.findAll<Trader>().observable()
            masterNameProperty.bindBidirectional(Session.masterUserProperty.select { it.nameProperty })
        }
    }

    fun login() {
        with(view) {
            val name = nameProperty.get()
            val user = Database.findBy(Trader::name, name) ?: return
            if (!user.checkPw(pwProperty.get())) {
                FxDialogs.showError("Falsches Passwort")
            } else {
                FxDialogs.showInformation("Logged in")
                Session.userProperty.set(user)
            }
        }
    }

    fun master() {
        with(view) {
            val master = Database.findBy(Trader::name, masterNameProperty.get())
            master?.master = true
            Session.masterUserProperty.set(master)
        }
    }

}