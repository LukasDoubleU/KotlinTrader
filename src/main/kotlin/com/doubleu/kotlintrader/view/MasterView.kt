package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.MasterController
import tornadofx.*

class MasterView : View("Master Functions") {

    val controller by inject<MasterController>()

    override val root = vbox {
        button("Nächste Runde") {
            action { controller.nextStep() }
        }
        button("Ereignis auslösen") {
            action { controller.trigEvent() }
        }
        button("Neues Spiel") {
            action { controller.resetGame() }
        }
    }
}
