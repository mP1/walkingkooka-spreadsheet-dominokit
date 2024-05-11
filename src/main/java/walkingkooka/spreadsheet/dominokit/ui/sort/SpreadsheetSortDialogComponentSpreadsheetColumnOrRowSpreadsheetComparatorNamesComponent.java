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

package walkingkooka.spreadsheet.dominokit.ui.sort;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.columnorrowcomparatornames.SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link walkingkooka.spreadsheet.dominokit.ui.Component} that supports editing an individual {@link walkingkooka.spreadsheet.compare.SpreadsheetComparatorName}
 * within a larger {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesList}.
 */
final class SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent implements HtmlElementComponent<HTMLDivElement, SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent>,
        TreePrintable {

    static SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent with(final int index) {
        return new SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(index);
    }

    private SpreadsheetSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(final int index) {
        final String idPrefix = ID_PREFIX + index;

        final SpreadsheetFlexLayout parent = SpreadsheetFlexLayout.emptyRow();

        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent names = SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
                .setId(idPrefix + SpreadsheetIds.TEXT_BOX);
        parent.appendChild(names);
        this.names = names;

        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender appender = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender.empty(index);
        parent.appendChild(appender);
        this.appender = appender;

        final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover remover = SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover.empty(index);
        parent.appendChild(remover);
        this.remover = remover;

        this.parent = parent;
    }

    void refresh(final String columnOrRowSpreadsheetComparatorNames,
                 final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                 final SpreadsheetSortDialogComponentContext context) {
        this.names.setStringValue(
                Optional.ofNullable(
                        columnOrRowSpreadsheetComparatorNames.isEmpty() ?
                                null :
                                columnOrRowSpreadsheetComparatorNames
                )
        );

        final Optional<SpreadsheetColumnOrRowReference> columnOrRow = SpreadsheetColumnOrRowSpreadsheetComparatorNames.tryParseColumnOrRow(columnOrRowSpreadsheetComparatorNames);

        List<SpreadsheetComparatorNameAndDirection> comparatorNameAndDirections;

        try {
            comparatorNameAndDirections = SpreadsheetColumnOrRowSpreadsheetComparatorNames.parse(columnOrRowSpreadsheetComparatorNames)
                    .comparatorNameAndDirections();
        } catch (final RuntimeException ignore) {
            comparatorNameAndDirections = Lists.empty();
        }

        this.appender.refresh(
                columnOrRow,
                comparatorNameAndDirections,
                (names) -> columnOrRowSpreadsheetComparatorNamesToHistoryToken.apply(
                        Optional.of(names)
                ),
                context
        );
        this.remover.refresh(
                columnOrRow,
                comparatorNameAndDirections,
                columnOrRowSpreadsheetComparatorNamesToHistoryToken,
                context
        );
    }

    private final SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent names;

    private final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender appender;

    private final SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover remover;

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.parent.element();
    }

    private final SpreadsheetFlexLayout parent;

    private final static String ID_PREFIX = SpreadsheetSortDialogComponent.ID_PREFIX + "comparator-";

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.parent.printTree(printer);
        }
        printer.outdent();
    }
}
