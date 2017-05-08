package com.doubleu.trader.database

import com.doubleu.trader.view.LoginView
import tornadofx.*

class MainView : View() {

	override val root = tabpane {
		tab("Login") {
			LoginView()
		}
	}

}