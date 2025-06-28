package com.mdrlzy.budgetwise.core.ui.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object DateTimeHelper {
    fun startOfDay(date: OffsetDateTime) = date.withHour(0).withMinute(0)

    fun endOfDay(date: OffsetDateTime) = date.withHour(23).withMinute(59)

    fun fullDate(date: OffsetDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy")
        return formatter.format(date)
    }

    fun shortDate(date: OffsetDateTime): String {
        val formatter = DateTimeFormatter.ofPattern("d MMM HH:mm")
        return formatter.format(date)
    }
}
