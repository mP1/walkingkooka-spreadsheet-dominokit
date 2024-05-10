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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A mock of main/SpreadsheetTextBox with the same public interface and a helpful {@link TreePrintable}. This will be useful for unit tests to verify the rough apperance of a component that includes
 * {@link SpreadsheetTextBox}.
 */
public final class SpreadsheetTextBox implements ValueComponent<HTMLFieldSetElement, String, SpreadsheetTextBox>,
        TreePrintable {

    public static SpreadsheetTextBox empty() {
        return new SpreadsheetTextBox();
    }

    @Override
    public SpreadsheetTextBox setId(final String id) {
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
        this.value = value;
        return this;
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

    @Override
    public SpreadsheetTextBox validate() {
        return this;
    }

    @Override
    public boolean isDisabled() {
        return this.disabled;
    }

    @Override
    public SpreadsheetTextBox setDisabled(final boolean disabled) {
        this.disabled = true;
        return this;
    }

    private boolean disabled;

    @Override
    public SpreadsheetTextBox addChangeListener(final ChangeListener<Optional<String>> listener) {
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

    public Optional<String> helperText() {
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

    @Override
    public Optional<String> value() {
        return Optional.empty();
    }

    @Override
    public HTMLFieldSetElement element() {
        throw new UnsupportedOperationException();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            final List<String> components = Lists.array();

            final String label = this.label;
            if (null != label) {
                components.add(label + ":");
            }

            final Optional<String> value = this.value;
            if (value.isPresent()) {
                components.add("[" + value.get() + "]");
            }

            final String id = this.id;
            if (null != id) {
                components.add("id=" + id);
            }

            final Optional<String> helperText = this.helperText;
            if (helperText.isPresent()) {
                components.add("helperText=" + CharSequences.quoteAndEscape(helperText.get()));
            }

            if (this.disabled) {
                components.add("DISABLED");
            }

            if (this.required) {
                components.add("REQUIRED");
            }

            printer.println(
                    components.stream()
                            .collect(Collectors.joining(" "))
            );
        }
        printer.outdent();
    }
}
