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

package walkingkooka.spreadsheet.dominokit.ui.datatable;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * A {@link ValueComponent} wrapper around a {@link DataTable}.
 */
public interface SpreadsheetDataTableComponentLike<T> extends ValueComponent<HTMLDivElement, List<T>, SpreadsheetDataTableComponentLike<T>>,
        TreePrintable {

    // TreePrintable....................................................................................................

    default void printTreeTable(final List<ColumnConfig<T>> columnConfigs,
                                final BiFunction<Integer, T, HtmlElementComponent<?, ?>> cellRenderer,
                                final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            // print columns
            printer.println("COLUMN(S)");
            printer.indent();
            {
                for (final ColumnConfig<T> columnConfig : columnConfigs) {
                    printer.println(
                            columnConfig.getTitle()
                    );
                }
            }
            printer.outdent();

            final Optional<List<T>> maybeValues = this.value();
            if (maybeValues.isPresent()) {
                printer.println("ROW(S)");
                printer.indent();
                {
                    int row = 0;
                    final int columnCount = columnConfigs.size();

                    for (final T value : maybeValues.get()) {
                        printer.println("ROW " + row);
                        printer.indent();
                        {
                            for (int column = 0; column < columnCount; column++) {
                                // eventually all HtmlElementComponents will implement printTree
                                TreePrintable.printTreeOrToString(
                                        cellRenderer.apply(
                                                column,
                                                value
                                        ),
                                        printer
                                );
                                printer.lineStart();
                            }
                        }
                        printer.outdent();
                        row++;
                    }
                }
                printer.outdent();
            }

            printer.println();
        }
        printer.outdent();
    }
}
