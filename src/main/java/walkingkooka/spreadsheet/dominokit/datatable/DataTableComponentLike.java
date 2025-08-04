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
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
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
abstract class DataTableComponentLike<T> implements TableComponent<HTMLDivElement, List<T>, DataTableComponent<T>>,
    ComponentWithChildren<DataTableComponent<T>, HTMLDivElement> {

    DataTableComponentLike() {
        super();
    }

    @Override
    public final DataTableComponent<T> focus() {
        return (DataTableComponent<T>) this;
    }

    @Override
    public final boolean isEditing() {
        return false; // always NO
    }

    // prev/next links..................................................................................................

    public abstract DataTableComponent<T> previousNextLinks(final String idPrefix);

    final HistoryTokenAnchorComponent previous(final String idPrefix) {
        return HistoryTokenAnchorComponent.empty()
            .setId(idPrefix + "previous" + SpreadsheetElementIds.LINK)
            .setTextContent("previous")
            .setIconBefore(
                Optional.of(
                    SpreadsheetIcons.tablePrevious()
                )
            );
    }

    final HistoryTokenAnchorComponent next(final String idPrefix) {
        return HistoryTokenAnchorComponent.empty()
            .setId(idPrefix + "next" + SpreadsheetElementIds.LINK)
            .setTextContent("next")
            .setIconAfter(
                Optional.of(
                    SpreadsheetIcons.tableNext()
                )
            );
    }

    public abstract DataTableComponent<T> setPrevious(final Optional<HistoryToken> historyToken);

    public abstract DataTableComponent<T> setNext(final Optional<HistoryToken> historyToken);

    /**
     * Updates the previous and next links using the history token to fetch the offset and count.
     * Note the next link will include the last row of the current view, and the previous link will include the first row of the current view.
     */
    public final DataTableComponent<T> refreshPreviousNextLinks(final HistoryToken historyToken,
                                                                final int defaultCount) {
        final HistoryTokenOffsetAndCount offsetAndCount = historyToken.offsetAndCount();
        final int offset = offsetAndCount.offset()
            .orElse(0);
        final int count = offsetAndCount.count()
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

        return (DataTableComponent<T>) this;
    }

    // header...........................................................................................................

    public abstract DataTableComponent<T> hideHeaders();

    // plugins..........................................................................................................

    /**
     * Registers the {@link org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin}
     */
    public abstract DataTableComponent<T> bodyScrollPlugin();

    /**
     * Prepares text for the {@link BodyScrollPlugin} which will be printed by {@link #printTreeTable(List, boolean, DataTableComponentCellRenderer, List, IndentingPrinter)}.
     */
    static String bodyScrollPluginText() {
        return "BodyScrollPlugin";
    }

    /**
     * Registers a plugin which will display an ICON and the given TITLE when the table is empty.
     */
    public abstract DataTableComponent<T> emptyStatePlugin(final Icon<?> icon,
                                                           final String title);

    /**
     * Prepares text for the {@link EmptyStatePlugin} which will be printed by {@link #printTreeTable(List, boolean, DataTableComponentCellRenderer, List, IndentingPrinter)}.
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
    public final boolean isEmpty() {
        return false == this.value().isPresent();
    }

    // TreePrintable....................................................................................................

    final void printTreeTable(final List<ColumnConfig<T>> columnConfigs,
                              final boolean headersHidden,
                              final DataTableComponentCellRenderer<T> cellRenderer,
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
