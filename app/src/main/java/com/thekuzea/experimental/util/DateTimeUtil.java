package com.thekuzea.experimental.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateTimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    public static Date convertLocalDateTimeToDate(final LocalDateTime localDateTime) {
        return Date.from(
                localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    public static LocalDateTime convertDateToLocalDateTime(final Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String convertOffsetDateTimeToString(final OffsetDateTime offsetDateTime) {
        return FORMATTER.format(offsetDateTime);
    }

    public static OffsetDateTime convertStringToOffsetDateTime(final String dateTime) {
        return OffsetDateTime.parse(dateTime, FORMATTER);
    }
}
