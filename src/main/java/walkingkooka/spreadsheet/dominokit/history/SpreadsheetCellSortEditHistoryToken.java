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
import walkingkooka.spreadsheet.compare.SpreadsheetCellSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.List;

/**
 * This {@link HistoryToken} represents a EDIT dialog to edit the sort parameters.
 */
public final class SpreadsheetCellSortEditHistoryToken extends SpreadsheetCellSortHistoryToken {

    static SpreadsheetCellSortEditHistoryToken with(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                    final List<SpreadsheetCellSpreadsheetComparatorNames> comparatorNames) {
        return new SpreadsheetCellSortEditHistoryToken(
                id,
                name,
                anchoredSelection,
                comparatorNames
        );
    }

    private SpreadsheetCellSortEditHistoryToken(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                final List<SpreadsheetCellSpreadsheetComparatorNames> comparatorNames) {
        super(
                id,
                name,
                anchoredSelection,
                comparatorNames
        );
    }

    @Override
    UrlFragment sortUrlFragment() {
        final List<SpreadsheetCellSpreadsheetComparatorNames> comparatorNames = this.comparatorNames;

        return comparatorNames.isEmpty() ?
                EDIT :
                EDIT.append(
                        UrlFragment.SLASH.append(
                                UrlFragment.with(
                                        SpreadsheetCellSpreadsheetComparatorNames.listToString(comparatorNames)
                                )
                        )
                );
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellSortEditHistoryToken(
                id,
                name,
                anchoredSelection,
                this.comparatorNames
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return HistoryToken.cellSortSave(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                SpreadsheetCellSpreadsheetComparatorNames.parseList(value)
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // EDIT dialog appears!
    }
}
