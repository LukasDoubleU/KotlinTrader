package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Ort
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Schiff_has_Ware
import com.doubleu.kotlintrader.util.Session
import com.doubleu.kotlintrader.view.TradeView
import tornadofx.*

class TradeController : Controller() {

    val view by inject<TradeView>()

    fun connected() {
        with(view) {
            orte.clear()
            orte += Database.findAll<Ort>()
            hafenTable.items = Database.findAll<Ort_has_Ware>().observable()
            schiffTable.items = Database.findAll<Schiff_has_Ware>().observable()
            traderProperty.value = Session.loggedInUserProperty.name
        }
    }

    fun buy() {
        TODO("not implemented")
    }

    fun sell() {
        TODO("not implemented")
    }


}