package ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun SerialConnection(
    refreshPorts: () -> Unit,
    ports: List<String>,
    selectedPort: String?,
    selectPort: (String) -> Unit,
    connectToPort: () -> Unit,
    disconnectFromPort: () -> Unit,
    isConnected: Boolean,
    connectedPortName: String?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly
    ) {
        ConnectionStatus(connectedPortName)
        Selection(
            refreshPorts, ports, selectedPort, selectPort, connectToPort, disconnectFromPort, isConnected
        )
    }
}


@Composable
private fun ConnectionStatus(connectedPortName: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(20.dp).clip(CircleShape)
                .background(if (connectedPortName != null) Color.Green else Color.Red)
        )
        Spacer(Modifier.padding(5.dp))
        Text(if (connectedPortName != null) "Connected: $connectedPortName" else "Not connected")
    }
}

@Composable
private fun Selection(
    refreshPorts: () -> Unit,
    ports: List<String>,
    selectedPort: String?,
    selectPort: (String) -> Unit,
    connectToPort: () -> Unit,
    disconnectFromPort: () -> Unit,
    isConnected: Boolean
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = { refreshPorts() }) { Text("Refresh") }
        DpSpacer(10)
        PortDropdown(ports = ports, selectedPort = selectedPort, selectPort = selectPort)
        DpSpacer(10)
        Button(onClick = { connectToPort() }) { Text("Connect") }
        DpSpacer(10)
        Button(onClick = { disconnectFromPort() }, enabled = isConnected) { Text("Disconnect") }
    }
}

@Composable
private fun PortDropdown(
    modifier: Modifier = Modifier,
    ports: List<String>,
    selectedPort: String?,
    selectPort: (String) -> Unit
) {
    if (ports.isNotEmpty() && selectedPort != null) {
        var expanded by remember { mutableStateOf(false) }

        Box(modifier = modifier) {
            Text(
                text = selectedPort,
                modifier = Modifier.clickable { expanded = true }.background(Color.LightGray).padding(8.dp)
            )

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                ports.forEach { port ->
                    DropdownMenuItem(onClick = {
                        selectPort(port)
                        expanded = false
                    }) {
                        Text(text = port)
                    }
                }
            }
        }
    } else {
        Text(text = "No ports available")
    }
}
