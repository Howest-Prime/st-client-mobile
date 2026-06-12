package be.howest.howestprime.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class MovieReminderOptionTest {

    @Test
    fun TWO_MINUTES_has_correct_minutesBefore() {
        assertEquals(2, MovieReminderOption.TWO_MINUTES.minutesBefore)
    }

    @Test
    fun THIRTY_MINUTES_has_correct_minutesBefore() {
        assertEquals(30, MovieReminderOption.THIRTY_MINUTES.minutesBefore)
    }

    @Test
    fun ONE_HOUR_has_correct_minutesBefore() {
        assertEquals(60, MovieReminderOption.ONE_HOUR.minutesBefore)
    }

    @Test
    fun TWO_HOURS_has_correct_minutesBefore() {
        assertEquals(120, MovieReminderOption.TWO_HOURS.minutesBefore)
    }

    @Test
    fun ONE_DAY_has_correct_minutesBefore() {
        assertEquals(1440, MovieReminderOption.ONE_DAY.minutesBefore)
    }

    @Test
    fun all_options_have_positive_minutesBefore() {
        for (option in MovieReminderOption.entries) {
            assert(option.minutesBefore > 0) { "${option.name} minutesBefore must be positive" }
        }
    }

    @Test
    fun entries_are_ordered_ascending_by_minutesBefore() {
        val values = MovieReminderOption.entries.map { it.minutesBefore }
        assertEquals(values.sorted(), values)
    }
}
