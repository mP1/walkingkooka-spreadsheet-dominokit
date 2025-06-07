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
import org.dominokit.domino.ui.timepicker.TimePicker;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.text.printer.IndentingPrinter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A time picker that displays a timePicker that supports picking {@link LocalTime}.
 * A {@link Supplier} is required to supply a NOW time when an {@link Optional#empty()} is given to {@link #setValue(Optional)}.
 */
public final class SpreadsheetTimeComponent implements FormValueComponent<HTMLDivElement, LocalTime, SpreadsheetTimeComponent> {

    public static SpreadsheetTimeComponent empty(final String id,
                                                 final Supplier<LocalTime> clearValue) {
        return new SpreadsheetTimeComponent(
            id,
            clearValue
        );
    }

    private SpreadsheetTimeComponent(final String id,
                                     final Supplier<LocalTime> clearValue) {
        this.setId(id);

        this.clearValue = Objects.requireNonNull(clearValue, "clearValue");
        this.timePicker = TimePicker.create(); // TODO Add support allowing user to pick locale/DateTimeSymbols/DecimalNumberSymbols ?
    }

    @Override
    public SpreadsheetTimeComponent setId(final String id) {
        this.element().id = id;
        return this;
    }

    @Override
    public String id() {
        return this.element().id;
    }

    @Override
    public SpreadsheetTimeComponent setLabel(final String label) {
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
    public SpreadsheetTimeComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTimeComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public SpreadsheetTimeComponent setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<LocalTime> value() {
        return Optional.of(
            Instant.ofEpochMilli(
                    this.timePicker.getDate()
                        .getTime()
                ).atZone(ZONE_ID)
                .toLocalTime()
        );
    }

    @Override
    public SpreadsheetTimeComponent setValue(final Optional<LocalTime> value) {
        Objects.requireNonNull(value, "value");

        this.timePicker.setDate(
            toDate(
                value.orElse(
                    this.clearValue.get()
                )
            )
        );

        return this;
    }

    private final Supplier<LocalTime> clearValue;

    @Override
    public SpreadsheetTimeComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTimeComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTimeComponent required() {
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
    public SpreadsheetTimeComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
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
                dateToLocalTime(oldTime),
                dateToLocalTime(newTime)
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
    public SpreadsheetTimeComponent addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTimeComponent addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTimeComponent addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTimeComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTimeComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTimeComponent focus() {
        // NOP
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.timePicker.element();
    }

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

    // helpers..........................................................................................................

    /**
     * Helper that is used to translate {@link Date} from/to {@link LocalTime}.
     */
    private static Optional<LocalTime> dateToLocalTime(final Date date) {
        return Optional.ofNullable(
            null != date ?
                Instant.ofEpochMilli(date.getTime())
                    .atZone(ZONE_ID)
                    .toLocalTime() :
                null
        );
    }

    private static Date toDate(final LocalTime time) {
        return new Date(
            time.atDate(LocalDate.EPOCH)
                .atZone(ZONE_ID)
                .toInstant()
                .toEpochMilli()
        );
    }

    private final static ZoneId ZONE_ID = ZoneId.systemDefault();
}
