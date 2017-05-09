package com.doubleu.trader.view

import com.doubleu.kotlintrader.util.FxDialogs
import com.doubleu.kotlintrader.util.Session
import com.doubleu.trader.Trader
import com.doubleu.trader.database.Database
import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import javafx.scene.control.TableView
import tornadofx.*

/**
 * View mit den Login-Funktionen
 *
 * TODO: Create LoginController
 * TODO: Sexy GUI
 */
class LoginView : View("Login") {

    val ipProperty = SimpleStringProperty("localhost")
    val dbProperty = SimpleStringProperty("mmbbs_trader")

    val nameProperty = SimpleStringProperty("otto")
    val pwProperty = SimpleStringProperty("otto")

    val masterProperty = SimpleStringProperty("nero")

    lateinit var databaseBox: Node
    lateinit var loginBox: Node
    lateinit var masterBox: Node
    lateinit var userTable: TableView<Trader>

    override val root = hbox {
        vbox {
            databaseBox = vbox {
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
                    setOnMouseClicked {
                        Database.connect(ipProperty.get(), dbProperty.get())
                        userTable.items = Database.findAll(Trader::class)
                    }
                }
            }
            databaseBox.disableWhen { Database.connectedProperty }
            loginBox = vbox {
                label("Name")
                textfield(nameProperty)
                label("Passwort")
                textfield(pwProperty)
                button("Login") {
                    setOnMouseClicked {
                        val name = nameProperty.get()
                        val user = Database.findBy(Trader::class, Trader::name, name)
                        if(user==null){
                            FxDialogs.showError("User $name not found")
                            return@setOnMouseClicked
                        }
                        if (!user.checkPw(pwProperty.get())) {
                            FxDialogs.showError("Falsches Passwort")
                        } else {
                            FxDialogs.showInformation("Logged in")
                            Session.userProperty.set(user)
                        }
                    }
                }
            }
            loginBox.disableWhen { Database.connectedProperty.not() }
            masterBox = hbox {
                button("Master") {
                    // TODO
                }
                textfield(masterProperty)
            }
            masterBox.disableWhen { Database.connectedProperty.not() }
        }

        vbox {
            userTable = tableview {
                column("ID", Trader::id)
                column("Name", Trader::name)
                column("Geld", Trader::geld)
            }
        }
        autosize()
    }

}