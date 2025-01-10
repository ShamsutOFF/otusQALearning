package data

enum class Day {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

fun Day.isWeekend(): Boolean {
    return this == Day.SATURDAY || this == Day.SUNDAY
}