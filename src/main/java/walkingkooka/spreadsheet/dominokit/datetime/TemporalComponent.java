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

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.datepicker.CalendarDay;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.spreadsheet.dominokit.value.HasValueWatchers;
import walkingkooka.text.printer.IndentingPrinter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

abstract class TemporalComponent<V, C extends TemporalComponent<V, C>> implements FormValueComponent<HTMLDivElement, V, C>,
    FormValueComponentTreePrintable<HTMLDivElement, C, V>,
    HasValueWatchers<HTMLDivElement, V, C> {

    /**
     * Helper used to transform a {@link CalendarDay} into a {@link LocalDate}.
     */
    static Optional<LocalDate> calendarDayToLocalDate(final CalendarDay day) {
        return dateToLocalDate(
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

    static Optional<LocalDateTime> toLocalDateTime(final Optional<LocalDate> date,
                                                   final Optional<LocalTime> time,
                                                   final Supplier<LocalDateTime> clearValue) {
        final LocalDate dateOrNull = date.orElse(null);
        final LocalTime timeOrNull = time.orElse(null);

        return Optional.ofNullable(
            null != dateOrNull || null != timeOrNull ?
                LocalDateTime.of(
                    null != dateOrNull ?
                        dateOrNull :
                        clearValue.get().toLocalDate(),
                    null != timeOrNull ?
                        timeOrNull :
                        clearValue.get().toLocalTime()
                ) :
                null
        );
    }

    final static ZoneId ZONE_ID = ZoneId.systemDefault();

    TemporalComponent() {
        super();
    }

    @Override
    public final C hideMarginBottom() {
        return (C) this;
    }

    @Override
    public final C removeBorders() {
        return (C) this;
    }

    @Override
    public final C removePadding() {
        return (C) this;
    }

    public final C autocompleteOff() {
        return (C) this;
    }

    public final C clearIcon() {
        return (C) this;
    }

    public final C disableSpellcheck() {
        return (C) this;
    }

    public final C enterFiresValueChange() {
        return (C) this;
    }

    public final C magnifyingGlassIcon() {
        return (C) this;
    }

    // FormValueComponentTreePrintable..................................................................................

    @Override
    public final void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }

   // Object...........................................................................................................

    @Override
    public final String toString() {
        return this.value()
            .map(Object::toString)
            .orElse("");
    }
}
    