package viewmodel

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import jssc.SerialPort
import model.MotorConfig
import java.io.File
import java.time.Instant


class ControllerViewModel {

    val motorConfig = MotorConfig()


    private val _fingerCount: MutableLiveData<Int> = MutableLiveData(0)
    val fingerCount: LiveData<Int>
        get() = _fingerCount

    private val _pointerAngle: MutableLiveData<Float> = MutableLiveData(0f)
    val pointerAngle: LiveData<Float>
        get() = _pointerAngle

    private val _fingerPos: MutableLiveData<List<Float>> = MutableLiveData(listOf())
    val fingerPos: LiveData<List<Float>>
        get() = _fingerPos

    private val _buttonPress: MutableLiveData<Boolean> = MutableLiveData(false)
    val buttonPress: LiveData<Boolean>
        get() = _buttonPress

    private val directory = File("/dev")
    private val cuPorts = directory.listFiles { file ->
        file.name.startsWith("cu.usbserial")
    }

    //private val _serialPort: SerialPort = SerialPort("/dev/cu.usbserial-1460")
    private val _serialPort: SerialPort = SerialPort(cuPorts?.first()?.absolutePath)

    init {
        _serialPort.openPort()
        _serialPort.setParams(115200, 8, 1, 0)

        var time = Instant.now()
        var counter = 0;

        var buffer = ""
        _serialPort.addEventListener { event ->
            if (event.isRXCHAR) {
                val data = _serialPort.readBytes(event.eventValue)
                for (c in String(data)){
                    if(c == '#'){
                        if(buffer.startsWith("#")){
                            //println(buffer)
                            try {
                                val fields = buffer.substring(1).split(";").map { it.toInt() }
                                if(fields[fields.size-1] == buffer.length){
                                    //Button
                                    val pressed = fields[0] != 0
                                    if(_buttonPress.value != pressed)
                                        _buttonPress.value = pressed
                                    //Pointer Angle
                                    val angle = fields[1].toFloat()/100
                                    if(_pointerAngle.value != angle)
                                        _pointerAngle.value = angle
                                    //Finger count
                                    if(_fingerCount.value != fields[2])
                                        _fingerCount.value = fields[2]
                                    //Finger pos
                                    val positions = mutableListOf<Float>()
                                    for (i in 0 until fields[2]){
                                        positions.add(fields[3+i].toFloat()/100)
                                    }
                                    if(_fingerPos.value != positions)
                                        _fingerPos.value = positions
                                }else{
                                    println("Error: False package length")
                                }
                            }catch (_: NumberFormatException){
                                println("Error: Not an Integer")
                            }
                        }
                        buffer = "#"

                        counter++
                        if(time.epochSecond+1 < Instant.now().epochSecond){
                            time = Instant.now()
                            println(counter)
                            counter = 0
                        }
                    }else{
                        buffer+= c
                    }
                }
            }
        }
    }

    fun sendData(){
        var output = "#"
        output+= motorConfig.snapStrength.value.toString() + ";"

        for (touchSnapPoints in motorConfig.touchSnapPoints.value){
            output+= "$touchSnapPoints;"
        }

        output += "$"
        println(output)
        _serialPort.writeBytes(output.toByteArray())
    }

}
