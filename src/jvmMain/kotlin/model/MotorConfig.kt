package model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class MotorConfig {

    private val snapStrength: MutableStateFlow<Float> = MutableStateFlow(1f)
    fun getSnapStrength() = snapStrength.asStateFlow()

    fun changeSnapStrength(value: Float){
        snapStrength.value = value
    }


    private val touchSnapPoints: MutableStateFlow<List<Int>> = MutableStateFlow(listOf(12,12,12,12,12,12))
    fun getTouchSnapPoints() = touchSnapPoints.asStateFlow()


    fun changeTouchSnapPointsValue(value: Int, index: Int){
        val list = touchSnapPoints.value.toMutableList()
        list[index] = value
        touchSnapPoints.value = list
    }

}