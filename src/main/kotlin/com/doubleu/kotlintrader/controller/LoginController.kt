package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import com.doubleu.kotlintrader.util.FxDialogs
import com.doubleu.kotlintrader.util.Session
import com.doubleu.kotlintrader.view.LoginView
import tornadofx.*

class LoginController : SuperController() {

    val view: LoginView by inject()

    /**
     * Creates a database connection with the provided data
     */
    fun connect() {
        with(view) {
            Database.connect(ipProperty.get(), dbProperty.get())
            refreshUsers()
            Session.masterUserProperty.set(Database.findFirstBy(Trader::master, true))
            masterNameProperty.set(Session.masterUserProperty.get()?.name)
        }
    }


    // TODO Users should be stored in Session
    fun refreshUsers() = with(view) { userTable.items = Database.findAll<Trader>().observable() }

    /**
     * Login, verifies password, gives notification
     */
    fun login() = with(view) { Session.login(nameProperty.value, pwProperty.value) }

    /**
     * Attempts to assign a new master
     */
    fun master() {
        with(view) {
            val name = masterNameProperty.get()
            Database.findFirstBy(Trader::name, name)?.let {
                // check if there are any other masters
                Database.findAllBy(Trader::master, true).forEach {
                    // they shall no longer be masters
                    it.master = false
                }
                // declare the new master
                it.master = true
                Session.masterUserProperty.set(it)
                FxDialogs.showInformation("$name was declared the new master")
                refreshUsers()
            }
        }
    }

}