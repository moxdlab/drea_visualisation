package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import model.MotorConfig


@Composable
fun Config(
    motorConfig: MotorConfig,
    sendData: () -> Unit,
    refreshPorts: () -> Unit,
    portList: Flow<List<String>>,
    selectedPortFlow: Flow<String?>,
    selectPort: (String) -> Unit,
    connectToPort: () -> Unit,
    disconnectFromPort: () -> Unit,
    connectedPortNameFlow: Flow<String?>
) {
    Config(
        motorConfig.getSnapStrength(),
        motorConfig.getTouchSnapPoints(),
        motorConfig::changeSnapStrength,
        motorConfig::changeTouchSnapPointsValue,
        sendData,
        refreshPorts,
        portList,
        selectedPortFlow,
        selectPort,
        connectToPort,
        disconnectFromPort,
        connectedPortNameFlow
    )
}


@Composable
fun Config(
    snapStrength: Flow<Float>,
    touchSnapPoints: Flow<List<Int>>,
    changeSnapStrength: (Float) -> Unit,
    changeTouchSnapPointsValue: (Int, Int) -> Unit,
    sendData: () -> Unit,
    refreshPorts: () -> Unit,
    portList: Flow<List<String>>,
    selectedPortFlow: Flow<String?>,
    selectPort: (String) -> Unit,
    connectToPort: () -> Unit,
    disconnectFromPort: () -> Unit,
    connectedPortNameFlow: Flow<String?>
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Configuration", fontSize = 30.sp)

        val isConnected by connectedPortNameFlow.map { connectedPortName ->
            connectedPortName != null
        }.collectAsState(initial = false)

        SnapStrength(snapStrength, changeSnapStrength, sendData, isConnected)

        val touchSnapPointList by touchSnapPoints.collectAsState(listOf(0))
        //Same Snaps on different Touches
        TouchSnapPoint(touchSnapPointList, changeTouchSnapPointsValue, sendData, isConnected)

        //Different Snaps on different Touches
        /*
        LazyColumn(
            modifier = Modifier.height(300.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(touchSnapPointList.size) { index ->
                TouchSnapPoints(touchSnapPointList[index], index, changeTouchSnapPointsValue)
            }
        }

        Button(onClick = {sendData()}){
            Text("Upload")
        }
        */
        //Dummy Data
        SerialConnectionStatus(connectedPortNameFlow)
        SerialSelection(refreshPorts, portList, selectedPortFlow, selectPort, connectToPort, disconnectFromPort, isConnected)
    }
}

@Composable
fun TouchSnapPoints(touchValue: Int, i: Int, changeTouchSnapPointsValue: (Int, Int) -> Unit) {
    val pattern = remember { Regex("^\\d*\$") }
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Snap Strength $i: ", modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "$touchValue", modifier = Modifier.padding(horizontal = 8.dp))
        TextField(
            onValueChange = {
                val value = if (it == "") {
                    0
                } else if (!it.matches(pattern)) {
                    touchValue
                } else if (it.toInt() >= 1000) {
                    999
                } else {
                    it.toInt()
                }
                changeTouchSnapPointsValue(value, i)
            },
            value = touchValue.toString()
        )
    }
}

//TODO Slider composable
@Composable
fun TouchSnapPoint(touchValue: List<Int>, changeTouchSnapPointsValue: (Int, Int) -> Unit, sendData: () -> Unit, isConnected: Boolean) {

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Snap Points: ", modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "${touchValue.last()}", modifier = Modifier.padding(horizontal = 8.dp))
        Slider(
            enabled = isConnected,
            value = touchValue.last().toFloat(),
            onValueChange = { newValue ->
                for (i in touchValue.indices) {
                    changeTouchSnapPointsValue(newValue.toInt(), i)
                }
                sendData()
            },
            valueRange = 0f..360f,
            steps = 359,
            modifier = Modifier.padding(horizontal = 8.dp).width(250.dp)
        )
    }
}


@Composable
fun SnapStrength(snapStrength: Flow<Float>, changeTouchSnapPointsValue: (Float) -> Unit, sendData: () -> Unit, isConnected: Boolean) {
    val snapStrengthValue by snapStrength.collectAsState(1f)

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Snap Strength: ", modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "${snapStrengthValue.toInt()}", modifier = Modifier.padding(horizontal = 8.dp))
        Slider(
            enabled = isConnected,
            value = snapStrengthValue,
            onValueChange = { newValue ->
                changeTouchSnapPointsValue(newValue)
                sendData()
            },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.padding(horizontal = 8.dp).width(250.dp)
        )
    }
}