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
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.spreadsheet.dominokit.value.HasValueWatchers;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;

import java.util.Optional;

abstract class IntegerBoxComponentLike implements FormValueComponent<HTMLFieldSetElement, Integer, IntegerBoxComponent>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, IntegerBoxComponent, Integer>,
    HasValueWatchers<HTMLFieldSetElement, Integer, IntegerBoxComponent> {

    IntegerBoxComponentLike() {
        super();
    }

    public abstract  IntegerBoxComponent max(final int value);

    public abstract  IntegerBoxComponent min(final int value);

    public abstract  IntegerBoxComponent step(final int step);

    public abstract IntegerBoxComponent pattern(final String pattern);

    // addXXXListener...................................................................................................

    @Override
    public final IntegerBoxComponent addBlurListener(final EventListener listener) {
        return this.addEventListener(
            EventType.blur,
            listener
        );
    }

    @Override
    public final IntegerBoxComponent addClickListener(final EventListener listener) {
        return this.addEventListener(
            EventType.click,
            listener
        );
    }

    @Override
    public final IntegerBoxComponent addContextMenuListener(final EventListener listener) {
        return this.addEventListener(
            EventType.contextmenu,
            listener
        );
    }

    @Override
    public final IntegerBoxComponent addFocusListener(final EventListener listener) {
        return this.addEventListener(
            EventType.focus,
            listener
        );
    }

    @Override
    public final IntegerBoxComponent addInputListener(final EventListener listener) {
        return this.addEventListener(
            EventType.input,
            listener
        );
    }

    @Override
    public final IntegerBoxComponent addKeyDownListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keydown,
            listener
        );
    }

    @Override
    public final IntegerBoxComponent addKeyUpListener(final EventListener listener) {
        return this.addEventListener(
            EventType.keyup,
            listener
        );
    }

    abstract IntegerBoxComponent addEventListener(final EventType eventType,
                                                  final EventListener listener);

    abstract IntegerBoxComponent removeEventListener(final EventType eventType,
                                                     final EventListener listener);

    public abstract IntegerBoxComponent clearIcon();

    public abstract IntegerBoxComponent enterFiresValueChange();

    public abstract IntegerBoxComponent setValidator(final Validator<Optional<Integer>> validator);

    // HasValueWatcher..................................................................................................

    @Override
    public final Runnable addValueWatcher(final ValueWatcher<Integer> watcher) {
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
}
