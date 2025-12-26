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
import walkingkooka.spreadsheet.dominokit.cell.value.SpreadsheetCellValueDialogComponent;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportAnchor;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.validation.ValueType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellValueSaveHistoryTokenTest extends SpreadsheetCellValueHistoryTokenTestCase<SpreadsheetCellValueSaveHistoryToken> {

    private final static ValueType VALUE_TYPE = ValueType.with("number");

    private final static Optional<?> VALUE = Optional.of(
        ExpressionNumberKind.BIG_DECIMAL.create(123.5)
    );

    @Test
    public void testWithNullValueTypeFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                null,
                Optional.empty()
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
        final Optional<?> value = Optional.empty();

        this.setSaveValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE,
                Optional.of("Previous")
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

    @Test
    public void testSetSaveValueWithNotEmptyDate() {
        final Optional<?> value = Optional.of(
            LocalDate.of(
                1999,
                12,
                31
            )
        );

        this.setSaveValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.DATE,
                Optional.of("Previous")
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

    @Test
    public void testSetSaveValueWithNotEmptyTime() {
        final Optional<?> value = Optional.of(
            LocalTime.of(
                12,
                58,
                59
            )
        );

        this.setSaveValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.TIME,
                Optional.of("Previous")
            ),
            value,
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.TIME,
                value
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
                ValueType.DATE,
                Optional.of("Previous")
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
    public void testSetSaveStringValueWithNotEmptyText() {
        final String value = "Hello123";

        this.setSaveStringValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.TEXT,
                Optional.of("Previous")
            ),
            JsonNode.string(value)
                .toString(),
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.TEXT,
                Optional.of(value)
            )
        );
    }

    @Test
    public void testSetSaveStringValueWithNotEmptyNumber() {
        final String value = "1.25";

        this.setSaveStringValueAndCheck(
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.NUMBER,
                Optional.of("Previous")
            ),
            JsonNode.string(value)
                .toString(),
            HistoryToken.cellValueSave(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                ValueType.NUMBER,
                Optional.of(
                    EXPRESSION_NUMBER_KIND.create(1.25)
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
            historyToken.setValue(historyToken.valueType())
        );
    }

    @Test
    public void testSetValueDifferent() {
        final ValueType valueType = ValueType.DATE;

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
                Optional.empty()
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

    // parse............................................................................................................

    @Test
    public void testParseDateToday() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/date/save/today",
            SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetValueType.DATE,
                Optional.of(SpreadsheetCellValueDialogComponent.TODAY_TEXT)
            )
        );
    }

    @Test
    public void testParseDateTimeToday() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/date-time/save/now",
            SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetValueType.DATE_TIME,
                Optional.of(SpreadsheetCellValueDialogComponent.NOW_TEXT)
            )
        );
    }

    @Test
    public void testParseTimeToday() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/time/save/now",
            SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetValueType.TIME,
                Optional.of(SpreadsheetCellValueDialogComponent.NOW_TEXT)
            )
        );
    }

    @Test
    public void testParseNumber() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/number/save/\"1.5\"",
            SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetValueType.NUMBER,
                Optional.of(
                    EXPRESSION_NUMBER_KIND.create(1.5)
                )
            )
        );
    }

    @Test
    public void testParseText() {
        this.parseAndCheck(
            "/123/SpreadsheetName456/cell/A1/value/text/save/\"Hello\"",
            SpreadsheetCellValueSaveHistoryToken.with(
                ID,
                NAME,
                CELL.setDefaultAnchor(),
                SpreadsheetValueType.TEXT,
                Optional.of("Hello")
            )
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
