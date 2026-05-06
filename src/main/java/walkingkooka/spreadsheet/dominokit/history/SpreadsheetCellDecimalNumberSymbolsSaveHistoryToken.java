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
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken extends SpreadsheetCellDecimalNumberSymbolsHistoryToken implements Value<Optional<DecimalNumberSymbols>> {

    static SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken with(final SpreadsheetId spreadsheetId,
                                                                    final SpreadsheetName spreadsheetName,
                                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                                    final Optional<DecimalNumberSymbols> decimalNumberSymbols) {
        return new SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            decimalNumberSymbols
        );
    }

    private SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken(final SpreadsheetId spreadsheetId,
                                                                final SpreadsheetName spreadsheetName,
                                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                                final Optional<DecimalNumberSymbols> decimalNumberSymbols) {
        super(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            decimalNumberSymbols
        );
    }

    @Override
    public Optional<DecimalNumberSymbols> value() {
        return this.decimalNumberSymbols;
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellDecimalNumberSymbolsSelect(
            this.spreadsheetId,
            this.spreadsheetName,
            this.anchoredSelection()
        );
    }

    @Override
    HistoryToken replaceSpreadsheetIdSpreadsheetNameAnchoredSelection(final SpreadsheetId spreadsheetId,
                                                                      final SpreadsheetName spreadsheetName,
                                                                      final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            this.decimalNumberSymbols
        );
    }

    // cell/A1/decimalNumberSymbols/save/DecimalNumberSymbols
    @Override
    UrlFragment decimalNumberSymbolsUrlFragment() {
        return saveUrlFragment(this.value());
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .patchDecimalNumberSymbols(
                this.spreadsheetId,
                this.anchoredSelection().selection(),
                this.decimalNumberSymbols
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellDecimalNumberSymbolsSave(
            this.spreadsheetId,
            this.spreadsheetName,
            this.anchoredSelection,
            this.decimalNumberSymbols
        );
    }
}
