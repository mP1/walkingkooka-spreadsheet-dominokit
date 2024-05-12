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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBoxTreePrintable;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A mock of main/SpreadsheetIntegerBox with the same public interface and a helpful {@link TreePrintable}. This will be useful for unit tests to verify the rough apperance of a component that includes
 * {@link SpreadsheetIntegerBox}.
 */
public final class SpreadsheetIntegerBox implements ValueComponent<HTMLFieldSetElement, Integer, SpreadsheetIntegerBox>,
        SpreadsheetTextBoxTreePrintable<SpreadsheetIntegerBox, Integer> {

    public static SpreadsheetIntegerBox empty() {
        return new SpreadsheetIntegerBox();
    }

    @Override
    public SpreadsheetIntegerBox setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public SpreadsheetIntegerBox setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
    public SpreadsheetIntegerBox setValue(final Optional<Integer> value) {
        Objects.requireNonNull(value, "value");
        this.value = value;
        return this;
    }

    @Override
    public Optional<Integer> value() {
        return this.value;
    }

    private Optional<Integer> value = Optional.empty();

    @Override
    public SpreadsheetIntegerBox focus() {
        return this;
    }

    @Override
    public SpreadsheetIntegerBox optional() {
        this.required = false;
        return this;
    }

    @Override
    public SpreadsheetIntegerBox required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

    @Override
    public SpreadsheetIntegerBox validate() {
        return this;
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public SpreadsheetIntegerBox setErrors(final List<String> errors) {
        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors = Lists.array();

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public SpreadsheetIntegerBox setDisabled(final boolean disabled) {
        this.disabled = true;
        return this;
    }

    private boolean disabled;

    @Override
    public SpreadsheetIntegerBox addChangeListener(final ChangeListener<Optional<Integer>> listener) {
        return this;
    }

    @Override
    public SpreadsheetIntegerBox addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetIntegerBox addKeydownListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetIntegerBox addKeyupListener(final EventListener listener) {
        return this;
    }

    @Override
    public SpreadsheetIntegerBox alwaysShowHelperText() {
        return this;
    }

    @Override
    public SpreadsheetIntegerBox setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");
        this.helperText = text;
        return this;
    }

    public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
    public SpreadsheetIntegerBox hideMarginBottom() {
        return this;
    }

    @Override
    public SpreadsheetIntegerBox removeBorders() {
        return this;
    }

    public SpreadsheetIntegerBox clearIcon() {
        return this;
    }

    public SpreadsheetIntegerBox disableSpellcheck() {
        return this;
    }

    public SpreadsheetIntegerBox enterFiresValueChange() {
        return this;
    }

    public SpreadsheetIntegerBox setValidator(final Validator<String> validator) {
        return this;
    }

    public SpreadsheetIntegerBox max(final int value) {
        this.max = value;
        return this;
    }

    private int max;

    public SpreadsheetIntegerBox min(final int value) {
        this.min = value;
        return this;
    }

    private int min;

    @Override
    public HTMLFieldSetElement element() {
        throw new UnsupportedOperationException();
    }

    // SpreadsheetTextBoxTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        // NOP
    }
}
