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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

/**
 * This token represents an action to clear a cell or cell-range and then edit its formula
 * <pre>
 * /123/SpreadsheetName456/cell/A1/clear-and-formula
 * /123/SpreadsheetName456/cell/B2:C3/clear-and-formula
 * /123/SpreadsheetName456/cell/Label123/clear-and-formula
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell-or-cell-range-or-label/clear-and-formula
 * </pre>
 */
public final class SpreadsheetCellClearAndFormulaHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellClearAndFormulaHistoryToken with(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellClearAndFormulaHistoryToken(
            id,
            name,
            anchoredSelection
        );
    }

    private SpreadsheetCellClearAndFormulaHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
            id,
            name,
            anchoredSelection
        );
    }

    @Override
    UrlFragment cellUrlFragment() {
        return CLEAR_AND_FORMULA;
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        ).clearAndFormula();
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .deleteDelta(
                this.id,
                this.anchoredSelection()
                    .selection()
            );

        context.pushHistoryToken(
            context.historyToken()
                .formula()
        );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellClearAndFormula(
            this.id,
            this.name,
            this.anchoredSelection
        );
    }
}
