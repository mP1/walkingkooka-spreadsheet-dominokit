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

package walkingkooka.spreadsheet.dominokit.select;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.spreadsheet.dominokit.value.HasValueWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for {@link SelectComponent} that captures common members for main/test.
 */
abstract class SelectComponentLike<T> implements FormValueComponent<HTMLFieldSetElement, T, SelectComponent<T>>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, SelectComponent<T>, T>,
    HasValueWatchers<HTMLFieldSetElement, T, SelectComponent<T>> {

    SelectComponentLike() {
        super();
    }

    /**
     * Appends a new value to the drop down.
     */
    abstract public SelectComponent<T> appendOption(final Optional<T> value);

    abstract public SelectComponent<T> clearOptions();

    // helperText.......................................................................................................

    @Override
    public final SelectComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SelectComponent<T> setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Optional<String> helperText() {
        return Optional.empty();
    }

    // addXXXListener...................................................................................................

    @Override
    public final SelectComponent<T> addBlurListener(final EventListener listener) {
        return this.addEventListener(
            EventType.blur,
            listener
        );
    }

    @Override
    public final SelectComponent<T> addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public final SelectComponent<T> addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public final SelectComponent<T> addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public final SelectComponent<T> addInputListener(final EventListener listener) {
        return this.addEventListener(
            EventType.input,
            listener
        );
    }

    @Override
    public final SelectComponent<T> addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public final SelectComponent<T> addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    abstract SelectComponent<T> addEventListener(final EventType type,
                                                 final EventListener listener);

    abstract SelectComponent<T> removeChangeListener(final ChangeListener<Optional<T>> listener);

    // HasValueWatchers.................................................................................................

    @Override
    public final Runnable addValueWatcher(final ValueWatcher<T> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        final ChangeListener<Optional<T>> changeListener = (final Optional<T> oldValue,
                                                            final Optional<T> newValue) -> watcher.onValue(newValue);
        this.addChangeListener(changeListener);
        return () -> this.removeChangeListener(changeListener);
    }
}
