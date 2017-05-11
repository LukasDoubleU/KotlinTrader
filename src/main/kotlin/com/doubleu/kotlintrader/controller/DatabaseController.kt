package com.doubleu.kotlintrader.controller

import tornadofx.*

object DatabaseController : Controller() {

    val tradeController by inject<TradeController>()

    fun connected() = tradeController.connected()

}