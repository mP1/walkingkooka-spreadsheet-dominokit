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

package walkingkooka.spreadsheet.dominokit.datatable;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin;
import org.dominokit.domino.ui.datatable.plugins.summary.EmptyStatePlugin;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.value.ValueComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link ValueComponent} wrapper around a {@link DataTable}.
 */
public interface SpreadsheetDataTableComponentLike<T> extends ValueComponent<HTMLDivElement, List<T>, SpreadsheetDataTableComponent<T>>,
        ComponentWithChildren<SpreadsheetDataTableComponent<T>, HTMLDivElement>,
        TreePrintable {

    SpreadsheetDataTableComponent<T> setCssText(final String css);

    // label............................................................................................................

    @Override
    default SpreadsheetDataTableComponent<T> setLabel(final String label) {
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

    // prev/next links..................................................................................................

    SpreadsheetDataTableComponent<T> previousNextLinks(final String idPrefix,
                                                       final HistoryTokenContext context);

    default HistoryTokenAnchorComponent previous(final String idPrefix,
                                                 final HistoryTokenContext context) {
        return context.historyToken()
                .link(idPrefix + "previous")
                .setTextContent("previous")
                .setIconBefore(
                        Optional.of(
                                SpreadsheetIcons.tablePrevious()
                        )
                );
    }

    default HistoryTokenAnchorComponent next(final String idPrefix,
                                             final HistoryTokenContext context) {
        return context.historyToken()
                .link(idPrefix + "next")
                .setTextContent("next")
                .setIconAfter(
                        Optional.of(
                                SpreadsheetIcons.tableNext()
                        )
                );
    }

    SpreadsheetDataTableComponent setPrevious(final Optional<HistoryToken> historyToken);

    SpreadsheetDataTableComponent setNext(final Optional<HistoryToken> historyToken);

    // header...........................................................................................................

    SpreadsheetDataTableComponent<T> hideHeaders();

    // plugins..........................................................................................................

    /**
     * Registers the {@link org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin}
     */
    SpreadsheetDataTableComponent<T> bodyScrollPlugin();

    /**
     * Prepares text for the {@link BodyScrollPlugin} which will be printed by {@link #printTreeTable(List, boolean, SpreadsheetDataTableComponentCellRenderer, List, IndentingPrinter)}.
     */
    static String bodyScrollPluginText() {
        return "BodyScrollPlugin";
    }

    /**
     * Registers a plugin which will display an ICON and the given TITLE when the table is empty.
     */
    SpreadsheetDataTableComponent<T> emptyStatePlugin(final Icon<?> icon,
                                                      final String title);

    /**
     * Prepares text for the {@link EmptyStatePlugin} which will be printed by {@link #printTreeTable(List, boolean, SpreadsheetDataTableComponentCellRenderer, List, IndentingPrinter)}.
     */
    static String emptyStatePluginText(final Icon<?> icon,
                                       final String title) {
        Objects.requireNonNull(icon, "icon");
        CharSequences.failIfNullOrEmpty(title, "title");

        return "EmptyStatePlugin (" + icon.getName() + ") " + CharSequences.quoteAndEscape(title);
    }

    // CanBeEmpty.......................................................................................................

    /**
     * A table is never empty, best to show columns or any empty state plugin etc even when there are no rows.
     */
    @Override
    default boolean isEmpty() {
        return false == this.value().isPresent();
    }

    // TreePrintable....................................................................................................

    default void printTreeTable(final List<ColumnConfig<T>> columnConfigs,
                                final boolean headersHidden,
                                final SpreadsheetDataTableComponentCellRenderer<T> cellRenderer,
                                final List<String> plugins,
                                final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println(
                    "id=" +
                            CharSequences.subSequence(
                                    this.id(),
                                    0,
                                    -1
                            ) +
                            SpreadsheetElementIds.TABLE
            );

            // print columns
            if (false == headersHidden) {
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
            }

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
                                        cellRenderer.render(
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

            {
                final List<IsElement<?>> children = this.children();
                if (children.size() > 0) {
                    printer.println("CHILDREN");
                    printer.indent();
                    {
                        for (final IsElement<?> child : children) {
                            TreePrintable.printTreeOrToString(
                                    child,
                                    printer
                            );
                        }
                    }
                    printer.outdent();
                }
            }

            {
                if (plugins.size() > 0) {
                    printer.println("PLUGINS");
                    printer.indent();
                    {
                        for (final String plugin : plugins) {
                            printer.println(plugin);
                        }
                    }
                    printer.outdent();
                }
            }
        }
        printer.outdent();
    }
}
