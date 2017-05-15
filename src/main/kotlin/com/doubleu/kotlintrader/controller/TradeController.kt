package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.model.Ort
import com.doubleu.kotlintrader.model.Schiff
import com.doubleu.kotlintrader.util.FxDialogs
import com.doubleu.kotlintrader.view.TradeView
import tornadofx.*

class TradeController : Controller() {

    private val view by inject<TradeView>()
    private var oldOrt = Session.ort

    init {
        // TODO maybe use ChangeListener
        Session.ortProperty.mutateOnChange { travel(it) }
    }

    fun travel(nach: Ort?): Ort? {
        // Just return the new value if it was null previously
        val von = oldOrt
        if (von == null || von == nach) {
            oldOrt = nach
            return oldOrt
        }
        // Null setting shouldn't happen but whatever
        if (nach == null) return von
        // Return in case some coordinates aren't present
        val distance = von.distanceTo(nach)
        val schiff = Session.schiff ?: return von
        var fahrtkosten = schiff.fahrkosten
        fahrtkosten = fahrtkosten ?: schiff.fahrkosten ?: Schiff.FAHRTKOSTEN_DEFAULT
        val price = (fahrtkosten * distance).toLong()
        val decision = FxDialogs.showConfirm("Reise bestätigen", "Von ${von.name} nach ${nach.name} reisen?\nDas würde $price kosten!")
        if (decision != FxDialogs.OK && decision != FxDialogs.YES) return von
        val trader = Session.loggedInUser ?: return von
        val remaining = (trader.geld - price).toLong()
        if (remaining < 0) {
            FxDialogs.showError("Nicht genug Geld!"); return von
        }
        trader.geld = remaining.toDouble()
        oldOrt = nach
        return nach
    }

    fun buy() {
        TODO("not implemented")
    }

    fun sell() {
        TODO("not implemented")
    }


}