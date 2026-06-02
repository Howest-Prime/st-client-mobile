package be.howest.howestprime.domain.model

enum class BrightnessStatus {
    DARK,
    DIM,
    NORMAL,
    BRIGHT,
    NOT_AVAILABLE
}

data class BrightnessState(
    private val lux: Float
) {
    val status: BrightnessStatus = when {
        lux < 50 -> BrightnessStatus.DARK
        lux < 200 -> BrightnessStatus.DIM
        lux < 500 -> BrightnessStatus.NORMAL
        lux < 1000 -> BrightnessStatus.BRIGHT
        else -> BrightnessStatus.NOT_AVAILABLE
    }
}