package com.doubleu.kotlintrader.data

import com.doubleu.kotlintrader.controller.TradeController
import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.model.Trader
import com.doubleu.kotlintrader.view.TradeView
import javafx.application.Platform
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

object Data {

    /**
     * A Property representing an Entity
     */
    abstract class EntityProperty<T : Entity<T>> : SimpleObjectProperty<T>() {
        /**
         * An Entity model representing the inherited [Entity][T].
         * By Contract every Entity has one.
         */
        abstract val model: ItemViewModel<T?>
    }

    /**
     * Represents the current user, may be null
     */
    object User : EntityProperty<Trader>() {
        /**
         * Indicates whether a user is logged in
         */
        val isLoggedIn = Bindings.isNotNull(this)!!
        override val model = Trader.Model(this)

        init {
            onChange { user ->
                // Update the fields that depend on this
                if (user == null) {
                    ort = null
                    schiff = null
                    Title.update("Kotlin Trader")
                } else {
                    Title.update("Kotlin Trader - Logged in as ${user.name}")
                    schiff = Schiffe.find { it.trader_id == user.id }
                }
            }
        }
    }

    /**
     * Represents the current user, may be null
     */
    var user: Trader? by User

    /**
     * Represents the current Ort, may be null
     */
    object Ort : EntityProperty<com.doubleu.kotlintrader.model.Ort>() {
        override val model = com.doubleu.kotlintrader.model.Ort.Model(this)
        val traderController = find<TradeController>()
        val tradeView = find<TradeView>()

        init {
            // Update itself when the [Orte] are being reloaded
            Orte.onLoadFinish { ort = it.find { it.id == schiff?.ort_id } }
            // Update the fields that depend on this
            mutateOnChange {
                val new = traderController.travel(it)
                if (new == null || new == schiff?.ort)
                    OrtWaren.clear()
                else {
                    schiff?.ort = new
                    OrtWaren.load()
                }
                return@mutateOnChange new
            }
        }
    }

    /**
     * Represents the current Ort, may be null
     */
    var ort: com.doubleu.kotlintrader.model.Ort? by Ort

    /**
     * Represents the current MasterUser, may be null
     */
    object MasterUser : EntityProperty<Trader>() {
        override val model = Trader.Model(this)
        val isLoggedIn = User.isLoggedIn.and(Bindings.equal(User, this))!!

        init {
            // Update itself when the [Users] are being loaded
            Users.onLoadFinish { masterUser = it.find { it.master } }
            // All previous masters (should only be one) are no longer masters
            onChange {
                Users.filter { it.master }.forEach { it.master = false }
                it?.master = true
            }
        }
    }

    /**
     * Represents the current MasterUser, may be null
     */
    var masterUser: com.doubleu.kotlintrader.model.Trader? by MasterUser

    /**
     * Represents the current Schiff, may be null
     */
    object Schiff : EntityProperty<com.doubleu.kotlintrader.model.Schiff>() {
        override val model = com.doubleu.kotlintrader.model.Schiff.Model(this)

        init {
            // Update itself when the [Schiffe] are being loaded
            Schiffe.onLoadFinish { schiff = Schiffe.find { it.trader_id == user?.id } }
            onChange { schiff ->
                // Refresh the Ort when the ship changes.
                // Usually happens when another user logs in.
                if (schiff == null) {
                    ort = null
                    SchiffWaren.clear()
                } else {
                    ort = Orte.find { it.id == schiff.ort_id }
                    SchiffWaren.load()
                }
            }
        }
    }

    /**
     * Represents the current Schiff, may be null
     */
    var schiff: com.doubleu.kotlintrader.model.Schiff? by Schiff

    private object Title {
        val primaryStage = FX.primaryStage

        fun update(title: String) {
            Platform.runLater {
                if (primaryStage.titleProperty().isBound) {
                    primaryStage.titleProperty().unbind()
                }
                primaryStage.title = title
            }
        }
    }
}