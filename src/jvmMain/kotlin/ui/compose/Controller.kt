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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewmodel.ControllerViewModel


@Composable
@Preview
fun Controller(modifier: Modifier = Modifier, fingerCount: Int) {
    Box(
        modifier = modifier
            .size(250.dp, 250.dp)
            .clip(CircleShape)
            .background(Color.Gray)
            .wrapContentSize(Alignment.Center)
    ){
        Text(fingerCount.toString(), style = TextStyle(fontSize = 50.sp, color = Color.White))
    }
}

@Composable
fun SnapPoints(numberOfSnapPoints: Int) {
    val steps = 360 / numberOfSnapPoints.toFloat()
    for (i in 0..numberOfSnapPoints) {
            SnapPointsSurface(modifier = Modifier.fillMaxSize().rotate(steps * i))
    }
}

@Preview
@Composable
fun SnapPointsSurface(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(100.dp, 100.dp)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 150.dp),
            10,
            1,
            Color.Blue
        )
    }
}

@Composable
fun Pointer(modifier: Modifier = Modifier.fillMaxSize(), currentAngle: Float){
    Box(
        modifier = modifier.size(100.dp, 100.dp).rotate(currentAngle)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 150.dp),
            20,
            3,
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
fun Touches(fingerPos: List<Float>) {
    for (i in fingerPos.indices) {
        //It is 361 when not set
        if(fingerPos[i]!=361f){
            Touch(modifier = Modifier.fillMaxSize().rotate(fingerPos[i]/2), fingerPos[i]/2)
        }
    }
}

@Composable
fun Touch(modifier: Modifier = Modifier.fillMaxSize(), currentAngle: Float){
    Box(
        modifier = modifier.size(100.dp, 100.dp).rotate(currentAngle)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 120.dp),
            3,
            20,
            Color.Green
        )
    }
}




