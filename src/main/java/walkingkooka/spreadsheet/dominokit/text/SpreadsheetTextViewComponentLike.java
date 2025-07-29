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
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Optional;

/**
 * A barebones component that may be used to display text. Many methods such as support for listeners etc all throw {@link UnsupportedOperationException}.
 */
abstract class SpreadsheetTextViewComponentLike implements FormValueComponent<HTMLDivElement, String, SpreadsheetTextViewComponent> {

    SpreadsheetTextViewComponentLike() {
        super();
    }

    @Override
    public final SpreadsheetTextViewComponent setLabel(final String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final String label() {
        return "";
    }

    @Override
    public final Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    public final SpreadsheetTextViewComponent validate() {
        return (SpreadsheetTextViewComponent) this;
    }

    @Override
    public final SpreadsheetTextViewComponent optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent required() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isRequired() {
        return false;
    }

    @Override
    public final boolean isDisabled() {
        return false;
    }

    @Override
    public final SpreadsheetTextViewComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent addChangeListener(final ChangeListener<Optional<String>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent addClickListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final SpreadsheetTextViewComponent hideMarginBottom() {
        return (SpreadsheetTextViewComponent) this;
    }

    @Override
    public final SpreadsheetTextViewComponent removeBorders() {
        return (SpreadsheetTextViewComponent) this;
    }

    @Override
    public final SpreadsheetTextViewComponent focus() {
        // ignored
        return (SpreadsheetTextViewComponent) this;
    }

    @Override
    public final List<String> errors() {
        return Lists.empty();
    }

    @Override
    public final SpreadsheetTextViewComponent setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEditing() {
        return false;
    }

    @Override
    public final void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(
                CharSequences.quoteAndEscape(
                    this.value()
                        .orElse("")
                )
            );
        }
        printer.outdent();
    }
}
