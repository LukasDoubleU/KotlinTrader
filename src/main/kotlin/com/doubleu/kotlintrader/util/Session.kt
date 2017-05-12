package com.doubleu.kotlintrader.util

import com.doubleu.kotlintrader.controller.SuperController
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

object Session : SuperController() {

    val loggedInUserProperty = SimpleObjectProperty<Trader?>()
    var loggedInUser by loggedInUserProperty

    val masterUserProperty = SimpleObjectProperty<Trader?>()
    var masterUser by masterUserProperty

    val isLoggedIn = Bindings.isNotNull(loggedInUserProperty)!!
    val users = mutableListOf<Trader>().observable()

    override fun onLogin(user: Trader) = updateTitle("Kotlin Trader - Logged in as ${user.name}")

    override fun onLogout() = updateTitle("Kotlin Trader")

    private fun updateTitle(title: String) = primaryStage.titleProperty().bind(SimpleStringProperty(title))

    fun login(name: String, pw: String) {
        val user = Database.findFirstBy(Trader::name, name) ?: return
        if (!user.checkPw(pw)) {
            FxDialogs.showError("Falsches Passwort")
        } else {
            FxDialogs.showInformation("Logged in as $name")
            loggedInUser = user
        }
    }

    fun logout() {
        loggedInUser = null
    }

}