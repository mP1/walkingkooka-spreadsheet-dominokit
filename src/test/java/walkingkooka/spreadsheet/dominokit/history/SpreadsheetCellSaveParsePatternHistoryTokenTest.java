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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetParsePattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveParsePatternHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveParsePatternHistoryToken> {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.parseCell("A2"),
                                Optional.of(
                                        SpreadsheetPattern.parseNumberParsePattern("#.#")
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
                () -> SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A2:A3").setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                Optional.of(
                                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                                ),
                                SpreadsheetSelection.parseCell("A3"),
                                Optional.of(
                                        SpreadsheetPattern.parseDateTimeParsePattern("dd/mm/yyyy hh:mm")
                                ),
                                SpreadsheetSelection.parseCell("A4"),
                                Optional.of(
                                        SpreadsheetPattern.parseTimeParsePattern("hh:mm")
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
                "/123/SpreadsheetName456/cell/A1/save/parse-pattern",
                SpreadsheetCellSelectHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor()
                )
        );
    }

    // {
    //   "A1": {
    //     "type": "spreadsheet-number-parse-pattern",
    //     "value": "0.00"
    //   }
    // }
    @Test
    public void testParseOneCell() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1/save/parse-pattern/%7B%0A++%22A1%22:+%7B%0A++++%22type%22:+%22spreadsheet-number-parse-pattern%22%2C%0A++++%22value%22:+%220.00%22%0A++%7D%0A%7D",
                SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.A1.setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                Optional.of(
                                        SpreadsheetPattern.parseNumberParsePattern("0.00")
                                )
                        )
                )
        );
    }

    // {
    //   "A1": {
    //     "type": "spreadsheet-date-parse-pattern",
    //     "value": "dd/mm/yyyy"
    //   },
    //   "A2": {
    //     "type": "spreadsheet-time-parse-pattern",
    //     "value": "hh:mm"
    //   }
    // }
    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/parse-pattern/%7B%0A++%22A1%22:+%7B%0A++++%22type%22:+%22spreadsheet-date-parse-pattern%22%2C%0A++++%22value%22:+%22dd/mm/yyyy%22%0A++%7D%2C%0A++%22A2%22:+%7B%0A++++%22type%22:+%22spreadsheet-time-parse-pattern%22%2C%0A++++%22value%22:+%22hh:mm%22%0A++%7D%0A%7D",
                SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A2")
                                .setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                Optional.of(
                                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                                ),
                                SpreadsheetSelection.parseCell("A2"),
                                Optional.of(
                                        SpreadsheetPattern.parseTimeParsePattern("hh:mm")
                                )
                        )
                )
        );
    }

    // {
    //   "A1": null
    // }
    @Test
    public void testParseCellWithoutPattern() {
        this.parseAndCheck(
                "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/parse-pattern/%7B%22A1%22%3Anull%7D",
                SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A2")
                                .setDefaultAnchor(),
                        Maps.of(
                                SpreadsheetSelection.A1,
                                Optional.empty()
                        )
                )
        );
    }

    // {
    //   "A1": {
    //     "type": "spreadsheet-number-parse-pattern",
    //     "value": "#.##"
    //   }
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> cellToParsePattern = Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                        SpreadsheetPattern.parseNumberParsePattern("#.##")
                )
        );
        this.urlFragmentAndCheck(
                SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        cellToParsePattern
                ),
                "/123/SpreadsheetName456/cell/A1/save/parse-pattern/" +
                        marshallMapWithOptionalTypedValues(cellToParsePattern)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> cellToParsePattern = Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                        SpreadsheetPattern.parseNumberParsePattern("$0.00")
                )
        );

        this.urlFragmentAndCheck(
                SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SELECTION,
                        cellToParsePattern
                ),
                "/123/SpreadsheetName456/cell/A1/save/parse-pattern/" +
                        marshallMapWithOptionalTypedValues(cellToParsePattern)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> cellToFormulaText = Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                ),
                SpreadsheetSelection.parseCell("A2"),
                Optional.of(
                        SpreadsheetPattern.parseDateTimeParsePattern("dd/mm/yyyy hh:mm")
                ),
                SpreadsheetSelection.parseCell("A3"),
                Optional.of(
                        SpreadsheetPattern.parseTimeParsePattern("hh:mm")
                )
        );

        this.urlFragmentAndCheck(
                SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A3")
                                .setDefaultAnchor(),
                        cellToFormulaText
                ),
                "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/parse-pattern/" +
                        marshallMapWithOptionalTypedValues(cellToFormulaText)
        );
    }

    @Test
    public void testUrlFragmentWithMissingPattern() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParsePattern>> cellToFormulaText = Maps.of(
                SpreadsheetSelection.A1,
                Optional.empty()
        );

        this.urlFragmentAndCheck(
                SpreadsheetCellSaveParsePatternHistoryToken.with(
                        ID,
                        NAME,
                        SpreadsheetSelection.parseCellRange("A1:A3")
                                .setDefaultAnchor(),
                        cellToFormulaText
                ),
                "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/parse-pattern/" +
                        marshallMapWithOptionalTypedValues(cellToFormulaText)
        );
    }

    @Override
    SpreadsheetCellSaveParsePatternHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                   final SpreadsheetName name,
                                                                   final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveParsePatternHistoryToken.with(
                id,
                name,
                anchoredSelection,
                Maps.of(
                        SpreadsheetSelection.A1,
                        Optional.of(
                                SpreadsheetPattern.parseNumberParsePattern("0.00")
                        )
                )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveParsePatternHistoryToken> type() {
        return SpreadsheetCellSaveParsePatternHistoryToken.class;
    }
}
