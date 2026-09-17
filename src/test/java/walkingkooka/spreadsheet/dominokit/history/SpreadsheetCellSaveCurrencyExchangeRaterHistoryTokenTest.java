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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.map.Maps;
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.collection.SpreadsheetCellReferenceToCurrencyExchangeRaterSelectorMap;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveCurrencyExchangeRaterHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken>
    implements SpreadsheetMetadataTesting {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    OPTIONAL_CURRENCY_EXCHANGE_RATER
                )
            )
        );

        this.getMessageAndCheck(
            thrown,
            "Save value includes cells A2 outside A1"
        );
    }

    @Test
    public void testWithSaveFormulasOutsideRangeFails2() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCellRange("A2:A3")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    OPTIONAL_CURRENCY_EXCHANGE_RATER,
                    SpreadsheetSelection.parseCell("A3"),
                    OPTIONAL_CURRENCY_EXCHANGE_RATER,
                    SpreadsheetSelection.parseCell("A4"),
                    OPTIONAL_CURRENCY_EXCHANGE_RATER
                )
            )
        );

        this.getMessageAndCheck(
            thrown,
            "Save value includes cells A1, A4 outside A2:A3"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseNoCellsFails() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/currencyExchangeRater",
            SpreadsheetCellSelectHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": CURRENCY
    // }
    @Test
    public void testParseOneCell() {
        final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> map = Maps.of(
            SpreadsheetSelection.A1,
            OPTIONAL_CURRENCY_EXCHANGE_RATER
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/currencyExchangeRater/" + marshallMap(map),
            SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": CURRENCY
    //   "A2": DIFFERENT_CURRENCY
    // }
    @Test
    public void testParseSeveralCells() {
        final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> map = Maps.of(
            SpreadsheetSelection.A1,
            OPTIONAL_CURRENCY_EXCHANGE_RATER,
            SpreadsheetSelection.parseCell("A2"),
            OPTIONAL_DIFFERENT_CURRENCY_EXCHANGE_RATER
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/currencyExchangeRater/" + marshallMap(map),
            SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": null
    // }
    @Test
    public void testParseOneCellWithoutSymbols() {
        final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/currencyExchangeRater/" + marshallMap(map),
            SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": Currency
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> cellToCurrencyExchangeRater = Maps.of(
            SpreadsheetSelection.A1,
            OPTIONAL_CURRENCY_EXCHANGE_RATER
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION,
                cellToCurrencyExchangeRater
            ),
            "/123/SpreadsheetName456/cell/A1/save/currencyExchangeRater/" +
                marshallMap(cellToCurrencyExchangeRater)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> cellToCurrencyExchangeRater = Maps.of(
            SpreadsheetSelection.A1,
            OPTIONAL_DIFFERENT_CURRENCY_EXCHANGE_RATER
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION,
                cellToCurrencyExchangeRater
            ),
            "/123/SpreadsheetName456/cell/A1/save/currencyExchangeRater/" +
                marshallMap(cellToCurrencyExchangeRater)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> cellToCurrencyExchangeRater = Maps.of(
            SpreadsheetSelection.A1,
            OPTIONAL_CURRENCY_EXCHANGE_RATER,
            SpreadsheetSelection.parseCell("A2"),
            OPTIONAL_DIFFERENT_CURRENCY_EXCHANGE_RATER,
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                CurrencyExchangeRaterSelector.parse("different-currency-exchange-rater-333")
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToCurrencyExchangeRater
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/currencyExchangeRater/" +
                marshallMap(cellToCurrencyExchangeRater)
        );
    }

    @Test
    public void testUrlFragmentWithNoCurrency() {
        final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> cellToCurrencyExchangeRater = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToCurrencyExchangeRater
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/currencyExchangeRater/" +
                marshallMap(cellToCurrencyExchangeRater)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> map) {
        return marshall(
            SpreadsheetCellReferenceToCurrencyExchangeRaterSelectorMap.with(map)
        );
    }

    // setSaveStringValue...............................................................................................

    @Test
    public void testSetSaveValueWithDifferentCurrencyExchangeRaterSelector() {
        final Map<SpreadsheetCellReference, Optional<CurrencyExchangeRaterSelector>> value = Maps.of(
            CELL,
            OPTIONAL_DIFFERENT_CURRENCY_EXCHANGE_RATER
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                            final SpreadsheetName name,
                                                                            final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                OPTIONAL_CURRENCY_EXCHANGE_RATER
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken> type() {
        return SpreadsheetCellSaveCurrencyExchangeRaterHistoryToken.class;
    }
}
