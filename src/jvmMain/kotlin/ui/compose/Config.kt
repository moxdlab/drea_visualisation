package ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewmodel.ControllerViewModel

@Composable
fun Config(viewModel: ControllerViewModel){
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        for (i in 0 until viewModel.motorConfig.stepsOnFingerCount.size){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("FingerCount: $i")
                ChangeButtons(viewModel, i)
            }

        }
    }
}

@Composable
fun ChangeButtons(viewModel: ControllerViewModel, index:Int){


    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
            ){
        Button(onClick = {
            viewModel.changeStepsOnFingerCount(index, viewModel.motorConfig.stepsOnFingerCount[index]-1)
        }){
            Text("-")
        }
        Text("${viewModel.motorConfig.stepsOnFingerCount[index]}", modifier = Modifier.padding(15.dp))
        Button(onClick = {
            viewModel.changeStepsOnFingerCount(index, viewModel.motorConfig.stepsOnFingerCount[index]+1)
        }){
            Text("+")
        }
    }
}