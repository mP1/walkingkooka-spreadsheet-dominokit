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
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public abstract class SpreadsheetCellLabelHistoryTokenTestCase<T extends SpreadsheetCellLabelHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T> {

    SpreadsheetCellLabelHistoryTokenTestCase() {
        super();
    }

    // delete...........................................................................................................

    @Test
    public final void testDelete() {
        this.deleteAndCheck(
            this.createHistoryToken()
        );
    }

    // close............................................................................................................

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    // labelMappingReference............................................................................................

    @Test
    public final void testLabelMappingReferenceWhenCell() {
        this.labelMappingReferenceAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            ),
            CELL
        );
    }

    @Test
    public final void testLabelMappingReferenceWhenCellRange() {
        this.labelMappingReferenceAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                CELL_RANGE.setDefaultAnchor()
            ),
            CELL_RANGE
        );
    }

    @Test
    public final void testLabelMappingReferenceWhenLabel() {
        this.labelMappingReferenceAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                LABEL.setDefaultAnchor()
            ),
            LABEL
        );
    }

    // setLabelMappingReference............................................................................................

    @Test
    public final void testSetLabelMappingReferenceWhenCellWithSameCell() {
        this.setLabelMappingReferenceAndCheck(CELL);
    }

    @Test
    public final void testSetLabelMappingReferenceWhenCellRangeWithSameCellRange() {
        this.setLabelMappingReferenceAndCheck(CELL_RANGE);
    }

    @Test
    public final void testSetLabelMappingReferenceWhenLabelWithSameLabel() {
        this.setLabelMappingReferenceAndCheck(
            LABEL
        );
    }

    abstract void setLabelMappingReferenceAndCheck(final SpreadsheetExpressionReference target);

    @Test
    public final void testSetLabelMappingReferenceWhenCellWithDifferentCell() {
        this.setLabelMappingReferenceAndCheck(
            CELL,
            CELL.addColumn(10)
        );
    }

    @Test
    public final void testSetLabelMappingReferenceWhenCellRangeWithDifferentCellRange() {
        this.setLabelMappingReferenceAndCheck(
            CELL_RANGE,
            CELL_RANGE.add(
                10,
                10
            )
        );
    }

    @Test
    public final void testSetLabelMappingReferenceWhenLabelWithDifferentLabel() {
        this.setLabelMappingReferenceAndCheck(
            LABEL,
            SpreadsheetSelection.labelName("Different")
        );
    }

    abstract void setLabelMappingReferenceAndCheck(final SpreadsheetExpressionReference selection,
                                                   final SpreadsheetExpressionReference target);

    // navigation.......................................................................................................

    @Test
    public final void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public final void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public final void testSetSaveValueWithEmptyLabelName() {
        this.setSaveValueFails(
            Optional.empty()
        );
    }

    @Test
    public final void testSetSaveValueWithDifferentLabelName() {
        final SpreadsheetLabelName value = SpreadsheetSelection.labelName("Different");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.cellLabelSave(
                ID,
                NAME,
                SELECTION,
                value
            )
        );
    }
}
