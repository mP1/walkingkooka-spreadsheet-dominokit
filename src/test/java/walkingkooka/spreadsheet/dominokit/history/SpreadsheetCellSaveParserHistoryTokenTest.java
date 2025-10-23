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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToSpreadsheetParserSelectorMap;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveParserHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveParserHistoryToken> {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(
                        SpreadsheetPattern.parseNumberParsePattern("#.#")
                            .spreadsheetParserSelector()
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
            () -> SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3").setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                            .spreadsheetParserSelector()
                    ),
                    SpreadsheetSelection.parseCell("A3"),
                    Optional.of(
                        SpreadsheetPattern.parseDateTimeParsePattern("dd/mm/yyyy hh:mm")
                            .spreadsheetParserSelector()
                    ),
                    SpreadsheetSelection.parseCell("A4"),
                    Optional.of(
                        SpreadsheetPattern.parseTimeParsePattern("hh:mm")
                            .spreadsheetParserSelector()
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
            "/123/SpreadsheetName456/cell/A1/save/parser",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    // {
    //   "A1": "number 0.00"
    // }
    @Test
    public void testParseOneCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/parser/%7B%20%22A1%22%3A%20%22number%200.00%22%20%7D",
            SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        SpreadsheetPattern.parseNumberParsePattern("0.00")
                            .spreadsheetParserSelector()
                    )
                )
            )
        );
    }

    // {
    //   "A1": "date dd/mm/yyyy",
    //   "A2": "time-parse-pattern hh:mm"
    // }
    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/parser/%7B%20%22A1%22%3A%20%22date%20dd%2Fmm%2Fyyyy%22%2C%20%22A2%22%3A%20%22time-parse-pattern%20hh%3Amm%22%20%7D",
            SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    Optional.of(
                        SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                            .spreadsheetParserSelector()
                    ),
                    SpreadsheetSelection.parseCell("A2"),
                    Optional.of(
                        SpreadsheetPattern.parseTimeParsePattern("hh:mm")
                            .spreadsheetParserSelector()
                    )
                )
            )
        );
    }

    // {
    //   "A1": null
    // }
    @Test
    public void testParseCellWithoutParser() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/bottom-right/save/parser/%7B%22A1%22%3Anull%7D",
            SpreadsheetCellSaveParserHistoryToken.with(
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
    //   "A1": "number #.##"
    // }
    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> cellToParser = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                SpreadsheetPattern.parseNumberParsePattern("#.##")
                    .spreadsheetParserSelector()
            )
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToParser
            ),
            "/123/SpreadsheetName456/cell/A1/save/parser/" +
                marshallMap(cellToParser)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> cellToParser = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                SpreadsheetPattern.parseNumberParsePattern("$0.00")
                    .spreadsheetParserSelector()
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToParser
            ),
            "/123/SpreadsheetName456/cell/A1/save/parser/" +
                marshallMap(cellToParser)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> cellToParserText = Maps.of(
            SpreadsheetSelection.A1,
            Optional.of(
                SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                    .spreadsheetParserSelector()
            ),
            SpreadsheetSelection.parseCell("A2"),
            Optional.of(
                SpreadsheetPattern.parseDateTimeParsePattern("dd/mm/yyyy hh:mm")
                    .spreadsheetParserSelector()
            ),
            SpreadsheetSelection.parseCell("A3"),
            Optional.of(
                SpreadsheetPattern.parseTimeParsePattern("hh:mm")
                    .spreadsheetParserSelector()
            )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToParserText
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/parser/" +
                marshallMap(cellToParserText)
        );
    }

    @Test
    public void testUrlFragmentWithMissingParser() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> cellToParserText = Maps.of(
            SpreadsheetSelection.A1,
            Optional.empty()
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToParserText
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/parser/" +
                marshallMap(cellToParserText)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> map) {
        return marshall(
            SpreadsheetCellReferenceToSpreadsheetParserSelectorMap.with(map)
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentParser() {
        final Map<SpreadsheetCellReference, Optional<SpreadsheetParserSelector>> value = Maps.of(
            CELL,
            Optional.of(
                SpreadsheetParserSelector.parse("different")
            )
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveParserHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveParserHistoryToken createHistoryToken(final SpreadsheetId id,
                                                             final SpreadsheetName name,
                                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveParserHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                Optional.of(
                    SpreadsheetPattern.parseNumberParsePattern("0.00")
                        .spreadsheetParserSelector()
                )
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveParserHistoryToken> type() {
        return SpreadsheetCellSaveParserHistoryToken.class;
    }
}
