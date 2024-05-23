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

package walkingkooka.spreadsheet.dominokit.ui.suggestbox;

import elemental2.dom.Element;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.elements.SpanElement;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.forms.suggest.SuggestBox;
import org.dominokit.domino.ui.forms.suggest.SuggestOption;
import org.dominokit.domino.ui.forms.suggest.SuggestionsStore;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ui.validator.SpreadsheetValidators;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasText;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static org.dominokit.domino.ui.utils.ElementsFactory.elements;

/**
 * A text box component that includes support for finding a label.
 */
public final class SpreadsheetSuggestBoxComponent<T extends HasText> implements SpreadsheetSuggestBoxComponentLike<T> {

    public static <T extends HasText> SpreadsheetSuggestBoxComponent<T> with(final Function<String, T> parser,
                                                                             final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore) {
        Objects.requireNonNull(parser, "parser");
        Objects.requireNonNull(suggestionsStore, "suggestionsStore");

        return new SpreadsheetSuggestBoxComponent<>(
                parser,
                suggestionsStore
        );
    }

    private SpreadsheetSuggestBoxComponent(final Function<String, T> parser,
                                           final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore) {
        this.parser = parser;
        final SuggestBox<String, SpanElement, SuggestOption<String>> suggestBox = SuggestBox.create(
                suggestionsStore
        );
        this.suggestBox = suggestBox;
        suggestBox.setAutoValidation(true);

        this.required();
        this.validate();
    }

    // id...............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setId(final String id) {
        this.suggestBox.getInputElement()
                .setId(id);
        return this;
    }

    @Override
    public String id() {
        return this.suggestBox.getInputElement()
                .getId();
    }

    // label............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setLabel(final String label) {
        this.suggestBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.suggestBox.getLabel();
    }

    // helperText.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> alwaysShowHelperText() {
        final DominoElement<Element> element = elements.elementOf(
                this.suggestBox.element()
                        .firstElementChild
        );
        element.setHeight(HELPER_TEXT_HEIGHT);
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");

        this.suggestBox.setHelperText(
                text.orElse(null)
        );
        return this;
    }

    @Override
    public Optional<String> helperText() {
        return Optional.ofNullable(
                this.suggestBox.getHelperText()
        );
    }

    // StringValue......................................................................................................

    public SpreadsheetSuggestBoxComponent<T> setStringValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");

        this.suggestBox.withValue(
                value.orElse(""),
                true // silent
        );
        return this;
    }

    public Optional<String> stringValue() {
        final String text = this.suggestBox.getStringValue();

        return Optional.ofNullable(
                CharSequences.isNullOrEmpty(text) ?
                        null :
                        text
        );
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setValue(final Optional<T> label) {
        Objects.requireNonNull(label, "label");

        this.suggestBox.setValue(
                label.map(T::text)
                        .orElse("")
        );
        return this;
    }

    @Override //
    public Optional<T> value() {
        return tryParse(
                this.suggestBox.getStringValue()
        );
    }

    private Optional<T> tryParse(final String string) {
        T value;

        try {
            value = this.parser.apply(string);
        } catch (final Exception ignore) {
            value = null;
        }

        return Optional.ofNullable(value);
    }

    private final Function<String, T> parser;

    // isDisabled.......................................................................................................

    @Override
    public boolean isDisabled() {
        return this.suggestBox.isDisabled();
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setDisabled(final boolean disabled) {
        this.suggestBox.setDisabled(disabled);
        return this;
    }

    // validation.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> optional() {
        this.required = false;
        return this.setValidator(
                SpreadsheetValidators.optional(
                        SpreadsheetValidators.tryCatch(SpreadsheetSelection::labelName)
                )
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> required() {
        this.required = true;
        return this.setValidator(
                SpreadsheetValidators.tryCatch(SpreadsheetSelection::labelName)
        );
    }

    private SpreadsheetSuggestBoxComponent setValidator(final Validator<Optional<String>> validator) {
        this.suggestBox.getValidators()
                .clear();
        this.suggestBox.addValidator(
                SpreadsheetSuggestBoxComponentValidator.with(validator)
        );
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    boolean required;

    @Override
    public SpreadsheetSuggestBoxComponent<T> validate() {
        this.suggestBox.validate();
        return this;
    }

    @Override
    public List<String> errors() {
        return Lists.readOnly(
                this.suggestBox.getErrors()
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> setErrors(final List<String> errors) {
        this.suggestBox.invalidate(
                Lists.immutable(errors)
        );
        return this;
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> addChangeListener(final ChangeListener<Optional<T>> listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addChangeListener(
                (final String oldValue,
                 final String newValue) -> listener.onValueChanged(
                        tryParse(oldValue),
                        tryParse(newValue)
                )
        );

        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addFocusListener(final EventListener listener) {
        this.suggestBox.addEventListener(
                EventType.focus,
                listener
        );
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addKeydownListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addEventListener(
                EventType.keydown,
                listener
        );
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> addKeyupListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addEventListener(
                EventType.keyup,
                listener
        );
        return this;
    }

    // focus............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> focus() {
        this.suggestBox.focus();
        return this;
    }

    // styling..........................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> hideMarginBottom() {
        this.suggestBox.setMarginBottom("");
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent<T> removeBorders() {
        this.suggestBox.getInputElement()
                .parent()
                .parent()
                .setBorder("0")
                .setCssProperty("border-radius", 0);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent<T> setCssText(final String css) {
        Objects.requireNonNull(css, "css");

        this.suggestBox.cssText(css);
        return this;
    }

    // IsElement........................................................................................................

    @Override
    public HTMLFieldSetElement element() {
        return this.suggestBox.element();
    }

    private final SuggestBox<String, SpanElement, SuggestOption<String>> suggestBox;

    // node.............................................................................................................

    @Override
    public Node node() {
        return this.element();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.element()
                .toString();
    }
}
