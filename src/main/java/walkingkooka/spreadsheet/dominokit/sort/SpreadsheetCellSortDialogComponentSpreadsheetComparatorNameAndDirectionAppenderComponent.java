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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorDirection;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorNameAndDirection;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.card.CardComponent;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A container that shows LINKS for each {@link SpreadsheetComparatorName missing from the index {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}
 * from the parent {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
 */
final class SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent implements HtmlComponent<HTMLDivElement, SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent> {

    /**
     * Creates an empty {@link SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent}.
     */
    static SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent empty(final String id,
                                                                                                          final Function<SpreadsheetColumnOrRowSpreadsheetComparatorNames, HistoryToken> setter) {
        return new SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent(
            id,
            setter
        );
    }

    private SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent(final String id,
                                                                                                     final Function<SpreadsheetColumnOrRowSpreadsheetComparatorNames, HistoryToken> setter) {
        this.flex = SpreadsheetFlexLayout.row();
        this.root = CardComponent.empty()
            .setTitle("Append comparator(s)")
            .appendChild(this.flex);

        this.id = id;
        this.setter = setter;
    }

    /**
     * Creates links to append each of the {@link walkingkooka.spreadsheet.compare.SpreadsheetComparatorName} that are missing from the current {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}.
     */
    void refresh(final Optional<SpreadsheetColumnOrRowReferenceOrRange> columnOrRow,
                 final List<SpreadsheetComparatorNameAndDirection> spreadsheetComparatorNameAndDirections,
                 final SpreadsheetCellSortDialogComponentContext context) {
        this.flex.removeAllChildren();
        this.root.hide();

        if (columnOrRow.isPresent()) {
            final List<SpreadsheetComparatorNameAndDirection> copy = Lists.array();
            copy.addAll(spreadsheetComparatorNameAndDirections);

            this.refresh0(
                columnOrRow.get(),
                copy,
                context
            );
        }
    }

    void refresh0(final SpreadsheetColumnOrRowReferenceOrRange columnOrRow,
                  final List<SpreadsheetComparatorNameAndDirection> spreadsheetComparatorNameAndDirections,
                  final SpreadsheetCellSortDialogComponentContext context) {
        final SpreadsheetFlexLayout flex = this.flex;

        final HistoryToken historyToken = context.historyToken();

        final String idPrefix = this.id;

        final Set<SpreadsheetComparatorName> existing = spreadsheetComparatorNameAndDirections.stream()
            .map(SpreadsheetComparatorNameAndDirection::name)
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

            flex.appendChild(
                historyToken.link(idPrefix + "append-" + i)
                    .setTextContent(comparatorName.toString())
                    .setHistoryToken(
                        Optional.of(
                            setter.apply(append)
                        )
                    )
            );

            spreadsheetComparatorNameAndDirections.remove(addIndex);

            i++;
        }

        if (i > 0) {
            this.root.show();
        }
    }

    private final String id;

    private final Function<SpreadsheetColumnOrRowSpreadsheetComparatorNames, HistoryToken> setter;

    // setCssText.......................................................................................................

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent setCssText(final String css) {
        this.root.setCssText(css);
        return this;
    }

    // setCssProperty...................................................................................................

    @Override
    public SpreadsheetCellSortDialogComponentSpreadsheetComparatorNameAndDirectionAppenderComponent setCssProperty(final String name,
                                                                                                                   final String value) {
        this.root.setCssProperty(
            name,
            value
        );
        return this;
    }

    // isEditing........................................................................................................

    @Override
    public boolean isEditing() {
        return HtmlComponent.hasFocus(this.element());
    }

    // HtmlComponent.............................................................................................

    @Override
    public HTMLDivElement element() {
        return this.root.element();
    }

    /**
     * The parent holding all the current links to remove individual components of a {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}
     */
    private final CardComponent root;

    /**
     * A flex layout that holds the actual links.
     */
    private final SpreadsheetFlexLayout flex;

    // TreePrintable....................................................................................................

    /**
     * If there are no links, nothing will be printed.
     */
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
