package model

data class MultiKnob(
    var fingerCount: Int,
    var pointerAngle: Float,
    var fingerPosition: List<Float>,
    var isButtonPressed: Boolean
)
