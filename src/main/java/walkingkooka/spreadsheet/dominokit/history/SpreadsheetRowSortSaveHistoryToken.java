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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.HasValue;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

/**
 * This {@link HistoryToken} invokes the server with the parameters to sort the selected row-range with the given comparators.
 * <pre>
 * /123/SpreadsheetName456/row/1/sort/save/Z=text
 *
 * /spreadsheet-id/spreadsheet-name/row/row or row-range/sort/save/SpreadsheetColumnOrRowSpreadsheetComparatorsList#text
 * </pre>
 */
public final class SpreadsheetRowSortSaveHistoryToken extends SpreadsheetRowSortHistoryToken
    implements HasValue<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> {

    static SpreadsheetRowSortSaveHistoryToken with(final SpreadsheetId spreadsheetId,
                                                   final SpreadsheetName spreadsheetName,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        return new SpreadsheetRowSortSaveHistoryToken(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            comparatorNames
        );
    }

    private SpreadsheetRowSortSaveHistoryToken(final SpreadsheetId spreadsheetId,
                                               final SpreadsheetName spreadsheetName,
                                               final AnchoredSpreadsheetSelection anchoredSelection,
                                               final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        super(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection
        );


        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isLabelName()) {
            selection.toRowRange()
                .comparatorNamesBoundsCheck(comparatorNames);
        }

        this.comparatorNames = comparatorNames;
    }

    @Override
    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesList value() {
        return this.comparatorNames;
    }

    final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames;

    @Override
    UrlFragment sortUrlFragment() {
        return saveUrlFragment(this.value());
    }

    @Override
    HistoryToken replaceSpreadsheetIdSpreadsheetNameAnchoredSelection(final SpreadsheetId spreadsheetId,
                                                                      final SpreadsheetName spreadsheetName,
                                                                      final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetRowSortSaveHistoryToken(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            this.comparatorNames
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous.clearAction());

        context.spreadsheetDeltaFetcher()
            .getSortCells(
                this.spreadsheetId,
                this.anchoredSelection()
                    .selection()
                    .toCellRange(),
                this.comparatorNames
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitRowSortSave(
            this.spreadsheetId,
            this.spreadsheetName,
            this.anchoredSelection,
            this.comparatorNames
        );
    }
}
