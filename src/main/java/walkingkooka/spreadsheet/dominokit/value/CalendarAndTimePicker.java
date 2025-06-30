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

package walkingkooka.spreadsheet.dominokit.value;

import org.dominokit.domino.ui.datepicker.CalendarDay;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 * A collection of helpers to assist conversion between {@link Date} and {@link LocalDate} or {@link LocalTime}.
 */
final class CalendarAndTimePicker {

    /**
     * Helper used to transform a {@link CalendarDay} into a {@link LocalDate}.
     */
    static Optional<LocalDate> calendarDayToLocalDate(final CalendarDay day) {
        return CalendarAndTimePicker.dateToLocalDate(
            null != day ?
                day.getDate() :
                null
        );
    }

    /**
     * Helper that is used to translate {@link Date} to {@link LocalDate}.
     */
    static Optional<LocalDate> dateToLocalDate(final Date date) {
        return Optional.ofNullable(
            null != date ?
                Instant.ofEpochMilli(date.getTime())
                    .atZone(ZONE_ID)
                    .toLocalDate() :
                null
        );
    }

    static Date localDateToDate(final LocalDate date) {
        return new Date(
            date.atStartOfDay()
                .atZone(ZONE_ID)
                .toInstant()
                .toEpochMilli()
        );
    }

    /**
     * Helper that is used to translate {@link Date} from/to {@link LocalTime}.
     */
    static Optional<LocalTime> dateToLocalTime(final Date date) {
        return Optional.ofNullable(
            null != date ?
                Instant.ofEpochMilli(date.getTime())
                    .atZone(ZONE_ID)
                    .toLocalTime() :
                null
        );
    }

    static Date localTimeToDate(final LocalTime time) {
        return new Date(
            time.atDate(LocalDate.EPOCH)
                .atZone(ZONE_ID)
                .toInstant()
                .toEpochMilli()
        );
    }

    final static ZoneId ZONE_ID = ZoneId.systemDefault();

    /**
     * Stop creation
     */
    private CalendarAndTimePicker() {
        throw new UnsupportedOperationException();
    }
}
