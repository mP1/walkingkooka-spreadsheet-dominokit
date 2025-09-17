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

package walkingkooka.spreadsheet.dominokit.sort;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * A container that presents LINKS each missing a component from the parent {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
 */
final class SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent implements HtmlComponentDelegator<HTMLDivElement, SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent> {

    /**
     * Creates an empty {@link SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent}.
     */
    static SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent empty(final String id,
                                                                                                         final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter) {
        return new SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent(
            id,
            setter
        );
    }

    private SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionRemoverComponent(final String id,
                                                                                                    final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter) {
        this.root = CardComponent.empty()
            .setTitle("Remove comparator(s)");

        this.id = id;
        this.setter = setter;
    }

    /**
     * Creates links to append each of the {@link walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName} that are missing from the current {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
     */
    void refresh(final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow,
                 final List<SpreadsheetComparatorNameAndDirection> spreadsheetComparatorNameAndDirections,
                 final SpreadsheetCellSortDialogComponentContext context) {
        final CardComponent root = this.root;
        root.removeAllChildren()
            .hide();

        if (columnOrRow.isPresent()) {
            this.refresh0(
                columnOrRow.get(),
                spreadsheetComparatorNameAndDirections,
                context
            );
        }
    }

    void refresh0(final SpreadsheetColumnOrRowReferenceOrRange columnOrRow,
                  final List<SpreadsheetComparatorNameAndDirection> spreadsheetComparatorNameAndDirections,
                  final SpreadsheetCellSortDialogComponentContext context) {
        final CardComponent root = this.root;

        final HistoryToken historyToken = context.historyToken();

        final String idPrefix = this.id;

        final int count = spreadsheetComparatorNameAndDirections.size();

        for (int i = 0; i < count; i++) {
            final List<SpreadsheetComparatorNameAndDirection> removed = Lists.array();
            removed.addAll(spreadsheetComparatorNameAndDirections);
            final String text = removed.remove(i)
                .name()
                .text();

            root.appendChild(
                historyToken.link(idPrefix + "remove-" + i)
                    .setTextContent(text)
                    .setHistoryToken(
                        Optional.of(
                            this.setter.apply(
                                Optional.ofNullable(
                                    removed.isEmpty() ?
                                        null :
                                        SpreadsheetColumnOrRowSpreadsheetComparatorNames.with(
                                            columnOrRow,
                                            removed
                                        )
                                )
                            )
                        )
                    )
            );
        }

        if (count > 0) {
            root.show();
        }
    }

    private final String id;

    private final Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter;

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element());
    }

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLDivElement, ?> htmlComponent() {
        return this.root;
    }

    /**
     * The parent holding all the current links to remove individual components of a {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}
     */
    private final CardComponent root;

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        if (this.root.isNotEmpty()) {
            printer.println(this.getClass().getSimpleName());
            printer.indent();
            {
                this.root.printTree(printer);
                printer.lineStart();
            }
            printer.outdent();
        }
    }
}
