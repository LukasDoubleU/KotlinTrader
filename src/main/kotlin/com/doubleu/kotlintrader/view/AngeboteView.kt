package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.data.Angebote
import com.doubleu.kotlintrader.model.Ort_has_Ware
import javafx.geometry.Pos
import tornadofx.*

/**
 * Displays all [offers][Angebote] in a Table.
 * Offers are represented by [Ort_has_Ware].
 */
class AngeboteView : View("Angebote") {
    override val root = vbox {
        stackpane {
            vboxConstraints {
                marginTop = 30.0
                marginLeftRight(60.0)
            }
            tableview(Angebote.get()) {
                column("Stadt", Ort_has_Ware::ortName)
                column("Ware", Ort_has_Ware::wareName)
                column("Menge", Ort_has_Ware::menge)
                column("Preis", Ort_has_Ware::preis)

                Angebote.onLoadFinish { resizeColumnsToFitContent() }
            }

            progressindicator {
                stackpaneConstraints {
                    alignment = Pos.BOTTOM_RIGHT
                    marginRight = 10.0
                    marginBottom = 10.0
                }
                setMaxSize(50.0, 50.0)
                visibleWhen {
                    Angebote.loading
                }
            }
        }
    }
}