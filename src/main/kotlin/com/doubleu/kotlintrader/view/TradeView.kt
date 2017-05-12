package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.TradeController
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Schiff_has_Ware
import com.doubleu.kotlintrader.util.Session
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.TableView
import tornadofx.*

class TradeView : View("Trade") {

    val controller by inject<TradeController>()

    private val mengeProperty = SimpleIntegerProperty(0)

    private lateinit var hafenTable: TableView<Ort_has_Ware>
    private lateinit var schiffTable: TableView<Schiff_has_Ware>

    override val root = vbox {
        // Hafen & Schiff
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
        }
        hbox {
            label("Trader")
            textfield(Session.masterUserProperty.stringBinding { it?.name ?: "" })
            label("Geld")
            textfield(Session.loggedInUserProperty.doubleBinding { it?.geld ?: 0.0 })
        }
        hbox {
            label("Standort")
            textfield(Session.ortProperty.stringBinding { it?.name ?: "" })
            label("Reise nach")
            combobox(Session.ortProperty, Session.orte)
        }
    }
}