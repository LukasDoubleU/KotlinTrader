package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.Session
import com.doubleu.kotlintrader.controller.TradeController
import com.doubleu.kotlintrader.extensions.center
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Schiff_has_Ware
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.TableView
import javafx.scene.layout.ColumnConstraints
import tornadofx.*

class TradeView : View("Trade") {

    val controller by inject<TradeController>()

    private val mengeProperty = SimpleIntegerProperty(0)

    private lateinit var hafenTable: TableView<Ort_has_Ware>
    private lateinit var schiffTable: TableView<Schiff_has_Ware>

    override val root = vbox(20) {
        paddingAll = 20
        center()
        // Hafen & Schiff
        gridpane {
            columnConstraints.addAll(columnConstraints(40), columnConstraints(20), columnConstraints(40))
            // Hafen Spalte
            vbox(10) {
                gridpaneConstraints {
                    columnRowIndex(0, 0)
                    marginLeft = 10.0
                }
                label("Hafen")
                hafenTable = tableview(Session.ortWaren) {
                    column("ID", Ort_has_Ware::ware_id)
                    column("Name", Ort_has_Ware::wareName)
                    column("Menge", Ort_has_Ware::menge)
                    column("Preis", Ort_has_Ware::preis)
                    items.onChange { resizeColumnsToFitContent() }
                }
            }
            // Handels Spalte
            vbox(20) {
                gridpaneConstraints {
                    columnRowIndex(1, 0)
                    marginLeftRight(10.0)
                }
                center()
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
            vbox(10) {
                gridpaneConstraints {
                    columnRowIndex(2, 0)
                    marginRight = 10.0
                }
                label("Schiff")
                schiffTable = tableview(Session.schiffWaren) {
                    column("ID", Schiff_has_Ware::ware_id)
                    column("Name", Schiff_has_Ware::wareName)
                    column("Menge", Schiff_has_Ware::menge)
                    items.onChange { resizeColumnsToFitContent() }
                }
            }
        }
        hbox(40) {
            vbox(20) {
                label("Trader:")
                label("Standort:")
            }
            vbox(20) {
                text(Session.loggedInUserProperty.stringBinding { it?.name ?: "" })
                text(Session.ortProperty.stringBinding { it?.name ?: "" }) {
                    // To Prevent Layout reordering on valueChange
                    minWidth = 75.0
                }
            }
            vbox(20) {
                label("Geld:")
                label("Reise nach:")
            }
            vbox(20) {
                text(Session.loggedInUserProperty.doubleBinding { it?.geld ?: 0.0 }.asString())
                combobox(Session.ortProperty, Session.orte) {
                    disableWhen { Session.schiffProperty.booleanBinding { it?.blocked ?: false } }
                }
            }
        }
    }

    private fun columnConstraints(number: Number): ColumnConstraints {
        val cc = ColumnConstraints()
        cc.percentWidth = number.toDouble()
        return cc
    }
}