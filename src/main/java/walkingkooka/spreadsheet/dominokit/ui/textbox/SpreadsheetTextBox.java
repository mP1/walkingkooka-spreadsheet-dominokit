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

package walkingkooka.spreadsheet.dominokit.ui.textbox;

import elemental2.dom.Event;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Optional;

/**
 * A textbox that adds a few extras that should be common to all text boxes.
 */
public final class SpreadsheetTextBox implements ValueComponent<HTMLFieldSetElement, String> {

    public static SpreadsheetTextBox empty() {
        return new SpreadsheetTextBox();
    }

    private SpreadsheetTextBox() {
        this.textBox = new TextBox()
                .setMarginBottom("0");
    }

    @Override
    public SpreadsheetTextBox addChangeListener(final ChangeListener<Optional<String>> listener) {
        this.textBox.addChangeListener(
                SpreadsheetTextBoxChangeListener.with(listener)
        );
        return this;
    }

    public SpreadsheetTextBox clearIcon() {
        this.textBox.apply(
                self -> self.appendChild(
                        PostfixAddOn.of(
                                SpreadsheetIcons.close()
                                        .clickable()
                                        .addClickListener(
                                                event -> this.textBox.clear()
                                        )
                        )
                )
        );
        return this;
    }

    public SpreadsheetTextBox disableSpellcheck() {
        this.textBox.getInputElement()
                .element()
                .spellcheck = false;
        return this;
    }

    @Override
    public SpreadsheetTextBox focus() {
        this.textBox.focus();
        return this;
    }

    public SpreadsheetTextBox enterFiresValueChange() {
        final TextBox textBox = this.textBox;

        textBox.addEventListener(
                EventType.keydown.getName(),
                (final Event event) -> {
                    final KeyboardEvent keyboardEvent = Js.cast(event);
                    switch (Key.fromEvent(keyboardEvent)) {
                        case Enter:
                            event.preventDefault();
                            textBox.triggerChangeListeners(
                                    textBox.getValue(), // old ???
                                    textBox.getValue()// new
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
    public SpreadsheetTextBox setId(final String id) {
        this.textBox.getInputElement()
                .setId(id);
        return this;
    }

    @Override
    public SpreadsheetTextBox setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }


    public SpreadsheetTextBox setValidator(final Validator<String> validator) {
        final TextBox textBox = this.textBox;
        textBox.setAutoValidation(true);
        textBox.getValidators().clear();
        textBox.addValidator(
                SpreadsheetTextBoxValidator.with(validator)
        );
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.textBox.element();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetTextBox setValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");

        this.textBox.setValue(
                value.orElse(null)
        );
        return this;
    }

    @Override //
    public Optional<String> value() {
        return Optional.ofNullable(
                CharSequences.nullToEmpty(
                        this.textBox.getValue()
                ).toString()
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetTextBox required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetTextBox optional() {
        throw new UnsupportedOperationException();
    }

    // Object...........................................................................................................

    public String toString() {
        return this.textBox.toString();
    }

    private final TextBox textBox;
}
