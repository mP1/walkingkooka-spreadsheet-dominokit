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
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellCurrencyExchangeRaterSaveHistoryToken extends SpreadsheetCellCurrencyExchangeRaterHistoryToken implements HasValue<Optional<CurrencyExchangeRaterSelector>> {

    static SpreadsheetCellCurrencyExchangeRaterSaveHistoryToken with(final SpreadsheetId spreadsheetId,
                                                                     final SpreadsheetName spreadsheetName,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                     final Optional<CurrencyExchangeRaterSelector> currency) {
        return new SpreadsheetCellCurrencyExchangeRaterSaveHistoryToken(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            currency
        );
    }

    private SpreadsheetCellCurrencyExchangeRaterSaveHistoryToken(final SpreadsheetId spreadsheetId,
                                                                 final SpreadsheetName spreadsheetName,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                                 final Optional<CurrencyExchangeRaterSelector> currency) {
        super(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            currency
        );
    }

    @Override
    public Optional<CurrencyExchangeRaterSelector> value() {
        return this.currencyExchangeRater;
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellCurrencySelect(
            this.spreadsheetId,
            this.spreadsheetName,
            this.anchoredSelection()
        );
    }

    @Override
    HistoryToken replaceSpreadsheetIdSpreadsheetNameAnchoredSelection(final SpreadsheetId spreadsheetId,
                                                                      final SpreadsheetName spreadsheetName,
                                                                      final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellCurrencyExchangeRaterSaveHistoryToken(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            this.currencyExchangeRater
        );
    }

    // cell/A1/currencyExchangeRater/save/CurrencyExchangeRaterSelector
    @Override
    UrlFragment currencyExchangeRaterUrlFragment() {
        return saveUrlFragment(this.value());
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .patchCurrencyExchangeRater(
                this.spreadsheetId,
                this.anchoredSelection().selection(),
                this.currencyExchangeRater
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellCurrencyExchangeRaterSave(
            this.spreadsheetId,
            this.spreadsheetName,
            this.anchoredSelection,
            this.currencyExchangeRater
        );
    }
}
