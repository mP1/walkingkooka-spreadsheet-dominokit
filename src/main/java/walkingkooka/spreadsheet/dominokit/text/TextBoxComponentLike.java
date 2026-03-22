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
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.spreadsheet.dominokit.dom.HasEventListeners;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;

import java.util.Optional;

abstract class TextBoxComponentLike implements FormValueComponent<HTMLFieldSetElement, String, TextBoxComponent>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, TextBoxComponent, String>,
    HasEventListeners<String, TextBoxComponent> {

    TextBoxComponentLike() {
        super();
    }

    public abstract TextBoxComponent autocompleteOff();

    public abstract TextBoxComponent clearIcon();

    public abstract TextBoxComponent disableSpellcheck();

    public abstract TextBoxComponent enterFiresValueChange();

    public abstract TextBoxComponent magnifyingGlassIcon();

    @Override
    public final TextBoxComponent optional() {
        this.required = false;

        this.setValidator(this.validator());
        this.validate();
        return (TextBoxComponent) this;
    }

    @Override
    public final TextBoxComponent required() {
        this.required = true;

        this.setValidator(this.validator());
        this.validate();
        return (TextBoxComponent) this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    public final Optional<Validator<Optional<String>>> validator() {
        return this.validator;
    }

    Optional<Validator<Optional<String>>> validator = Optional.empty();

    public abstract TextBoxComponent setValidator(final Optional<Validator<Optional<String>>> validator);

    // HasEventListeners................................................................................................

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
}
