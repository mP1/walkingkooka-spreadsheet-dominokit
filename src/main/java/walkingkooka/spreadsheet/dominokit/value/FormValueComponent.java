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

import elemental2.dom.HTMLElement;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A {@link FormValueComponentLike} that also includes value getter/setter support.
 */
public interface FormValueComponent<E extends HTMLElement, V, C extends FormValueComponent<E, V, C>>
    extends FormValueComponentLike<E, C>,
    ValueComponent<E, V, C> {

    // addValueWatcherSkipsIfErrors.....................................................................................

    /**
     * Adds a {@link ValueWatcher} which will only be fired if this {@link FormValueComponent} has no errors.
     */
    default Runnable addValueWatcherSkipIfErrors(final ValueWatcher<V> watcher) {
        return this.addValueWatcher(
            this.skipIfErrorPresent(watcher)
        );
    }

    default C addValueWatcherSkipIfErrors2(final ValueWatcher<V> watcher,
                                           final Consumer<Runnable> remover) {
        this.addValueWatcher2(
            this.skipIfErrorPresent(watcher),
            remover
        );
        return (C) this;
    }

    default C addValueWatcherSkipIfErrors2(final ValueWatcher<V> watcher) {
        this.addValueWatcher(
            this.skipIfErrorPresent(watcher)
        );
        return (C) this;
    }

    /**
     * Wraps the given {@link ValueWatcher} which will only be fired if this {@link FormValueComponent} has no errors.
     */
    default ValueWatcher<V> skipIfErrorPresent(final ValueWatcher<V> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return (Optional<V> value) -> {
            if(this.errors().isEmpty()) {
                watcher.onValue(value);
            }
        };
    }
}
