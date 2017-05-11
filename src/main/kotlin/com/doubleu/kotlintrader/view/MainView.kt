package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.AngeboteController
import com.doubleu.kotlintrader.controller.LoginController
import com.doubleu.kotlintrader.controller.MasterController
import com.doubleu.kotlintrader.controller.TradeController
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.util.Session
import tornadofx.*

class MainView : View("Kotlin Trader") {

    val loginController by inject<LoginController>()
    val loginView by inject<LoginView>()
    val tradeController by inject<TradeController>()
    val tradeView by inject<TradeView>()
    val angeboteController by inject<AngeboteController>()
    val angeboteView by inject<AngeboteView>()
    val masterController by inject<MasterController>()
    val masterView by inject<MasterView>()

    override val root = tabpane {
        Session.stage = primaryStage
        primaryStage.icons += resources.image("/favicon.png")
        setMinSize(600.0, 400.0)
        setMaxSize(600.0, 400.0)

        tab(loginView) {
            isClosable = false
        }

        tab(tradeView) {
            disableProperty().bind(Session.loggedIn.not())
            isClosable = false
        }

        tab(angeboteView) {
            disableProperty().bind(Database.connectedProperty.not())
            isClosable = false
        }

        tab(masterView) {
            disableProperty().bind(Database.connectedProperty.not())
            isClosable = false
        }
    }

}