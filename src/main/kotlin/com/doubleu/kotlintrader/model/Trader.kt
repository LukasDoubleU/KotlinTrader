package com.doubleu.trader

class Trader(override val id: Int) : Entity() {

	var name by DatabaseDelegate<String>()
	var passwort by DatabaseDelegate<String>()
	var geld by DatabaseDelegate<Double>()
	var master by DatabaseDelegate<Boolean>()

}