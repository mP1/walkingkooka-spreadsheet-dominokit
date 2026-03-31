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
import walkingkooka.spreadsheet.dominokit.FakeHtmlComponent;

import java.util.Optional;

public class FakeValueComponent<E extends HTMLElement, V, C extends ValueComponent<E, V, C>> extends FakeHtmlComponent<E, C>
    implements ValueComponent<E, V, C> {

    public FakeValueComponent() {
        super();
    }

    // ValueComponent...................................................................................................

    @Override
    public Optional<V> value() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C setValue(final Optional<V> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<V> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C setDisabled(boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public C hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public C blur() {
        throw new UnsupportedOperationException();
    }
}
