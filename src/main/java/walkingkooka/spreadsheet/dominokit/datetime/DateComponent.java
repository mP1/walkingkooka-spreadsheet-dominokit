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

import org.dominokit.domino.ui.datepicker.Calendar;
import org.dominokit.domino.ui.datepicker.CalendarDay;
import org.dominokit.domino.ui.datepicker.DateSelectionListener;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A date picker that displays a calendar that supports picking {@link LocalDate}.
 */
public final class DateComponent extends DominoKitPickerComponent<LocalDate, DateComponent> {

    public static DateComponent empty(final String id,
                                      final Supplier<LocalDate> clearValue) {
        return new DateComponent(
            id,
            clearValue
        );
    }

    private DateComponent(final String id,
                          final Supplier<LocalDate> clearValue) {
        super(
            clearValue
        );
        this.calendar = Calendar.create();
        this.bodyElement.insertFirst(this.calendar.element());
        this.setId(id);
    }

    @Override
    public Optional<LocalDate> value() {
        return dateToLocalDate(
            this.calendar.getDate()
        );
    }

    @Override
    public DateComponent setValue(final Optional<LocalDate> value) {
        Objects.requireNonNull(value, "value");

        this.calendar.setDate(
            localDateToDate(
                value.orElse(
                    this.clearValue.get()
                )
            )
        );

        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<LocalDate> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        final DateSelectionListener dateSelectionListener = (final CalendarDay oldDay,
                                                             final CalendarDay newDay) -> watcher.onValue(
            calendarDayToLocalDate(newDay)
        );

        this.calendar.addDateSelectionListener(dateSelectionListener);
        return () -> this.calendar.removeDateSelectionListener(dateSelectionListener);
    }

    // HtmlComponent....................................................................................................

    @Override
    public DateComponent setCssText(final String css) {
        this.calendar.cssText(css);
        return this;
    }

    @Override
    public DateComponent setCssProperty(final String name,
                                        final String value) {
        this.calendar.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public DateComponent removeCssProperty(final String name) {
        this.calendar.removeCssProperty(name);
        return this;
    }

    public DateComponent resetView() {
        this.calendar.resetView();
        return this;
    }

    @Override
    public DateComponent focus() {
        // NOP
        return this;
    }

    @Override
    public DateComponent blur() {
        // NOP
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.calendar.isExpanded();
    }

    private final Calendar calendar;
}
