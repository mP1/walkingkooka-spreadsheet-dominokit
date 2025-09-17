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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellReferenceListHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellReferenceListHistoryToken> {

    // with.............................................................................................................

    @Test
    public void testWithNullReferencesFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null
            )
        );
    }

    // count............................................................................................................

    @Test
    public void testCount() {
        this.countAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public void testCount2() {
        final int count = 123;

        this.countAndCheck(
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(count)
                )
            ),
            count
        );
    }

    // setCount.........................................................................................................

    @Test
    public void testSetCountWithSame() {
        final SpreadsheetCellReferenceListHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setCount(historyToken.count())
        );
    }

    @Test
    public void testSetCountWithDifferent() {
        final OptionalInt count = OptionalInt.of(123);

        this.setCountAndCheck(
            this.createHistoryToken(),
            count,
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY.setCount(count)
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

    // offset...........................................................................................................

    @Test
    public void testOffset() {
        this.offsetAndCheck(
            this.createHistoryToken()
        );
    }

    @Test
    public void testOffset2() {
        final int offset = 123;

        this.offsetAndCheck(
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(offset)
                )
            ),
            offset
        );
    }

    // setOffset........................................................................................................

    @Test
    public void testSetOffsetWithSame() {
        final SpreadsheetCellReferenceListHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setOffset(historyToken.offset())
        );
    }

    @Test
    public void testSetOffsetWithDifferent() {
        final OptionalInt offset = OptionalInt.of(456);

        this.setOffsetAndCheck(
            this.createHistoryToken(),
            offset,
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY.setOffset(offset)
            )
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/references");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/references"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/references"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/references"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/references",
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseCellOffset() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/references/offset/123",
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(123)
                )
            )
        );
    }

    @Test
    public void testParseCellCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/references/count/123",
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY.setCount(
                    OptionalInt.of(123)
                )
            )
        );
    }

    @Test
    public void testParseCellOffsetAndCount() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/references/offset/123/count/456",
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY.setOffset(
                    OptionalInt.of(123)
                ).setCount(
                    OptionalInt.of(456)
                )
            )
        );
    }

    @Test
    public void testParseCellRange() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3/references",
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL_RANGE.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseCellRangeAndAnchor() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3/references",
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                CELL_RANGE.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseCellRangeStar() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/*/references",
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.ALL_CELLS.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    @Test
    public void testParseCellRangeStarAndAnchor() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/*/bottom-right/references",
            SpreadsheetCellReferenceListHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.BOTTOM_RIGHT),
                HistoryTokenOffsetAndCount.EMPTY
            )
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
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
            this.createHistoryToken()
        );
    }

    // setSaveStringValue.....................................................................................................

    @Test
    public void testSetSaveValueWithDifferentCell() {
        final Set<SpreadsheetCell> value = Sets.of(
            CELL.setFormula(SpreadsheetFormula.EMPTY.setText("different"))
        );

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value)
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
            Optional.of(value)
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
            Optional.of(value)
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
            Optional.of(value)
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
            Optional.of(value)
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

    // patternKind......................................................................................................

    @Test
    public void testPatternKind() {
        this.patternKindAndCheck(
            this.createHistoryToken()
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
    SpreadsheetCellReferenceListHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                final SpreadsheetName name,
                                                                final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellReferenceListHistoryToken.with(
            id,
            name,
            selection,
            HistoryTokenOffsetAndCount.EMPTY
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellReferenceListHistoryToken> type() {
        return SpreadsheetCellReferenceListHistoryToken.class;
    }
}
