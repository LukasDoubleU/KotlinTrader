package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.LoginController
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableView
import tornadofx.*

/**
 * View mit den Login-Funktionen
 *
 * TODO: Sexy GUI
 */
class LoginView : View("Login") {

    val controller: LoginController by inject()

    val ipProperty = SimpleStringProperty("localhost")
    val dbProperty = SimpleStringProperty("mmbbs_trader")

    val nameProperty = SimpleStringProperty("otto")
    val pwProperty = SimpleStringProperty("otto")

    val masterNameProperty = SimpleStringProperty()

    lateinit var userTable: TableView<Trader>

    override val root = hbox {
        vbox {
            vbox {
                hbox {
                    vbox {
                        label("Server IP")
                        textfield(ipProperty)
                    }
                    vbox {
                        label("Datenbank")
                        textfield(dbProperty)
                    }
                }
                button("Connect") {
                    action { controller.connect() }
                }
                disableWhen { Database.connectedProperty }
            }
            vbox {
                label("Name")
                textfield(nameProperty)
                label("Passwort")
                textfield(pwProperty)
                button("Login") {
                    action { controller.login() }
                }
                disableWhen { Database.connectedProperty.not() }
            }
            hbox {
                button("Master") {
                    action { controller.master() }
                }
                textfield(masterNameProperty)
                disableWhen { Database.connectedProperty.not() }
            }
        }

        vbox {
            userTable = tableview {
                column("ID", Trader::id)
                column("Name", Trader::name)
                column("Geld", Trader::geld)
                column("Master", Trader::master)
                isEditable = true
            }
        }
        autosize()
    }

}