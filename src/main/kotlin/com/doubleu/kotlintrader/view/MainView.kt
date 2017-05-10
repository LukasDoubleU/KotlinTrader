package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.database.Database
import tornadofx.*

class MainView : View("Kotlin Trader") {

    val loginView: LoginView by inject()
    val tradeView: TradeView by inject()
    val angeboteView: AngeboteView by inject()
    val masterView: MasterView by inject()

    override val root = tabpane {
        primaryStage.icons += resources.image("/favicon.png")
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