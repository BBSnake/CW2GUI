package com.snayk.cw2.app

import com.snayk.cw2.view.MainView
import javafx.stage.Stage
import tornadofx.App

class MyApp: App(MainView::class) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.isResizable = false
    }
}