package com.doubleu.kotlintrader.controller

import com.doubleu.kotlintrader.data.OrtWaren
import com.doubleu.kotlintrader.data.Storage
import com.doubleu.kotlintrader.database.Entity
import com.doubleu.kotlintrader.model.Ort_has_Ware
import tornadofx.*

class MasterController : Controller() {

    fun nextStep() {
        OrtWaren.forEach { progress(it) }
    }

    private fun progress(ortWare: Ort_has_Ware) {

    }

    fun trigEvent() {
        TODO("not implemented")
    }

    /**
     * Resets all Entities that are currently being
     */
    fun resetGame() = Storage.all().forEach {
        it.asSequence()
                .filter { it is Entity<out Entity<*>> }
                .forEach { (it as Entity<out Entity<*>>).reset() }
        nextStep()
    }
}