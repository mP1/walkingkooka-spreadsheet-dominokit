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

package walkingkooka.spreadsheet.dominokit.ui.textnode;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Optional;

/**
 * Defines the public interface for a {@link SpreadsheetTextNodeComponent}.
 */
public interface SpreadsheetTextNodeComponentLike extends ValueComponent<HTMLDivElement, TextNode, SpreadsheetTextNodeComponent>,
        TreePrintable {

    // id...............................................................................................................
    @Override
    default SpreadsheetTextNodeComponent setId(final String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    default String id() {
        return null;
    }

    // label............................................................................................................

    @Override
    default SpreadsheetTextNodeComponent setLabel(String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    default String label() {
        return "";
    }

    // disabled.........................................................................................................

    @Override
    default boolean isDisabled() {
        return false;
    }

    @Override
    default SpreadsheetTextNodeComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    // helperText.......................................................................................................

    @Override
    default SpreadsheetTextNodeComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    default Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    default SpreadsheetTextNodeComponent setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    // validation.......................................................................................................

    @Override
    default SpreadsheetTextNodeComponent validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    default boolean isRequired() {
        throw new UnsupportedOperationException();
    }

    @Override
    default List<String> errors() {
        return Lists.empty();
    }

    @Override
    default SpreadsheetTextNodeComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent setCssText(String css) {
        throw new UnsupportedOperationException();
    }

    // events...........................................................................................................

    @Override
    default SpreadsheetTextNodeComponent addChangeListener(final ChangeListener<Optional<TextNode>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetTextNodeComponent focus() {
        throw new UnsupportedOperationException();
    }

    // TreePrintable....................................................................................................

    @Override
    default void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        final String text = this.value()
                .map(TextNode::text)
                .orElse("");
        if (false == text.isEmpty()) {
            printer.indent();
            {
                printer.println(text);
            }
            printer.outdent();
        }
    }
}
