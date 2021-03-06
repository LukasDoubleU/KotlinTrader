package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.data.OrtWaren
import com.doubleu.kotlintrader.data.Schiffe
import com.doubleu.kotlintrader.data.Storage
import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.extensions.random
import com.doubleu.kotlintrader.model.Schiff
import com.doubleu.kotlintrader.util.FxDialogs
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.control.ProgressIndicator
import tornadofx.*
import java.math.BigDecimal
import java.util.concurrent.ThreadLocalRandom

class MasterController : Controller() {

    /**
     * Progresses the game to the next round by recalcing availability and prices.
     * Ships are being unblocked.
     */
    fun nextStep() {
        OrtWaren.forEach {
            with(it) {
                val verbrauchAbs = menge.toDouble() * verbrauch
                val prodAbs = menge.toDouble() * (produktion + 1)
                menge = BigDecimal((menge.toDouble() + prodAbs - verbrauchAbs).toInt())
                preis = BigDecimal(random(ware.basispreis / 2, ware.basispreis * 2))
            }
        }
        Schiffe.forEach { it.blocked = false }
        FxDialogs.showInformation("Neue Runde begonnen." +
                "\nMenge der Waren vor Ort und deren Preise wurden angepasst.")
    }

    /**
     * Triggers an event that randomly blocks 20% of the ships
     */
    fun trigEvent() {
        val rand = ThreadLocalRandom.current()
        val blocked = mutableListOf<Schiff>()
        Schiffe.forEach {
            if (rand.nextDouble() > 0.79) {
                blocked += it
                it.blocked = true
            }
        }
        val traderList = blocked.map { it.trader.name }.joinToString(prefix = " • ")
        FxDialogs.showInformation("Die Schiffe der folgenden Trader wurden geblockt:\n$traderList")
    }

    /**
     * Resets all Entities that are currently loaded
     */
    fun resetGame() {
        val infoDialog = Dialog<Any>()
        infoDialog.dialogPane.content = ProgressIndicator()
        infoDialog.contentText = "Setze Werte auf ihren Standard zurueck..."
        infoDialog.show()
        runAsync {
            Storage.all().forEach {
                it.asSequence()
                        .filter { it is Entity<out Entity<*>> }
                        .forEach { (it as Entity<out Entity<*>>).reset() }
            }
        } ui {
            infoDialog.dialogPane.buttonTypes.addAll(ButtonType.CANCEL)
            infoDialog.hide()
            FxDialogs.showInformation("Die Werte wurden auf ihren Standard zurueckgesetzt")
        }
    }
}