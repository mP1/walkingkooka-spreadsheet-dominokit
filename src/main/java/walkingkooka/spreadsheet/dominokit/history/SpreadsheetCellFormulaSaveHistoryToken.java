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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Objects;

/**
 * Saves or replaces the formula expression for the selected cell. Other components of the cell such as style or formatting are not modified.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/formula/save/=1+2
 * /spreadsheet-id/spreadsheet-name/cell/cell-or-cell-range-or-label/formula/save/new-formula-expression-here
 * </pre>
 */
public final class SpreadsheetCellFormulaSaveHistoryToken extends SpreadsheetCellFormulaHistoryToken {

    static SpreadsheetCellFormulaSaveHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                       final String formula) {
        return new SpreadsheetCellFormulaSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            formula
        );
    }

    private SpreadsheetCellFormulaSaveHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final String text) {
        super(
            id,
            name,
            anchoredSelection
        );

        this.text = Objects.requireNonNull(text, "text");
    }

    public String text() {
        return this.text;
    }

    private final String text;

    @Override
    UrlFragment formulaUrlFragment() {
        return saveUrlFragment(this.text);
    }

    @Override
    public HistoryToken clearAction() {
        return cellFormula(
            this.id(),
            this.name(),
            this.anchoredSelection()
        );
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        ).formula()
            .save(this.text);
    }

    @Override
    HistoryToken save0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .saveFormulaText(
                this.id(),
                this.anchoredSelection().selection(),
                this.text
            );
    }
}
