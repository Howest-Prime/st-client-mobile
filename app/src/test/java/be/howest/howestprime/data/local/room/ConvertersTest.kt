package be.howest.howestprime.data.local.room

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class ConvertersTest {

    private val converters = Converters()

    @Test
    fun dateToTimestamp_converts_LocalDateTime_to_ISO_string() {
        val dt = LocalDateTime.of(2026, 7, 15, 20, 30, 0)
        val result = converters.dateToTimestamp(dt)
        assertEquals("2026-07-15T20:30:00", result)
    }

    @Test
    fun fromTimestamp_converts_ISO_string_to_LocalDateTime() {
        val result = converters.fromTimestamp("2026-07-15T20:30:00")
        assertEquals(LocalDateTime.of(2026, 7, 15, 20, 30, 0), result)
    }

    @Test
    fun dateToTimestamp_returns_null_for_null_input() {
        assertNull(converters.dateToTimestamp(null))
    }

    @Test
    fun fromTimestamp_returns_null_for_null_input() {
        assertNull(converters.fromTimestamp(null))
    }

    @Test
    fun LocalDateTime_round_trips_through_converters() {
        val original = LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        val converted = converters.dateToTimestamp(original)
        val recovered = converters.fromTimestamp(converted)
        assertEquals(original, recovered)
    }

    @Test
    fun dateToString_converts_LocalDate_to_ISO_string() {
        val date = LocalDate.of(2026, 3, 5)
        assertEquals("2026-03-05", converters.dateToString(date))
    }

    @Test
    fun fromDateString_converts_ISO_string_to_LocalDate() {
        val result = converters.fromDateString("2026-03-05")
        assertEquals(LocalDate.of(2026, 3, 5), result)
    }

    @Test
    fun dateToString_returns_null_for_null_input() {
        assertNull(converters.dateToString(null))
    }

    @Test
    fun fromDateString_returns_null_for_null_input() {
        assertNull(converters.fromDateString(null))
    }

    @Test
    fun LocalDate_round_trips_through_converters() {
        val original = LocalDate.of(2026, 1, 1)
        val converted = converters.dateToString(original)
        val recovered = converters.fromDateString(converted)
        assertEquals(original, recovered)
    }
}
