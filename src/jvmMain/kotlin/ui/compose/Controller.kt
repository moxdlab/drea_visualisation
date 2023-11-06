package ui.compose

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import model.MotorConfig
import viewmodel.ControllerViewModel

val SIZE = 2

@Composable
@Preview
fun Controller(modifier: Modifier = Modifier, touchCountLiveData: LiveData<Int>, buttonPressLiveData: LiveData<Boolean>) {
    val touchCount = touchCountLiveData.observeAsState()
    val buttonPress = buttonPressLiveData.observeAsState()

    val color = if(buttonPress.value){
        Color.Red
    }else{
        Color.Gray
    }
    Box(
        modifier = modifier
            .size((250*SIZE).dp, (250*SIZE).dp)
            .clip(CircleShape)
            .background(color)
            .wrapContentSize(Alignment.Center)
    ){
        Text(touchCount.value.toString(), style = TextStyle(fontSize = (50*SIZE).sp, color = Color.White))
    }
}

@Composable
fun SnapPoints(motorConfig: MotorConfig, touchCountLiveData: LiveData<Int>, controllerViewModel: ControllerViewModel) {
    val touchSnapPoints = motorConfig.touchSnapPoints.observeAsState()
    val touchCount = touchCountLiveData.observeAsState()

    if(touchSnapPoints.value[touchCount.value] != 0){
        val steps = 360 / touchSnapPoints.value[touchCount.value].toFloat()
        for (i in 0..touchSnapPoints.value[touchCount.value]) {
            SnapPointsSurface(modifier = Modifier.fillMaxSize().rotate( 180 + steps * i))
        }
    }
}

@Preview
@Composable
fun SnapPointsSurface(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size((100*SIZE).dp, (100*SIZE).dp)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (150*SIZE).dp),
            10*SIZE,
            1*SIZE,
            Color.Blue
        )
    }
}

@Composable
fun Pointer(modifier: Modifier = Modifier.fillMaxSize(), currentAngleLiveData: LiveData<Float>){
    val currentAngle = currentAngleLiveData.observeAsState()
    Box(
        modifier = modifier.size((100*SIZE).dp, (100*SIZE).dp).rotate(currentAngle.value)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (150*SIZE).dp),
            20*SIZE,
            3*SIZE,
            Color.Red
        )
    }
}

@Preview
@Composable
fun Tick(modifier: Modifier = Modifier, length: Int, width: Int, color:Color) {
    Box(
        modifier = modifier
            .size(width.dp, length.dp)
            .background(color)
    )
}


@Composable
fun Touches(fingerPosLiveData: LiveData<List<Float>>) {
    val fingerPos = fingerPosLiveData.observeAsState()
    for (i in fingerPos.value.indices) {
        Touch(modifier = Modifier.fillMaxSize().rotate(fingerPos.value[i]/2), fingerPos.value[i]/2)
    }
}

@Composable
fun Touch(modifier: Modifier = Modifier.fillMaxSize(), currentAngle: Float){
    Box(
        modifier = modifier.size((100*SIZE).dp, (100*SIZE).dp).rotate(currentAngle)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (120*SIZE).dp),
            3*SIZE,
            20*SIZE,
            Color.Green
        )
    }
}




