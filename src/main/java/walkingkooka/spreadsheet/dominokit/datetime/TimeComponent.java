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

import org.dominokit.domino.ui.forms.TimeBox;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A component that includes a {@link TimeBox} and a {@link org.dominokit.domino.ui.timepicker.TimePicker}, allowing
 * the user to enter or select a {@link LocalTime}.
 * A {@link Supplier} is required to supply a NOW time when an {@link Optional#empty()} is given to {@link #setValue(Optional)}.
 */
public final class TimeComponent extends DominoKitPickerComponent<LocalTime, TimeComponent> {

    public static TimeComponent empty(final String id,
                                      final Supplier<LocalTime> clearValue) {
        return new TimeComponent(
            id,
            clearValue
        );
    }

    private TimeComponent(final String id,
                          final Supplier<LocalTime> clearValue) {
        super(clearValue);

        this.timeBox = TimeBox.create() // DateTimeFormatInfo
            .withTimePicker(
                (parent, c) -> c.withHeader()
            );

        this.bodyElement.insertFirst(
            this.timeBox.element()
        );
        this.setId(id);
    }

    @Override
    public Optional<LocalTime> value() {
        return dateToLocalTime(
            this.timeBox.getValue()
        );
    }

    @Override
    public TimeComponent setValue(final Optional<LocalTime> value) {
        Objects.requireNonNull(value, "value");

        // pause/set/unpause necessary otherwise when dialog resets the "cell" will also be saved with the reset value
        this.timeBox.pauseChangeListeners();
        this.timeBox.setValue(
            localTimeToDate(
                value.orElse(
                    this.clearValue.get()
                )
            )
        );
        this.timeBox.resumeChangeListeners();

        return this;
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<LocalTime> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        final ChangeListener<Date> changeListener = (final Date oldValue,
                                                     final Date newValue) -> {
            watcher.onValue(
                dateToLocalTime(newValue)
            );
        };

        this.timeBox.addChangeListener(changeListener);
        return () -> this.timeBox.removeChangeListener(changeListener);
    }

    // HtmlComponent....................................................................................................

    @Override
    public TimeComponent setCssText(final String css) {
        this.timeBox.cssText(css);
        return this;
    }

    @Override
    public TimeComponent setCssProperty(final String name,
                                        final String value) {
        this.timeBox.setCssProperty(
            name,
            value
        );
        return this;
    }

    @Override
    public TimeComponent removeCssProperty(final String name) {
        this.timeBox.removeCssProperty(name);
        return this;
    }

    @Override
    public TimeComponent focus() {
        this.timeBox.focus();
        return this;
    }

    @Override
    public TimeComponent blur() {
        this.timeBox.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.timeBox.isFocused();
    }

    private final TimeBox timeBox;
}
