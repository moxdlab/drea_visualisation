package ui.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DpSpacer(dp: Int, modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.padding(dp.dp))
}