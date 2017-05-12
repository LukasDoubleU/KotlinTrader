package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.util.Session
import tornadofx.*

class MainView : View("Kotlin Trader") {

    val loginView by inject<LoginView>()
    val tradeView by inject<TradeView>()
    val angeboteView by inject<AngeboteView>()
    val masterView by inject<MasterView>()

    override val root = tabpane {
        primaryStage.icons += resources.image("/favicon.png")
        setPrefSize(600.0, 400.0)

        tab(loginView) {
            isClosable = false
        }

        // TODO try to bind enable-state properly

        tab(tradeView) {
            disableProperty().bind(Session.isLoggedIn.not())
            isClosable = false
        }

        tab(angeboteView) {
            disableProperty().bind(Database.connected.not())
            isClosable = false
        }

        tab(masterView) {
            disableProperty().bind(Session.isMasterUserLoggedIn.not())
            isClosable = false
        }
    }

}