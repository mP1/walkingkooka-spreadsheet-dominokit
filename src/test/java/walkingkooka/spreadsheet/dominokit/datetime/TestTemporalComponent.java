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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ValidatorHelper;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.util.Objects;
import java.util.Optional;

abstract class TestTemporalComponent<V, C extends TestTemporalComponent<V, C>> extends TemporalComponent<V, C>
    implements TestHtmlElementComponent<HTMLDivElement, C>,
    ValidatorHelper {

    TestTemporalComponent() {
        super();
    }

    @Override
    public final C addChangeListener(final ChangeListener<Optional<V>> listener) {
        return (C) this;
    }

    @Override
    public final C addClickListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    // HasValueWatchers.................................................................................................

    @Override
    public final Runnable addValueWatcher(final ValueWatcher<V> watcher) {
        Objects.requireNonNull(watcher, "watcher");
        return () -> {};
    }
}
