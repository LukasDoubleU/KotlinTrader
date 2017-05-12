package com.doubleu.kotlintrader.view

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

    private val connected = Database.connected
    private val loggedIn = Session.isLoggedIn

    // TODO Store the following in some local file

    private val ipProperty = SimpleStringProperty("localhost")
    private val dbProperty = SimpleStringProperty("mmbbs_trader")

    private val nameProperty = SimpleStringProperty("otto")
    private val pwProperty = SimpleStringProperty("otto")

    private lateinit var userTable: TableView<Trader>

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
                        action { Database.connect(ipProperty.get(), dbProperty.get()) }
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
                textfield(nameProperty) {
                    disableWhen { loggedIn }
                }
                label("Passwort")
                textfield(pwProperty) {
                    disableWhen { loggedIn }
                }
                buttonbar {
                    button("Login") {
                        action { Session.login(nameProperty.get(), pwProperty.get()) }
                        enableWhen { Database.connected.and(loggedIn.not()) }
                    }
                    button("Logout") {
                        action { Session.logout() }
                        enableWhen { loggedIn }
                    }
                }
            }
            // Master Felder
            hbox {
                label("Master")
                combobox(Session.masterUserProperty, Session.users) {
                    enableWhen { connected }
                    valueProperty().onChange { userTable.refresh() }
                }
            }
        }
        // Trader Tabelle
        vbox {
            userTable = tableview(Session.users) {
                column("ID", Trader::id)
                column("Name", Trader::name)
                column("Geld", Trader::geld)
                column("Master", Trader::master)
            }
        }
    }
}