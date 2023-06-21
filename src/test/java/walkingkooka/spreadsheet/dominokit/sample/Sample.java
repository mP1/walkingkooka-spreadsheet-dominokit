/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.spreadsheet.dominokit.sample;

import walkingkooka.collect.list.Lists;
import walkingkooka.datetime.DateTime;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public final class Sample {

    public static void main(final String[] args) {
        System.out.println("NumberFormat.instance");
        print(NumberFormat.getInstance());

        System.out.println("NumberFormat.currency");
        print(NumberFormat.getCurrencyInstance());

        System.out.println("NumberFormat.percent");
        print(NumberFormat.getPercentInstance());

//        System.out.println("DateTimeFormatter.ofLocalizedDate SHORT");
//        print(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
//
//        System.out.println("DateTimeFormatter.ofLocalizedDate MEDIUM");
//        print(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
//
//        System.out.println("DateTimeFormatter.ofLocalizedDate LONG");
//        print(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
//
//        System.out.println("DateTimeFormatter.ofLocalizedDate FULL");
//        print(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
//
//        //DateTimeFormatter.ofLocalizedDate(null).withZone(ZoneId.of("UTF"));
//
//
//        System.out.println("DateTimeFormatter.ofLocalizedTime SHORT");
//        print(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
//
//        System.out.println("DateTimeFormatter.ofLocalizedTime MEDIUM");
//        print(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM));
//
//        System.out.println("DateTimeFormatter.ofLocalizedTime LONG");
//        print(DateTimeFormatter.ofLocalizedTime(FormatStyle.LONG));
//
//        System.out.println("DateTimeFormatter.ofLocalizedTime FULL");
//        print(DateTimeFormatter.ofLocalizedTime(FormatStyle.FULL));

        System.out.println("DateFormat.getDateInstance SHORT");
        printDate(DateFormat.getDateInstance(DateFormat.SHORT));

        System.out.println("DateFormat.getDateInstance MEDIUM");
        printDate(DateFormat.getDateInstance(DateFormat.MEDIUM));

        System.out.println("DateFormat.getDateInstance LONG");
        printDate(DateFormat.getDateInstance(DateFormat.LONG));

        for (final int date : Lists.of(DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG)) {
            for (final int time : Lists.of(DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG)) {
                System.out.println("DateFormat.getDateTimeInstance " + dateFormatStyle(date) + " " + dateFormatStyle(time));
                printDateTime(DateFormat.getDateTimeInstance(date, time));
            }
        }

        System.out.println("DateFormat.getTimeInstance SHORT");
        printTime(DateFormat.getTimeInstance(DateFormat.SHORT));

        System.out.println("DateFormat.getTimeInstance MEDIUM");
        printTime(DateFormat.getTimeInstance(DateFormat.MEDIUM));

        System.out.println("DateFormat.getTimeInstance LONG");
        printTime(DateFormat.getTimeInstance(DateFormat.LONG));
    }

    private static String dateFormatStyle(final int value) {
        switch (value) {
            case DateFormat.SHORT:
                return "SHORT";
            case DateFormat.MEDIUM:
                return "MEDIUM";
            case DateFormat.LONG:
                return "LONG";
            case DateFormat.FULL:
                return "FULL";
            default:
                throw new IllegalArgumentException("Invalid DateFormat style constant=" + value);
        }
    }

    private static void print(final NumberFormat format) {
        final DecimalFormat decimalFormat = (DecimalFormat) format;
        System.out.println(decimalFormat.toPattern());

        System.out.println(format.format(+1.25) + "\t\t" + format.format(-1.25) + "\t\t" + format.format(0));
        System.out.println();
    }

    private static void printDate(final DateFormat format) {
        final SimpleDateFormat simpleDateFormat = simpleDateFormat(format);

        System.out.println(simpleDateFormat.toPattern());

        final Date date = DateTime.localDateToDate(
                LocalDate.of(2000, 12, 31)
        );

        System.out.println(date);
        System.out.println(simpleDateFormat.format(date));
        System.out.println();
    }

    private static void printDateTime(final DateFormat format) {
        final SimpleDateFormat simpleDateFormat = simpleDateFormat(format);

        System.out.println(simpleDateFormat.toPattern());

        final Date date = DateTime.localDateTimeToDate(
                LocalDateTime.of(2000, 12, 31, 12, 58, 59)
        );

        System.out.println(date);
        System.out.println(simpleDateFormat.format(date));
        System.out.println();
    }

    private static void printTime(final DateFormat format) {
        final SimpleDateFormat simpleDateFormat = simpleDateFormat(format);

        System.out.println(simpleDateFormat.toPattern());

        final Date date = DateTime.localTimeToDate(
                LocalTime.of(12, 58, 59)
        );

        System.out.println(date);
        System.out.println(simpleDateFormat.format(date));
        System.out.println();
    }

    private static SimpleDateFormat simpleDateFormat(final DateFormat format) {
        final SimpleDateFormat simpleDateFormat = (SimpleDateFormat) format;
        return new SimpleDateFormat(
                DateTime.simpleDateFormatPatternWithoutTimezone(simpleDateFormat.toPattern())
        );
    }
}
