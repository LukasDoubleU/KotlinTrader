package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.Session
import com.doubleu.kotlintrader.model.Ort_has_Ware
import tornadofx.*

class AngeboteView : View("Angebote") {
    override val root = tableview(Session.angebote) {
        column("Stadt", Ort_has_Ware::ortName)
        column("Ware", Ort_has_Ware::wareName)
        column("Menge", Ort_has_Ware::menge)
        column("Preis", Ort_has_Ware::preis)
    }
}
