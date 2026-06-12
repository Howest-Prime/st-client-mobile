package be.howest.howestprime.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class BrightnessStateTest {

    @Test
    fun lux_below_50_is_DARK() {
        assertEquals(BrightnessStatus.DARK, BrightnessState(0f).status)
        assertEquals(BrightnessStatus.DARK, BrightnessState(49.9f).status)
    }

    @Test
    fun lux_at_50_is_DIM() {
        assertEquals(BrightnessStatus.DIM, BrightnessState(50f).status)
    }

    @Test
    fun lux_between_50_and_200_is_DIM() {
        assertEquals(BrightnessStatus.DIM, BrightnessState(100f).status)
        assertEquals(BrightnessStatus.DIM, BrightnessState(199.9f).status)
    }

    @Test
    fun lux_at_200_is_NORMAL() {
        assertEquals(BrightnessStatus.NORMAL, BrightnessState(200f).status)
    }

    @Test
    fun lux_between_200_and_500_is_NORMAL() {
        assertEquals(BrightnessStatus.NORMAL, BrightnessState(350f).status)
        assertEquals(BrightnessStatus.NORMAL, BrightnessState(499.9f).status)
    }

    @Test
    fun lux_at_500_is_BRIGHT() {
        assertEquals(BrightnessStatus.BRIGHT, BrightnessState(500f).status)
    }

    @Test
    fun lux_between_500_and_1000_is_BRIGHT() {
        assertEquals(BrightnessStatus.BRIGHT, BrightnessState(750f).status)
        assertEquals(BrightnessStatus.BRIGHT, BrightnessState(999.9f).status)
    }

    @Test
    fun lux_at_1000_is_TOO_BRIGHT() {
        assertEquals(BrightnessStatus.TOO_BRIGHT, BrightnessState(1000f).status)
    }

    @Test
    fun lux_above_1000_is_TOO_BRIGHT() {
        assertEquals(BrightnessStatus.TOO_BRIGHT, BrightnessState(5000f).status)
    }

    @Test
    fun negative_lux_is_DARK() {
        assertEquals(BrightnessStatus.DARK, BrightnessState(-1f).status)
    }
}
