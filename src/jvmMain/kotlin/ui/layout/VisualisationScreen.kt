package ui.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.compose.Configuration
import ui.compose.Controller
import viewmodel.ControllerViewModel

@Composable
fun ControllerVisualisation(viewModel: ControllerViewModel) {

    Box {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {


            val snapStrength by viewModel.motorConfig.getSnapStrength().collectAsState()
            val touchSnapPoints by viewModel.motorConfig.getTouchSnapPoints().collectAsState()

            val connectedPortName by viewModel.serialConnection.getConnectedPortName().collectAsState()
            val ports by viewModel.serialConnection.getPortList().collectAsState()
            val selectedPort by viewModel.serialConnection.getSelectedPort().collectAsState(ports.firstOrNull())
            Configuration(
                snapStrength,
                touchSnapPoints,
                viewModel.motorConfig::changeSnapStrength,
                viewModel.motorConfig::changeTouchSnapPointsValue,
                viewModel::sendData,
                viewModel.serialConnection::refreshPorts,
                ports,
                selectedPort,
                viewModel.serialConnection::selectPort,
                viewModel::connectToPort,
                viewModel.serialConnection::disconnectFromPort,
                connectedPortName
            )

            val multiKnob by viewModel.multiKnob.collectAsState()
            Controller(multiKnob, touchSnapPoints)
        }
    }
}