package com.doubleu.kotlintrader.util

import com.doubleu.kotlintrader.model.Trader
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.stage.Stage
import tornadofx.*

object Session {

    lateinit var stage: Stage
    val userProperty = SimpleObjectProperty<Trader?>()
    val user = userProperty.get()
    val masterUserProperty = SimpleObjectProperty<Trader?>()
    val loggedIn = SimpleBooleanProperty(false)

    init {
        // when the logged in user changes
        userProperty.onChange {
            val title = userProperty.get()?.let {
                "Kotlin Trader - Logged in as ${it.name}"
            } ?: "Kotlin Trader"
            stage.titleProperty().bind(SimpleStringProperty(title))
        }
    }

    fun logout() {
        userProperty.set(null)
        loggedIn.set(false)
    }

}