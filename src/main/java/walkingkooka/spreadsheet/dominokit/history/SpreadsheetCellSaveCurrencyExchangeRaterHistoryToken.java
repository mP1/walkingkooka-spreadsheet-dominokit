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

import walkingkooka.currency.CurrencyExchangeRater;
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.value.collection.SpreadsheetCellReferenceToCurrencyExchangeRaterSelectorMap;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link CurrencyExchangeRaterSelector} for many cells over another range.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/currencyExchangeRater/XYZ
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/currencyExchangeRater/{@link CurrencyExchangeRater} for each selected cell.
 * </pre>
 */
public final class SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<CurrencyExchangeRaterSelector>> {

    static SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken with(final SpreadsheetId spreadsheetId,
                                                                     final SpreadsheetName spreadsheetName,
                                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                                     final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> value) {
        return new SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            SpreadsheetCellReferenceToCurrencyExchangeRaterSelectorMap.with(value)
        );
    }

    private SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken(final SpreadsheetId spreadsheetId,
                                                                 final SpreadsheetName spreadsheetName,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                                 final SpreadsheetCellReferenceToCurrencyExchangeRaterSelectorMap value) {
        super(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            value
        );
    }

    @Override //
    SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken replace(final SpreadsheetId spreadsheetId,
                                                                 final SpreadsheetName spreadsheetName,
                                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                                 final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> value) {
        return new SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken(
            spreadsheetId,
            spreadsheetName,
            anchoredSelection,
            SpreadsheetCellReferenceToCurrencyExchangeRaterSelectorMap.with(value)
        );
    }

    @Override
    SpreadsheetCellReferenceToCurrencyExchangeRaterSelectorMap parseSaveValue(final TextCursor cursor) {
        return parseJson(
            cursor,
            SpreadsheetCellReferenceToCurrencyExchangeRaterSelectorMap.class
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return CURRENCY_EXCHANGE_RATER;
    }

    // HistoryWatcher...................................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsCurrencyExchangeRater(
                this.spreadsheetId,
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
