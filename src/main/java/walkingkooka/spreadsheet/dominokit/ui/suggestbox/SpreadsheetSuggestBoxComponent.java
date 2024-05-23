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
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.dominokit.domino.ui.utils.ElementsFactory.elements;

/**
 * A text box component that includes support for finding a label.
 */
public final class SpreadsheetSuggestBoxComponent implements SpreadsheetSuggestBoxComponentLike {

    public static SpreadsheetSuggestBoxComponent with(final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore) {
        Objects.requireNonNull(suggestionsStore, "suggestionsStore");

        return new SpreadsheetSuggestBoxComponent(suggestionsStore);
    }

    private SpreadsheetSuggestBoxComponent(final SuggestionsStore<String, SpanElement, SuggestOption<String>> suggestionsStore) {
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
    public SpreadsheetSuggestBoxComponent setId(final String id) {
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
    public SpreadsheetSuggestBoxComponent setLabel(final String label) {
        this.suggestBox.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.suggestBox.getLabel();
    }

    // helperText.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent alwaysShowHelperText() {
        final DominoElement<Element> element = elements.elementOf(
                this.suggestBox.element()
                        .firstElementChild
        );
        element.setHeight(HELPER_TEXT_HEIGHT);
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent setHelperText(final Optional<String> text) {
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

    public SpreadsheetSuggestBoxComponent setStringValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");

        this.suggestBox.withValue(
                value.orElse(""),
                true // silent
        );
        return this;
    }

    public String stringValue() {
        return this.suggestBox.getStringValue();
    }

    // Value............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent setValue(final Optional<SpreadsheetLabelName> label) {
        Objects.requireNonNull(label, "label");

        this.suggestBox.setValue(
                label.map(SpreadsheetLabelName::text)
                        .orElse("")
        );
        return this;
    }

    @Override //
    public Optional<SpreadsheetLabelName> value() {
        return tryParse(
                this.suggestBox.getStringValue()
        );
    }

    private Optional<SpreadsheetLabelName> tryParse(final String value) {
        SpreadsheetLabelName label;

        try {
            label = SpreadsheetSelection.labelName(value);
        } catch (final Exception ignore) {
            label = null;
        }

        return Optional.ofNullable(label);
    }

    // isDisabled.......................................................................................................

    @Override
    public boolean isDisabled() {
        return this.suggestBox.isDisabled();
    }

    @Override
    public SpreadsheetSuggestBoxComponent setDisabled(final boolean disabled) {
        this.suggestBox.setDisabled(disabled);
        return this;
    }

    // validation.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent optional() {
        this.required = false;
        return this.setValidator(
                SpreadsheetValidators.optional(
                        SpreadsheetValidators.tryCatch(SpreadsheetSelection::labelName)
                )
        );
    }

    @Override
    public SpreadsheetSuggestBoxComponent required() {
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
    public SpreadsheetSuggestBoxComponent validate() {
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
    public SpreadsheetSuggestBoxComponent setErrors(final List<String> errors) {
        this.suggestBox.invalidate(
                Lists.immutable(errors)
        );
        return this;
    }

    // events...........................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent addChangeListener(final ChangeListener<Optional<SpreadsheetLabelName>> listener) {
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
    public SpreadsheetSuggestBoxComponent addFocusListener(final EventListener listener) {
        this.suggestBox.addEventListener(
                EventType.focus,
                listener
        );
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent addKeydownListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addEventListener(
                EventType.keydown,
                listener
        );
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent addKeyupListener(final EventListener listener) {
        Objects.requireNonNull(listener, "listener");

        this.suggestBox.addEventListener(
                EventType.keyup,
                listener
        );
        return this;
    }

    // focus............................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent focus() {
        this.suggestBox.focus();
        return this;
    }

    // styling..........................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent hideMarginBottom() {
        this.suggestBox.setMarginBottom("");
        return this;
    }

    @Override
    public SpreadsheetSuggestBoxComponent removeBorders() {
        this.suggestBox.getInputElement()
                .parent()
                .parent()
                .setBorder("0")
                .setCssProperty("border-radius", 0);
        return this;
    }

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetSuggestBoxComponent setCssText(final String css) {
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
