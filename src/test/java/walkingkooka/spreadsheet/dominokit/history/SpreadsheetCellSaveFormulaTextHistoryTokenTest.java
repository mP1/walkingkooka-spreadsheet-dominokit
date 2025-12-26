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
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellReferenceToFormulaTextMap;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveFormulaTextHistoryTokenTest extends SpreadsheetCellSaveMapHistoryTokenTestCase<SpreadsheetCellSaveFormulaTextHistoryToken> {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveFormulaTextHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.parseCell("A2"),
                    "=1+2"
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
            () -> SpreadsheetCellSaveFormulaTextHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3").setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    "=1",
                    SpreadsheetSelection.parseCell("A3"),
                    "=33",
                    SpreadsheetSelection.parseCell("A4"),
                    "=444"
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
            "/123/SpreadsheetName456/cell/A1/save/formula",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseOneCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/save/formula/{\"A1\":\"=1\"}",
            SpreadsheetCellSaveFormulaTextHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    "=1"
                )
            )
        );
    }

    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/save/formula/{\"A1\":\"=1\",\"A2\":\"=2\"}",
            SpreadsheetCellSaveFormulaTextHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                Maps.of(
                    SpreadsheetSelection.A1,
                    "=1",
                    SpreadsheetSelection.parseCell("A2"),
                    "=2"
                )
            )
        );
    }

    @Test
    public void testUrlFragment() {
        final Map<SpreadsheetCellReference, String> cellToFormulaText = Maps.of(
            SpreadsheetSelection.A1,
            "'ABC"
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveFormulaTextHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToFormulaText
            ),
            "/123/SpreadsheetName456/cell/A1/save/formula/" + marshallMap(cellToFormulaText)
        );
    }

    @Test
    public void testUrlFragment2() {
        final Map<SpreadsheetCellReference, String> cellToFormulaText = Maps.of(
            SpreadsheetSelection.A1,
            "=1+2"
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveFormulaTextHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cellToFormulaText
            ),
            "/123/SpreadsheetName456/cell/A1/save/formula/" + marshallMap(cellToFormulaText)
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Map<SpreadsheetCellReference, String> cellToFormulaText = Maps.of(
            SpreadsheetSelection.A1,
            "=1",
            SpreadsheetSelection.parseCell("A2"),
            "=22",
            SpreadsheetSelection.parseCell("A3"),
            "=333"
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveFormulaTextHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cellToFormulaText
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/formula/" + marshallMap(cellToFormulaText)
        );
    }

    private static String marshallMap(final Map<SpreadsheetCellReference, String> map) {
        return marshall(
            SpreadsheetCellReferenceToFormulaTextMap.with(map)
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentFormula() {
        final Map<SpreadsheetCellReference, String> value = Maps.of(
            CELL,
            "different"
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.cellSaveFormulaText(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveFormulaTextHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                  final SpreadsheetName name,
                                                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveFormulaTextHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Maps.of(
                SpreadsheetSelection.A1,
                "=1+2"
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveFormulaTextHistoryToken> type() {
        return SpreadsheetCellSaveFormulaTextHistoryToken.class;
    }
}
