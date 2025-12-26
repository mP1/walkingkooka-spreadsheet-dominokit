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
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetLabelMappingSaveHistoryTokenTest extends SpreadsheetLabelMappingHistoryTokenTestCase2<SpreadsheetLabelMappingSaveHistoryToken> {

    private final static SpreadsheetExpressionReference REFERENCE = CELL;

    @Test
    @Override
    public void testLabelMappingReference() {
        this.labelMappingReferenceAndCheck(
            this.createHistoryToken(),
            REFERENCE
        );
    }

    // setLabelMappingReference............................................................................................

    @Test
    public void testSetLabelMappingReferenceWithCell() {
        this.setLabelMappingReferenceAndCheck(
            SpreadsheetSelection.parseCell("Z99")
        );
    }

    @Test
    public void testSetLabelMappingReferenceWithCellRange() {
        this.setLabelMappingReferenceAndCheck(
            SpreadsheetSelection.parseCellRange("B2:C3")
        );
    }

    @Test
    public void testSetLabelMappingReferenceWithLabel() {
        this.setLabelMappingReferenceAndCheck(
            SpreadsheetSelection.labelName("Label999")
        );
    }

    // labelName........................................................................................................

    @Test
    public void testLabelName() {
        this.labelNameAndCheck(
            this.createHistoryToken(),
            LABEL
        );
    }

    // setSaveStringValue.....................................................................................................

    @Test
    public void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public void testSetSaveValueWithDifferentCell() {
        final SpreadsheetExpressionReference value = SpreadsheetSelection.parseCell("Z9");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.labelMappingSave(
                ID,
                NAME,
                LABEL.setLabelMappingReference(value)
            )
        );
    }

    @Test
    public void testSetSaveValueWithDifferentCellRange() {
        final SpreadsheetExpressionReference value = SpreadsheetSelection.parseCellRange("A1:Z9");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.labelMappingSave(
                ID,
                NAME,
                LABEL.setLabelMappingReference(value)
            )
        );
    }

    @Test
    public void testSetSaveValueWithDifferentLabel() {
        final SpreadsheetExpressionReference value = SpreadsheetSelection.labelName("Different");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            Optional.of(value),
            HistoryToken.labelMappingSave(
                ID,
                NAME,
                LABEL.setLabelMappingReference(value)
            )
        );
    }

    // urlFragment......................................................................................................

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/label/Label123/save/A1"
        );
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
            SpreadsheetLabelMappingSaveHistoryToken.with(
                ID,
                NAME,
                LABEL.setLabelMappingReference(SpreadsheetSelection.parseCellRange("B2:C3"))
            ),
            "/123/SpreadsheetName456/label/Label123/save/B2:C3"
        );
    }

    @Test
    public void testClearAction() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.labelMappingSelect(
                ID,
                NAME,
                LABEL
            )
        );
    }

    @Override
    SpreadsheetLabelMappingSaveHistoryToken createHistoryToken(final SpreadsheetId id,
                                                               final SpreadsheetName name,
                                                               final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingSaveHistoryToken.with(
            id,
            name,
            label.setLabelMappingReference(REFERENCE)
        );
    }

    @Override
    public Class<SpreadsheetLabelMappingSaveHistoryToken> type() {
        return SpreadsheetLabelMappingSaveHistoryToken.class;
    }
}
