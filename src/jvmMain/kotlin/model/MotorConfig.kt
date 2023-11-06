package model

import dev.icerock.moko.mvvm.livedata.MutableLiveData

class MotorConfig {

    var maximalTouches = 5

    var snapStrength: MutableLiveData<Float> = MutableLiveData(0f)

    var touchSnapPoints: MutableLiveData<List<Int>> = MutableLiveData(listOf())


    init {
        maximalTouches = 5
        snapStrength.value = 1f
        touchSnapPoints.value = listOf(0,12,12,12,12,12)
    }

    fun changeTouchSnapPointsValue(value: Int, index: Int){
        val list = touchSnapPoints.value.toMutableList()
        list[index] = value
        touchSnapPoints.value = list
    }

}