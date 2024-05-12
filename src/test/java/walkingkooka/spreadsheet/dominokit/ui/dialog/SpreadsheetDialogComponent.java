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

package walkingkooka.spreadsheet.dominokit.ui.dialog;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;

public final class SpreadsheetDialogComponent implements SpreadsheetDialogComponentLike {

    /**
     * Factory that creates a new empty {@link SpreadsheetDialogComponent}.
     */
    public static SpreadsheetDialogComponent with(final String id,
                                                  final String title,
                                                  final boolean includeClose,
                                                  final HistoryTokenContext context) {
        Objects.requireNonNull(id, "id");
        CharSequences.failIfNullOrEmpty(title, "title");
        Objects.requireNonNull(context, "context");

        return new SpreadsheetDialogComponent(
                id,
                title,
                includeClose,
                context
        );
    }

    private SpreadsheetDialogComponent(final String id,
                                       final String title,
                                       final boolean includeClose,
                                       final HistoryTokenContext context) {
        this.id = id;
        this.title = title;
        this.includeClose = includeClose;
    }

    // main/SpreadsheetDialogComponent public members...................................................................

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void open() {
        this.open = true;
    }

    @Override
    public void close() {
        this.open = false;
    }

    private boolean open;

    // title............................................................................................................

    @Override
    public void setTitle(final String title) {
        this.title = title;
    }

    @Override
    public String title() {
        return this.title;
    }

    private String title;

    // id...............................................................................................................

    private String id;

    @Override
    public String id() {
        return this.id;
    }

    // id...............................................................................................................

    private boolean includeClose;

    @Override
    public SpreadsheetDialogComponent appendChild(final IsElement<?> child) {
        Objects.requireNonNull(child, "child");
        this.children.add(child);
        return this;
    }

    private List<IsElement<?>> children = Lists.array();

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        throw new UnsupportedOperationException();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(this.title);
            printer.println("id=" + id + " includeClose=" + includeClose);

            printer.indent();
            {
                for (final IsElement<?> child : this.children) {
                    TreePrintable.printTreeOrToString(
                            child,
                            printer
                    );
                    printer.lineStart();
                }
            }
            printer.outdent();
        }
        printer.outdent();
    }
}
