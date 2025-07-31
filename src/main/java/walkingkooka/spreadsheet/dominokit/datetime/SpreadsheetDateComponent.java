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

import elemental2.dom.EventListener;
import org.dominokit.domino.ui.datepicker.Calendar;
import org.dominokit.domino.ui.datepicker.CalendarDay;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A date picker that displays a calendar that supports picking {@link LocalDate}.
 */
public final class SpreadsheetDateComponent extends SpreadsheetPickerComponent<LocalDate, SpreadsheetDateComponent> {

    public static SpreadsheetDateComponent empty(final String id,
                                                 final Supplier<LocalDate> clearValue) {
        return new SpreadsheetDateComponent(
            id,
            clearValue
        );
    }

    private SpreadsheetDateComponent(final String id,
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
        return CalendarAndTimePicker.dateToLocalDate(
            this.calendar.getDate()
        );
    }

    @Override
    public SpreadsheetDateComponent setValue(final Optional<LocalDate> value) {
        Objects.requireNonNull(value, "value");

        this.calendar.setDate(
            CalendarAndTimePicker.localDateToDate(
                value.orElse(
                    this.clearValue.get()
                )
            )
        );

        return this;
    }

    @Override
    public SpreadsheetDateComponent setCssText(final String css) {
        this.calendar.cssText(css);
        return this;
    }

    @Override
    public SpreadsheetDateComponent setCssProperty(final String name,
                                                   final String value) {
        this.calendar.setCssProperty(
            name,
            value
        );
        return this;
    }

    public SpreadsheetDateComponent resetView() {
        this.calendar.resetView();
        return this;
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetDateComponent addChangeListener(final ChangeListener<Optional<LocalDate>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.calendar.addDateSelectionListener(
            (final CalendarDay oldDay,
             final CalendarDay newDay) -> listener.onValueChanged(
                CalendarAndTimePicker.calendarDayToLocalDate(oldDay),
                CalendarAndTimePicker.calendarDayToLocalDate(newDay)
            )
        );

        return this;
    }

    @Override
    public SpreadsheetDateComponent addClickListener(final EventListener listener) {
        this.calendar.addClickListener(listener);
        return this;
    }

    @Override
    public SpreadsheetDateComponent focus() {
        // NOP
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.calendar.isExpanded();
    }

    private final Calendar calendar;
}
