package com.doubleu.trader

import com.doubleu.trader.database.Database
import com.doubleu.trader.database.MainView
import com.doubleu.trader.model.Fahrt
import tornadofx.*

class KotlinTraderApp : App(MainView::class) {
    init {
        Database.connect("mmbbs_trader", "root")
        println("Connected")
        val trader = Trader(1)
        println("Name: ${trader.name} Geld: ${trader.geld}")

        val fahrt = Fahrt(1, 2)
        println(fahrt.id_von)
        println(fahrt.id_nach)
        println(fahrt.strecke)
    }
}