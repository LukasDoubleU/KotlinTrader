package com.doubleu.kotlintrader.util

import com.doubleu.kotlintrader.model.Trader
import com.doubleu.kotlintrader.view.MainView
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

object Session {

    val stage = find<MainView>().primaryStage
    val userProperty = SimpleObjectProperty<Trader?>()
    val masterUserProperty = SimpleObjectProperty<Trader?>()

    init {
        // when the logged in user changes
        userProperty.onChange {
            val title = userProperty.get()?.let {
                "Kotlin Trader - Logged in as ${it.name}"
            } ?: "Kotlin Trader"
            stage.titleProperty().bind(SimpleStringProperty(title))
        }
    }

}