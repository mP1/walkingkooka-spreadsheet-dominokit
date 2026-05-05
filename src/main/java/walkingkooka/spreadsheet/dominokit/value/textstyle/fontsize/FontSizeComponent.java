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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontsize;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponent;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilter;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.FontSize;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A component that supports displaying or editing as text or picking from a list of {@link FontSize}.
 */
public final
class FontSizeComponent implements SuggestBoxComponentDelegator<FontSize, FontSizeComponent>,
    TextStylePropertyComponent<HTMLFieldSetElement, FontSize, FontSizeComponent>,
    TreePrintable {

    /**
     * A {@link FontSizeComponent} which is initially empty, possible options to select must be added after
     * creation.
     */
    public static  FontSizeComponent empty(final FontSizeComponentContext context) {
        return new FontSizeComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private FontSizeComponent(final FontSizeComponentContext context) {
        this.suggestBox = SuggestBoxComponent.with(
            context,
            context::createMenuItem
        );
        this.context = context;
    }

    // TextStylePropertyComponent.......................................................................................

    @Override
    public boolean filterTest(final TextStylePropertyFilter filter) {
        Objects.requireNonNull(filter, "filter");

        return filter.testComponent(this);
    }

    @Override
    public Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
        return TEXT_STYLE_PROPERTY_FILTER_KINDS_FONT;
    }

    // FormValueComponent...............................................................................................

    @Override
    public FontSizeComponent focus() {
        this.suggestBox.focus();
        return this;
    }

    @Override
    public FontSizeComponent blur() {
        this.suggestBox.blur();
        return this;
    }

    @Override
    public boolean isEditing() {
        return this.suggestBox.isEditing();
    }

    // Value............................................................................................................

    @Override
    public FontSizeComponent setValue(final Optional<FontSize> fontSize) {
        Objects.requireNonNull(fontSize, "fontSize");

        this.suggestBox.setValue(
            fontSize.flatMap(this.context::toValue)
        );
        return this;
    }

    private final FontSizeComponentContext context;

    @Override //
    public Optional<FontSize> value() {
        return this.suggestBox.value();
    }

    @Override
    public Runnable addValueWatcher(final ValueWatcher<FontSize> watcher) {
        return this.suggestBox.addValueWatcher(watcher);
    }

    // SuggestBoxComponentDelegator.....................................................................................

    @Override
    public SuggestBoxComponent<FontSize> suggestBoxComponent() {
        return this.suggestBox;
    }

    private final SuggestBoxComponent<FontSize> suggestBox;

    // HasName..........................................................................................................

    @Override
    public TextStylePropertyName<FontSize> name() {
        return TextStylePropertyName.FONT_SIZE;
    }

    // HasFocusBlurEventListener........................................................................................

    @Override
    public FontSizeComponent addBlurListener(final EventListener listener) {
        this.suggestBox.addBlurListener(listener);
        return this;
    }

    @Override
    public FontSizeComponent addFocusListener(final EventListener listener) {
        this.suggestBox.addFocusListener(listener);
        return this;
    }

    @Override
    public FontSizeComponent addFocusInListener(final EventListener listener) {
        this.suggestBox.addFocusInListener(listener);
        return this;
    }

    @Override
    public FontSizeComponent addFocusOutListener(final EventListener listener) {
        this.suggestBox.addFocusOutListener(listener);
        return this;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.suggestBox.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.suggestBox.printTree(printer);
        }
        printer.outdent();
    }
}