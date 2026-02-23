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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToCurrencyMap;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Currency;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveCurrencyHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveCurrencyHistoryToken>
    implements SpreadsheetMetadataTesting {

    private final static Currency CURRENCY2 = Currency.getInstance("AUD");

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(CURRENCY)
                )
            )
        );

        this.checkEquals(
            "Save value includes cells A2 outside A1",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testWithSaveFormulasOutsideRangeFails2() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(CURRENCY),
                    SpreadsheetSelection.parseCell("A3"),
                    Optional.of(CURRENCY),
                    SpreadsheetSelection.parseCell("A4"),
                    Optional.of(CURRENCY)
                )
            )
        );

        this.checkEquals(
            "Save value includes cells A1, A4 outside A2:A3",
            thrown.getMessage(),
            "message"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseNoCellsFails() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/currency",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": CURRENCY
    // }
    @Test
    public void testParseOneCell() {
        final Map<SpreadsheetCellReference, Optional<Currency>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(CURRENCY)
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/currency/" + marshallMap(map),
            SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": CURRENCY
    //   "A2": CURRENCY2
    // }
    @Test
    public void testParseSeveralCells() {
        final Map<SpreadsheetCellReference, Optional<Currency>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(CURRENCY),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(CURRENCY2)
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/currency/" + marshallMap(map),
            SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
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
        final Map<SpreadsheetCellReference, Optional<Currency>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/currency/" + marshallMap(map),
            SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
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
        final Map<SpreadsheetCellReference, Optional<Currency>> cellToCurrency = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(CURRENCY)
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToCurrency
            ),
            "/123/SpreadsheetName456/cell/A1/save/currency/" +
                marshallMap(cellToCurrency)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<Currency>> cellToCurrency = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(CURRENCY2)
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToCurrency
            ),
            "/123/SpreadsheetName456/cell/A1/save/currency/" +
                marshallMap(cellToCurrency)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<Currency>> cellToCurrency = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(CURRENCY),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(CURRENCY2),
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                Currency.getInstance("CAD")
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToCurrency
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/currency/" +
                marshallMap(cellToCurrency)
        );
    }

    @Test
    public void testUrlFragmentWithNoCurrency() {
        final Map<SpreadsheetCellReference, Optional<Currency>> cellToCurrency = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToCurrency
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/currency/" +
                marshallMap(cellToCurrency)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<Currency>> map) {
        return marshall(
            SpreadsheetCellReferenceToCurrencyMap.with(map)
        );
    }

    // setSaveStringValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentCurrency() {
        final Map<SpreadsheetCellReference, Optional<Currency>> value = Maps.of(
            CELL,
            Optional.of(
                Currency.getInstance("CAD")
            )
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveCurrencyHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveCurrencyHistoryToken createHistoryToken(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveCurrencyHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(CURRENCY)
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveCurrencyHistoryToken> type() {
        return SpreadsheetCellSaveCurrencyHistoryToken.class;
    }
}
