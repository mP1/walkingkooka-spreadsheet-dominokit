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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken extends SpreadsheetCellDecimalNumberSymbolsHistoryToken implements Value<Optional<DecimalNumberSymbols>> {

    static SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken with(final SpreadsheetId id,
                                                                    final SpreadsheetName name,
                                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                                    final Optional<DecimalNumberSymbols> decimalNumberSymbols) {
        return new SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            decimalNumberSymbols
        );
    }

    private SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                                final Optional<DecimalNumberSymbols> decimalNumberSymbols) {
        super(
            id,
            name,
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
            this.id(),
            this.name(),
            this.anchoredSelection()
        );
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellDecimalNumberSymbolsSaveHistoryToken(
            id,
            name,
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
                this.id(),
                this.anchoredSelection().selection(),
                this.decimalNumberSymbols
            );
    }
}
