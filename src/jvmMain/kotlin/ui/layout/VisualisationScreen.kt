package ui.layout

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.icerock.moko.mvvm.livedata.compose.observeAsState
import ui.compose.*
import viewmodel.ControllerViewModel

@Composable
@Preview
fun ControllerVisualisation(modifier: Modifier = Modifier.fillMaxSize(), viewModel: ControllerViewModel) {

    val fingerCount by viewModel.fingerCount.observeAsState()
    val pointerAngle by viewModel.pointerAngle.observeAsState()
    val fingerPos by viewModel.fingerPos.observeAsState()

    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Box{
            Controller(modifier = Modifier.align(Alignment.Center), fingerCount)
            //SnapPoints(viewModel.motorConfig.stepsOnFingerCount[fingerCount])
            SnapPoints(12)
            Pointer(currentAngle = -pointerAngle)
            Touches(fingerPos)

        }

        Config(viewModel)
    }
}