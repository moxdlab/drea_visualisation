package model

import dev.icerock.moko.mvvm.livedata.MutableLiveData

class MotorConfig {

    var maximalTouches: MutableLiveData<Int> = MutableLiveData(0)

    var snapStrength: MutableLiveData<Float> = MutableLiveData(0f)

    var touchSnapPoints: MutableLiveData<List<Int>> = MutableLiveData(listOf())


    init {
        maximalTouches.value = 5
        snapStrength.value = 1f
        touchSnapPoints.value = listOf(0,0,1,4,12,48)
    }


    fun setMaximalTouches(newValue:Int){
        maximalTouches.value = newValue
        val list: MutableList<Int> = touchSnapPoints.value.toMutableList()

        var change = false;

        while (newValue+1 > list.size){
            list.add(0)
            change = true
        }
        while (newValue+1 < list.size){
            list.removeLast()
            change = true
        }

        if(change){
            touchSnapPoints.value = list
        }
    }

    fun changeTouchSnapPointsValue(value: Int, index: Int){
        val list = touchSnapPoints.value.toMutableList()
        list[index] = value
        touchSnapPoints.value = list
    }

}