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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * An event that captures the new value and {@link AppContext}.
 */
final class ValueWatcherEvent<T> implements Consumer<ValueWatcher<T>> {

    static <T> ValueWatcherEvent<T> with(final Optional<T> value,
                                         final AppContext context) {
        return new ValueWatcherEvent<>(
            Objects.requireNonNull(value, "value"),
            Objects.requireNonNull(context, "context")
        );
    }

    private ValueWatcherEvent(final Optional<T> value,
                              final AppContext context) {
        this.value = value;
        this.context = context;
    }

    @Override
    public void accept(final ValueWatcher<T> watcher) {
        try {
            this.fire(watcher);
        } catch (final Exception cause) {
            this.context.error(
                this.getClass().getSimpleName() + ".accept exception: " + cause.getMessage(),
                cause
            );
        }
    }

    private void fire(final ValueWatcher<T> watcher) {
        watcher.onValue(
            this.value,
            this.context
        );
    }

    private final Optional<T> value;

    private final AppContext context;

    @Override
    public String toString() {
        return CharSequences.quoteIfChars(this.value)
            .toString();
    }
}
