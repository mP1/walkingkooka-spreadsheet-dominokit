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
import walkingkooka.collect.list.ImmutableList;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.ui.card.SpreadsheetCard;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Optional;

/**
 * A container that presents LINKS each missing a component from the parent {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
 */
final class SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover implements HtmlElementComponent<HTMLDivElement, SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover>,
        TreePrintable {

    /**
     * Creates an empty {@link SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover}.
     */
    static SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover empty(final int index) {
        return new SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover(index);
    }

    private SpreadsheetSortDialogComponentSpreadsheetComparatorNameAndDirectionRemover(final int index) {
        this.parent = SpreadsheetCard.empty();

        this.index = index;
    }

    /**
     * Clears existing links and recreates a link that will remove an individual component of the parent {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
     */
    void refresh(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList namesList,
                 final SpreadsheetSortDialogComponentContext context) {

        final SpreadsheetCard parent = this.parent;
        parent.clear();

        final HistoryToken historyToken = context.historyToken();

        final int index = this.index;
        final SpreadsheetColumnOrRowSpreadsheetComparatorNames names = namesList.get(index);
        final String idPrefix = SpreadsheetSortDialogComponent.ID_PREFIX + index + '-';

        int i = 0;
        final ImmutableList<SpreadsheetComparatorNameAndDirection> nameAndDirections = Lists.immutable(
                names.comparatorNameAndDirections()
        );

        if (nameAndDirections.size() > 1) {
            for (final SpreadsheetComparatorNameAndDirection comparatorNameAndDirections : names.comparatorNameAndDirections()) {

                final SpreadsheetColumnOrRowSpreadsheetComparatorNames removed = names.setComparatorNameAndDirections(
                        nameAndDirections.removeAtIndex(i)
                );

                // replace the old names at $index with $removed
                parent.appendChild(
                        historyToken.link(idPrefix + "-remove-" + i)
                                .setTextContent(comparatorNameAndDirections.toString())
                                .setHistoryToken(
                                        Optional.of(
                                                historyToken.setSave(
                                                        namesList.replace(
                                                                index,
                                                                removed
                                                        ).text()
                                                )
                                        )
                                )
                );

                i++;
            }
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
