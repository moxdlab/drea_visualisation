package viewmodel

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import jssc.SerialPort
import model.MotorConfig


class ControllerViewModel: ViewModel() {

    val motorConfig = MotorConfig()


    private val _fingerCount: MutableLiveData<Int> = MutableLiveData(0)
    val fingerCount: LiveData<Int>
        get() = _fingerCount

    private val _pointerAngle: MutableLiveData<Float> = MutableLiveData(0f)
    val pointerAngle: LiveData<Float>
        get() = _pointerAngle

    private val _fingerPos: MutableLiveData<List<Float>> = MutableLiveData(listOf(0f,0f,0f,0f,0f))
    val fingerPos: LiveData<List<Float>>
        get() = _fingerPos


    private var _serialPort: SerialPort = SerialPort("/dev/cu.usbserial-1440")

    init {
        // replace with your Arduino's serial port name
        _serialPort.openPort()
        _serialPort.setParams(9600, 8, 1, 0)


        var buffer = ""
        var currentDataType = 'X'
        var buildPos = mutableListOf<Float>()
        var error = false



        _serialPort.addEventListener { event ->
            if (event.isRXCHAR) {
                val data = _serialPort.readBytes(event.eventValue)
                for (c in String(data)){
                    if (c == '[' && !error){
                        error=true;
                        println("Error")
                    }else if (c == '\n'){
                        error = false
                    }else if (error) continue
                    if(c == 'T' || c == 'A' || c == 'P'){
                        currentDataType = c
                        buffer = ""
                    }else if(c == 'e'){
                        if(currentDataType == 'P'){
                            buildPos.add(buffer.toFloat())
                        }
                        buffer = ""
                    }else if(c == 'E'){
                        if(currentDataType == 'T'){
                            if(_fingerCount.value != buffer.toInt())
                                _fingerCount.value = buffer.toInt()
                        }else if(currentDataType == 'A'){
                            if(_pointerAngle.value != buffer.toFloat())
                                _pointerAngle.value = buffer.toFloat()
                        }else if(currentDataType == 'P'){
                            _fingerPos.value = buildPos
                            buildPos = mutableListOf()
                        }
                        buffer = ""
                    }else{
                        buffer += c
                    }
                }
            }
        }
    }

    fun changeStepsOnFingerCount(index:Int, steps:Int){
        if(steps >= 0) {
            motorConfig.stepsOnFingerCount[index] = steps
            writeToSerial("$index$steps;")
            println("$index$steps")
        }

    }

    fun writeToSerial(input: String){
        _serialPort.writeBytes(input.toByteArray())
    }

}
