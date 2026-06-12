package be.howest.howestprime.data.messaging.json

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class LocalDateTimeAdapterTest {

    private val adapter = LocalDateTimeAdapter()

    @Test
    fun toJson_converts_LocalDateTime_to_ISO_string() {
        val dt = LocalDateTime.of(2026, 7, 15, 20, 30, 0)
        assertEquals("2026-07-15T20:30", adapter.toJson(dt))
    }

    @Test
    fun fromJson_converts_ISO_string_to_LocalDateTime() {
        val result = adapter.fromJson("2026-07-15T20:30:00")
        assertEquals(LocalDateTime.of(2026, 7, 15, 20, 30, 0), result)
    }

    @Test
    fun round_trip_produces_equal_LocalDateTime() {
        val original = LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        val json = adapter.toJson(original)
        val recovered = adapter.fromJson(json)
        assertEquals(original, recovered)
    }

    @Test
    fun toJson_handles_midnight() {
        val midnight = LocalDateTime.of(2026, 1, 1, 0, 0, 0)
        val result = adapter.fromJson(adapter.toJson(midnight))
        assertEquals(midnight, result)
    }
}
