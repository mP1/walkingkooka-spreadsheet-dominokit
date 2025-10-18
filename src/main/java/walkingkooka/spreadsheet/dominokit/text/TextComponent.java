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
import elemental2.dom.HTMLDivElement;
import elemental2.dom.Node;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link FormValueComponent} that holds text within a text node. This will need to be special cased by SpreadsheetDataTable.
 */
public final class TextComponent implements FormValueComponent<HTMLDivElement, String, TextComponent> {

    public static TextComponent with(final Optional<String> value) {
        return new TextComponent(value);
    }

    private TextComponent(final Optional<String> value) {
        this.setValue(value);
    }

    @Override
    public TextComponent setId(final String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String id() {
        return null;
    }

    @Override
    public TextComponent setLabel(String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String label() {
        return "";
    }

    @Override
    public boolean isDisabled() {
        return false;
    }

    @Override
    public TextComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public TextComponent setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<String> value() {
        return this.value;
    }

    private Optional<String> value;

    @Override
    public TextComponent setValue(final Optional<String> value) {
        Objects.requireNonNull(value, "value");
        this.value = value;
        return this;
    }

    @Override
    public TextComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRequired() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> errors() {
        return Lists.empty();
    }

    @Override
    public TextComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent setCssText(String css) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent setCssProperty(final String name,
                                        final String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent removeCssProperty(final String name) {
        throw new UnsupportedOperationException();
    }

    // events...........................................................................................................

    @Override
    public TextComponent addBlurListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent addChangeListener(final ChangeListener<Optional<String>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent addClickListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent addContextMenuListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent addKeyDownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent addKeyUpListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent removePadding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TextComponent focus() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    // width............................................................................................................

    @Override
    public int width() {
        throw new UnsupportedOperationException();
    }

    // height...........................................................................................................

    @Override
    public int height() {
        throw new UnsupportedOperationException();
    }

    // IsElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        throw new UnsupportedOperationException();
    }

    // node.............................................................................................................

    @Override
    public Node node() {
        // would be better to do create the TextNode in setValue but that would cause failures in test/TextBoxComponent.setValue
        if (null == this.node) {
            this.node = Doms.textNode(
                this.value.orElse("")
            );
        }
        return node;
    }

    private Node node;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(
                CharSequences.quoteAndEscape(
                    this.value.orElse("")
                )
            );
        }
        printer.outdent();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return CharSequences.quoteAndEscape(
            this.value.orElse("")
        ).toString();
    }
}
