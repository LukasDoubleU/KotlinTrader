package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.LoginController
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.model.Trader
import com.doubleu.kotlintrader.util.Session
import javafx.beans.property.SimpleStringProperty
import javafx.scene.control.TableView
import tornadofx.*

/**
 * View mit den Login-Funktionen
 *
 * TODO: Sexy GUI
 */
class LoginView : View("Login") {

    val controller by inject<LoginController>()
    val connected = Database.connected

    val ipProperty = SimpleStringProperty("localhost")
    val dbProperty = SimpleStringProperty("mmbbs_trader")

    val nameProperty = SimpleStringProperty("otto")
    val pwProperty = SimpleStringProperty("otto")

    val masterNameProperty = SimpleStringProperty()

    lateinit var userTable: TableView<Trader>

    override val root = hbox {
        vbox {
            // Datenbankverbindung Felder
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
            // Login Felder
            vbox {
                label("Name")
                textfield(nameProperty)
                label("Passwort")
                textfield(pwProperty)
                button("Login") {
                    action { controller.login() }
                }
                disableWhen { Session.isLoggedIn }
            }
            // Master Felder
            hbox {
                textfield(masterNameProperty)
                button("Master") {
                    action { controller.master() }
                }
                disableWhen { connected.not() }
            }
        }
        // Trader Tabelle
        vbox {
            userTable = tableview<Trader> {
                column("ID", Trader::id)
                column("Name", Trader::name)
                column("Geld", Trader::geld)
                column("Master", Trader::master)
            }
        }
    }

}