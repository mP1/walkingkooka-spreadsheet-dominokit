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

package walkingkooka.spreadsheet.dominokit.datetime;

import org.gwtproject.i18n.shared.cldr.DateTimeFormatInfo;
import walkingkooka.datetime.DateTimeSymbols;

import java.time.temporal.WeekFields;
import java.util.Collection;

/**
 * A {@link DateTimeFormatInfo} that sources its values from the given {@link DateTimeSymbols}.
 */
abstract class TemporalComponentContextDateTimeFormatInfo<C extends TemporalComponentContext<?>> implements DateTimeFormatInfo {

    TemporalComponentContextDateTimeFormatInfo(final C context) {
        super();
        this.context = context;
    }

    // DateTimeFormatInfo...............................................................................................

    @Override
    public final String[] ampms() {
        return toArray(
            this.dateTimeSymbols()
                .ampms()
        );
    }

    @Override
    public abstract String dateFormat();

    @Override
    public final String dateFormatFull() {
        return this.dateFormat();
    }

    @Override
    public final String dateFormatLong() {
        return this.dateFormat();
    }

    @Override
    public final String dateFormatMedium() {
        return this.dateFormat();
    }

    @Override
    public final String dateFormatShort() {
        return this.dateFormat();
    }

    @Override
    public final String dateTime(final String timePattern, 
                                 final String datePattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String dateTimeFull(final String timePattern, 
                                     final String datePattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String dateTimeLong(final String timePattern, 
                                     final String datePattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String dateTimeMedium(final String timePattern, 
                                       final String datePattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String dateTimeShort(final String timePattern, 
                                      final String datePattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String[] erasFull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String[] erasShort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int firstDayOfTheWeek() {
        if(-1 == this.firstDayOfTheWeek) {
            this.firstDayOfTheWeek = WeekFields.of(this.context.locale())
                .getFirstDayOfWeek()
                .getValue()
                % 7;
        }
        return this.firstDayOfTheWeek;
    }

    private int firstDayOfTheWeek = -1; // lazy init

    @Override
    public final String formatDay() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String formatHour12Minute() {
        return ""; // only temporary, real timeFormat will eventually be called.
    }

    @Override
    public final String formatHour12MinuteSecond() {
        return "";
    }

    @Override
    public final String formatHour24Minute() {
        return "";
    }

    @Override
    public final String formatHour24MinuteSecond() {
        return "";
    }

    @Override
    public final String formatMinuteSecond() {
        return "";
    }

    @Override
    public final String formatMonthAbbrev() {
        return "";
    }

    @Override
    public final String formatMonthAbbrevDay() {
        return "";
    }

    @Override
    public final String formatMonthFull() {
        return "";
    }

    @Override
    public final String formatMonthFullDay() {
        return "";
    }

    @Override
    public final String formatMonthFullWeekdayDay() {
        return "";
    }

    @Override
    public final String formatMonthNumDay() {
        return "";
    }

    @Override
    public final String formatYear() {
        return "";
    }

    @Override
    public final String formatYearMonthAbbrev() {
        return "";
    }

    @Override
    public final String formatYearMonthAbbrevDay() {
        return "";
    }

    @Override
    public final String formatYearMonthFull() {
        return "";
    }

    @Override
    public final String formatYearMonthFullDay() {
        return "";
    }

    @Override
    public final String formatYearMonthNum() {
        return "";
    }

    @Override
    public final String formatYearMonthNumDay() {
        return "";
    }

    @Override
    public final String formatYearMonthWeekdayDay() {
        return "";
    }

    @Override
    public final String formatYearQuarterFull() {
        return "";
    }

    @Override
    public final String formatYearQuarterShort() {
        return "";
    }

    @Override
    public final String[] monthsFull() {
        return toArray(
            this.dateTimeSymbols()
                .monthNames()
        );
    }

    @Override
    public final String[] monthsFullStandalone() {
        return this.monthsFull();
    }

    @Override
    public final String[] monthsNarrow() {
        return toArray(
            this.dateTimeSymbols()
                .monthNameAbbreviations()
        );
    }

    @Override
    public final String[] monthsNarrowStandalone() {
        return this.monthsNarrow();
    }

    @Override
    public final String[] monthsShort() {
        return toArray(
            this.dateTimeSymbols()
                .monthNameAbbreviations()
        );
    }

    @Override
    public final String[] monthsShortStandalone() {
        return this.monthsShort();
    }

    @Override
    public final String[] quartersFull() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String[] quartersShort() {
        throw new UnsupportedOperationException();
    }

    @Override
    public abstract String timeFormat();

    @Override
    public final String timeFormatFull() {
        return this.timeFormat();
    }

    @Override
    public final String timeFormatLong() {
        return this.timeFormat();
    }

    @Override
    public final String timeFormatMedium() {
        return this.timeFormat();
    }

    @Override
    public final String timeFormatShort() {
        return this.timeFormat();
    }

    @Override
    public final String[] weekdaysFull() {
        return toArray(
            this.dateTimeSymbols().weekDayNames()
        );
    }

    @Override
    public final String[] weekdaysFullStandalone() {
        return this.weekdaysFull();
    }

    @Override
    public final String[] weekdaysNarrow() {
        return toArray(
            this.dateTimeSymbols().weekDayNameAbbreviations()
        );
    }

    @Override
    public final String[] weekdaysNarrowStandalone() {
        return this.weekdaysNarrow();
    }

    @Override
    public final String[] weekdaysShort() {
        return toArray(
            this.dateTimeSymbols().weekDayNames()
        );
    }

    @Override
    public final String[] weekdaysShortStandalone() {
        return this.weekdaysShort();
    }

    @Override
    public int weekendEnd() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int weekendStart() {
        throw new UnsupportedOperationException();
    }

    private DateTimeSymbols dateTimeSymbols() {
        return this.context.dateTimeSymbols();
    }

    final C context;

    private String[] toArray(final Collection<String> strings) {
        return strings.toArray(
            new String[strings.size()]
        );
    }

    // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.dateTimeSymbols().toString();
    }
}
