// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ui.layout.ControllerVisualisation
import viewmodel.ControllerViewModel


fun main() = application {


    val controllerViewModel = ControllerViewModel()

    Window(onCloseRequest = ::exitApplication, title = "MultiKnob Visualisation") {
        ControllerVisualisation(viewModel = controllerViewModel)
    }
}
