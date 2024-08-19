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

package walkingkooka.spreadsheet.dominokit.history.util;

import walkingkooka.ToStringBuilder;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link HistoryTokenWatcher} that watches all history token events, and filters only a certain type mostly extracting the save value.
 * <br>
 * This is useful for tracking the last pattern saves and then create menu items in a menu.
 */
public final class HistoryTokenRecorder<T> implements HistoryTokenWatcher {

    public static <T> HistoryTokenRecorder<T> with(final Function<HistoryToken, Optional<T>> mapper,
                                                   final int max) {
        Objects.requireNonNull(mapper, "mapper");
        if (max <= 0) {
            throw new IllegalArgumentException("Invalid max " + max + " <= 0");
        }

        return new HistoryTokenRecorder<>(
                mapper,
                max
        );
    }

    private HistoryTokenRecorder(final Function<HistoryToken, Optional<T>> mapper,
                                 final int max) {
        this.mapper = mapper;
        this.max = max;
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final List<T> values = this.values;

        final Optional<T> maybeNewValue = this.mapper.apply(
                context.historyToken()
        );
        if (maybeNewValue.isPresent()) {
            final T newValue = maybeNewValue.get();

            values.remove(newValue);
            values.add(
                    0,
                    newValue
            );
            final int size = values.size();
            if (size > this.max) {
                values.remove(
                        size - 1
                ); // remove oldest.
            }
        }
    }

    private final Function<HistoryToken, Optional<T>> mapper;

    /**
     * Clears the cache of values.
     */
    public void clear() {
        this.values.clear();
    }

    /**
     * Return the last {#max} values, with the first element holding the most recent {@link HistoryToken}.
     */
    public List<T> values() {
        return Lists.readOnly(this.values);
    }

    private final List<T> values = Lists.linkedList();

    private final int max;

    @Override
    public String toString() {
        return ToStringBuilder.empty()
                .label("max")
                .value(this.max)
                .value(this.values)
                .build();
    }
}
