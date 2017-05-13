package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.*
import com.doubleu.kotlintrader.util.FxDialogs
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import kotlin.reflect.KProperty1

/**
 * Holds Session dependent information that can thus not be stored in the database.
 * All info is stored in [Properties][javafx.beans.property.Property].
 * Properties that depend on each other are also updated by this class.
 */
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
        loggedInUserProperty.onChange {
            it?.let { onLogin(it) } ?: onLogout()
        }
        schiffProperty.onChange { refreshSchiff(it) }
        ortProperty.onChange { refreshOrt(it) }
    }

    /**
     * Refreshes the fields depending on the [schiff].
     * Runs async.
     */
    private fun refreshSchiff(schiff: Schiff?) {
        schiffWaren.clear()
        if (schiff != null) {
            runAsync {
                val ortId = schiff.ort_id
                if (ortId == null) throw throw RuntimeException("Data consistency error! No ort was found for id $ortId")
                findOrt(Ort::id, ortId)
            } ui {
                ort = it
            }
            runAsync { Database.findAllBy(Schiff_has_Ware::schiff_id, schiff.id) } ui {
                schiffWaren += it
            }
        } else {
            ort = null
        }
    }

    /**
     * Refreshes the fields depending on the [ort]
     */
    private fun refreshOrt(ort: Ort?) {
        ortWaren.clear()
        if (ort != null)
            runAsync { Database.findAllBy(Ort_has_Ware::ort_id, ort.id) } ui {
                ortWaren += it
            }
    }

    /**
     * Updates the application title and refreshes the [user] dependent fields, which it does async
     */
    fun onLogin(user: Trader) {
        updateTitle("Kotlin Trader - Logged in as ${user.name}")
        runAsync {
            Database.findFirstBy(Schiff::trader_id, user.id)
                    ?: throw RuntimeException("Data consistency error! No ship was found for user id ${user.id}")
        } ui {
            schiff = it
        }
    }

    /**
     * Attempts to login the [Trader] with the given name.
     * Will display an error when the [password][pw] is wrong.
     */
    fun login(name: String, pw: String) {
        val user = findUser(Trader::name, name) ?: return
        if (!user.checkPw(pw)) {
            FxDialogs.showError("Falsches Passwort")
        } else {
            loggedInUser = user
        }
    }

    /**
     * Tries to find the [user][Trader] whose [property] matches the given [value].
     * Searches in the [users] already loaded in the [Session].
     * Displays an error when no [user][Trader] was found.
     */
    fun <V : Any?> findUser(property: KProperty1<Trader, V>, value: V): Trader? {
        val user = users.find { property.get(it) == value }
        if (user == null) FxDialogs.showError("User with ${property.name} '$value' was not found")
        return user
    }

    /**
     * Tries to find the [ort][Ort] whose [property] matches the given [value].
     * Searches in the [orte] already loaded in the [Session].
     * Displays an error when no [ort][Ort] was found.
     */
    fun <V : Any?> findOrt(property: KProperty1<Ort, V>, value: V): Ort? {
        val ort = orte.find { property.get(it) == value }
        if (ort == null) FxDialogs.showError("Ort with ${property.name} '$value' was not found")
        return ort
    }

    /**
     * Performs the [logout]
     */
    fun logout() {
        loggedInUser = null
    }

    /**
     * Hook method that is executed when a [Database] connection was created.
     * Asyncly fills in the [Session]s properties.
     */
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

    /**
     * Hook method, called when the [Database] connection is lost.
     */
    override fun onDisconnect() {
        logout()
        users.clear()
        orte.clear()
        angebote.clear()
    }

    /**
     * Hook method that is called when a logout was performed.
     * Clears [Session] properties.
     */
    fun onLogout() {
        updateTitle("Kotlin Trader")
        schiff = null
    }

    /**
     * Updates the Application's title to the given [title]
     */
    private fun updateTitle(title: String) {
        if (primaryStage.titleProperty().isBound) {
            primaryStage.titleProperty().unbind()
        }
        primaryStage.title = title
    }

}