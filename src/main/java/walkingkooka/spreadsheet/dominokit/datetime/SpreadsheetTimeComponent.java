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
import org.dominokit.domino.ui.timepicker.TimePicker;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A time picker that displays a timePicker that supports picking {@link LocalTime}.
 * A {@link Supplier} is required to supply a NOW time when an {@link Optional#empty()} is given to {@link #setValue(Optional)}.
 */
public final class SpreadsheetTimeComponent extends SpreadsheetPickerComponent<LocalTime, SpreadsheetTimeComponent> {

    public static SpreadsheetTimeComponent empty(final String id,
                                                 final Supplier<LocalTime> clearValue) {
        return new SpreadsheetTimeComponent(
            id,
            clearValue
        );
    }

    private SpreadsheetTimeComponent(final String id,
                                     final Supplier<LocalTime> clearValue) {
        super(clearValue);
        this.timePicker = TimePicker.create(); // TODO Add support allowing user to pick locale/DateTimeSymbols/DecimalNumberSymbols ?
        this.bodyElement.insertFirst(this.timePicker.element());
        this.setId(id);
    }

    @Override
    public Optional<LocalTime> value() {
        return CalendarAndTimePicker.dateToLocalTime(
            this.timePicker.getDate()
        );
    }

    @Override
    public SpreadsheetTimeComponent setValue(final Optional<LocalTime> value) {
        Objects.requireNonNull(value, "value");

        this.timePicker.setDate(
            CalendarAndTimePicker.localTimeToDate(
                value.orElse(
                    this.clearValue.get()
                )
            )
        );

        return this;
    }

    @Override
    public SpreadsheetTimeComponent setCssText(final String css) {
        this.timePicker.cssText(css);
        return this;
    }

    @Override
    public SpreadsheetTimeComponent setCssProperty(final String name,
                                                   final String value) {
        this.timePicker.setCssProperty(
            name,
            value
        );
        return this;
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetTimeComponent addChangeListener(final ChangeListener<Optional<LocalTime>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.timePicker.addTimeSelectionListener(
            (final Date oldTime,
             final Date newTime) -> listener.onValueChanged(
                CalendarAndTimePicker.dateToLocalTime(oldTime),
                CalendarAndTimePicker.dateToLocalTime(newTime)
            )
        );

        return this;
    }

    @Override
    public SpreadsheetTimeComponent addClickListener(final EventListener listener) {
        this.timePicker.addClickListener(listener);
        return this;
    }

    @Override
    public SpreadsheetTimeComponent focus() {
        // NOP
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.timePicker.isExpanded();
    }

    // IsElement........................................................................................................

//    @Override
//    public HTMLDivElement element() {
//        return this.timePicker.element();
//    }

    private final TimePicker timePicker;
}
