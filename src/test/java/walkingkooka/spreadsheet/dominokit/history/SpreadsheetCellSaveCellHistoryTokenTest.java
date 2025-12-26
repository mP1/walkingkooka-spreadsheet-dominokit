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
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.engine.collection.SpreadsheetCellSet;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellSaveCellHistoryTokenTest extends SpreadsheetCellSaveHistoryTokenTestCase<SpreadsheetCellSaveCellHistoryToken> {

    @Test
    public void testWithSaveFormulasOutsideRangeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetCellSaveCellHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Sets.of(
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY.setText("=1+2")
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
            () -> SpreadsheetCellSaveCellHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A2:A3")
                    .setDefaultAnchor(),
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                    SpreadsheetSelection.parseCell("A3")
                        .setFormula(SpreadsheetFormula.EMPTY),
                    SpreadsheetSelection.parseCell("A4")
                        .setFormula(SpreadsheetFormula.EMPTY)
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
            "/123/SpreadsheetName456/cell/A1/save/cell",
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
            "/123/SpreadsheetName456/cell/A1/save/cell/{\"A1\":{\"formula\":{\"text\":\"\"}}}",
            SpreadsheetCellSaveCellHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY)
                )
            )
        );
    }

    @Test
    public void testParseSeveralCells() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1:A2/save/cell/{\"A1\":{\"formula\":{}},\"A2\":{\"formula\":{\"text\": \"=2\"}}}",
            SpreadsheetCellSaveCellHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A2")
                    .setDefaultAnchor(),
                Sets.of(
                    SpreadsheetSelection.A1.setFormula(SpreadsheetFormula.EMPTY),
                    SpreadsheetSelection.parseCell("A2")
                        .setFormula(
                            SpreadsheetFormula.EMPTY.setText("=2")
                        )
                )
            )
        );
    }

    @Test
    public void testUrlFragment() {
        final Set<SpreadsheetCell> cells = Sets.of(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("'A")
            )
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCellHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cells
            ),
            "/123/SpreadsheetName456/cell/A1/save/cell/" +
                marshall(
                    SpreadsheetCellSet.with(cells)
                )
        );
    }

    @Test
    public void testUrlFragment2() {
        final Set<SpreadsheetCell> cells = Sets.of(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=1+2")
            )
        );
        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCellHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                cells
            ),
            "/123/SpreadsheetName456/cell/A1/save/cell/" +
                marshall(
                    SpreadsheetCellSet.with(cells)
                )
        );
    }

    @Test
    public void testUrlFragmentWithMultipleCells() {
        final Set<SpreadsheetCell> cells = Sets.of(
            SpreadsheetSelection.A1.setFormula(
                SpreadsheetFormula.EMPTY.setText("=1")
            ),
            SpreadsheetSelection.parseCell("A2")
                .setFormula(
                    SpreadsheetFormula.EMPTY.setText("=22")
                ),
            SpreadsheetSelection.parseCell("A3")
                .setFormula(
                    SpreadsheetFormula.EMPTY.setText("=333")
                )
        );

        this.urlFragmentAndCheck(
            SpreadsheetCellSaveCellHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.parseCellRange("A1:A3")
                    .setDefaultAnchor(),
                cells
            ),
            "/123/SpreadsheetName456/cell/A1:A3/bottom-right/save/cell/" +
                marshall(
                    SpreadsheetCellSet.with(cells)
                )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentCell() {
        final Set<SpreadsheetCell> value = Sets.of(
            CELL
                .setFormula(SpreadsheetFormula.EMPTY.setText("different"))
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            SpreadsheetCellSaveCellHistoryToken.with(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Override
    SpreadsheetCellSaveCellHistoryToken createHistoryToken(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellSaveCellHistoryToken.with(
            id,
            name,
            anchoredSelection,
            Sets.of(
                SpreadsheetSelection.A1.setFormula(
                    SpreadsheetFormula.EMPTY.setText("=1+2")
                )
            )
        );
    }

    @Override
    public Class<SpreadsheetCellSaveCellHistoryToken> type() {
        return SpreadsheetCellSaveCellHistoryToken.class;
    }
}
