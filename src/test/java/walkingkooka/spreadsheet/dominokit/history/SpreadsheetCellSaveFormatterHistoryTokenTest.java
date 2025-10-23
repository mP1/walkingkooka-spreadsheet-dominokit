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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveFormatterHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveFormatterHistoryToken> {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(
                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                            .spreadsheetFormatterSelector()
                    )
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
            () -> SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3").setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                            .spreadsheetFormatterSelector()
                    ),
                    SpreadsheetSelection.parseCell("A3"),
                    Optional.of(
                        SpreadsheetPattern.parseDateTimeFormatPattern("dd/mm/yyyy hh:mm")
                            .spreadsheetFormatterSelector()
                    ),
                    SpreadsheetSelection.parseCell("A4"),
                    Optional.of(
                        SpreadsheetPattern.parseTimeFormatPattern("hh:mm")
                            .spreadsheetFormatterSelector()
                    )
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
            "/123/SpreadsheetName456/cell/A1/save/formatter",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": "text-format @"
    // }
    @Test
    public void testParseOneCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/formatter/%7B%0A%20%20%20%22A1%22%3A%20%22text%20%40%22%0A%7D",
            SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                            .spreadsheetFormatterSelector()
                    )
                )
            )
        );
    }

    // {
    //   "A1": "date-format dd/mm/yyyy",
    //   "A2": "time-format hh:mm"
    // }
    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/formatter/%7B%0A%20%20%22A1%22%3A%20%22date%20dd%2Fmm%2Fyyyy%22%2C%0A%20%20%22A2%22%3A%20%22time%20hh%3Amm%22%0A%7D",
            SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                            .spreadsheetFormatterSelector()
                    ),
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(
                        SpreadsheetPattern.parseTimeFormatPattern("hh:mm")
                            .spreadsheetFormatterSelector()
                    )
                )
            )
        );
    }

    // {
    //   "A1": null
    // }
    @Test
    public void testParseOneCellWithoutPattern() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/formatter/%7B%22A1%22%3Anull%7D",
            SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.empty()
                )
            )
        );
    }

    // {
    //   "A1": {
    //     "type": "spreadsheet-number",
    //     "value": "#.##"
    //   }
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> cellToFormatter = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                SpreadsheetPattern.parseNumberFormatPattern("#.##")
                    .spreadsheetFormatterSelector()
            )
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToFormatter
            ),
            "/123/SpreadsheetName456/cell/A1/save/formatter/" +
                marshallMap(cellToFormatter)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> cellToFormatter = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                SpreadsheetPattern.parseTextFormatPattern("@@")
                    .spreadsheetFormatterSelector()
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToFormatter
            ),
            "/123/SpreadsheetName456/cell/A1/save/formatter/" +
                marshallMap(cellToFormatter)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> cellToFormulaText = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                    .spreadsheetFormatterSelector()
            ),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(
                SpreadsheetPattern.parseDateTimeFormatPattern("dd/mm/yyyy hh:mm")
                    .spreadsheetFormatterSelector()
            ),
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                SpreadsheetPattern.parseTimeFormatPattern("hh:mm")
                    .spreadsheetFormatterSelector()
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToFormulaText
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/formatter/" +
                marshallMap(cellToFormulaText)
        );
    }

    @Test
    public void testUrlFragmentWithNoFormatter() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> cellToFormulaText = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToFormulaText
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/formatter/" +
                marshallMap(cellToFormulaText)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> map) {
        return marshall(
            SpreadsheetCellReferenceToSpreadsheetFormatterSelectorMap.with(map)
        );
    }

    // setSaveStringValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentFormatter() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatterSelector>> value = Maps.of(
            CELL,
            Optional.of(
                SpreadsheetFormatterSelector.parse("different")
            )
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveFormatterHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveFormatterHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveFormatterHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                    SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN.spreadsheetFormatterSelector()
                )
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveFormatterHistoryToken> type() {
        return SpreadsheetCellSaveFormatterHistoryToken.class;
    }
}
