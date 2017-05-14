package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.Session
import com.doubleu.kotlintrader.database.Database
import javafx.scene.control.Tab
import javafx.scene.control.TabPane
import javafx.scene.control.Tooltip
import tornadofx.*

/**
 * Tying the other views together in a [TabPane]
 */
class MainView : View("Kotlin Trader") {

    val loginView by inject<LoginView>()
    val tradeView by inject<TradeView>()
    val angeboteView by inject<AngeboteView>()
    val masterView by inject<MasterView>()

    override val root = tabpane {
        primaryStage.icons += resources.image("/favicon.png")
        setPrefSize(600.0, 400.0)
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

        tab(loginView)

        tab(tradeView) {
            showReasonForDisable(this, "You are not logged in!")
            disableProperty().bind(Session.isLoggedIn.not())
        }

        tab(angeboteView) {
            showReasonForDisable(this, "There is no database connection!")
            disableProperty().bind(Database.connected.not())
        }

        tab(masterView) {
            showReasonForDisable(this, "You are not the Master User!")
            disableProperty().bind(Session.isMasterUserLoggedIn.not())
        }
    }

    private fun showReasonForDisable(tab: Tab, reason: String) {
        tab.disableProperty().onChange {
            if (it) {
                if (tab.tooltip == null) tab.tooltip = Tooltip()
                tab.tooltip.text = reason
            } else tab.tooltip = null
        }
    }

}