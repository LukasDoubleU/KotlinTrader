package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.MasterController
import com.doubleu.kotlintrader.extensions.center
import javafx.scene.text.Font
import tornadofx.*

class MasterView : View("Master Functions") {

    val controller by inject<MasterController>()

    override val root = vbox(20) {
        center()
        button("Nächste Runde") {
            font = Font.font(24.0)
            action { controller.nextStep() }
        }
        button("Ereignis auslösen") {
            font = Font.font(24.0)
            action { controller.trigEvent() }
        }
        button("Neues Spiel") {
            vboxConstraints { marginTop = 20.0 }
            font = Font.font(24.0)
            action { controller.resetGame() }
        }
    }
}