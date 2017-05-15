package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.*
import com.doubleu.kotlintrader.util.FxDialogs
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.concurrent.Task
import tornadofx.*
import kotlin.reflect.KProperty1

/**
 * Holds Session dependent information that can thus not be stored in the database.
 * All info is stored in [Properties][javafx.beans.property.Property].
 * Properties that depend on each other are also updated by this class.
 */
object Session : Controller() {

    val users = mutableListOf<Trader>().observable()

    val orte = mutableListOf<Ort>().observable()
    val ortProperty = SimpleObjectProperty<Ort?>()
    var ort by ortProperty
    val ortModel = Ort.Model(ortProperty)
    val ortWaren = mutableListOf<Ort_has_Ware>().observable()

    val schiffProperty = SimpleObjectProperty<Schiff?>()
    var schiff by schiffProperty
    val schiffWaren = mutableListOf<Schiff_has_Ware>().observable()

    val angebote = mutableListOf<Ort_has_Ware>().observable()

    val loggedInUserProperty = SimpleObjectProperty<Trader?>()
    var loggedInUser by loggedInUserProperty
    val loggedInUserModel = Trader.Model(loggedInUserProperty)
    val isLoggedIn = Bindings.isNotNull(loggedInUserProperty)!!

    val masterUserProperty = SimpleObjectProperty<Trader?>()
    var masterUser by masterUserProperty
    val isMasterUserLoggedIn = isLoggedIn.and(Bindings.equal(loggedInUserProperty, masterUserProperty))!!

    private val runningTasks = mutableListOf<SessionTask<*>>().observable()

    /**
     * Indicates whether there are active [Tasks][SessionTask] running in this Session
     */
    val loading = Bindings.isNotEmpty(runningTasks)!!

    init {
        Database.connected.onChange {
            if (it) onConnect() else onDisconnect()
        }
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
            async {
                val ortId = schiff.ort_id
                if (ortId == null) throw throw RuntimeException("Data consistency error! No ort was found for id $ortId")
                findOrt(Ort::id, ortId)
            } ui {
                ort = it
            }
            async { Database.findAllBy(Schiff_has_Ware::schiff_id, schiff.id) } ui {
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
            async { Database.findAllBy(Ort_has_Ware::ort_id, ort.id) } ui {
                ortWaren += it
            }
    }

    /**
     * Updates the application title and refreshes the [user] dependent fields, which it does async
     */
    fun onLogin(user: Trader) {
        updateTitle("Kotlin Trader - Logged in as ${user.name}")
        async {
            Database.findFirstBy(Schiff::trader_id, user.id)
                    ?: throw RuntimeException("Data consistency error! No ship was found for user id ${user.id}")
        } ui {
            schiff = it
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
     * Hook method that is executed when a [Database] connection was created.
     * Asyncly fills in the [Session]s properties.
     */
    fun onConnect() {
        async { Database.findAll<Trader>() } ui {
            users += it
            masterUser = users.find { it.master }
        }
        async { Database.findAll<Ort>() } ui {
            orte += it
        }
        async { Database.findAll<Ort_has_Ware>().sortedBy { it.ortName }.sortedBy { it.wareName }.sortedByDescending { it.preis } } ui {
            angebote += it
        }
    }

    /**
     * Executes the given [Function][func] asyncly.
     */
    private fun <T> async(func: SessionTask<*>.() -> T) = SessionTask(func).start()

    /**
     * Hook method, called when the [Database] connection is lost.
     */
    fun onDisconnect() {
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
        Platform.runLater {
            schiff = null
        }
    }

    /**
     * Updates the Application's title to the given [title]
     */
    private fun updateTitle(title: String) {
        Platform.runLater {
            if (primaryStage.titleProperty().isBound) {
                primaryStage.titleProperty().unbind()
            }
            primaryStage.title = title
        }
    }

    /**
     * Task for async execution.
     * Peeked from [FXTask].
     * Updates the [Loading-Property][loading] of the Session.
     */
    private class SessionTask<T>(val func: SessionTask<*>.() -> T) : Task<T>() {

        init {
            setOnFailed({ Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), exception) })
        }

        override fun call() = func(this)

        override fun done() {
            synchronized(runningTasks) {
                runningTasks.remove(this)
            }
        }

        fun start(): SessionTask<T> {
            synchronized(runningTasks) {
                runningTasks += this
            }
            Thread(this).start()
            return this
        }

        infix fun ui(func: (T) -> Unit) = success(func)

        infix fun success(func: (T) -> Unit): Task<T> {
            Platform.runLater {
                setOnSucceeded { func(value) }
            }
            return this
        }
    }

}