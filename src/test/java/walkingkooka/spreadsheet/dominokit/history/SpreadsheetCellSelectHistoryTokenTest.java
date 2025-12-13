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
import walkingkooka.collect.set.Sets;
import walkingkooka.color.Color;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;

public final class SpreadsheetCellSelectHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellSelectHistoryToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseCellRange() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                CELL_RANGE.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseCellRangeAndAnchor() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                CELL_RANGE.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseCellRangeStar() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/*",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.ALL_CELLS.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseCellRangeStarAndAnchor() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/*/bottom-right",
            SpreadsheetCellSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
            )
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // freezeOrEmpty....................................................................................................

    @Test
    public void testFreezeOrEmptyCellInvalid() {
        this.freezeOrEmptyAndCheck(
            SpreadsheetSelection.parseCell("B2")
                .setDefaultAnchor()
        );
    }

    @Test
    public void testFreezeOrEmptyCellRangeInvalid() {
        this.freezeOrEmptyAndCheck(
            SpreadsheetSelection.parseCellRange("C3:D4")
                .setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
        );
    }

    // Formatter........................................................................................................

    @Test
    public void testFormatter() {
        this.formatterAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellFormatterSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentCell() {
        final Set<SpreadsheetCell> value = Sets.of(
            CELL.setFormula(SpreadsheetFormula.EMPTY.setText("different"))
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.cellSaveCell(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

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
            HistoryToken.cellSaveFormatter(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

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
            HistoryToken.cellSaveParser(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Test
    public void testSetSaveValueWithDifferentStyle() {
        final Map<SpreadsheetCellReference, TextStyle> value = Maps.of(
            CELL,
            TextStyle.EMPTY.set(
                TextStylePropertyName.COLOR,
                Color.parse("#999")
            )
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.cellSaveStyle(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }

    @Test
    public void testSetSaveValueWithOptionalEmpty() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.empty()
        );
    }

    // setLabel.........................................................................................................

    @Test
    public void testSetLabelName() {
        this.setLabelNameAndCheck(
            this.createHistoryToken(),
            LABEL,
            HistoryToken.labelMappingSelect(
                ID,
                NAME,
                LABEL
            )
        );
    }

    // save.........................................................................................................

    @Test
    public void testSetSaveValue() {
        final AnchoredSpreadsheetSelection selection = CELL.setDefaultAnchor();
        final String formulaText = "=1";
        final HistoryToken historyToken = HistoryToken.cellSelect(ID, NAME, selection);

        assertSame(
            historyToken.setSaveStringValue(formulaText),
            historyToken
        );
    }

    // unfreezeOrEmpty..................................................................................................

    @Test
    public void testUnfreezeOrEmptyCellInvalid() {
        this.unfreezeOrEmptyAndCheck(
            SpreadsheetSelection.parseCell("B2")
                .setDefaultAnchor()
        );
    }

    @Test
    public void testUnfreezeOrEmptyCellRangeInvalid() {
        this.unfreezeOrEmptyAndCheck(
            SpreadsheetSelection.parseCellRange("C3:D4")
                .setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT)
        );
    }

    @Override
    SpreadsheetCellSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellSelectHistoryToken.with(
            id,
            name,
            selection
        );
    }

    @Override
    public Class<SpreadsheetCellSelectHistoryToken> type() {
        return SpreadsheetCellSelectHistoryToken.class;
    }
}
