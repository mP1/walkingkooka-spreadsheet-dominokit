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
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTreePrintable;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A mock of main/IntegerBoxComponent with the same public interface and a helpful {@link TreePrintable}. This will be useful for unit tests to verify the rough apperance of a component that includes
 * {@link IntegerBoxComponent}.
 */
public final class IntegerBoxComponent implements FormValueComponent<HTMLFieldSetElement, Integer, IntegerBoxComponent>,
    FormValueComponentTreePrintable<HTMLFieldSetElement, IntegerBoxComponent, Integer>,
    TestHtmlElementComponent<HTMLFieldSetElement, IntegerBoxComponent>,
    ValidatorHelper {

    public static IntegerBoxComponent empty() {
        return new IntegerBoxComponent();
    }

    @Override
    public IntegerBoxComponent setId(final String id) {
        this.id = id;
        return this;
    }

    @Override
    public String id() {
        return this.id;
    }

    private String id;

    @Override
    public IntegerBoxComponent setLabel(final String label) {
        this.label = label;
        return this;
    }

    @Override
    public String label() {
        return this.label;
    }

    private String label;

    @Override
    public IntegerBoxComponent setValue(final Optional<Integer> value) {
        Objects.requireNonNull(value, "value");
        this.value = value;

        return validate();
    }

    @Override
    public Optional<Integer> value() {
        return this.value;
    }

    private Optional<Integer> value = Optional.empty();

    @Override
    public IntegerBoxComponent focus() {
        return this;
    }

    @Override
    public IntegerBoxComponent optional() {
        this.required = false;
        return this;
    }

    @Override
    public IntegerBoxComponent required() {
        this.required = true;
        return this;
    }

    @Override
    public boolean isRequired() {
        return this.required;
    }

    private boolean required;

    @Override
    public IntegerBoxComponent validate() {
        this.setErrors(
            this.validateAndGetErrors(
                this.value,
                this.validator
            )
        );
        return this;
    }

    @Override
    public List<String> errors() {
        return this.errors;
    }

    @Override
    public IntegerBoxComponent setErrors(final List<String> errors) {
        Objects.requireNonNull(errors, "errors");
        this.errors = Lists.immutable(errors);
        return this;
    }

    private List<String> errors = Lists.array();

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public IntegerBoxComponent setDisabled(final boolean disabled) {
        this.disabled = true;
        return this;
    }

    private boolean disabled;

    @Override
    public IntegerBoxComponent addChangeListener(final ChangeListener<Optional<Integer>> listener) {
        return this;
    }

    @Override
    public IntegerBoxComponent addClickListener(final EventListener listener) {
        return this;
    }

    @Override
    public IntegerBoxComponent addContextMenuListener(final EventListener listener) {
        return this;
    }

    @Override
    public IntegerBoxComponent addFocusListener(final EventListener listener) {
        return this;
    }

    @Override
    public IntegerBoxComponent addKeyDownListener(final EventListener listener) {
        return this;
    }

    @Override
    public IntegerBoxComponent addKeyUpListener(final EventListener listener) {
        return this;
    }

    @Override
    public IntegerBoxComponent alwaysShowHelperText() {
        return this;
    }

    @Override
    public IntegerBoxComponent setHelperText(final Optional<String> text) {
        Objects.requireNonNull(text, "text");
        this.helperText = text;
        return this;
    }

    @Override public Optional<String> helperText() {
        return this.helperText;
    }

    private Optional<String> helperText = Optional.empty();

    @Override
    public IntegerBoxComponent hideMarginBottom() {
        return this;
    }

    @Override
    public IntegerBoxComponent removeBorders() {
        return this;
    }

    public IntegerBoxComponent clearIcon() {
        return this;
    }

    public IntegerBoxComponent disableSpellcheck() {
        return this;
    }

    public IntegerBoxComponent enterFiresValueChange() {
        return this;
    }

    public IntegerBoxComponent setValidator(final Optional<Validator<Optional<Integer>>> validator) {
        Objects.requireNonNull(validator, "validator");

        this.validator = validator;
        return this;
    }

    private Optional<Validator<Optional<Integer>>> validator = Optional.empty();

    public IntegerBoxComponent max(final int value) {
        this.max = value;
        return this;
    }

    private Integer max;

    public IntegerBoxComponent min(final int value) {
        this.min = value;
        return this;
    }

    private Integer min;

    public IntegerBoxComponent step(final int step) {
        this.step = step;
        return this;
    }

    private Integer step;

    public IntegerBoxComponent pattern(final String pattern) {
        Objects.requireNonNull(pattern, "pattern");
        this.pattern = pattern;
        return this;
    }

    private String pattern;

    // FormValueComponentTreePrintable..................................................................................

    @Override
    public void treePrintAlternateValues(final IndentingPrinter printer) {
        this.printLabelAndValue(
            "min",
            this.min,
            printer
        );
        this.printLabelAndValue(
            "max",
            this.max,
            printer
        );
        this.printLabelAndValue(
            "step",
            this.step,
            printer
        );
        this.printLabelAndValue(
            "pattern",
            this.pattern,
            printer
        );

        printer.lineStart();
    }

    private void printLabelAndValue(final String label,
                                    final Object value,
                                    final IndentingPrinter printer) {
        if (null != value) {
            printer.print(" " + label + ": " + CharSequences.quoteIfChars(value));
        }
    }
}
