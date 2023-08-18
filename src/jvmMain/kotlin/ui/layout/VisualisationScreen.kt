package ui.layout

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ui.compose.*
import viewmodel.ControllerViewModel

@Composable
@Preview
fun ControllerVisualisation(viewModel: ControllerViewModel) {

    Box {
        Row (
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Config(viewModel.motorConfig, viewModel::sendData)
            Box{
                Controller(modifier = Modifier.align(Alignment.Center), viewModel.fingerCount, viewModel.buttonPress)
                SnapPoints(viewModel.motorConfig, viewModel.fingerCount, viewModel)
                Pointer(currentAngleLiveData = viewModel.pointerAngle)
                Touches(viewModel.fingerPos)
            }
        }
    }

}