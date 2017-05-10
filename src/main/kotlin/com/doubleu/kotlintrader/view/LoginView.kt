package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.LoginController
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import javafx.beans.property.SimpleStringProperty
import tornadofx.*

/**
 * View mit den Login-Funktionen
 *
 * TODO: Sexy GUI
 */
class LoginView : View("Login") {

    val controller: LoginController by inject()
    val connected = Database.connectedProperty

    val ipProperty = SimpleStringProperty("localhost")
    val dbProperty = SimpleStringProperty("mmbbs_trader")

    val nameProperty = SimpleStringProperty("otto")
    val pwProperty = SimpleStringProperty("otto")

    val masterNameProperty = SimpleStringProperty()

    val userTable = tableview<Trader> {
        column("ID", Trader::id)
        column("Name", Trader::name)
        column("Geld", Trader::geld)
        column("Master", Trader::master)
    }

    override val root = hbox {
        vbox {
            // Datenbankverbindung Felder
            spacer { }
            vbox {
                hbox {
                    vbox {
                        label("Server IP")
                        textfield(ipProperty)
                        disableWhen { connected }
                    }
                    vbox {
                        label("Datenbank")
                        textfield(dbProperty)
                        disableWhen { connected }
                    }
                }
                buttonbar {
                    button("Connect") {
                        action { controller.connect() }
                        disableWhen { connected }
                    }
                    button("Disconnect") {
                        action { Database.disconnect() }
                        enableWhen { connected }
                    }
                }
            }
            spacer { }
            // Login Felder
            vbox {
                label("Name")
                textfield(nameProperty)
                label("Passwort")
                textfield(pwProperty)
                button("Login") {
                    action { controller.login() }
                }
                disableWhen { connected.not() }
            }
            spacer { }
            // Master Felder
            hbox {
                textfield(masterNameProperty)
                button("Master") {
                    action { controller.master() }
                }
                disableWhen { connected.not() }
            }
            spacer { }
        }
        // Trader Tabelle
        vbox {
            userTable
        }
    }

}