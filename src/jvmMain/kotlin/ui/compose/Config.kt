package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
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
    maximalTouches: MutableLiveData<Int>,
    snapStrength: MutableLiveData<Float>,
    touchSnapPoints: MutableLiveData<List<Int>>,
    setMaximalTouches: (Int)->Unit,
    changeTouchSnapPointsValue: (Int,Int)->Unit,
    sendData:()->Unit
) {


    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        Text("Configuration", fontSize = 30.sp)

        SnapStrength(snapStrength)
        MaximalTouches(maximalTouches, setMaximalTouches)

        val touchSnapPointList by touchSnapPoints.observeAsState()
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
fun SnapStrength(snapStrength: MutableLiveData<Float>) {
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
            },
            valueRange = 0f..10f,
            steps = 9,
            modifier = Modifier.padding(horizontal = 8.dp).width(250.dp)
        )
    }
}

@Preview
@Composable
fun MaximalTouches(maximalTouches: MutableLiveData<Int>, setMaximalTouches: (Int) -> Unit) {

    val maxTouchesValue by maximalTouches.observeAsState()

    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Maximal Touches: ", modifier = Modifier.padding(horizontal = 8.dp))
        Text(text = "$maxTouchesValue", modifier = Modifier.padding(horizontal = 8.dp))
        Slider(
            value = maxTouchesValue.toFloat(),
            onValueChange = { newValue ->
                setMaximalTouches(newValue.toInt())
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
        motorConfig.maximalTouches,
        motorConfig.snapStrength,
        motorConfig.touchSnapPoints,
        motorConfig::setMaximalTouches,
        motorConfig::changeTouchSnapPointsValue,
        sendData
    )
}