package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.TradeController
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Ort
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Ware
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class TradeView : View("Trade") {

    val controller: TradeController by inject()

    val mengeProperty = SimpleIntegerProperty(0)
    val traderProperty = SimpleStringProperty("<trader>")
    val geldProperty = SimpleDoubleProperty(0.0)
    val standortProperty = SimpleStringProperty("<standort>")
    val ortProperty = SimpleObjectProperty<Ort>()

    val hafenTable = tableview<Ort_has_Ware> {
        column("ID", Ort_has_Ware::ware_id)
//        column("Name", Ort_has_Ware::ortName)
        column("Preis", Ort_has_Ware::preis)
        column("Menge", Ort_has_Ware::menge)
    }

    val schiffTable = tableview<Ware> {
        column("ID", Ware::id)
        column("Name", Ware::name)
//                    column("Menge", Ware -> Schiff -> Menge) TODO
    }

    override val root = vbox {
        hbox {
            // Hafen Spalte
            vbox {
                label("Hafen")
                hafenTable
            }
            // Handels Spalte
            vbox {
                spacer {}
                label("Menge")
                textfield(mengeProperty)
                button("Kaufen ->>-") {
                    action { controller.buy() }
                }
                button("-<<- Verkaufen") {
                    action { controller.sell() }
                }
            }
            // Schiff Spalte
            vbox {
                label("Schiff")
                schiffTable
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
                combobox<Ort>(ortProperty, Database.findAll<Ort>())
            }
        }
    }
}
