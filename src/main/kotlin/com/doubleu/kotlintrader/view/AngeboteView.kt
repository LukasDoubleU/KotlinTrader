package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.Session
import com.doubleu.kotlintrader.model.Ort_has_Ware
import tornadofx.*

/**
 * Displays all [offers][Session.angebote] in a Table.
 * Offers are represented by [Ort_has_Ware].
 */
class AngeboteView : View("Angebote") {
    override val root = vbox {
        stackpane {
            vboxConstraints {
                marginTop = 30.0
                marginLeftRight(60.0)
            }
            tableview(Session.angebote) {
                column("Stadt", Ort_has_Ware::ortName)
                column("Ware", Ort_has_Ware::wareName)
                column("Menge", Ort_has_Ware::menge)
                column("Preis", Ort_has_Ware::preis)

                visibleWhen {
                    Session.loading.not()
                }
            }

            progressindicator {
                visibleWhen {
                    Session.loading
                }
            }
        }
    }
}