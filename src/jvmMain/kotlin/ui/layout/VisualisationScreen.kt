package ui.layout

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.compose.*
import viewmodel.ControllerViewModel

@Composable
@Preview
fun ControllerVisualisation(viewModel: ControllerViewModel) {

    Box {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Config(
                viewModel.motorConfig,
                viewModel::sendData,
                viewModel.serialConnection::refreshPorts,
                viewModel.serialConnection.getPortList(),
                viewModel.serialConnection::selectPort,
                viewModel.serialConnection::connectToPort
            )
            Box {
                val multiKnob = viewModel.multiKnob.collectAsState()
                Controller(
                    modifier = Modifier.align(Alignment.Center),
                    multiKnob.value.fingerCount,
                    multiKnob.value.isButtonPressed
                )
                SnapPoints(viewModel.motorConfig.getTouchSnapPoints(), multiKnob.value.fingerCount)
                Pointer(currentAngle = multiKnob.value.pointerAngle)
                Touches(multiKnob.value.fingerPosition)
            }
        }
    }

}