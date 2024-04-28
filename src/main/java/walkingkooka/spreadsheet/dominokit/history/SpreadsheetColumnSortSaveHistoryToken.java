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

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

/**
 * This {@link HistoryToken} invokes the server with the parameters to sort the selected column-range with the given comparators.
 */
public final class SpreadsheetColumnSortSaveHistoryToken extends SpreadsheetColumnSortHistoryToken {

    static SpreadsheetColumnSortSaveHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        return new SpreadsheetColumnSortSaveHistoryToken(
                id,
                name,
                anchoredSelection,
                comparatorNames
        );
    }

    private SpreadsheetColumnSortSaveHistoryToken(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                  final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames) {
        super(
                id,
                name,
                anchoredSelection
        );


        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isLabelName()) {
            selection.toColumnRange()
                    .comparatorNamesCheck(comparatorNames);
        }

        this.comparatorNames = comparatorNames;
    }

    public SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames() {
        return this.comparatorNames;
    }

    final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparatorNames;

    @Override
    UrlFragment sortUrlFragment() {
        return this.saveUrlFragment(this.comparatorNames);
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetColumnSortSaveHistoryToken(
                id,
                name,
                anchoredSelection,
                this.comparatorNames
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // call server here to SORT...
    }
}
