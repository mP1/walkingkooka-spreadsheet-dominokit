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

package walkingkooka.spreadsheet.dominokit.ui.integerbox;

import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.IntegerBox;
import org.dominokit.domino.ui.forms.validations.RequiredValidator;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;

import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.utils.ElementsFactory.elements;

/**
 * An IntegerBox that adds a few extras that should be common to all text boxes.
 */
public final class SpreadsheetIntegerBox implements ValueComponent<HTMLFieldSetElement, Integer, SpreadsheetIntegerBox> {

    public static SpreadsheetIntegerBox empty() {
        return new SpreadsheetIntegerBox();
    }

    private SpreadsheetIntegerBox() {
        this.integerBox = new IntegerBox();

        this.integerBox.getInputElement().element().type = "number";
        this.integerBox.getInputElement().element().step = "1";
    }

    public SpreadsheetIntegerBox max(final int value) {
        this.integerBox.getInputElement()
                .element()
                .max = String.valueOf(value);
        return this;
    }

    public SpreadsheetIntegerBox min(final int value) {
        this.integerBox.getInputElement()
                .element()
                .min = String.valueOf(value);
        return this;
    }

    @Override
    public SpreadsheetIntegerBox addChangeListener(final ChangeListener<Optional<Integer>> listener) {
        this.integerBox.addChangeListener(
                SpreadsheetIntegerBoxChangeListener.with(listener)
        );
        return this;
    }

    @Override
    public SpreadsheetIntegerBox addFocusListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");
        this.integerBox.getInputElement()
                .addEventListener(
                        EventType.focus,
                        listener::handleEvent
                );
        return this;
    }

    @Override
    public SpreadsheetIntegerBox addKeydownListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.integerBox.addEventListener(
                EventType.keydown,
                listener
        );
        return this;
    }

    @Override
    public SpreadsheetIntegerBox addKeyupListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.integerBox.addEventListener(
                EventType.keyup,
                listener
        );
        return this;
    }

    public SpreadsheetIntegerBox clearIcon() {
        this.integerBox.apply(
                self -> self.appendChild(
                        PostfixAddOn.of(
                                SpreadsheetIcons.textBoxClear()
                                        .clickable()
                                        .addClickListener(
                                                event -> this.integerBox.clear()
                                        )
                        )
                )
        );
        return this;
    }

    @Override
    public SpreadsheetIntegerBox focus() {
        this.integerBox.focus();
        return this;
    }

    public SpreadsheetIntegerBox enterFiresValueChange() {
        final IntegerBox integerBox = this.integerBox;

        integerBox.addEventListener(
                EventType.keydown.getName(),
                (final Event event) -> {
                    final KeyboardEvent keyboardEvent = Js.cast(event);
                    switch (Key.fromEvent(keyboardEvent)) {
                        case Enter:
                            event.preventDefault();
                            integerBox.triggerChangeListeners(
                                    integerBox.getValue(), // old ???
                                    integerBox.getValue()// new
                            );
                            break;
                        default:
                            // ignore other keys
                            break;
                    }
                }
        );
        return this;
    }

    @Override
    public SpreadsheetIntegerBox alwaysShowHelperText() {
        final DominoElement<Element> element = elements.elementOf(
                this.integerBox.element()
                        .firstElementChild
        );
        element.setHeight(HELPER_TEXT_HEIGHT);
        return this;
    }

    @Override
    public SpreadsheetIntegerBox setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.integerBox.setHelperText(
                text.orElse(null)
        );
        return this;
    }

    @Override
    public SpreadsheetIntegerBox hideMarginBottom() {
        this.integerBox.setMarginBottom("0");
        return this;
    }

    @Override
    public SpreadsheetIntegerBox removeBorders() {
        this.integerBox.getInputElement()
                .parent()
                .setBorder("0")
                .setCssProperty("border-radius", 0);
        return this;
    }

    @Override
    public SpreadsheetIntegerBox setId(final String id) {
        this.integerBox.getInputElement()
                .setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.integerBox.getInputElement()
                .getId();
    }

    @Override
    public SpreadsheetIntegerBox setLabel(final String label) {
        this.integerBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.integerBox.getLabel();
    }

    public SpreadsheetIntegerBox setValidator(final Validator<Integer> validator) {
        final IntegerBox integerBox = this.integerBox;
        integerBox.setAutoValidation(true);
        integerBox.getValidators().clear();
        integerBox.addValidator(
                SpreadsheetIntegerBoxValidator.with(validator)
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.integerBox.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetIntegerBox setValue(final Optional<Integer> value) {
        Objects.requireNonNull(value, "value");

        this.integerBox.setValue(
                value.orElse(null)
        );
        return this;
    }

    @Override //
    public Optional<Integer> value() {
        return Optional.ofNullable(
                this.integerBox.getValue()
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public boolean isDisabled() {
        return this.integerBox.isDisabled();
    }

    @Override
    public SpreadsheetIntegerBox setDisabled(final boolean disabled) {
        this.integerBox.setDisabled(disabled);
        return this;
    }

    @Override
    public SpreadsheetIntegerBox required() {
        this.required = true;
        return this.setValidator(
                new RequiredValidator<>(this.integerBox)
        );
    }

    @Override
    public SpreadsheetIntegerBox optional() {
        throw new UnsupportedOperationException();
    }

    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

    @Override
    public SpreadsheetIntegerBox validate() {
        this.integerBox.validate();
        return this;
    }

    // Object...........................................................................................................

    public String toString() {
        return this.integerBox.toString();
    }

    private final IntegerBox integerBox;
}
