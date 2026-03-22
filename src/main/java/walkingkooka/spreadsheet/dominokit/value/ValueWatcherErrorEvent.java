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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * An event that captures a new or different errors probably belonging to a {@link ValueComponent} that also implements
 * {@link walkingkooka.spreadsheet.dominokit.ComponentWithErrors}.
 */
final class ValueWatcherErrorEvent<T> extends ValueWatcherEvent<T> {

    static <T> ValueWatcherErrorEvent<T> with(final Optional<List<String>> errors) {
        return new ValueWatcherErrorEvent<>(
            Objects.requireNonNull(errors, "errors")
        );
    }

    private ValueWatcherErrorEvent(final Optional<List<String>> errors) {
        this.errors = errors;
    }

    @Override
    public void accept(final ValueWatcher<T> watcher) {
        this.fire(watcher);
    }

    private void fire(final ValueWatcher<T> watcher) {
        watcher.onErrors(
            this.errors
        );
    }

    private final Optional<List<String>> errors;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.errors.toString();
    }
}
