package com.mdrlzy.budgetwise.core.network

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object NetworkUtils {
    private val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

    fun toUtcString(dateTime: OffsetDateTime): String {
        return dateTime
            .withOffsetSameInstant(ZoneId.of("UTC").rules.getOffset(dateTime.toInstant()))
            .format(formatter)
    }

    fun fromUtcString(utcString: String): OffsetDateTime {
        val utcDateTime = OffsetDateTime.parse(utcString)
        return utcDateTime.atZoneSameInstant(ZoneId.systemDefault()).toOffsetDateTime()
    }
}