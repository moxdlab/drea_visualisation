package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import model.MotorConfig

@Preview
@Composable
fun Config(
    snapStrength: MutableLiveData<Float>,
    touchSnapPoints: MutableLiveData<List<Int>>,
    changeTouchSnapPointsValue: (Int,Int)->Unit,
    sendData:()->Unit
) {


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        Text("Configuration", fontSize = 30.sp)

        SnapStrength(snapStrength, sendData)

        val touchSnapPointList by touchSnapPoints.observeAsState()
        //Same Snaps on different Touches
        TouchSnapPoint(touchSnapPointList, changeTouchSnapPointsValue, sendData)

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
    }
}

@Preview
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
                val value = if(it == ""){
                    0
                }else if(!it.matches(pattern)){
                    touchValue
                }else if(it.toInt() >= 1000){
                    999
                }else{
                    it.toInt()
                }
                changeTouchSnapPointsValue(value, i)
            },
            value = touchValue.toString()
        )
    }
}

@Preview
@Composable
fun TouchSnapPoint(touchValue: List<Int>, changeTouchSnapPointsValue: (Int, Int) -> Unit, sendData: () -> Unit) {

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Snap Strength: ", modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "${touchValue.last()}", modifier = Modifier.padding(horizontal = 8.dp))
        Slider(
            value = touchValue.last().toFloat(),
            onValueChange = { newValue ->
                for (i in 1 until touchValue.size){
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



@Preview
@Composable
fun SnapStrength(snapStrength: MutableLiveData<Float>, sendData: () -> Unit) {
    val snapStrengthValue by snapStrength.observeAsState()

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Snap Strength: ", modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "${snapStrengthValue.toInt()}", modifier = Modifier.padding(horizontal = 8.dp))
        Slider(
            value = snapStrengthValue,
            onValueChange = { newValue ->
                snapStrength.value = newValue
                sendData()
            },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.padding(horizontal = 8.dp).width(250.dp)
        )
    }
}

@Composable
fun Config(motorConfig: MotorConfig, sendData:()->Unit){
    Config(
        motorConfig.snapStrength,
        motorConfig.touchSnapPoints,
        motorConfig::changeTouchSnapPointsValue,
        sendData
    )
}