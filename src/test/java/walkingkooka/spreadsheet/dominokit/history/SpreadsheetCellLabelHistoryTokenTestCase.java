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

    // labelMappingTarget...............................................................................................

    @Test
    public final void testLabelMappingTargetWhenCell() {
        this.labelMappingTargetAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                CELL.setDefaultAnchor()
            ),
            CELL
        );
    }

    @Test
    public final void testLabelMappingTargetWhenCellRange() {
        this.labelMappingTargetAndCheck(
            this.createHistoryToken(
                ID,
                NAME,
                RANGE.setDefaultAnchor()
            ),
            RANGE
        );
    }

    @Test
    public final void testLabelMappingTargetWhenLabel() {
        this.labelMappingTargetAndCheck(
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
    public final void testsetLabelMappingReferenceWhenCellWithSameCell() {
        this.setLabelMappingReferenceAndCheck(CELL);
    }

    @Test
    public final void testsetLabelMappingReferenceWhenCellRangeWithSameCellRange() {
        this.setLabelMappingReferenceAndCheck(RANGE);
    }

    @Test
    public final void testsetLabelMappingReferenceWhenLabelWithSameLabel() {
        this.setLabelMappingReferenceAndCheck(
            LABEL
        );
    }

    abstract void setLabelMappingReferenceAndCheck(final SpreadsheetExpressionReference target);

    @Test
    public final void testsetLabelMappingReferenceWhenCellWithDifferentCell() {
        this.setLabelMappingReferenceAndCheck(
            CELL,
            CELL.addColumn(10)
        );
    }

    @Test
    public final void testsetLabelMappingReferenceWhenCellRangeWithDifferentCellRange() {
        this.setLabelMappingReferenceAndCheck(
            RANGE,
            RANGE.add(
                10,
                10
            )
        );
    }

    @Test
    public final void testsetLabelMappingReferenceWhenLabelWithDifferentLabel() {
        this.setLabelMappingReferenceAndCheck(
            LABEL,
            SpreadsheetSelection.labelName("Different")
        );
    }

    abstract void setLabelMappingReferenceAndCheck(final SpreadsheetExpressionReference selection,
                                                final SpreadsheetExpressionReference target);

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
