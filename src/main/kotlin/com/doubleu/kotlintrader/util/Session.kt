package com.doubleu.kotlintrader.util

import com.doubleu.kotlintrader.controller.SuperController
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Ort
import com.doubleu.kotlintrader.model.Schiff
import com.doubleu.kotlintrader.model.Trader
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import kotlin.reflect.KProperty1

object Session : SuperController() {

    val users = mutableListOf<Trader>().observable()

    val orte = mutableListOf<Ort>().observable()
    val ortProperty = SimpleObjectProperty<Ort?>()
    var ort by ortProperty

    val schiffProperty = SimpleObjectProperty<Schiff?>()
    var schiff by schiffProperty

    val loggedInUserProperty = SimpleObjectProperty<Trader?>()
    var loggedInUser by loggedInUserProperty
    val isLoggedIn = Bindings.isNotNull(loggedInUserProperty)!!

    val masterUserProperty = SimpleObjectProperty<Trader?>()
    var masterUser by masterUserProperty
    val isMasterUserLoggedIn = isLoggedIn.and(Bindings.equal(loggedInUserProperty, masterUserProperty))!!

    init {
        masterUserProperty.onChange {
            users.forEach { it.master = false }
            it?.let { it.master = true }
        }
        isLoggedIn.onChange {
            if (it) onLogin(Session.loggedInUser!!) else onLogout()
        }
    }

    fun login(name: String, pw: String) {
        val user = findUser(Trader::name, name) ?: return
        if (!user.checkPw(pw)) {
            FxDialogs.showError("Falsches Passwort")
        } else {
            loggedInUser = user
        }
    }

    fun <V : Any?> findUser(property: KProperty1<Trader, V>, value: V): Trader? {
        val user = users.find { property.get(it) == value }
        if (user == null) FxDialogs.showError("User with ${property.name} '$value' was not found")
        return user
    }

    fun <V : Any?> findOrt(property: KProperty1<Ort, V>, value: V): Ort? {
        val ort = orte.find { property.get(it) == value }
        if (ort == null) FxDialogs.showError("Ort with ${property.name} '$value' was not found")
        return ort
    }

    fun logout() {
        loggedInUser = null
    }

    override fun onConnect() {
        users += Database.findAll<Trader>()
        masterUser = users.find { it.master ?: false }
        orte += Database.findAll<Ort>()
    }

    override fun onDisconnect() {
        users.clear()
        orte.clear()
        logout()
    }

    fun onLogin(user: Trader) {
        updateTitle("Kotlin Trader - Logged in as ${user.name}")
        val userId = user.id
        schiff = Database.findFirstBy(Schiff::trader_id, userId)
        schiff?.let {
            val ortId = it.ort_id
            if (ortId == null) throw throw RuntimeException("Data consistency error! No ort was found for id $ortId")
            ort = findOrt(Ort::id, ortId)
        } ?: throw RuntimeException("Data consistency error! No ship was found for user id $userId")
    }

    fun onLogout() = updateTitle("Kotlin Trader")

    private fun updateTitle(title: String) {
        if (primaryStage.titleProperty().isBound) {
            primaryStage.titleProperty().unbind()
        }
        primaryStage.title = title
    }

}