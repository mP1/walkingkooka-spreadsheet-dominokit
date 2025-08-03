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
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ValidatorHelper;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A mock of main/SpreadsheetTextBox with the same public interface and a helpful {@link TreePrintable}. This will be useful for unit tests to verify the rough apperance of a component that includes
 * {@link SpreadsheetTextBox}.
 */
public final class SpreadsheetTextBox implements FormValueComponent<HTMLFieldSetElement, String, SpreadsheetTextBox>,
    SpreadsheetTextBoxTreePrintable<SpreadsheetTextBox, String>,
    TestHtmlElementComponent<HTMLFieldSetElement, SpreadsheetTextBox>,
    ValidatorHelper {

    public static SpreadsheetTextBox empty() {
        return new SpreadsheetTextBox();
    }

    @Override
    public SpreadsheetTextBox setId(final String id) {
        CharSequences.failIfNullOrEmpty(id, "id");

        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public SpreadsheetTextBox setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
    public SpreadsheetTextBox setValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");
        this.value = EMPTY_STRING.equals(value) ?
            Optional.empty() :
            value;

        return validate();
    }

    private final static Optional<String> EMPTY_STRING = Optional.of("");

    @Override
    public Optional<String> value() {
        return this.value;
    }

    private Optional<String> value = Optional.empty();

    @Override
    public SpreadsheetTextBox focus() {
        return this;
    }

    @Override
    public SpreadsheetTextBox optional() {
        this.required = false;
        return this;
    }

    @Override
    public SpreadsheetTextBox required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

    public SpreadsheetTextBox setValidator(final Validator<Optional<String>> validator) {
        this.validator = validator;
        return this;
    }

    private Validator<Optional<String>> validator;

    @Override
    public SpreadsheetTextBox validate() {
        this.setErrors(
            this.validateAndGetErrors(
                value,
                Optional.ofNullable(this.validator)
            )
        );
        return this;
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public SpreadsheetTextBox setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");
        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors = Lists.empty();

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public SpreadsheetTextBox setDisabled(final boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    private boolean disabled;

    @Override
    public SpreadsheetTextBox addChangeListener(final ChangeListener<Optional<String>> listener) {
        return this;
    }

    @Override
    public SpreadsheetTextBox addClickListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetTextBox addContextMenuListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetTextBox addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetTextBox addKeydownListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetTextBox addKeyupListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetTextBox alwaysShowHelperText() {
        return this;
    }

    @Override
    public SpreadsheetTextBox setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");
        this.helperText = text;
        return this;
    }

    @Override public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
    public SpreadsheetTextBox hideMarginBottom() {
        return this;
    }

    @Override
    public SpreadsheetTextBox removeBorders() {
        return this;
    }

    public SpreadsheetTextBox autocompleteOff() {
        return this;
    }

    public SpreadsheetTextBox clearIcon() {
        return this;
    }

    public SpreadsheetTextBox disableSpellcheck() {
        return this;
    }

    public SpreadsheetTextBox enterFiresValueChange() {
        return this;
    }

    public SpreadsheetTextBox magnifyingGlassIcon() {
        return this;
    }

    // FIXES
    //
    // java.lang.NoSuchMethodError: walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextBox.setCssText(Ljava/lang/String;)Lwalkingkooka/spreadsheet/dominokit/ui/textbox/SpreadsheetTextBox;
    @Override
    public SpreadsheetTextBox setCssText(final String css) {
        Objects.requireNonNull(css, "css");
        return this;
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    // SpreadsheetTextBoxTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
