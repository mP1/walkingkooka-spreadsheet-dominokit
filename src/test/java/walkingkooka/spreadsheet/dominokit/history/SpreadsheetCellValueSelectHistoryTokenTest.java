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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.validation.ValueType;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellValueSelectHistoryTokenTest extends SpreadsheetCellValueHistoryTokenTestCase<SpreadsheetCellValueSelectHistoryToken> {

    private final static ValueType VALUE_TYPE = ValueType.TEXT;

    // with.............................................................................................................

    @Test
    public void testWithNullValueTypeFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueSelectHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null
            )
        );
    }

    // value............................................................................................................

    @Test
    public void testValueType() {
        this.checkEquals(
            this.createHistoryToken()
                .valueType(),
            Optional.of(VALUE_TYPE)
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithEmptyOptional() {
        this.setSaveValueAndCheck(
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE
            ),
            Optional.empty(),
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE,
                Optional.empty()
            )
        );
    }

    @Test
    public void testSetSaveValueWithNotEmptyDate() {
        final Optional<LocalDate> value = Optional.of(
            LocalDate.of(1999, 12, 31)
        );

        this.setSaveValueAndCheck(
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE
            ),
            value,
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE,
                value
            )
        );
    }

    // setSaveStringValue...............................................................................................

    @Test
    public void testSetSaveStringValueWithEmptyString() {
        this.setSaveValueAndCheck(
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE
            ),
            Optional.empty(),
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE,
                Optional.empty()
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithNotEmptyString() {
        this.setSaveStringValueAndCheck(
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE
            ),
            "\"1999-12-31\"",
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE,
                Optional.of(
                    LocalDate.of(1999, 12, 31)
                )
            )
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValueWithSame() {
        final SpreadsheetCellValueHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setValue(
                historyToken.valueType()
            )
        );
    }

    @Test
    public void testSetValueWithDifferent() {
        final SpreadsheetCellValueHistoryToken historyToken = this.createHistoryToken();

        final ValueType different = ValueType.with("different");

        this.setValueAndCheck(
            historyToken,
            Optional.of(different),
            SpreadsheetCellValueSelectHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                different
            )
        );
    }

    // parse............................................................................................................

    @Test
    public void testParseCell() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/text",
            SpreadsheetCellValueSelectHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.TEXT
            )
        );
    }

    @Test
    public void testParseCellRange() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/B2:C3/bottom-right/value/text",
            SpreadsheetCellValueSelectHistoryToken.with(
                ID,
                NAME,
                CELL_RANGE.setDefaultAnchor(),
                ValueType.TEXT
            )
        );
    }

    @Test
    public void testParseCellRangeStar() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/*/bottom-right/value/text",
            SpreadsheetCellValueSelectHistoryToken.with(
                ID,
                NAME,
                SpreadsheetSelection.ALL_CELLS.setDefaultAnchor(),
                ValueType.TEXT
            )
        );
    }

    @Test
    public void testParseLabel() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/Label123/value/text",
            SpreadsheetCellValueSelectHistoryToken.with(
                ID,
                NAME,
                LABEL.setDefaultAnchor(),
                ValueType.TEXT
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/value/text");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/value/text"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/value/text"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/value/text"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // close............................................................................................................

    @Test
    public void testClose() {
        this.closeAndCheck(
            HistoryToken.cellSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    @Override
    SpreadsheetCellValueSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                              final SpreadsheetName name,
                                                              final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellValueSelectHistoryToken.with(
            id,
            name,
            selection,
            VALUE_TYPE
        );
    }

    @Override
    public Class<SpreadsheetCellValueSelectHistoryToken> type() {
        return SpreadsheetCellValueSelectHistoryToken.class;
    }
}
