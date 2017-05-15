package com.doubleu.kotlintrader.data

import com.doubleu.kotlintrader.controller.TradeController
import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.onChangeWithOld
import com.doubleu.kotlintrader.model.Trader
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

abstract class EntityProperty<T : Entity<T>> : SimpleObjectProperty<T>() {
    abstract val model: ItemViewModel<T?>
}

object Data {

    object User : EntityProperty<Trader>() {
        val isLoggedIn = Bindings.isNotNull(this)!!
        override val model = Trader.Model(this)
    }

    var user: Trader? by User

    object Ort : EntityProperty<com.doubleu.kotlintrader.model.Ort>() {
        override val model = com.doubleu.kotlintrader.model.Ort.Model(this)

        init {
            onChangeWithOld {
                von, nach ->
                find<TradeController>().travel(von, nach)
            }
            // Refresh the Ort when the ship changes.
            // Usually happens when another user logs in.
            Schiff.onChange {
                val schiffOrtId = it?.ort_id
                ort = Orte.find { it.id == schiffOrtId }
            }
            User.onChange {
                if (it == null) ort = null
            }
        }
    }

    var ort: com.doubleu.kotlintrader.model.Ort? by Ort

    object MasterUser : EntityProperty<Trader>() {
        override val model = Trader.Model(this)
        val isLoggedIn = User.isLoggedIn.and(Bindings.equal(User, this))!!

        init {
            Users.onLoadFinish {
                masterUser = it.firstOrNull { it.master }
            }
            // All previous masters (should only be one) are no longer masters
            onChange {
                Users.filter { it.master }.forEach { it.master = false }
                it?.master = true
            }
        }
    }

    var masterUser: com.doubleu.kotlintrader.model.Trader? by MasterUser

    object Schiff : EntityProperty<com.doubleu.kotlintrader.model.Schiff>() {
        override val model = com.doubleu.kotlintrader.model.Schiff.Model(this)

        init {
            User.onChange {
                it?.let {
                    val traderId = it.id
                    schiff = Schiffe.find { it.trader_id == traderId }
                }
            }
        }
    }

    var schiff: com.doubleu.kotlintrader.model.Schiff? by Schiff

    private object Title {
        val primaryStage = FX.primaryStage

        init {
            User.onChange {
                update(if (it == null) "Kotlin Trader"
                else "Kotlin Trader - Logged in as ${it.name}")
            }
        }

        private fun update(title: String) {
            Platform.runLater {
                if (primaryStage.titleProperty().isBound) {
                    primaryStage.titleProperty().unbind()
                }
                primaryStage.title = title
            }
        }
    }
}