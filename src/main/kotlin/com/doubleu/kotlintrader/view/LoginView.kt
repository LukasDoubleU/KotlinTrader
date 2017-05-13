package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.Session
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.extensions.center
import com.doubleu.kotlintrader.extensions.fillHorizontally
import com.doubleu.kotlintrader.model.Trader
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import tornadofx.*

/**
 * Presents the option for the database connection,
 * the login and assigning the master user.
 * The table on the right lists all available users.
 */
class LoginView : View("Login") {

    private val connected = Database.connected
    private val loggedIn = Session.isLoggedIn

    // TODO Store the following in some local file

    private val ipProperty = SimpleStringProperty("localhost")
    private val dbProperty = SimpleStringProperty("mmbbs_trader")

    private val nameProperty = SimpleStringProperty("nero")
    private val pwProperty = SimpleStringProperty("nero")

    private lateinit var userTable: TableView<Trader>
    private lateinit var userNameField: TextField

    override val root = hbox(20) {
        paddingAll = 20
        center()
        vbox(20) {
            alignment = Pos.TOP_CENTER
            // Datenbankverbindung Felder
            vbox(10) {
                hbox(10) {
                    vbox(10) {
                        label("Server IP")
                        textfield(ipProperty)
                        disableWhen { connected }
                    }
                    vbox(10) {
                        label("Datenbank")
                        textfield(dbProperty)
                        disableWhen { connected }
                    }
                }
                buttonbar {
                    button("Connect") {
                        action {
                            Database.connect(ipProperty.get(), dbProperty.get())
                            userNameField.requestFocus()
                        }
                        disableWhen { connected }
                    }
                    button("Disconnect") {
                        action { Database.disconnect() }
                        enableWhen { connected }
                    }
                }
            }
            // Login Felder
            vbox(10) {
                label("Name")
                userNameField = textfield(nameProperty) {
                    enableWhen { connected.and(loggedIn.not()) }
                }
                label("Passwort")
                passwordfield(pwProperty) {
                    enableWhen { connected.and(loggedIn.not()) }
                }
                buttonbar {
                    button("Login") {
                        action { Session.login(nameProperty.get(), pwProperty.get()) }
                        enableWhen { connected.and(loggedIn.not()).and(Bindings.isNotEmpty(Session.users)) }
                    }
                    button("Logout") {
                        action { Session.logout() }
                        enableWhen { loggedIn }
                    }
                }
            }
            // Master Felder
            hbox(10) {
                fillHorizontally()
                label("Master")
                combobox(Session.masterUserProperty, Session.users) {
                    fillHorizontally()
                    enableWhen { connected }
                    valueProperty().onChange { userTable.refresh() }
                }
            }
        }
        // Trader Tabelle
        vbox {
            userTable = tableview(Session.users) {
                isFocusTraversable = false

                column("ID", Trader::id)
                column("Name", Trader::name)
                column("Geld", Trader::geld)

                contextmenu {
                    item("Assign New Master").action {
                        selectedItem?.let {
                            Session.masterUser = it
                        }
                    }
                }
            }
        }
    }
}