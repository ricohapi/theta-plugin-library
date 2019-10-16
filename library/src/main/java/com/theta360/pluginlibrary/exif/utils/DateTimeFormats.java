/**
 * Copyright 2018 Ricoh Company, Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.theta360.pluginlibrary.exif.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

/**
 * DateTimeFormats class
 */
public class DateTimeFormats {
    public static final String DATE_TIME_ZONE = "yyyy:MM:dd HH:mm:ssZZ";
    public static final String DATE_TIME = "yyyy:MM:dd HH:mm:ss";
    public static final String DATE_ISO = "yyyy-MM-dd";
    public static final String TIME_ISO = "HH:mm:ss";

    private static final String DATE = "yyyy:MM:dd";
    private static final String MIN_DATE_TIME_ZONE = "1582:01:01 00:00:00";

    private static final String PATTERN = "^(\\d{4}):(0[1-9]|1[0-2]):(0[1-9]|[12][0-9]|3[01]) ([0-9]|[0-1][0-9]|[2][0-3]):([0-9]|[0-5][0-9]):([0-9]|[0-5][0-9])[+-]([0-9]|[0-1][0-9]|[2][0-3]):([0-9]|[0-5][0-9])$";

    private static final DateTimeFormatter mDateTimeFormatter = new DateTimeFormatterBuilder()
            .appendYear(4, 4)
            .appendLiteral(':')
            .appendMonthOfYear(2)
            .appendLiteral(':')
            .appendDayOfMonth(2)
            .appendLiteral(' ')
            .appendHourOfDay(2)
            .appendLiteral(':')
            .appendMinuteOfHour(2)
            .appendLiteral(':')
            .appendSecondOfMinute(2)
            .appendTimeZoneOffset(null, true, 2, 2)
            .toFormatter();

    public static boolean isDateTimeZone(String dateTimeZone) {
        try {
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(dateTimeZone);

            if (matcher.find()) {
                DateTime dateTime = mDateTimeFormatter.parseLocalDateTime(dateTimeZone)
                        .toDateTime();

                DateTime minDateTime = getDateTime(MIN_DATE_TIME_ZONE);
                if (!dateTime.isEqual(minDateTime) && !dateTime.isAfter(minDateTime)) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static DateTime getDateTimeZone(String dateTimeZone) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_ZONE);
        LocalDateTime localDateTime = dateTimeFormatter.parseLocalDateTime(dateTimeZone);

        return localDateTime.toDateTime();
    }

    public static String getDateTimeZone(DateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_ZONE);

        return dateTimeFormatter.print(dateTime);
    }

    public static DateTime getDateTime(String dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME);
        LocalDateTime localDateTime = dateTimeFormatter.parseLocalDateTime(dateTime);

        return localDateTime.toDateTime();
    }

    public static String getDateTimeString(String _dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME);
        DateTime dateTime = getDateTimeZone(_dateTime);

        return dateTimeFormatter.print(dateTime);
    }

    public static String getDateTime(long _dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME);
        LocalDateTime dateTime = new LocalDateTime(_dateTime);

        return dateTime.toString(dateTimeFormatter);
    }

    public static String getDate(DateTime dateTime) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE);

        return dateTimeFormatter.print(dateTime);
    }

    public static String getTimeZone(String _dateTimeZone) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_ZONE);
        DateTime dateTime = dateTimeFormatter.withOffsetParsed().parseDateTime(_dateTimeZone);

        DateTimeZone dateTimeZone = dateTime.getZone();
        String timeZone;
        if (dateTimeZone == DateTimeZone.UTC) {
            timeZone = "+00:00";
        } else {
            timeZone = dateTimeZone.toString();
        }
        return timeZone;
    }
}
