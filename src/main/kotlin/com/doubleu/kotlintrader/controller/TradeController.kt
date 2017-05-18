package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.data.Data
import com.doubleu.kotlintrader.data.SchiffWaren
import com.doubleu.kotlintrader.model.Ort
import com.doubleu.kotlintrader.model.Ort_has_Ware
import com.doubleu.kotlintrader.model.Schiff_has_Ware
import com.doubleu.kotlintrader.util.FxDialogs
import tornadofx.*
import java.math.BigDecimal

class TradeController : Controller() {

    /**
     * Changing of the Ort requires the current [User][Data.user] to pay.
     * If he doesn't want to or can't the change is reverted.
     */
    fun travel(nach: Ort) {
        val von = Data.ort ?: return
        if (von == nach) return
        val distance = von.distanceTo(nach)
        val fahrtkosten = Data.schiff!!.fahrkosten
        val price = (fahrtkosten * distance).toLong()
        val decision = FxDialogs.showConfirm("Reise bestaetigen",
                "Von ${von.name} nach ${nach.name} reisen?\nDas wuerde $price kosten!")
        if (decision != FxDialogs.OK) return
        val remaining = Data.user!!.geld - price
        if (remaining < 0) {
            FxDialogs.showError("Nicht genug Geld!")
            return
        }
        Data.user!!.geld = remaining
        Data.ort = nach
    }

    /**
     * Attempts to buy the selected item in the given quantity
     */
    fun buy(selectedItem: Ort_has_Ware, menge: Int) {
        val price = menge * selectedItem.preis.toInt()
        if (menge > selectedItem.menge.toInt()) {
            FxDialogs.showWarning("Nicht mehr genuegend ${selectedItem.wareName} vorhanden")
            return
        }
        if (price > Data.user!!.geld) {
            FxDialogs.showError("Not enough money")
            return
        }
        val decs = FxDialogs.showConfirm("Kauf bestaetigen", "$menge ${selectedItem.wareName} fuer $price kaufen?")
        if (decs != FxDialogs.OK) return
        val ware = SchiffWaren.find { it.ware_id == selectedItem.ware_id } ?: throw RuntimeException()
        selectedItem.menge -= BigDecimal(menge)
        ware.menge += menge
        Data.user!!.geld -= price
    }

    /**
     * Attempts to sell the selected item in the given quantity
     */
    fun sell(selectedItem: Schiff_has_Ware, menge: Int) {
        if (menge > selectedItem.menge) {
            FxDialogs.showWarning("Nicht genuegend ${selectedItem.wareName} vorhanden")
            return
        }
        Data.user?.let { user ->
            val price = selectedItem.preisVorOrt() ?: return
            val decs = FxDialogs.showConfirm("Verkauf bestaetigen", "$menge ${selectedItem.wareName} fuer $price verkaufen?")
            if (decs != FxDialogs.OK) return
            val ware = SchiffWaren.find { it.ware_id == selectedItem.ware_id } ?: throw RuntimeException()
            selectedItem.menge += menge
            ware.menge -= menge
            user.geld += price
        }
    }
}