package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.Session
import com.doubleu.kotlintrader.database.Database
import javafx.scene.control.TabPane
import tornadofx.*

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

        // TODO try to bind enable-state properly

        tab(tradeView) {
            disableProperty().bind(Session.isLoggedIn.not())
        }

        tab(angeboteView) {
            disableProperty().bind(Database.connected.not())
        }

        tab(masterView) {
            disableProperty().bind(Session.isMasterUserLoggedIn.not())
        }
    }

}