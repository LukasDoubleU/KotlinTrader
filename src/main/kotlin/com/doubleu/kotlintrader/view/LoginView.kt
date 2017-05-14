package com.doubleu.kotlintrader.view

import com.doubleu.kotlintrader.controller.LoginController
import com.doubleu.kotlintrader.controller.Session
import com.doubleu.kotlintrader.database.Database
import com.doubleu.kotlintrader.extensions.center
import com.doubleu.kotlintrader.extensions.fillHorizontally
import com.doubleu.kotlintrader.model.Trader
import com.doubleu.kotlintrader.util.Settings
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

    val controller by inject<LoginController>()

    private val connected = Database.connected
    private val loggedIn = Session.isLoggedIn

    private val hostProperty = SimpleStringProperty(Settings.host)
    private val databaseProperty = SimpleStringProperty(Settings.database)
    private val dbUserProperty = SimpleStringProperty(Settings.dbUser)
    private val dbPasswordProperty = SimpleStringProperty(Settings.dbPassword)

    private val userProperty = SimpleStringProperty(Settings.user)
    private val passwordProperty = SimpleStringProperty(Settings.password)

    private lateinit var userTable: TableView<Trader>
    private lateinit var userNameField: TextField

    override val root = hbox(20) {
        paddingAll = 20
        center()
        vbox(20) {
            alignment = Pos.TOP_CENTER
            // Datenbankverbindung Felder
            vbox(10) {
                vbox(10) {
                    hbox(10) {
                        vbox(10) {
                            label("Host")
                            textfield(hostProperty)
                        }
                        vbox(10) {
                            label("Datenbank")
                            textfield(databaseProperty)
                        }
                    }
                    hbox(10) {
                        vbox(10) {
                            label("DB Nutzer")
                            textfield(dbUserProperty)
                        }
                        vbox(10) {
                            label("DB Passwort")
                            passwordfield(dbPasswordProperty)
                        }
                    }
                    disableWhen { connected }
                }
                buttonbar {
                    button("Connect") {
                        action {
                            controller.connect(hostProperty.get(), databaseProperty.get(), dbUserProperty.get(), dbPasswordProperty.get())
                            userNameField.requestFocus()
                        }
                        disableWhen { connected }
                    }
                    button("Disconnect") {
                        action { controller.disconnect() }
                        enableWhen { connected.and(Session.loading.not()) }
                    }
                }
            }
            // Login Felder
            vbox(10) {
                label("Name")
                userNameField = textfield(userProperty) {
                    enableWhen { connected.and(loggedIn.not()) }
                }
                label("Passwort")
                passwordfield(passwordProperty) {
                    enableWhen { connected.and(loggedIn.not()) }
                }
                buttonbar {
                    button("Login") {
                        action { controller.login(userProperty.get(), passwordProperty.get()) }
                        enableWhen { connected.and(loggedIn.not()).and(Bindings.isNotEmpty(Session.users)) }
                    }
                    button("Logout") {
                        action { controller.logout() }
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
        stackpane {
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

                visibleWhen {
                    Session.loading.not()
                }
            }
            progressindicator {
                visibleWhen {
                    Session.loading
                }
            }
        }
    }
}