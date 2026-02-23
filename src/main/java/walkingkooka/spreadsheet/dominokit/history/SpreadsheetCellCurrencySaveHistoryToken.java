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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Currency;
import java.util.Optional;

public final class SpreadsheetCellCurrencySaveHistoryToken extends SpreadsheetCellCurrencyHistoryToken implements Value<Optional<Currency>> {

    static SpreadsheetCellCurrencySaveHistoryToken with(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                                        final Optional<Currency> currency) {
        return new SpreadsheetCellCurrencySaveHistoryToken(
            id,
            name,
            anchoredSelection,
            currency
        );
    }

    private SpreadsheetCellCurrencySaveHistoryToken(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                    final Optional<Currency> currency) {
        super(
            id,
            name,
            anchoredSelection,
            currency
        );
    }

    @Override
    public Optional<Currency> value() {
        return this.currency;
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellCurrencySelect(
            this.id,
            this.name,
            this.anchoredSelection()
        );
    }

    @Override
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellCurrencySaveHistoryToken(
            id,
            name,
            anchoredSelection,
            this.currency
        );
    }

    // cell/A1/currency/save/Currency
    @Override
    UrlFragment currencyUrlFragment() {
        return saveUrlFragment(this.value());
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .patchCurrency(
                this.id,
                this.anchoredSelection().selection(),
                this.currency
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellCurrencySave(
            this.id,
            this.name,
            this.anchoredSelection,
            this.currency
        );
    }
}
