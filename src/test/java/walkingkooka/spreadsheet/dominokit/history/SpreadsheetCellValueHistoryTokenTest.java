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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;
import walkingkooka.validation.ValidationValueTypeName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellValueHistoryTokenTest extends SpreadsheetCellHistoryTokenTestCase<SpreadsheetCellValueHistoryToken> {

    private final static Optional<ValidationValueTypeName> VALUE_TYPE = Optional.of(
        ValidationValueTypeName.with("hello-value-type")
    );

    // with.............................................................................................................

    @Test
    public void testWithNullValueTypeFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null
            )
        );
    }

    // valueType........................................................................................................

    @Test
    public void testValueType() {
        this.checkEquals(
            this.createHistoryToken()
                .valueType(),
            VALUE_TYPE
        );
    }

    // setValueType.....................................................................................................

    @Test
    public void testSetValueTypeWithSame() {
        final SpreadsheetCellValueHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setValueType(
                historyToken.valueType()
            )
        );
    }

    @Test
    public void testSetValueTypeWithDifferent() {
        final SpreadsheetCellValueHistoryToken historyToken = this.createHistoryToken();

        final Optional<ValidationValueTypeName> different = Optional.of(
            ValidationValueTypeName.with("different")
        );

        this.checkEquals(
            SpreadsheetCellValueHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                different
            ),
            historyToken.setValueType(different)
        );
    }

    // UrlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
            SpreadsheetCellValueHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                VALUE_TYPE
            ),
            "/123/SpreadsheetName456/cell/A1/value/hello-value-type"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/value/hello-value-type"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/value/hello-value-type"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/value/hello-value-type"
        );
    }

    @Test
    public void testUrlFragmentCellMissingValueType() {
        this.urlFragmentAndCheck(
            SpreadsheetCellValueHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            ),
            "/123/SpreadsheetName456/cell/A1/value"
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value",
            SpreadsheetCellValueHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseCellRange() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3/value",
            SpreadsheetCellValueHistoryToken.with(
                ID,
                NAME,
                RANGE.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseLabel() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/Label123/value",
            SpreadsheetCellValueHistoryToken.with(
                ID,
                NAME,
                LABEL.setDefaultAnchor(),
                Optional.empty()
            )
        );
    }

    @Test
    public void testParseCellAndInvalidValueType() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/!hello-value-type",
            HistoryToken.cellSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            )
        );
    }

    @Test
    public void testParseCellAndValueType() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/hello-value-type",
            SpreadsheetCellValueHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                VALUE_TYPE
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

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValue() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            VALUE_TYPE
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
    SpreadsheetCellValueHistoryToken createHistoryToken(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellValueHistoryToken.with(
            id,
            name,
            selection,
            VALUE_TYPE
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellValueHistoryToken> type() {
        return SpreadsheetCellValueHistoryToken.class;
    }
}
