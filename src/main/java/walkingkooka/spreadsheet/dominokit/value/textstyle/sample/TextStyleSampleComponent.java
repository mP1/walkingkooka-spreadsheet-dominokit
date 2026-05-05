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

package walkingkooka.spreadsheet.dominokit.value.textstyle.sample;

import elemental2.dom.HTMLDivElement;
import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormElementComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStyleDialogComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStyle;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.Component} that contains sample text and accepting {@link TextStyle},
 * which is probably copied from {@link TextStyleDialogComponent}.
 */
final public class TextStyleSampleComponent implements FormValueComponent<HTMLFieldSetElement, TextStyle, TextStyleSampleComponent>,
    HtmlComponentDelegator<HTMLFieldSetElement, TextStyleSampleComponent>,
    ValueWatcher<TextStyle> {

    public static TextStyleSampleComponent empty() {
        return new TextStyleSampleComponent();
    }

    private TextStyleSampleComponent() {
        super();
        this.target = HtmlElementComponent.div()
            .setText(
                "The quick brown fox jumps over the lazy dog"
            );

        this.formElementComponent = FormElementComponent.with(
            HtmlElementComponent.div()
                .setCssText("margin-top: 4px; margin-bottom: 4px; margin-left: auto; margin-right: auto; width: 100%;")
                .appendChild(this.target)
        );
    }

    // ValueWatcher.....................................................................................................

    @Override
    public void onValue(final Optional<TextStyle> value) {
        this.setValue(value);
    }

    // ValueComponent...................................................................................................

    @Override
    public Optional<TextStyle> value() {
        return this.textStyle;
    }

    @Override
    public TextStyleSampleComponent setValue(final Optional<TextStyle> value) {
        Objects.requireNonNull(value, "value");

        this.textStyle = value;

        this.target.setCssText(
            value.orElse(TextStyle.EMPTY)
                .text()
        );
        return this;
    }

    private Optional<TextStyle> textStyle = Optional.empty();

    private final DivComponent target;

    @Override
    public Runnable addValueWatcher(final ValueWatcher<TextStyle> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisabled() {
        return true;
    }

    @Override
    public TextStyleSampleComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyleSampleComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        return false;
    }

    @Override
    public TextStyleSampleComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> errors() {
        return Lists.empty();
    }

    @Override
    public TextStyleSampleComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextStyleSampleComponent setLabel(final String label) {
        this.formElementComponent.setLabel(label);
        return this;
    }

    @Override
    public String label() {
        return this.formElementComponent.getLabel();
    }

    @Override
    public Optional<String> helperText() {
        final String helperText = this.formElementComponent.getHelperText();

        return Optional.ofNullable(
            CharSequences.isNullOrEmpty(helperText) ?
                null :
                helperText
        );
    }

    @Override
    public TextStyleSampleComponent setHelperText(final Optional<String> text) {
        this.formElementComponent.setHelperText(
            text.orElse("")
        );
        return this;
    }

    @Override
    public TextStyleSampleComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public TextStyleSampleComponent setDisabled(final boolean disabled) {
        return this;
    }

    @Override
    public TextStyleSampleComponent hideMarginBottom() {
        return this;
    }

    @Override
    public TextStyleSampleComponent removeBorders() {
        return this;
    }

    @Override
    public TextStyleSampleComponent removePadding() {
        return this;
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            this.formElementComponent.printTree(printer);
        }
        printer.outdent();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLFieldSetElement, ?> htmlComponent() {
        return this.formElementComponent;
    }

    private final FormElementComponent<TextStyle, HTMLDivElement, ?> formElementComponent;

}
