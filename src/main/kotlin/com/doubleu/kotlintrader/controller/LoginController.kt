package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.data.Data
import com.doubleu.kotlintrader.data.Users
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import com.doubleu.kotlintrader.util.FxDialogs
import com.doubleu.kotlintrader.util.Settings
import tornadofx.*

class LoginController : Controller() {

    /**
     * Attempts to login the [Trader] with the given name.
     * Will display an error when the [password][pw] is wrong.
     */
    fun login(name: String, pw: String) {
        val user = Users.find { it.name == name } ?: return
        if (!user.checkPw(pw)) {
            FxDialogs.showError("Falsches Passwort")
        } else {
            Data.user = user
            Settings.user = name
            Settings.password = pw
            Settings.store()
        }
    }

    /**
     * Attempts to asyncly create a database connection.
     * Saves the login data in the [Settings].
     */
    fun connect(host: String, database: String, dbUser: String, dbPassword: String) {
        runAsync {
            try {
                Database.connect(host, database, dbUser, dbPassword)
            } catch(e: Exception) {
                Settings.restoreDefaults()
                throw e
            }
            Settings.host = host
            Settings.database = database
            Settings.dbUser = dbUser
            Settings.dbPassword = dbPassword
            Settings.store()
        }
    }

    /**
     * Performs the [logout]
     */
    fun logout() {
        Data.user = null
    }

    /**
     * Disconnects from the [Database].
     * Also [logs out][logout].
     */
    fun disconnect() {
        runAsync {
            Database.disconnect()
        }
        logout()
    }

}