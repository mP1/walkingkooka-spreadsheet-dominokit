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

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin;
import org.dominokit.domino.ui.datatable.plugins.summary.EmptyStatePlugin;
import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.spreadsheet.dominokit.ComponentWithChildren;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponent;
import walkingkooka.spreadsheet.dominokit.value.TableComponent;
import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * A {@link FormValueComponent} wrapper around a {@link DataTable}.
 */
public interface SpreadsheetDataTableComponentLike<T> extends TableComponent<HTMLDivElement, List<T>, SpreadsheetDataTableComponent<T>>,
    ComponentWithChildren<SpreadsheetDataTableComponent<T>, HTMLDivElement>,
    TreePrintable {

    @Override
    default SpreadsheetDataTableComponent<T> focus() {
        return (SpreadsheetDataTableComponent<T>)this;
    }

    // prev/next links..................................................................................................

    SpreadsheetDataTableComponent<T> previousNextLinks(final String idPrefix);

    default HistoryTokenAnchorComponent previous(final String idPrefix) {
        return HistoryTokenAnchorComponent.empty()
            .setId(idPrefix + "previous" + SpreadsheetElementIds.LINK)
            .setTextContent("previous")
            .setIconBefore(
                Optional.of(
                    SpreadsheetIcons.tablePrevious()
                )
            );
    }

    default HistoryTokenAnchorComponent next(final String idPrefix) {
        return HistoryTokenAnchorComponent.empty()
            .setId(idPrefix + "next" + SpreadsheetElementIds.LINK)
            .setTextContent("next")
            .setIconAfter(
                Optional.of(
                    SpreadsheetIcons.tableNext()
                )
            );
    }

    SpreadsheetDataTableComponent<T> setPrevious(final Optional<HistoryToken> historyToken);

    SpreadsheetDataTableComponent<T> setNext(final Optional<HistoryToken> historyToken);

    /**
     * Updates the previous and next links using the history token to fetch the offset and count.
     * Note the next link will include the last row of the current view, and the previous link will include the first row of the current view.
     */
    default SpreadsheetDataTableComponent<T> refreshPreviousNextLinks(final HistoryToken historyToken,
                                                                      final int defaultCount) {
        final int offset = historyToken.offset()
            .orElse(0);
        final int count = historyToken.count()
            .orElse(defaultCount);

        final boolean previousDisabled = 0 == offset;
        final boolean nextDisabled = this.value()
            .map(Collection::size)
            .orElse(0) < count;

        this.setPrevious(
            Optional.ofNullable(
                false == previousDisabled ?
                    historyToken.setOffset(
                        OptionalInt.of(
                            Math.max(
                                0,
                                offset - count + 1
                            )
                        )
                    ) :
                    null
            )
        );
        this.setNext(
            Optional.ofNullable(
                false == nextDisabled ?
                    historyToken.setOffset(
                        OptionalInt.of(
                            offset + count - 1
                        )
                    ) :
                    null
            )
        );

        return (SpreadsheetDataTableComponent<T>) this;
    }

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
            if (maybeValues.map(v -> v.size() > 0).orElse(false)) {
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
