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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToCurrencyMap;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Currency;
import java.util.Map;
import java.util.Optional;

/**
 * This {@link HistoryToken} is used by to paste a {@link Currency} for many cells over another range.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/save/currency/XYZ
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/save/currency/{@link Currency} for each selected cell.
 * </pre>
 */
public final class SpreadsheetCellSaveCurrencyHistoryToken extends SpreadsheetCellSaveMapHistoryToken<Optional<Currency>> {

    static SpreadsheetCellSaveCurrencyHistoryToken with(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                                        final Map<SpreadsheetCellReference, Optional<Currency>> value) {
        return new SpreadsheetCellSaveCurrencyHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToCurrencyMap.with(value)
        );
    }

    private SpreadsheetCellSaveCurrencyHistoryToken(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                    final SpreadsheetCellReferenceToCurrencyMap value) {
        super(
            id,
            name,
            anchoredSelection,
            value
        );
    }

    @Override //
    SpreadsheetCellSaveCurrencyHistoryToken replace(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                    final Map<SpreadsheetCellReference, Optional<Currency>> value) {
        return new SpreadsheetCellSaveCurrencyHistoryToken(
            id,
            name,
            anchoredSelection,
            SpreadsheetCellReferenceToCurrencyMap.with(value)
        );
    }

    @Override
    SpreadsheetCellReferenceToCurrencyMap parseSaveValue(final TextCursor cursor) {
        return parseJson(
            cursor,
            SpreadsheetCellReferenceToCurrencyMap.class
        );
    }

    // HasUrlFragment..................................................................................................

    @Override
    UrlFragment urlFragmentSaveEntity() {
        return CURRENCY;
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
            .patchCellsCurrency(
                this.id,
                this.anchoredSelection().selection(),
                this.value()
            );
        context.pushHistoryToken(previous);
    }
}
