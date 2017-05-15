package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.data.Data
import com.doubleu.kotlintrader.model.Ort
import com.doubleu.kotlintrader.util.FxDialogs
import tornadofx.*

class TradeController : Controller() {

    fun travel(von: Ort?, nach: Ort?) {
        // Just return the new value if it was null previously
        if (von == null) return
        // Null setting shouldn't happen but whatever
        if (nach == null) return
        val distance = von.distanceTo(nach)
        val fahrtkosten = Data.schiff?.fahrkosten ?: 0f
        val price = (fahrtkosten * distance).toLong()
        val decision = FxDialogs.showConfirm("Reise bestaetigen", "Von ${von.name} nach ${nach.name} reisen?\nDas wuerde $price kosten!")
        if (decision != FxDialogs.OK && decision != FxDialogs.YES) return
        Data.user!!.let {
            val remaining = (it.geld) - price
            if (remaining < 0) {
                FxDialogs.showError("Nicht genug Geld!")
                return
            }
            it.geld = remaining
        }
    }

    fun buy() {
        TODO("not implemented") // TODO
    }

    fun sell() {
        TODO("not implemented") // TODO
    }


}