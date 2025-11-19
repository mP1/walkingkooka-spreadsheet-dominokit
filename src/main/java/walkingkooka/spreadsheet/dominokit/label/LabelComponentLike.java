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

package walkingkooka.spreadsheet.dominokit.label;

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;

abstract class LabelComponentLike implements ValueComponent<HTMLElement, String, LabelComponent> {

    LabelComponentLike() {
        super();
    }

    @Override
    public boolean isEditing() {
        return false;
    }
    
    @Override
    public final Runnable addValueWatcher(final ValueWatcher<String> watcher) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final boolean isDisabled() {
        return true;
    }

    @Override
    public LabelComponent setDisabled(final boolean disabled) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LabelComponent hideMarginBottom() {
        return (LabelComponent) this;
    }

    @Override
    public LabelComponent removeBorders() {
        return (LabelComponent) this;
    }

    @Override
    public final LabelComponent removePadding() {
        return (LabelComponent) this;
    }

    @Override
    public final LabelComponent focus() {
        return (LabelComponent) this;
    }

    @Override
    public final LabelComponent blur() {
        return (LabelComponent) this;
    }

    // TreePrintable....................................................................................................

    @Override
    public final void printTree(IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        final String value = this.value()
            .orElse("");
        if(false == value.isEmpty()) {
            printer.indent();
            {
                printer.println(
                    CharSequences.quoteAndEscape(value)
                );
            }
            printer.outdent();
        }
    }
}
