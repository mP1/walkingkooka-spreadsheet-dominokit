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

package walkingkooka.spreadsheet.dominokit.text;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.KeyboardEvent;
import org.dominokit.domino.ui.events.EventType;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.spreadsheet.dominokit.value.HasValueWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.util.Objects;
import java.util.Optional;

abstract class TextBoxComponentLike implements FormValueComponent<HTMLFieldSetElement, String, TextBoxComponent>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, TextBoxComponent, String>,
    HasValueWatchers<HTMLFieldSetElement, String, TextBoxComponent> {

    TextBoxComponentLike() {
        super();
    }

    // addXXXListener...................................................................................................

    @Override
    public final TextBoxComponent addBlurListener(final EventListener listener) {
        return this.addEventListener(
            EventType.blur,
            listener
        );
    }

    @Override
    public final TextBoxComponent addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public final TextBoxComponent addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public final TextBoxComponent addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public final TextBoxComponent addInputListener(final EventListener listener) {
        return this.addEventListener(
            EventType.input,
            listener
        );
    }

    @Override
    public final TextBoxComponent addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public final TextBoxComponent addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    abstract TextBoxComponent addEventListener(final EventType eventType,
                                               final EventListener listener);

    /**
     * Fires a {@link ValueWatcher#onValue(Optional)} when the value changes or ENTER is hit.
     */
    @Override
    public final Runnable addValueWatcher(final ValueWatcher<String> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        final EventListener keyDownListener = (e) -> {
            final KeyboardEvent keyboardEvent = (KeyboardEvent) e;
            if (Key.Enter.equals(keyboardEvent.key)) {
                watcher.onValue(this.value());
            }
        };
        this.addKeyDownListener(keyDownListener);

        final EventListener inputListener = (e) -> watcher.onValue(this.value());
        this.addInputListener(inputListener);

        return () -> {
            this.removeEventListener(
                EventType.keydown,
                keyDownListener
            );
            this.removeEventListener(
                EventType.input,
                inputListener
            );
        };
    }

    abstract void removeEventListener(final EventType type,
                                      final EventListener listener);
}
