package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.TradeController
import com.doubleu.kotlintrader.data.*
import com.doubleu.kotlintrader.extensions.center
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Schiff_has_Ware
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.layout.ColumnConstraints
import javafx.scene.paint.Color
import tornadofx.*

/**
 * View responsible for trading.
 * Left table shows [Ware][Ort_has_Ware] depending on the current [Ort][com.doubleu.kotlintrader.data.Data.Ort].
 * Right table shows [Ware][Schiff_has_Ware] depending on the current [Schiff][com.doubleu.kotlintrader.data.Data.Schiff].
 * Bottom panel shows information about the logged in [Trader][com.doubleu.kotlintrader.model.Trader] and his [Schiff][com.doubleu.kotlintrader.model.Schiff]
 */
class TradeView : View("Trade") {

    val controller by inject<TradeController>()

    private val mengeProperty = SimpleIntegerProperty(0)
    private lateinit var buyButton: Button
    private lateinit var sellButton: Button
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
                stackpane {
                    hafenTable = tableview(OrtWaren.get()) {
                        column("ID", Ort_has_Ware::idProperty)
                        column("Name", Ort_has_Ware::wareNameProperty)
                        column("Menge", Ort_has_Ware::mengeProperty)
                        column("Preis", Ort_has_Ware::preisProperty)

                        OrtWaren.onLoadFinish { resizeColumnsToFitContent() }
                    }
                    progressindicator {
                        stackpaneConstraints {
                            alignment = Pos.BOTTOM_RIGHT
                            marginRight = 10.0
                            marginBottom = 10.0
                        }
                        setMaxSize(50.0, 50.0)
                        visibleWhen {
                            OrtWaren.loading.or(Angebote.loading)
                        }
                    }
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
                textfield(mengeProperty) {
                    // Allow only numeric
                    textProperty().onChange {
                        val newValue = it ?: ""
                        if (!newValue.matches("\\d*".toRegex())) {
                            this@textfield.text = newValue.replace("[^\\d]".toRegex(), "")
                        }
                    }
                }
                buyButton = button("Kaufen ->>-") {
                    action { controller.buy(hafenTable.selectedItem!!, mengeProperty.get()) }
                }
                sellButton = button("-<<- Verkaufen") {
                    action { controller.sell(schiffTable.selectedItem!!, mengeProperty.get()) }
                }
            }
            // Schiff Spalte
            vbox(10) {
                gridpaneConstraints {
                    columnRowIndex(2, 0)
                    marginRight = 10.0
                }
                label("Schiff")
                stackpane {
                    schiffTable = tableview(SchiffWaren.get()) {
                        column("ID", Schiff_has_Ware::idProperty)
                        column("Name", Schiff_has_Ware::wareNameProperty)
                        column("Menge", Schiff_has_Ware::mengeProperty)

                        SchiffWaren.onLoadFinish { resizeColumnsToFitContent() }
                    }
                    progressindicator {
                        stackpaneConstraints {
                            alignment = Pos.BOTTOM_RIGHT
                            marginRight = 10.0
                            marginBottom = 10.0
                        }
                        setMaxSize(50.0, 50.0)
                        visibleWhen {
                            SchiffWaren.loading
                        }
                    }
                    buyButton.enableWhen {
                        Bindings.greaterThan(mengeProperty, 0)
                                .and(Bindings.isNotNull(hafenTable.selectionModel.selectedItemProperty()))
                    }
                    sellButton.enableWhen {
                        Bindings.greaterThan(mengeProperty, 0)
                                .and(Bindings.isNotNull(schiffTable.selectionModel.selectedItemProperty()))
                    }
                }
            }
        }
        hbox(40) {
            vbox(20) {
                label("Trader:")
                label("Standort:")
            }
            vbox(20) {
                text(Data.User.model.name)
                text(Data.Ort.model.name) {
                    // To Prevent Layout reordering on valueChange
                    minWidth = 75.0
                }
            }
            vbox(20) {
                label("Geld:")
                label("Reise nach:")
            }
            vbox(20) {
                text(Data.User.model.geldAsString)
                hbox(20) {
                    val blocked = Data.Schiff.model.blocked
                    combobox(Data.Ort, Orte.get()) {
                        disableWhen { blocked }
                    }
                    text("Das Schiff ist geblockt!") {
                        fill = Color.RED
                        visibleWhen { blocked }
                    }
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