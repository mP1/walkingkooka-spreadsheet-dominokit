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

package walkingkooka.spreadsheet.dominokit.find;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.Component;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Collection;
import java.util.List;

final class SpreadsheetFindDialogComponentGridLayout extends SpreadsheetFindDialogComponentGridLayoutLike {

    static SpreadsheetFindDialogComponentGridLayout empty() {
        return new SpreadsheetFindDialogComponentGridLayout();
    }

    private SpreadsheetFindDialogComponentGridLayout() {
        this.left = Lists.array();
        this.content = Lists.array();
        this.footer = Lists.array();
    }

    SpreadsheetFindDialogComponentGridLayout setLeft(final Collection<Component<?>> children) {
        this.left.addAll(children);
        return this;
    }

    private final List<Component<?>> left;

    SpreadsheetFindDialogComponentGridLayout setContent(final Collection<Component<?>> children) {
        this.content.addAll(children);
        return this;
    }

    private final List<Component<?>> content;

    SpreadsheetFindDialogComponentGridLayout setFooter(final Collection<Component<?>> children) {
        this.footer.addAll(children);
        return this;
    }

    private final List<Component<?>> footer;
    
    // Component........................................................................................................

    @Override
    public HTMLDivElement element() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());

        printer.indent();
        {
            this.printTree0(
                    "Left",
                    this.left,
                    printer
            );

            this.printTree0(
                    "Content",
                    this.content,
                    printer
            );

            this.printTree0(
                    "Footer",
                    this.footer,
                    printer
            );
        }
        printer.outdent();
    }

    private void printTree0(final String label,
                            final Collection<Component<?>> children,
                            final IndentingPrinter printer) {
        printer.println(label);
        printer.indent();
        {
            for (final Component<?> component : children) {
                component.printTree(printer);
            }
        }
        printer.outdent();
    }
}
