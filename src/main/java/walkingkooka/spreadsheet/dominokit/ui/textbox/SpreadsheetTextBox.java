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
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.Value;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIcons;

import java.util.Objects;

/**
 * A textbox that adds a few extras that should be common to all text boxes.
 */
public final class SpreadsheetTextBox implements IsElement<HTMLFieldSetElement>,
        Value<String> {

    public static SpreadsheetTextBox empty() {
        return new SpreadsheetTextBox();
    }

    private SpreadsheetTextBox() {
        this.textBox = new TextBox()
                .setMarginBottom("0");
    }

    public SpreadsheetTextBox addChangeListener(final ChangeListener<String> listener) {
        Objects.requireNonNull(listener, "listener");

        this.textBox.addChangeListener(listener);
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

    public void focus() {
        this.textBox.focus();
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

    public SpreadsheetTextBox setId(final String id) {
        this.textBox.getInputElement()
                .setId(id);
        return this;
    }

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

    public void setValue(final String value) {
        Objects.requireNonNull(value, "value");

        this.textBox.setValue(value);
    }

    @Override //
    public String value() {
        return this.textBox.getValue();
    }

    // Object...........................................................................................................

    public String toString() {
        return this.textBox.toString();
    }

    private final TextBox textBox;
}
