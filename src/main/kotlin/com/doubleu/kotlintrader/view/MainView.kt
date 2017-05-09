package com.doubleu.trader.database

import com.doubleu.kotlintrader.view.AngeboteView
import com.doubleu.kotlintrader.view.MasterView
import com.doubleu.kotlintrader.view.TradeView
import com.doubleu.trader.view.LoginView
import tornadofx.*

class MainView : View("Kotlin Trader") {

    val loginView: LoginView by inject()
    val tradeView: TradeView by inject()
    val angeboteView: AngeboteView by inject()
    val masterView: MasterView by inject()

    override val root = tabpane {
        setMinSize(600.0, 400.0)

        tab(loginView) {
            isClosable = false
        }

        tab(tradeView) {
            disableProperty().bind(Database.connectedProperty.not())
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