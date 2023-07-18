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
fun ControllerVisualisation(viewModel: ControllerViewModel) {

    val fingerCount by viewModel.fingerCount.observeAsState()
    val pointerAngle by viewModel.pointerAngle.observeAsState()
    val fingerPos by viewModel.fingerPos.observeAsState()
    val buttonPress by viewModel.buttonPress.observeAsState()

    Row (
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Config(viewModel)
        Box{
            Controller(modifier = Modifier.align(Alignment.Center), fingerCount, buttonPress)
            //SnapPoints(viewModel.motorConfig.stepsOnFingerCount[fingerCount])
            SnapPoints(12)
            Pointer(currentAngle = -pointerAngle)
            Touches(fingerPos)
        }
    }
}