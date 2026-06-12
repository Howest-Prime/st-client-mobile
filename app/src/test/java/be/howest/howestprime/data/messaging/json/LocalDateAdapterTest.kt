package be.howest.howestprime.data.messaging.json

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class LocalDateAdapterTest {

    private val adapter = LocalDateAdapter()

    @Test
    fun toJson_converts_LocalDate_to_ISO_string() {
        val date = LocalDate.of(2026, 7, 15)
        assertEquals("2026-07-15", adapter.toJson(date))
    }

    @Test
    fun fromJson_converts_ISO_string_to_LocalDate() {
        val result = adapter.fromJson("2026-07-15")
        assertEquals(LocalDate.of(2026, 7, 15), result)
    }

    @Test
    fun round_trip_produces_equal_LocalDate() {
        val original = LocalDate.of(2025, 1, 1)
        val json = adapter.toJson(original)
        val recovered = adapter.fromJson(json)
        assertEquals(original, recovered)
    }

    @Test
    fun toJson_handles_leap_day() {
        val leapDay = LocalDate.of(2024, 2, 29)
        assertEquals("2024-02-29", adapter.toJson(leapDay))
    }

    @Test
    fun fromJson_handles_leap_day() {
        assertEquals(LocalDate.of(2024, 2, 29), adapter.fromJson("2024-02-29"))
    }
}
