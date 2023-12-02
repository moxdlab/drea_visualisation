package ui.compose

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
import model.MultiKnob

const val SIZE = 2

@Composable
fun Controller(multiKnob: MultiKnob, touchSnapPoints: List<Int>){
    Box{
        Center(
            modifier = Modifier.align(Alignment.Center),
            multiKnob.fingerCount,
            multiKnob.isButtonPressed
        )
        SnapPoints(touchSnapPoints, multiKnob.fingerCount)
        Pointer(currentAngle = multiKnob.pointerAngle)
        Touches(multiKnob.fingerPosition)
    }
}

@Composable
private fun Center(modifier: Modifier = Modifier, touchCount: Int, buttonPress: Boolean) {

    val color = if (buttonPress) {
        Color.Blue
    } else {
        Color.Gray
    }
    Box(
        modifier = modifier
            .size((250 * SIZE).dp, (250 * SIZE).dp)
            .clip(CircleShape)
            .background(color)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(touchCount.toString(), style = TextStyle(fontSize = (50 * SIZE).sp, color = Color.White))
    }
}



@Composable
private fun SnapPoints(touchSnapPoints: List<Int>, touchCount: Int) {

    if (touchSnapPoints[touchCount] != 0) {
        val steps = 360 / touchSnapPoints[touchCount].toFloat()
        for (i in 0..touchSnapPoints[touchCount]) {
            SnapPointsSurface(modifier = Modifier.fillMaxSize().rotate(180 + steps * i))
        }
    }
}

@Composable
private fun SnapPointsSurface(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size((100 * SIZE).dp, (100 * SIZE).dp)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (150 * SIZE).dp),
            10 * SIZE,
            1 * SIZE,
            Color.Blue
        )
    }
}

@Composable
private fun Pointer(modifier: Modifier = Modifier.fillMaxSize(), currentAngle: Float) {
    Box(
        modifier = modifier.size((100 * SIZE).dp, (100 * SIZE).dp).rotate(currentAngle)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (150 * SIZE).dp),
            20 * SIZE,
            3 * SIZE,
            Color.Red
        )
    }
}

@Composable
private fun Tick(modifier: Modifier = Modifier, length: Int, width: Int, color: Color) {
    Box(
        modifier = modifier
            .size(width.dp, length.dp)
            .background(color)
    )
}


@Composable
private fun Touches(fingerPos: List<Float>) {
    for (i in fingerPos.indices) {
        Touch(modifier = Modifier.fillMaxSize().rotate(fingerPos[i] / 2), fingerPos[i] / 2)
    }
}

@Composable
private fun Touch(modifier: Modifier = Modifier.fillMaxSize(), currentAngle: Float) {
    Box(
        modifier = modifier.size((100 * SIZE).dp, (100 * SIZE).dp).rotate(currentAngle)
    ) {
        Tick(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (120 * SIZE).dp),
            3 * SIZE,
            20 * SIZE,
            Color.Green
        )
    }
}




