package be.howest.howestprime.data.local.room

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? =
        value?.let { LocalDateTime.parse(it, dateTimeFormatter) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? =
        date?.format(dateTimeFormatter)

    @TypeConverter
    fun fromDateString(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it, dateFormatter) }

    @TypeConverter
    fun dateToString(date: LocalDate?): String? =
        date?.format(dateFormatter)

    @TypeConverter
    fun fromCommaList(value: String?): List<String> =
        value?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()

    @TypeConverter
    fun toCommaList(list: List<String>?): String? =
        list?.joinToString(",")
}
