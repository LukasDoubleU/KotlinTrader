package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.*
import com.doubleu.kotlintrader.util.FxDialogs
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import kotlin.reflect.KProperty1

object Session : DatabaseAwareController() {

    val users = mutableListOf<Trader>().observable()

    val orte = mutableListOf<Ort>().observable()
    val ortProperty = SimpleObjectProperty<Ort?>()
    var ort by ortProperty
    val ortWaren = mutableListOf<Ort_has_Ware>().observable()

    val schiffProperty = SimpleObjectProperty<Schiff?>()
    var schiff by schiffProperty
    val schiffWaren = mutableListOf<Schiff_has_Ware>().observable()

    val angebote = mutableListOf<Ort_has_Ware>().observable()

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
            if (it) onLogin(loggedInUser!!) else onLogout()
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
        runAsync { Database.findAll<Trader>() } ui {
            users += it
            masterUser = users.find { it.master ?: false }
        }
        runAsync { Database.findAll<Ort>() } ui {
            orte += it
        }
        runAsync { Database.findAll<Ort_has_Ware>().sortedBy { it.ortName }.sortedBy { it.wareName }.sortedByDescending { it.preis } } ui {
            angebote += it
        }
    }

    override fun onDisconnect() {
        users.clear()
        orte.clear()
        angebote.clear()
        logout()
    }

    fun onLogin(user: Trader) {
        updateTitle("Kotlin Trader - Logged in as ${user.name}")
        runAsync {
            val userId = user.id
            Database.findFirstBy(Schiff::trader_id, userId)
        } ui {
            schiff = it
            schiff?.let {
                val schiff = it
                runAsync {
                    val ortId = schiff.ort_id
                    if (ortId == null) throw throw RuntimeException("Data consistency error! No ort was found for id $ortId")
                    findOrt(Ort::id, ortId)
                } ui {
                    ort = it
                    runAsync { Database.findAllBy(Ort_has_Ware::ort_id, ort!!.id) } ui {
                        ortWaren += it
                        runAsync { Database.findAllBy(Schiff_has_Ware::schiff_id, schiff.id) } ui {
                            schiffWaren += it
                        }
                    }
                }
            } ?: throw RuntimeException("Data consistency error! No ship was found for user id ${user.id}")
        }
    }

    fun onLogout() {
        updateTitle("Kotlin Trader")
        ortWaren.clear()
        schiffWaren.clear()
    }

    private fun updateTitle(title: String) {
        if (primaryStage.titleProperty().isBound) {
            primaryStage.titleProperty().unbind()
        }
        primaryStage.title = title
    }

}