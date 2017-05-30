package com.snayk.cw2.view

import com.snayk.cw2.system.DecisionSystem
import covering
import exhaustive
import javafx.scene.control.Alert
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import lem
import tornadofx.*
import java.io.File

class MainView : View("Rule Generator") {
    var systemFileTextField: TextField by singleAssign()
    var chosenFile: File? = null
    var decisionSystem: DecisionSystem? = null
    var systemText: TextArea by singleAssign()
    var algorithmsBox: VBox by singleAssign()

    override val root = vbox {
        padding = insets(10)
        spacing = 10.0

        label("Decision system file: ")
        hbox {
            spacing = 5.0
            systemFileTextField = textfield {
                style {
                    prefWidth = 400.px
                }
                isEditable = false
            }
            button("Select") {
                action {
                    chosenFile = chooseFile("Choose file with system",
                            arrayOf(FileChooser.ExtensionFilter("Text file", "*.txt"),
                                    FileChooser.ExtensionFilter("All types", "*.*"))).firstOrNull()

                    if (chosenFile?.absolutePath != null) {
                        systemFileTextField.text = chosenFile?.absolutePath
                    }
                }
            }
        }
        button("Read file") {
            action {
                if (chosenFile != null) {
                    try {
                        decisionSystem = DecisionSystem(chosenFile!!.readText())
                        systemText.text = decisionSystem.toString()
                        algorithmsBox.isVisible = true
                    } catch (e: Exception) {
                        alert(Alert.AlertType.ERROR, "Error", "Provided file is in wrong format or contains and error!")
                    }
                } else {
                    alert(Alert.AlertType.ERROR, "Error", "No file chosen!")
                }
            }
        }
        label("Decision System:")
        systemText = textarea {
            isEditable = false

        }
        algorithmsBox = vbox {
            spacing = 5.0
            isVisible = false
            label("Generate rules:")
            hbox {
                spacing = 5.0
                button("Covering") {
                    action {
                        try {
                            val rules = covering(decisionSystem as DecisionSystem)
                            decisionSystem?.resetCoverable()
                            RulesView(rules).openWindow()
                        } catch (e: Exception) {
                            alert(Alert.AlertType.ERROR, "Error", "Provided file is in wrong format or contains and error!")
                        }
                    }
                }
                button("Exhaustive") {
                    action {
                        try {
                            val rules = exhaustive(decisionSystem as DecisionSystem)
                            decisionSystem?.resetCoverable()
                            RulesView(rules).openWindow()
                        } catch (e: Exception) {
                            alert(Alert.AlertType.ERROR, "Error", "Provided file is in wrong format or contains and error!")
                        }
                    }
                }
                button("LEM2") {
                    action {
                        try {
                            val rules = lem(decisionSystem as DecisionSystem)
                            decisionSystem?.resetCoverable()
                            RulesView(rules).openWindow()
                        } catch (e: Exception) {
                            alert(Alert.AlertType.ERROR, "Error", "Provided file is in wrong format or contains and error!")
                        }
                    }
                }
            }
        }
    }
}