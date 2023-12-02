package ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Configuration(
    snapStrength: Float,
    touchSnapPoints: List<Int>,
    changeSnapStrength: (Float) -> Unit,
    changeTouchSnapPointsValue: (Int, Int) -> Unit,
    sendData: () -> Unit,
    refreshPorts: () -> Unit,
    ports: List<String>,
    selectedPort: String?,
    selectPort: (String) -> Unit,
    connectToPort: () -> Unit,
    disconnectFromPort: () -> Unit,
    connectedPortName: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Configuration", fontSize = 30.sp)

        val isConnected = !connectedPortName.isNullOrEmpty()

        SnapStrength(snapStrength, changeSnapStrength, sendData, isConnected)

        TouchSnapPoint(touchSnapPoints, changeTouchSnapPointsValue, sendData, isConnected)

        SerialConnection(
            refreshPorts,
            ports,
            selectedPort,
            selectPort,
            connectToPort,
            disconnectFromPort,
            isConnected,
            connectedPortName
        )

    }
}

@Composable
fun TouchSnapPoint(
    touchSnapPoints: List<Int>,
    changeTouchSnapPointsValue: (Int, Int) -> Unit,
    sendData: () -> Unit,
    isConnected: Boolean
) {

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Snap Points: ", modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "${touchSnapPoints.last()}", modifier = Modifier.padding(horizontal = 8.dp))
        Slider(
            enabled = isConnected, value = touchSnapPoints.last().toFloat(), onValueChange = { newValue ->
                for (i in touchSnapPoints.indices) {
                    changeTouchSnapPointsValue(newValue.toInt(), i)
                }
                sendData()
            }, valueRange = 0f..360f, steps = 359, modifier = Modifier.padding(horizontal = 8.dp).width(250.dp)
        )
    }
}


@Composable
fun SnapStrength(
    snapStrength: Float,
    changeTouchSnapPointsValue: (Float) -> Unit,
    sendData: () -> Unit,
    isConnected: Boolean
) {

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Snap Strength: ", modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "${snapStrength.toInt()}", modifier = Modifier.padding(horizontal = 8.dp))
        Slider(
            enabled = isConnected, value = snapStrength, onValueChange = { newValue ->
                changeTouchSnapPointsValue(newValue)
                sendData()
            }, valueRange = 0f..10f, steps = 9, modifier = Modifier.padding(horizontal = 8.dp).width(250.dp)
        )
    }
}