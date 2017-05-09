package com.doubleu.kotlintrader.util

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.stage.Stage
import tornadofx.*

object Session {

    lateinit var stage: Stage
    val userProperty = SimpleObjectProperty<Trader>()
    val masterUserProperty = SimpleObjectProperty<Trader>()
    val isMaster = masterUserProperty.selectBoolean { it?.masterProperty ?: SimpleBooleanProperty(false) }

    init {
        userProperty.onChange {
            val title = userProperty.get()?.let {
                "Kotlin Trader - Logged in as ${it.name}"
            } ?: "Kotlin Trader"
            stage.titleProperty().bind(SimpleStringProperty(title))
        }
        Database.connectedProperty.onChange {
            masterUserProperty.set(
                    if (Database.connectedProperty.get()) {
                        Database.findBy(Trader::master, true)
                    } else null
            )
        }
    }

}