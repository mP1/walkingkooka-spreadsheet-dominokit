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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLDivElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.dom.DivComponent;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.text.TextStyle;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.Component} that contains sample text and accepting {@link TextStyle},
 * which is probably copied from {@link TextStyleDialogComponent}.
 */
final public class TextStyleSampleComponent implements ValueComponent<HTMLDivElement, TextStyle, TextStyleSampleComponent>,
    HtmlComponentDelegator<HTMLDivElement, TextStyleSampleComponent>,
    ValueWatcher<TextStyle>{

    public static TextStyleSampleComponent empty() {
        return new TextStyleSampleComponent();
    }

    private TextStyleSampleComponent() {
        super();
        this.target = HtmlElementComponent.div()
            .setText(
                "The quick brown fox jumps over the lazy dog"
            );

        this.root = HtmlElementComponent.div()
            .setCssText("margin-top: 4px; margin-bottom: 4px; margin-left: auto; margin-right: auto; width: 100%;")
            .appendChild(this.target);
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

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            this.target.printTree(printer);
        }
        printer.outdent();
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.root;
    }

    private final DivComponent root;

}
