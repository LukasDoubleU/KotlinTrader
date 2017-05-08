package com.doubleu.trader.view

import tornadofx.*

class LoginView : View() {

	override val root = vbox {
		textfield("User")
		textfield("Password")
	}

}