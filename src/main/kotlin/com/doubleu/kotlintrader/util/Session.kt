package com.doubleu.kotlintrader.util

import com.doubleu.trader.Trader
import com.doubleu.trader.database.Database
import com.doubleu.trader.database.MainView
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

object Session {

    val stage = find(MainView::class).primaryStage
    val userProperty = SimpleObjectProperty<Trader>()
    val user = userProperty.get()
    val masterProperty = SimpleObjectProperty<Trader>()
    val master = masterProperty.get()

    init {
        userProperty.onChange {
            val title = userProperty.get()?.let {
                "Kotlin Trader - Logged in as ${it.name}"
            } ?: "Kotlin Trader"
            stage.titleProperty().bind(SimpleStringProperty(title))
        }
        Database.connectedProperty.onChange {
            masterProperty.set(
                    if (Database.connectedProperty.get()) {
                        Database.findBy(Trader::class, Trader::master, true)
                    } else null
            )

        }
    }

}
