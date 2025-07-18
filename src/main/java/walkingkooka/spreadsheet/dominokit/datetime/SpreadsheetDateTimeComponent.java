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
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.datepicker.Calendar;
import org.dominokit.domino.ui.datepicker.CalendarDay;
import org.dominokit.domino.ui.timepicker.TimePicker;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A date picker that displays a calendar and time picker {@link LocalDateTime}.
 */
public final class SpreadsheetDateTimeComponent implements FormValueComponent<HTMLDivElement, LocalDateTime, SpreadsheetDateTimeComponent> {

    public static SpreadsheetDateTimeComponent empty(final String id,
                                                     final Supplier<LocalDateTime> clearValue) {
        return new SpreadsheetDateTimeComponent(
            id,
            clearValue
        );
    }

    private SpreadsheetDateTimeComponent(final String id,
                                         final Supplier<LocalDateTime> clearValue) {
        final Calendar calendar = Calendar.create();
        this.calendar = calendar;

        final TimePicker timePicker = TimePicker.create();
        this.timePicker = timePicker;

        this.layout = SpreadsheetFlexLayout.row()
            .appendChild(calendar)
            .appendChild(timePicker);

        this.clearValue = Objects.requireNonNull(clearValue, "clearValue");
        this.setId(id);
    }

    @Override
    public SpreadsheetDateTimeComponent setId(final String id) {
        this.element().id = id;
        return this;
    }

    @Override
    public String id() {
        return this.element().id;
    }

    @Override
    public SpreadsheetDateTimeComponent setLabel(final String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String label() {
        return "";
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public SpreadsheetDateTimeComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public SpreadsheetDateTimeComponent setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<LocalDateTime> value() {
        return CalendarAndTimePicker.toLocalDateTime(
            CalendarAndTimePicker.dateToLocalDate(
                this.calendar.getDate()
            ),
            CalendarAndTimePicker.dateToLocalTime(
                this.timePicker.getDate()
            ),
            this.clearValue
        );
    }

    @Override
    public SpreadsheetDateTimeComponent setValue(final Optional<LocalDateTime> value) {
        Objects.requireNonNull(value, "value");

        this.calendar.setDate(
            CalendarAndTimePicker.localDateToDate(
                value.orElse(
                    this.clearValue.get()
                ).toLocalDate()
            )
        );

        this.timePicker.setDate(
            CalendarAndTimePicker.localTimeToDate(
                value.orElse(
                    this.clearValue.get()
                ).toLocalTime()
            )
        );

        return this;
    }

    private final Supplier<LocalDateTime> clearValue;

    @Override
    public SpreadsheetDateTimeComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> errors() {
        return Lists.empty();
    }

    @Override
    public SpreadsheetDateTimeComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent setCssText(final String css) {
        this.calendar.cssText(css);
        return this;
    }

    @Override
    public SpreadsheetDateTimeComponent setCssProperty(final String name,
                                                       final String value) {
        this.calendar.setCssProperty(
            name,
            value
        );
        return this;
    }

    public SpreadsheetDateTimeComponent resetView() {
        this.calendar.resetView();
        return this;
    }

    // events...........................................................................................................

    /**
     * Register a listener for both the date and time components. When an event is fired, the given {@link ChangeListener}
     * is fired combining the event component with the value of the other component.
     */
    @Override
    public SpreadsheetDateTimeComponent addChangeListener(final ChangeListener<Optional<LocalDateTime>> listener) {
        Objects.requireNonNull(listener, "listener");

        final Calendar calendar = this.calendar;
        final TimePicker timePicker = this.timePicker;
        final Supplier<LocalDateTime> clearValue = this.clearValue;

        calendar.addDateSelectionListener(
            (final CalendarDay oldDay,
             final CalendarDay newDay) -> {
                final Optional<LocalTime> time = CalendarAndTimePicker.dateToLocalTime(
                    timePicker.getDate()
                );
                listener.onValueChanged(
                    CalendarAndTimePicker.toLocalDateTime(
                        CalendarAndTimePicker.calendarDayToLocalDate(oldDay),
                        time,
                        clearValue
                    ),
                    CalendarAndTimePicker.toLocalDateTime(
                        CalendarAndTimePicker.calendarDayToLocalDate(newDay),
                        time,
                        clearValue
                    )
                );
            }
        );

        timePicker.addTimeSelectionListener(
            (final Date oldTime,
             final Date newTime) -> {
                final Optional<LocalDate> date = CalendarAndTimePicker.dateToLocalDate(
                    calendar.getDate()
                );
                listener.onValueChanged(
                    CalendarAndTimePicker.toLocalDateTime(
                        date,
                        CalendarAndTimePicker.dateToLocalTime(oldTime),
                        clearValue
                    ),
                    CalendarAndTimePicker.toLocalDateTime(
                        date,
                        CalendarAndTimePicker.dateToLocalTime(newTime),
                        clearValue
                    )
                );
            }
        );

        return this;
    }

    @Override
    public SpreadsheetDateTimeComponent addClickListener(final EventListener listener) {
        this.calendar.addClickListener(listener);
        this.timePicker.addClickListener(listener);
        return this;
    }

    @Override
    public SpreadsheetDateTimeComponent addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateTimeComponent focus() {
        // NOP
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.layout.element();
    }

    private final SpreadsheetFlexLayout layout;

    private final Calendar calendar;

    private final TimePicker timePicker;

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(
                this.toString()
            );
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.value()
            .map(Object::toString)
            .orElse("");
    }
}
