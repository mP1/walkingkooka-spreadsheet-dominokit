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

import elemental2.dom.Element;
import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.KeyboardEvent;
import jsinterop.base.Js;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.TextBox;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import org.dominokit.domino.ui.utils.PostfixAddOn;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.dom.Key;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.utils.ElementsFactory.elements;

/**
 * A textbox that adds a few extras that should be common to all text boxes.
 */
public final class TextBoxComponent extends TextBoxComponentLike
    implements FormValueComponent<HTMLFieldSetElement, String, TextBoxComponent>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, TextBoxComponent, String> {

    public static TextBoxComponent empty() {
        return new TextBoxComponent();
    }

    private TextBoxComponent() {
        this.textBox = new TextBox()
            .setEmptyAsNull(true);
    }

    @Override
    public TextBoxComponent addChangeListener(final ChangeListener<Optional<String>> listener) {
        this.textBox.addChangeListener(
            TextBoxComponentChangeListener.with(listener)
        );
        return this;
    }

    @Override
    TextBoxComponent addEventListener(final EventType eventType,
                                      final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.textBox.getInputElement()
            .addEventListener(
                eventType,
                listener
            );
        return this;
    }

    public TextBoxComponent autocompleteOff() {
        this.textBox.getInputElement()
            .element()
            .autocomplete = "off";
        return this;
    }

    public TextBoxComponent clearIcon() {
        this.textBox.apply(
            self -> self.appendChild(
                PostfixAddOn.of(
                    SpreadsheetIcons.textBoxClear()
                        .clickable()
                        .addClickListener(
                            event -> this.textBox.clear()
                        )
                )
            )
        );
        return this;
    }

    public TextBoxComponent disableSpellcheck() {
        this.textBox.getInputElement()
            .element()
            .spellcheck = false;
        return this;
    }

    @Override
    public TextBoxComponent focus() {
        this.textBox.focus();
        return this;
    }

    public TextBoxComponent enterFiresValueChange() {
        return this.addKeyDownListener(
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
    }

    public TextBoxComponent magnifyingGlassIcon() {
        this.textBox.apply(
            self -> self.appendChild(
                PostfixAddOn.of(
                    SpreadsheetIcons.textBoxMagnifyGlass()
                        .clickable()
                        .addClickListener(
                            event -> this.textBox.focus()
                        )
                )
            )
        );
        return this;
    }

    @Override
    public TextBoxComponent alwaysShowHelperText() {
        final DominoElement<Element> element = elements.elementOf(
            this.textBox.element()
                .firstElementChild
        );
        element.setHeight(HELPER_TEXT_HEIGHT);
        return this;
    }

    @Override
    public TextBoxComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.textBox.setHelperText(
            text.orElse(null)
        );
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return Optional.ofNullable(
            this.textBox.getHelperText()
        );
    }

    @Override
    public TextBoxComponent hideMarginBottom() {
        this.textBox.setMarginBottom("0");
        return this;
    }

    @Override
    public TextBoxComponent removeBorders() {
        this.textBox.getInputElement()
            .parent()
            .setBorder("0")
            .setCssProperty("border-radius", 0);
        return this;
    }

    @Override
    public TextBoxComponent removePadding() {
        this.textBox.getInputElement()
            .parent()
            .setPadding("0");
        return this;
    }

    @Override
    public TextBoxComponent setId(final String id) {
        CharSequences.failIfNullOrEmpty(id, "id");

        this.textBox.getInputElement()
            .setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.textBox.getInputElement()
            .getId();
    }

    @Override
    public TextBoxComponent setLabel(final String label) {
        this.textBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.textBox.getLabel();
    }

    public TextBoxComponent setValidator(final Validator<Optional<String>> validator) {
        final TextBox textBox = this.textBox;
        textBox.setAutoValidation(true);
        textBox.getValidators().clear();
        textBox.addValidator(
            TextBoxComponentValidator.with(validator)
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
    public TextBoxComponent setValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");

        this.textBox.withValue(
            value.orElse(null),
            true // silent dont fire change listeners.
        );
        return this;
    }

    @Override //
    public Optional<String> value() {
        final String value = this.textBox.getValue();

        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(value) ?
                null :
                value
        );
    }

    // HasValueWatchers.................................................................................................

    /**
     * Fires a {@link ValueWatcher#onValue(Optional)} when the value changes or ENTER is hit.
     */
    @Override
    public Runnable addValueWatcher(final ValueWatcher<String> watcher) {
        Objects.requireNonNull(watcher, "watcher");

        final EventListener keyDownListener = (e) -> {
            final KeyboardEvent keyboardEvent = (KeyboardEvent) e;
            if (Key.Enter.equals(keyboardEvent.key)) {
                watcher.onValue(this.value());
            }
        };
        final TextBox textBox = this.textBox;
        textBox.addEventListener(
            EventType.keydown,
            keyDownListener
        );

        final EventListener inputListener = (e) -> watcher.onValue(this.value());
        textBox.addEventListener(
            EventType.input,
            inputListener
        );

        return () -> {
            textBox.removeEventListener(
                EventType.keydown,
                keyDownListener
            );
            textBox.removeEventListener(
                EventType.input,
                inputListener
            );
        };
    }

    // ValueComponent...................................................................................................

    @Override
    public boolean isDisabled() {
        return this.textBox.isDisabled();
    }

    @Override
    public TextBoxComponent setDisabled(final boolean disabled) {
        this.textBox.setDisabled(disabled);
        return this;
    }

    @Override
    public TextBoxComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextBoxComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public TextBoxComponent validate() {
        this.textBox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return Lists.readOnly(
            this.textBox.getErrors()
        );
    }

    @Override
    public TextBoxComponent setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");

        this.textBox.invalidate(
            Lists.immutable(errors)
        );
        return this;
    }

    // width............................................................................................................

    @Override
    public int width() {
        return this.textBox.element()
            .offsetWidth;
    }

    // height...........................................................................................................

    @Override
    public int height() {
        return this.textBox.element()
            .offsetHeight;
    }

    // setCssText.......................................................................................................

    @Override
    public TextBoxComponent setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.textBox.cssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public TextBoxComponent setCssProperty(final String name,
                                           final String value) {
        Objects.requireNonNull(name, "name");

        this.textBox.setCssProperty(
            name,
            value
        );
        return this;
    }

    // removeCssProperty................................................................................................

    @Override
    public TextBoxComponent removeCssProperty(final String name) {
        Objects.requireNonNull(name, "name");

        this.textBox.removeCssProperty(name);
        return this;
    }


    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.textBox.element());
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.textBox.toString();
    }

    private final TextBox textBox;

    // FormValueComponentTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
