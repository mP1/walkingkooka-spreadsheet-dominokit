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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetFormatPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveFormatPatternHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveFormatPatternHistoryToken> {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellSaveFormatPatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.parseCell("A2"),
                                Optional.of(
                                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
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
                () -> SpreadsheetCellSaveFormatPatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A2:A3").setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                Optional.of(
                                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                                ),
                                SpreadsheetSelection.parseCell("A3"),
                                Optional.of(
                                        SpreadsheetPattern.parseDateTimeFormatPattern("dd/mm/yyyy hh:mm")
                                ),
                                SpreadsheetSelection.parseCell("A4"),
                                Optional.of(
                                        SpreadsheetPattern.parseTimeFormatPattern("hh:mm")
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
                "/123/SpreadsheetName456/cell/A1/save/format-pattern",
                SpreadsheetCellSelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor()
                )
        );
    }

    // {
    //   "A1": {
    //     "type": "spreadsheet-text-format-pattern",
    //     "value": "@"
    //   }
    // }
    @Test
    public void testParseOneCell() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/save/format-pattern/%7B%0A++%22A1%22:+%7B%0A++++%22type%22:+%22spreadsheet-text-format-pattern%22%2C%0A++++%22value%22:+%22@%22%0A++%7D%0A%7D",
                SpreadsheetCellSaveFormatPatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                Optional.of(
                                        SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                                )
                        )
                )
        );
    }

    // {
    //   "A1": {
    //     "type": "spreadsheet-date-format-pattern",
    //     "value": "dd/mm/yyyy"
    //   },
    //   "A2": {
    //     "type": "spreadsheet-time-format-pattern",
    //     "value": "hh:mm"
    //   }
    // }
    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/format-pattern/%7B%0A++%22A1%22:+%7B%0A++++%22type%22:+%22spreadsheet-date-format-pattern%22%2C%0A++++%22value%22:+%22dd/mm/yyyy%22%0A++%7D%2C%0A++%22A2%22:+%7B%0A++++%22type%22:+%22spreadsheet-time-format-pattern%22%2C%0A++++%22value%22:+%22hh:mm%22%0A++%7D%0A%7D",
                SpreadsheetCellSaveFormatPatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A2")
                                .setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                Optional.of(
                                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                                ),
                                SpreadsheetSelection.parseCell("A2"),
                                Optional.of(
                                        SpreadsheetPattern.parseTimeFormatPattern("hh:mm")
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
                "/123/SpreadsheetName456/cell/A1/save/format-pattern/%7B%22A1%22%3Anull%7D",
                SpreadsheetCellSaveFormatPatternHistoryToken.with(
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
    //     "type": "spreadsheet-number-format-pattern",
    //     "value": "#.##"
    //   }
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatPattern>> cellToFormatPattern = Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                        SpreadsheetPattern.parseNumberFormatPattern("#.##")
                )
        );
        this.urlFragmentAndCheck(
                SpreadsheetCellSaveFormatPatternHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        cellToFormatPattern
                ),
                "/123/SpreadsheetName456/cell/A1/save/format-pattern/" +
                        marshallMapWithOptionalTypedValues(cellToFormatPattern)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatPattern>> cellToFormatPattern = Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                        SpreadsheetPattern.parseTextFormatPattern("@@")
                )
        );

        this.urlFragmentAndCheck(
                SpreadsheetCellSaveFormatPatternHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        cellToFormatPattern
                ),
                "/123/SpreadsheetName456/cell/A1/save/format-pattern/" +
                        marshallMapWithOptionalTypedValues(cellToFormatPattern)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatPattern>> cellToFormulaText = Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                        SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
                ),
                SpreadsheetSelection.parseCell("A2"),
                Optional.of(
                        SpreadsheetPattern.parseDateTimeFormatPattern("dd/mm/yyyy hh:mm")
                ),
                SpreadsheetSelection.parseCell("A3"),
                Optional.of(
                        SpreadsheetPattern.parseTimeFormatPattern("hh:mm")
                )
        );

        this.urlFragmentAndCheck(
                SpreadsheetCellSaveFormatPatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A3")
                                .setDefaultAnchor(),
                        cellToFormulaText
                ),
                "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/format-pattern/" +
                        marshallMapWithOptionalTypedValues(cellToFormulaText)
        );
    }

    @Test
    public void testUrlFragmentWithNoPattern() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetFormatPattern>> cellToFormulaText = Maps.of(
                SpreadsheetSelection.A1,
                Optional.empty()
        );

        this.urlFragmentAndCheck(
                SpreadsheetCellSaveFormatPatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A3")
                                .setDefaultAnchor(),
                        cellToFormulaText
                ),
                "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/format-pattern/" +
                        marshallMapWithOptionalTypedValues(cellToFormulaText)
        );
    }

    @Override
    SpreadsheetCellSaveFormatPatternHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                    final SpreadsheetName name,
                                                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveFormatPatternHistoryToken.with(
                id,
                name,
                anchoredSelection,
                Maps.of(
                        SpreadsheetSelection.A1,
                        Optional.of(
                                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN
                        )
                )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveFormatPatternHistoryToken> type() {
        return SpreadsheetCellSaveFormatPatternHistoryToken.class;
    }
}
