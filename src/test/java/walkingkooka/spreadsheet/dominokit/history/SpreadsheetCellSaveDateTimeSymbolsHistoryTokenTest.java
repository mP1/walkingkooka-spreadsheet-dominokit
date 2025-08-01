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
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToDateTimeSymbolsMap;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.text.DateFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveDateTimeSymbolsHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveDateTimeSymbolsHistoryToken>
    implements SpreadsheetMetadataTesting {

    private final static DateTimeSymbols DATE_TIME_SYMBOLS2 = DateTimeSymbols.fromDateFormatSymbols(
        new DateFormatSymbols(Locale.GERMANY)
    );

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(DATE_TIME_SYMBOLS)
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
            () -> SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(DATE_TIME_SYMBOLS),
                    SpreadsheetSelection.parseCell("A3"),
                    Optional.of(DATE_TIME_SYMBOLS),
                    SpreadsheetSelection.parseCell("A4"),
                    Optional.of(DATE_TIME_SYMBOLS)
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
            "/123/SpreadsheetName456/cell/A1/save/dateTimeSymbols",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": DATE_TIME_SYMBOLS
    // }
    @Test
    public void testParseOneCell() {
        final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DATE_TIME_SYMBOLS)
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/dateTimeSymbols/" + marshallMap(map),
            SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": DATE_TIME_SYMBOLS
    //   "A2": DATE_TIME_SYMBOLS2
    // }
    @Test
    public void testParseSeveralCells() {
        final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DATE_TIME_SYMBOLS),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(DATE_TIME_SYMBOLS2)
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/dateTimeSymbols/" + marshallMap(map),
            SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
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
        final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> map = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/dateTimeSymbols/" + marshallMap(map),
            SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                map
            )
        );
    }

    // {
    //   "A1": DateTimeSymbols
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> cellToDateTimeSymbols = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DATE_TIME_SYMBOLS)
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToDateTimeSymbols
            ),
            "/123/SpreadsheetName456/cell/A1/save/dateTimeSymbols/" +
                marshallMap(cellToDateTimeSymbols)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> cellToDateTimeSymbols = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DATE_TIME_SYMBOLS2)
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToDateTimeSymbols
            ),
            "/123/SpreadsheetName456/cell/A1/save/dateTimeSymbols/" +
                marshallMap(cellToDateTimeSymbols)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> cellToDateTimeSymbols = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(DATE_TIME_SYMBOLS),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(DATE_TIME_SYMBOLS2),
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                DateTimeSymbols.fromDateFormatSymbols(
                    new DateFormatSymbols(Locale.ITALIAN)
                )
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToDateTimeSymbols
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/dateTimeSymbols/" +
                marshallMap(cellToDateTimeSymbols)
        );
    }

    @Test
    public void testUrlFragmentWithNoDateTimeSymbols() {
        final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> cellToDateTimeSymbols = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToDateTimeSymbols
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/dateTimeSymbols/" +
                marshallMap(cellToDateTimeSymbols)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> map) {
        return marshall(
            SpreadsheetCellReferenceToDateTimeSymbolsMap.with(map)
        );
    }

    // setSaveStringValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentDateTimeSymbols() {
        final Map<SpreadsheetCellReference, Optional<DateTimeSymbols>> value = Maps.of(
            CELL,
            Optional.of(
                DateTimeSymbols.fromDateFormatSymbols(
                    new DateFormatSymbols(Locale.ITALIAN)
                )
            )
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveDateTimeSymbolsHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                      final SpreadsheetName name,
                                                                      final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveDateTimeSymbolsHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(DATE_TIME_SYMBOLS)
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveDateTimeSymbolsHistoryToken> type() {
        return SpreadsheetCellSaveDateTimeSymbolsHistoryToken.class;
    }
}
