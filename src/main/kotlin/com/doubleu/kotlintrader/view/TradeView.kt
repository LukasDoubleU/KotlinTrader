package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.model.Ort
import com.doubleu.kotlintrader.model.Ware
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class TradeView : View("Trade") {

    val mengeProperty = SimpleIntegerProperty(0)
    val traderProperty = SimpleStringProperty("<trader>")
    val geldProperty = SimpleDoubleProperty(0.0)
    val standortProperty = SimpleStringProperty("<standort>")

    override val root = vbox {
        autosize()
        hbox {
            // Hafen Spalte
            vbox {
                label("Hafen")
                tableview<Ware> {
                    column("ID", Ware::id)
                    column("Name", Ware::name)
                    column("Preis", Ware::preis)
//                    column("Menge", Ware -> Ort -> Menge) TODO
                }
            }
            // Handels Spalte
            vbox {
                spacer {}
                label("Menge")
                textfield(mengeProperty)
                button("Kaufen ->>-") {
                    action {
                        //TODO
                    }
                }
                button("-<<- Verkaufen") {
                    action {
                        // TODO
                    }
                }
            }
            // Schiff Spalte
            vbox {
                label("Schiff")
                tableview<Ware> {
                    column("ID", Ware::id)
                    column("Name", Ware::name)
//                    column("Menge", Ware -> Schiff -> Menge) TODO
                }
            }
        }
        hbox {
            hbox {
                label("Trader")
                textfield(traderProperty)
                label("Geld")
                textfield(geldProperty)
            }
            hbox {
                label("Standort")
                textfield(standortProperty)
                label("Reise nach")
                combobox<Ort> {
                    //                    items = trader.orte TODO
                }
            }
        }
    }
}
