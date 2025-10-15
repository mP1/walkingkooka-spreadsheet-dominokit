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
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.validation.ValueTypeName;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellValueSaveHistoryTokenTest extends SpreadsheetCellValueHistoryTokenTestCase<SpreadsheetCellValueSaveHistoryToken> {

    private final static ValueTypeName VALUE_TYPE = ValueTypeName.with("number");

    private final static String VALUE = JSON_NODE_MARSHALL_CONTEXT.marshall(
        ExpressionNumberKind.BIG_DECIMAL.create(123.5)
    ).toString();

    @Test
    public void testWithNullValueTypeFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null,
                ""
            )
        );
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                VALUE_TYPE,
                null
            )
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public void testSetSaveValueWithEmptyOptional() {
        this.setSaveValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.DATE,
                "Previous"
            ),
            Optional.empty(),
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.DATE,
                ""
            )
        );
    }

    @Test
    public void testSetSaveValueWithNotEmptyDate() {
        this.setSaveValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.DATE,
                "Previous"
            ),
            Optional.of(
                "\"1999,12,31\""
            ),
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.DATE,
                "\"1999,12,31\""
            )
        );
    }

    @Test
    public void testSetSaveValueWithNotEmptyTime() {
        this.setSaveValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.TIME,
                "Previous"
            ),
            Optional.of(
                "\"12:58:59\""
            ),
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.TIME,
                "\"12:58:59\""
            )
        );
    }

    // setSaveStringValue...............................................................................................

    @Test
    public void testSetSaveStringValueWithEmptyString() {
        this.setSaveValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.DATE,
                "Previous"
            ),
            Optional.empty(),
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.DATE,
                ""
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithNotEmptyStringDate() {
        final String value = "\"1999,12,31\"";

        this.setSaveStringValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.DATE,
                "Previous"
            ),
            value,
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.DATE,
                value
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithNotEmptyStringTime() {
        final String value = JSON_NODE_MARSHALL_CONTEXT.marshall(
            LocalTime.of(12, 58, 59)
        ).toString();

        this.setSaveStringValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.TIME,
                "Previous"
            ),
            value,
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueTypeName.TIME,
                value
            )
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testSetValueWithSame() {
        final SpreadsheetCellValueHistoryToken historyToken = this.createHistoryToken();

        assertSame(
            historyToken,
            historyToken.setValue(historyToken.valueType())
        );
    }

    @Test
    public void testSetValueDifferent() {
        final ValueTypeName valueType = ValueTypeName.DATE;

        this.setValueAndCheck(
            this.createHistoryToken(),
            Optional.of(valueType),
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                valueType
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCellEmptySave() {
        this.urlFragmentAndCheck(
            SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                VALUE_TYPE,
                ""
            ),
            "/123/SpreadsheetName456/cell/A1/value/number/save/"
        );
    }

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/123/SpreadsheetName456/cell/A1/value/number/save/%22123.5%22");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            CELL_RANGE.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/B2:C3/top-left/value/number/save/%22123.5%22"
        );
    }

    @Test
    public void testUrlFragmentCellRangeStar() {
        this.urlFragmentAndCheck(
            SpreadsheetSelection.ALL_CELLS.setAnchor(SpreadsheetViewportAnchor.TOP_LEFT),
            "/123/SpreadsheetName456/cell/*/top-left/value/number/save/%22123.5%22"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/cell/Label123/value/number/save/%22123.5%22"
        );
    }

    // clearAction......................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellValueSelect(
                ID,
                NAME,
                SELECTION,
                VALUE_TYPE
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
                SELECTION
            )
        );
    }

    @Override
    SpreadsheetCellValueSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                            final SpreadsheetName name,
                                                            final AnchoredSpreadsheetSelection selection) {
        return SpreadsheetCellValueSaveHistoryToken.with(
            id,
            name,
            selection,
            VALUE_TYPE,
            VALUE
        );
    }

    @Override
    public Class<SpreadsheetCellValueSaveHistoryToken> type() {
        return SpreadsheetCellValueSaveHistoryToken.class;
    }
}
