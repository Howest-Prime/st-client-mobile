package be.howest.howestprime.data.messaging.json
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime

class LocalDateTimeJsonAdapter {

    class LocalDateTimeAdapter {
        @ToJson
        fun toJson(value: LocalDateTime): String = value.toString()

        @FromJson
        fun fromJson(value: String): LocalDateTime = LocalDateTime.parse(value)
    }
}