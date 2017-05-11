package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.TradeController
import com.doubleu.kotlintrader.model.Ort
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Schiff_has_Ware
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableView
import tornadofx.*

class TradeView : View("Trade") {

    val controller by inject<TradeController>()

    val orte = mutableListOf<Ort>().observable()

    val mengeProperty = SimpleIntegerProperty(0)
    val traderProperty = SimpleStringProperty("<trader>")
    val geldProperty = SimpleDoubleProperty(0.0)
    val standortProperty = SimpleStringProperty("<standort>")
    val ortProperty = SimpleObjectProperty<Ort>()

    lateinit var hafenTable: TableView<Ort_has_Ware>
    lateinit var schiffTable: TableView<Schiff_has_Ware>

    override val root = vbox {
        hbox {
            // Hafen Spalte
            vbox {
                label("Hafen")
                hafenTable = tableview<Ort_has_Ware> {
                    column("ID", Ort_has_Ware::ware_id)
                    column("Name", Ort_has_Ware::wareName)
                    column("Menge", Ort_has_Ware::menge)
                    column("Preis", Ort_has_Ware::preis)
                }
                // Handels Spalte
                vbox {
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
                    schiffTable = tableview<Schiff_has_Ware> {
                        column("ID", Schiff_has_Ware::ware_id)
                        column("Name", Schiff_has_Ware::wareName)
                        column("Menge", Schiff_has_Ware::menge)
                    }
                }
                hbox {
                    hbox {
                        label("Trader")
                        textfield(traderProperty) {
                            isEditable = false
                        }
                        label("Geld")
                        textfield(geldProperty) {
                            isEditable = false
                        }
                    }
                    hbox {
                        label("Standort")
                        textfield(standortProperty)
                        label("Reise nach")
                        combobox<Ort>(ortProperty, orte)
                    }
                }
            }
        }
    }
}
