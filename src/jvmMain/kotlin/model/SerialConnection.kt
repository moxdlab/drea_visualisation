package model

import jssc.SerialPort
import jssc.SerialPortException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.time.Instant

class SerialConnection {


    private val multiKnob = MutableStateFlow(MultiKnob(0, 0f, listOf(), false))
    fun getMultiKnob(): StateFlow<MultiKnob> = multiKnob.asStateFlow()


    private val directory = File("/dev")
    private val portList: MutableStateFlow<List<String>> = directory.listFiles { file ->
        file.name.startsWith("cu.usbserial")
    }?.let { MutableStateFlow(it.map { file -> file.absolutePath }) } ?: MutableStateFlow(listOf())

    fun getPortList() = portList.asStateFlow()

    fun refreshPorts() {
        portList.value = directory.listFiles { file ->
            file.name.startsWith("cu.usbserial")
        }?.let { it.map { file -> file.absolutePath } } ?: listOf()
    }

    private val selectedPort: MutableStateFlow<String?> =
        MutableStateFlow(if (portList.value.isNotEmpty()) portList.value.first() else null)

    //"/dev/cu.usbserial-1460"
    fun selectPort(port: String) {
        selectedPort.value = port
    }


    private var serialPort: SerialPort? = null

    fun connectToPort() {
        try {
            if (serialPort != null && serialPort!!.isOpened) {
                serialPort?.removeEventListener()
                serialPort?.closePort()
            }


            serialPort = SerialPort(selectedPort.value ?: return)
            serialPort!!.openPort()
            serialPort!!.setParams(115200, 8, 1, 0)
        } catch (ex: SerialPortException) {
            println(ex.message)
            return
        }

        var time = Instant.now()
        var counter = 0;

        var buffer = ""
        serialPort!!.addEventListener { event ->
            if (event.isRXCHAR) {
                val data = serialPort!!.readBytes(event.eventValue)
                for (c in String(data)) {
                    if (c == '#') {
                        if (buffer.startsWith("#")) {
                            //println(buffer)
                            try {
                                val fields = buffer.substring(1).split(";").map { it.toInt() }
                                if (fields[fields.size - 1] == buffer.length) {
                                    //Button
                                    val pressed = fields[0] != 0
                                    //Pointer Angle
                                    val angle = fields[1].toFloat() / 100
                                    //Finger count
                                    val fingerCount = fields[2]
                                    //Finger pos
                                    val fingerPositions = mutableListOf<Float>()
                                    for (i in 0 until fields[2]) {
                                        fingerPositions.add(fields[3 + i].toFloat() / 100)
                                    }

                                    multiKnob.value = MultiKnob(fingerCount, angle, fingerPositions, pressed)
                                } else {
                                    println("Error: False package length")
                                }
                            } catch (_: NumberFormatException) {
                                println("Error: Not an Integer")
                            }
                        }
                        buffer = "#"

                        counter++
                        if (time.epochSecond + 1 < Instant.now().epochSecond) {
                            time = Instant.now()
                            println(counter)
                            counter = 0
                        }
                    } else {
                        buffer += c
                    }
                }
            }
        }
    }


    fun sendData(snapStrength: Float, touchSnapPoint: List<Int>) {
        var output = "#"
        output += "$snapStrength;"

        for (touchSnapPoints in touchSnapPoint) {
            output += "$touchSnapPoints;"
        }

        output += "$"
        serialPort?.writeBytes(output.toByteArray()) ?: return
        println(output)
    }

}