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

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
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

    // label............................................................................................................

    @Override
    default SpreadsheetDataTableComponent<T> setLabel(String label) {
        throw new UnsupportedOperationException();
    }

    @Override
    default String label() {
        throw new UnsupportedOperationException();
    }

    // helperText.......................................................................................................

    @Override
    default SpreadsheetDataTableComponent<T> alwaysShowHelperText() {
        throw new UnsupportedOperationException();
    }

    @Override
    default Optional<String> helperText() {
        return Optional.empty();
    }

    @Override
    default SpreadsheetDataTableComponent<T> setHelperText(final Optional<String> text) {
        throw new UnsupportedOperationException();
    }

    // validate.........................................................................................................

    @Override
    default SpreadsheetDataTableComponent<T> validate() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> optional() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> required() {
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
    default SpreadsheetDataTableComponent<T> setErrors(final List<String> errors) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> setCssText(String css) {
        throw new UnsupportedOperationException();
    }

    // events...........................................................................................................

    @Override
    default SpreadsheetDataTableComponent<T> addChangeListener(final ChangeListener<Optional<List<T>>> listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> addFocusListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> addKeydownListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> addKeyupListener(final EventListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> hideMarginBottom() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> removeBorders() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetDataTableComponent<T> focus() {
        throw new UnsupportedOperationException();
    }

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
