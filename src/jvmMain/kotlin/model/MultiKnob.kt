package model

data class MultiKnob(
    var fingerCount: Int,
    var pointerAngle: Float,
    var touches: List<Touch>,
    var isButtonPressed: Boolean
)

data class Touch(
    val position: Float,
    val channels: Int,
    val weight: Int
)