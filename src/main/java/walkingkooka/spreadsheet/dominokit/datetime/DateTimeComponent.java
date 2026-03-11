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
import org.gwtproject.i18n.shared.cldr.DateTimeFormatInfo;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * A date picker that displays a calendar and time picker {@link LocalDateTime}.
 */
public final class DateTimeComponent extends DominoKitPickerComponent<LocalDateTime, DateTimeComponent> {

    public static DateTimeComponent empty(final String id,
                                          final DateTimeComponentContext context) {
        return new DateTimeComponent(
            id,
            context
        );
    }

    private DateTimeComponent(final String id,
                              final DateTimeComponentContext context) {
        super(context);

        final DateTimeFormatInfo dateTimeFormatInfo = DateTimeComponentContextDateTimeFormatInfo.with(context);

        this.dateBox = createDateBox(dateTimeFormatInfo);
        this.timeBox = createTimeBox(dateTimeFormatInfo);

        this.layout = FlexLayoutComponent.row()
            .appendChild(this.dateBox)
            .appendChild(this.timeBox);

        this.bodyElement.insertFirst(this.layout.element());

        this.setId(id);
    }

    @Override
    public Optional<LocalDateTime> value() {
        return toLocalDateTime(
            Optional.ofNullable(
                this.dateBox.getValue()
            ).flatMap(DateTimeComponent::dateToLocalDate),
            Optional.of(
                this.timeBox.getValue()
            ).flatMap(DateTimeComponent::dateToLocalTime)
        );
    }

    @Override
    public DateTimeComponent setValue(final Optional<LocalDateTime> value) {
        Objects.requireNonNull(value, "value");

        this.dateBox.pauseChangeListeners();
        this.timeBox.pauseChangeListeners();

        this.dateBox.setValue(
            localDateToDate(
                value.map(LocalDateTime::toLocalDate)
            )
        );

        this.timeBox.setValue(
            localTimeToDate(
                value.map(LocalDateTime::toLocalTime)
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

        // both ChangeListeners only fire when both the date & time are either empty or present.
        // firing when only one is set will cause the LocalDateTime to be cleared which will eventually clear both
        // the date and time components, making it impossible to enter a LocalDateTime.

        final ChangeListener<Date> dateBoxChangeListener = (final Date oldValue,
                                                            final Date newValue) -> {
            final Optional<LocalDate> date = dateToLocalDate(newValue);
            final Optional<LocalTime> time = dateToLocalTime(
                timeBox.getValue()
            );

            if (date.isPresent() == time.isPresent()) {
                watcher.onValue(
                    toLocalDateTime(
                        date,
                        time
                    )
                );
            }
        };

        this.dateBox.addChangeListener(dateBoxChangeListener);

        final ChangeListener<Date> timeBoxChangeListener = (final Date oldValue,
                                                            final Date newValue) -> {
            final Optional<LocalDate> date = dateToLocalDate(
                dateBox.getValue()
            );
            final Optional<LocalTime> time = dateToLocalTime(newValue);

            if (date.isPresent() == time.isPresent()) {
                watcher.onValue(
                    toLocalDateTime(
                        date,
                        time
                    )
                );
            }
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
