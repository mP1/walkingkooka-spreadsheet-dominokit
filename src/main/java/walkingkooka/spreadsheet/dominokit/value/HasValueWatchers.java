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

import java.util.function.Consumer;

public interface HasValueWatchers<E extends HTMLElement, V, C extends ValueComponent<E, V, C>> extends ValueComponent<E, V, C>{

    Runnable addValueWatcher(final ValueWatcher<V> watcher);

    default C addValueWatcher2(final ValueWatcher<V> watcher,
                               final Consumer<Runnable> remover) {
        remover.accept(
            this.addValueWatcher(watcher)
        );
        return (C) this;
    }

    default C addValueWatcher2(final ValueWatcher<V> watcher) {
        this.addValueWatcher(watcher);
        return (C) this;
    }
}
