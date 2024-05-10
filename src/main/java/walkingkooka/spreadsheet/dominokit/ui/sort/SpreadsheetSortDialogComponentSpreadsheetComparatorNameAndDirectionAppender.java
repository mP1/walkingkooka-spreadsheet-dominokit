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
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorDirection;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A container that shows LINKS for each {@link SpreadsheetComparatorName missing from the index {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}
 * from the parent {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
 */
final class SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender implements HtmlElementComponent<HTMLDivElement, SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender}.
     */
    static SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender empty(final int index) {
        return new SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender(index);
    }

    private SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionAppender(final int index) {
        this.parent = SpreadsheetCard.empty();

        this.index = index;
    }

    /**
     * Creates links to append each of the {@link walkingkooka.spreadsheet.compare.SpreadsheetComparatorName} that are missing from the current {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
     */
    void refresh(final Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> namesList,
                 final SpreadsheetColumnOrRowReference columnOrRow,
                 final SpreadsheetSortDialogComponentContext context) {
        final int index = this.index;

        final SpreadsheetColumnOrRowSpreadsheetComparatorNames columnOrRowSpreadsheetComparatorNames;
        final List<SpreadsheetComparatorNameAndDirection> spreadsheetComparatorNameAndDirections = Lists.array();
        Function<SpreadsheetColumnOrRowSpreadsheetComparatorNames, SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> columnOrRowSpreadsheetComparatorNamesToNamesList = null;

        if (namesList.isPresent()) {
            final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList namesListPresent = namesList.get();
            if (index < namesListPresent.size()) {
                columnOrRowSpreadsheetComparatorNames = namesListPresent.get(index);

                spreadsheetComparatorNameAndDirections.addAll(
                        columnOrRowSpreadsheetComparatorNames.comparatorNameAndDirections()
                );
                columnOrRowSpreadsheetComparatorNamesToNamesList = (names) ->
                        namesListPresent.replace(
                                index,
                                names
                        );
            } else {
                columnOrRowSpreadsheetComparatorNamesToNamesList = (names) ->
                        namesListPresent.concat(
                                names
                        );
            }
        }

        if (null == columnOrRowSpreadsheetComparatorNamesToNamesList) {
            columnOrRowSpreadsheetComparatorNamesToNamesList = (names) ->
                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.with(
                            Lists.of(names)
                    );
        }

        this.refresh0(
                columnOrRow,
                spreadsheetComparatorNameAndDirections,
                columnOrRowSpreadsheetComparatorNamesToNamesList,
                context
        );
    }

    void refresh0(final SpreadsheetColumnOrRowReference columnOrRow,
                  final List<SpreadsheetComparatorNameAndDirection> spreadsheetComparatorNameAndDirections,
                  final Function<SpreadsheetColumnOrRowSpreadsheetComparatorNames, SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> columnOrRowSpreadsheetComparatorNamesToNamesList,
                  final SpreadsheetSortDialogComponentContext context) {
        final SpreadsheetCard parent = this.parent;
        parent.clear();

        final HistoryToken historyToken = context.historyToken();

        final int index = this.index;
        final String idPrefix = SpreadsheetSortDialogComponent.ID_PREFIX + index + '-';

        final Set<SpreadsheetComparatorName> existing = spreadsheetComparatorNameAndDirections.stream()
                .map(nad -> nad.name())
                .collect(Collectors.toSet());

        final int addIndex = spreadsheetComparatorNameAndDirections.size();
        int i = 0;

        for (final SpreadsheetComparatorInfo info : context.spreadsheetComparatorInfos()) {

            final SpreadsheetComparatorName comparatorName = info.name();
            if (existing.contains(comparatorName)) {
                continue;
            }

            spreadsheetComparatorNameAndDirections.add(
                    comparatorName.setDirection(SpreadsheetComparatorDirection.DEFAULT)
            );

            final SpreadsheetColumnOrRowSpreadsheetComparatorNames append = SpreadsheetColumnOrRowSpreadsheetComparatorNames.with(
                    columnOrRow,
                    spreadsheetComparatorNameAndDirections
            );

            parent.appendChild(
                    historyToken.link(idPrefix + "-append-" + i)
                            .setTextContent(comparatorName.toString())
                            .setHistoryToken(
                                    Optional.of(
                                            historyToken.setSave(
                                                    columnOrRowSpreadsheetComparatorNamesToNamesList.apply(
                                                            append
                                                    ).text()
                                            )
                                    )
                            )
            );


            spreadsheetComparatorNameAndDirections.remove(addIndex);

            i++;
        }
    }

    /**
     * An index for this {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames} within a {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesList}.
     */
    private final int index;

    // HtmlElementComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.parent.element();
    }

    /**
     * The parent holding all the current links to remove individual components of a {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}
     */
    private final SpreadsheetCard parent;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.parent.printTree(printer);
            printer.lineStart();
        }
        printer.outdent();
    }
}
