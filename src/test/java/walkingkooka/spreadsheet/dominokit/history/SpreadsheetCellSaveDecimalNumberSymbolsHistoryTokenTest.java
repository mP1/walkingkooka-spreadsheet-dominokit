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
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToDecimalNumberSymbolsMap;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveDecimalNumberSymbolsHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken>
    implements SpreadsheetMetadataTesting {

    private final static DecimalNumberSymbols DECIMAL_NUMBER_SYMBOLS2 = DecimalNumberSymbols.fromDecimalFormatSymbols(
        '+',
        new DecimalFormatSymbols(Locale.GERMANY)
    );

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(DECIMAL_NUMBER_SYMBOLS)
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
            () -> SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(DECIMAL_NUMBER_SYMBOLS),
                    SpreadsheetSelection.parseCell("A3"),
                    Optional.of(DECIMAL_NUMBER_SYMBOLS),
                    SpreadsheetSelection.parseCell("A4"),
                    Optional.of(DECIMAL_NUMBER_SYMBOLS)
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
            "/123/SpreadsheetName456/cell/A1/save/decimalNumberSymbols",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": DECIMAL_NUMBER_SYMBOLS
    // }
    @Test
    public void testParseOneCell() {
        final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DECIMAL_NUMBER_SYMBOLS)
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/decimalNumberSymbols/" +
                UrlFragment.with(
                    marshallMap(map)
                ),
            SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": DECIMAL_NUMBER_SYMBOLS
    //   "A2": DECIMAL_NUMBER_SYMBOLS2
    // }
    @Test
    public void testParseSeveralCells() {
        final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DECIMAL_NUMBER_SYMBOLS),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(DECIMAL_NUMBER_SYMBOLS2)
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/decimalNumberSymbols/" +
                UrlFragment.with(
                    marshallMap(map)
                ),
            SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
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
        final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/decimalNumberSymbols/" +
                UrlFragment.with(
                    marshallMap(map)
                ),
            SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": DecimalNumberSymbols
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> cellToDecimalNumberSymbols = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DECIMAL_NUMBER_SYMBOLS)
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToDecimalNumberSymbols
            ),
            "/123/SpreadsheetName456/cell/A1/save/decimalNumberSymbols/" +
                UrlFragment.with(
                    marshallMap(cellToDecimalNumberSymbols)
                )
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> cellToDecimalNumberSymbols = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DECIMAL_NUMBER_SYMBOLS2)
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToDecimalNumberSymbols
            ),
            "/123/SpreadsheetName456/cell/A1/save/decimalNumberSymbols/" +
                UrlFragment.with(
                    marshallMap(cellToDecimalNumberSymbols)
                )
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> cellToDecimalNumberSymbols = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DECIMAL_NUMBER_SYMBOLS),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(DECIMAL_NUMBER_SYMBOLS2),
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                DecimalNumberSymbols.fromDecimalFormatSymbols(
                    '+',
                    new DecimalFormatSymbols(Locale.ITALIAN)
                )
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToDecimalNumberSymbols
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/decimalNumberSymbols/" +
                UrlFragment.with(
                    marshallMap(cellToDecimalNumberSymbols)
                )
        );
    }

    @Test
    public void testUrlFragmentWithNoDecimalNumberSymbols() {
        final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> cellToDecimalNumberSymbols = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToDecimalNumberSymbols
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/decimalNumberSymbols/" +
                UrlFragment.with(
                    marshallMap(cellToDecimalNumberSymbols)
                )
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> map) {
        return marshall(
            SpreadsheetCellReferenceToDecimalNumberSymbolsMap.with(map)
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentDecimalNumberSymbols() {
        final Map<SpreadsheetCellReference, Optional<DecimalNumberSymbols>> value = Maps.of(
            CELL,
            Optional.of(
                DecimalNumberSymbols.fromDecimalFormatSymbols(
                    '+',
                    new DecimalFormatSymbols(Locale.ITALIAN)
                )
            )
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                           final SpreadsheetName name,
                                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(DECIMAL_NUMBER_SYMBOLS)
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken> type() {
        return SpreadsheetCellSaveDecimalNumberSymbolsHistoryToken.class;
    }
}
