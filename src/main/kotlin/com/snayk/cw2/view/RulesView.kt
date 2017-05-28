package com.snayk.cw2.view

import tornadofx.*

class RulesView(val rules: String) : View("Rules") {
    override val root = vbox {
        padding = insets(10)
        spacing = 10.0
        textarea {
            text = rules
            isEditable = false
            maxWidth = 400.0
        }
    }
}
