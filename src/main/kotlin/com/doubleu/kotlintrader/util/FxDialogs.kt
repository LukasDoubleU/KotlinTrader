package com.doubleu.kotlintrader.util

import javafx.application.Platform
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import javafx.stage.StageStyle
import java.awt.Robot
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Wrapper for JavaFX's OptionPane.
 * Copied from StackOverflow.
 */
object FxDialogs {

    fun showInformation(message: String, title: String = "Information") {
        Platform.runLater {
            val alert = Alert(Alert.AlertType.INFORMATION)
            alert.initStyle(StageStyle.UTILITY)
            alert.title = title
            alert.headerText = title
            alert.contentText = message

            alert.showAndWait()
        }
    }

    fun showWarning(message: String, title: String = "Warning") {
        Platform.runLater {
            val alert = Alert(Alert.AlertType.WARNING)
            alert.initStyle(StageStyle.UTILITY)
            alert.title = title
            alert.headerText = title
            alert.contentText = message

            alert.showAndWait()
        }
    }

    fun showError(message: String, title: String = "Error") {
        Platform.runLater {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.initStyle(StageStyle.UTILITY)
            alert.title = title
            alert.headerText = title
            alert.contentText = message

            alert.showAndWait()
        }
    }

    fun showException(title: String, message: String, exception: Exception) {
        Platform.runLater {
            val alert = Alert(Alert.AlertType.ERROR)
            alert.initStyle(StageStyle.UTILITY)
            alert.title = "Exception"
            alert.headerText = title
            alert.contentText = message

            val sw = StringWriter()
            val pw = PrintWriter(sw)
            exception.printStackTrace(pw)
            val exceptionText = sw.toString()

            val label = Label("Details:")

            val textArea = TextArea(exceptionText)
            textArea.isEditable = false
            textArea.isWrapText = true

            textArea.maxWidth = java.lang.Double.MAX_VALUE
            textArea.maxHeight = java.lang.Double.MAX_VALUE
            GridPane.setVgrow(textArea, Priority.ALWAYS)
            GridPane.setHgrow(textArea, Priority.ALWAYS)

            val expContent = GridPane()
            expContent.maxWidth = java.lang.Double.MAX_VALUE
            expContent.add(label, 0, 0)
            expContent.add(textArea, 0, 1)

            alert.dialogPane.expandableContent = expContent

            alert.showAndWait()
        }
    }

    val YES = "Yes"
    val NO = "No"
    val OK = "OK"
    val CANCEL = "Cancel"

    fun showConfirm(title: String, message: String, vararg options: String): String {
        var options = options
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.initStyle(StageStyle.UTILITY)
        alert.title = "Choose an option"
        alert.headerText = title
        alert.contentText = message

        //To make enter key press the actual focused button, not the first one. Just like pressing "space".
        alert.dialogPane.addEventFilter(KeyEvent.KEY_PRESSED) { event ->
            if (event.code == KeyCode.ENTER) {
                event.consume()
                try {
                    val r = Robot()
                    r.keyPress(java.awt.event.KeyEvent.VK_SPACE)
                    r.keyRelease(java.awt.event.KeyEvent.VK_SPACE)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }

        if (options.isEmpty()) {
            options = arrayOf(OK, CANCEL)
        }

        val buttons = options.map { ButtonType(it) }

        alert.buttonTypes.setAll(buttons)

        val result = alert.showAndWait()
        if (!result.isPresent) {
            return CANCEL
        } else {
            return result.get().text
        }
    }

    fun showTextInput(title: String, message: String, defaultValue: String): String? {
        val dialog = TextInputDialog(defaultValue)
        dialog.initStyle(StageStyle.UTILITY)
        dialog.title = "Input"
        dialog.headerText = title
        dialog.contentText = message

        val result = dialog.showAndWait()
        if (result.isPresent) {
            return result.get()
        } else {
            return null
        }

    }

}