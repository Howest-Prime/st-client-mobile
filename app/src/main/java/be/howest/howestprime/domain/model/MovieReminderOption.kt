package be.howest.howestprime.domain.model

enum class MovieReminderOption(val minutesBefore: Int) {
    TWO_MINUTES(2),
    THIRTY_MINUTES(30),
    ONE_HOUR(60),
    TWO_HOURS(120),
    ONE_DAY(1440)
}
