package viewmodel

import model.MotorConfig
import model.SerialConnection


class ControllerViewModel {

    val motorConfig = MotorConfig()

    val serialConnection = SerialConnection()

    val multiKnob = serialConnection.getMultiKnob()

    fun sendData() {
        serialConnection.sendData(motorConfig.getSnapStrength().value, motorConfig.getTouchSnapPoints().value)
    }

}