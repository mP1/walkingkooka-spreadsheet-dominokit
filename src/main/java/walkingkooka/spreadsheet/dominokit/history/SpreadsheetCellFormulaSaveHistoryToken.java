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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Objects;

/**
 * Saves or replaces the formula expression for the selected cell. Other components of the cell such as style or formatting are not modified.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/formula/save/=1+2
 * /spreadsheet-id/spreadsheet-name/cell/cell-or-cell-range-or-label/formula/save/new-formula-expression-here
 * </pre>
 */
public final class SpreadsheetCellFormulaSaveHistoryToken extends SpreadsheetCellFormulaHistoryToken implements Value<String> {

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

    @Override
    public String value() {
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
            this.id,
            this.name,
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
            .setSaveStringValue(this.text);
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .patchFormula(
                this.id,
                this.anchoredSelection().selection(),
                SpreadsheetFormula.textPatch(text)
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellFormulaSave(
            this.id,
            this.name,
            this.anchoredSelection,
            this.text
        );
    }
}
