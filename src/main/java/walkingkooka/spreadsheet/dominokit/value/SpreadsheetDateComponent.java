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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.datepicker.Calendar;
import org.dominokit.domino.ui.datepicker.CalendarDay;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.printer.IndentingPrinter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A date picker that displays a calendar that supports picking {@link LocalDate}.
 */
public final class SpreadsheetDateComponent implements FormValueComponent<HTMLDivElement, LocalDate, SpreadsheetDateComponent> {

    public static SpreadsheetDateComponent empty(final String id,
                                                 final Supplier<LocalDate> clearValue) {
        return new SpreadsheetDateComponent(
            id,
            clearValue
        );
    }

    private SpreadsheetDateComponent(final String id,
                                     final Supplier<LocalDate> clearValue) {
        this.setId(id);

        this.clearValue = Objects.requireNonNull(clearValue, "clearValue");
    }

    @Override
    public SpreadsheetDateComponent setId(final String id) {
        this.element().id = id;
        return this;
    }

    @Override
    public String id() {
        return this.element().id;
    }

    @Override
    public SpreadsheetDateComponent setLabel(final String label) {
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
    public SpreadsheetDateComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public SpreadsheetDateComponent setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<LocalDate> value() {
        return Optional.of(
            Instant.ofEpochMilli(
                this.calendar.getDate()
                    .getTime()
            ).atZone(CalendarAndTimePicker.ZONE_ID)
            .toLocalDate()
        );
    }

    @Override
    public SpreadsheetDateComponent setValue(final Optional<LocalDate> value) {
        Objects.requireNonNull(value, "value");

        this.calendar.setDate(
            CalendarAndTimePicker.toDate(
                value.orElse(
                    this.clearValue.get()
                )
            )
        );

        return this;
    }

    private final Supplier<LocalDate> clearValue;

    @Override
    public SpreadsheetDateComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateComponent required() {
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
    public SpreadsheetDateComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
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
    public SpreadsheetDateComponent addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateComponent addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateComponent addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetDateComponent focus() {
        // NOP
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.calendar.element();
    }

    private final Calendar calendar = Calendar.create();

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
