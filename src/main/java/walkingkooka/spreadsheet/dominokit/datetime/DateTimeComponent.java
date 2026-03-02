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

import org.dominokit.domino.ui.forms.DateBox;
import org.dominokit.domino.ui.forms.TimeBox;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A date picker that displays a calendar and time picker {@link LocalDateTime}.
 */
public final class DateTimeComponent extends DominoKitPickerComponent<LocalDateTime, DateTimeComponent> {

    public static DateTimeComponent empty(final String id,
                                          final Supplier<LocalDateTime> clearValue) {
        return new DateTimeComponent(
            id,
            clearValue
        );
    }

    private DateTimeComponent(final String id,
                              final Supplier<LocalDateTime> clearValue) {
        super(clearValue);

        // TODO DateTimeFormatInfo https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/7000
        // TODO DateComponent missing locale aware pattern https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/7004
        this.dateBox = DateBox.create() // DateTimeFormatInfo
            .withCalendar(
                (parent, c) -> c.withHeader()
            );

        this.timeBox = TimeBox.create() // DateTimeFormatInfo
            .withTimePicker(
                (parent, c) -> c.withHeader()
            );

        this.layout = FlexLayoutComponent.row()
            .appendChild(dateBox)
            .appendChild(timeBox);

        this.bodyElement.insertFirst(this.layout.element());

        this.setId(id);
    }

    @Override
    public Optional<LocalDateTime> value() {
        return toLocalDateTime(
            dateToLocalDate(
                this.dateBox.getValue()
            ),
            dateToLocalTime(
                this.timeBox.getValue()
            ),
            this.clearValue
        );
    }

    @Override
    public DateTimeComponent setValue(final Optional<LocalDateTime> value) {
        Objects.requireNonNull(value, "value");

        this.dateBox.pauseChangeListeners();
        this.timeBox.pauseChangeListeners();

        this.dateBox.setValue(
            localDateToDate(
                value.orElse(
                    this.clearValue.get()
                ).toLocalDate()
            )
        );

        this.timeBox.setValue(
            localTimeToDate(
                value.orElse(
                    this.clearValue.get()
                ).toLocalTime()
            )
        );

        this.dateBox.resumeChangeListeners();
        this.timeBox.resumeChangeListeners();

        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<LocalDateTime> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        final DateBox dateBox = this.dateBox;
        final TimeBox timeBox = this.timeBox;
        final Supplier<LocalDateTime> clearValue = this.clearValue;

        final ChangeListener<Date> dateBoxChangeListener = (final Date oldValue,
                                                            final Date newValue) -> {
            watcher.onValue(
                toLocalDateTime(
                    dateToLocalDate(newValue), // date
                    dateToLocalTime(
                        timeBox.getValue()
                    ),
                    clearValue
                )
            );
        };

        this.dateBox.addChangeListener(dateBoxChangeListener);

        final ChangeListener<Date> timeBoxChangeListener = (final Date oldValue,
                                                            final Date newValue) -> {
            watcher.onValue(
                toLocalDateTime(
                    dateToLocalDate(
                        dateBox.getValue()
                    ), // date
                    dateToLocalTime(newValue), // time
                    clearValue
                )
            );
        };

        this.timeBox.addChangeListener(timeBoxChangeListener);

        return () -> {
            this.dateBox.removeChangeListener(dateBoxChangeListener);
            this.timeBox.removeChangeListener(timeBoxChangeListener);
        };
    }

    @Override
    public DateTimeComponent setCssText(final String css) {
        this.layout.setCssText(css);
        return this;
    }

    @Override
    public DateTimeComponent setCssProperty(final String name,
                                            final String value) {
        this.layout.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public DateTimeComponent removeCssProperty(final String name) {
        this.layout.removeCssProperty(name);
        return this;
    }

    public DateTimeComponent resetView() {
        return this;
    }

    @Override
    public DateTimeComponent focus() {
        // NOP
        return this;
    }

    @Override
    public DateTimeComponent blur() {
        // NOP
        return this;
    }

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element()) ||
            this.dateBox.isFocused() ||
            this.timeBox.isFocused();
    }

    private final FlexLayoutComponent layout;

    private final DateBox dateBox;

    private final TimeBox timeBox;
}
