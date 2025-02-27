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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetLabelMappingSelectHistoryTokenTest extends SpreadsheetLabelMappingHistoryTokenTestCase2<SpreadsheetLabelMappingSelectHistoryToken> {

    @Test
    public void testLabelMappingReference() {
        this.labelMappingReferenceAndCheck(this.createHistoryToken());
    }

    // setLabelMappingReference............................................................................................

    @Test
    public void testSetLabelMappingReferenceWithCell() {
        this.setLabelMappingReferenceAndCheck(
            SpreadsheetSelection.A1
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

    // label............................................................................................................

    @Test
    public void testLabelNameWhenPresent() {
        this.labelNameAndCheck(
            SpreadsheetLabelMappingSelectHistoryToken.with(
                ID,
                NAME,
                LABEL
            ),
            LABEL
        );
    }

    // setSaveValue.....................................................................................................

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

    // hasUrlFragment...................................................................................................

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            LABEL,
            "/123/SpreadsheetName456/label/Label123"
        );
    }

    // clearAction.....................................................................................................

    @Test
    public void testClearAction() {
        this.clearActionAndCheck();
    }

    // delete...........................................................................................................

    @Test
    public void testDelete() {
        final SpreadsheetLabelMappingHistoryToken token = this.createHistoryToken();
        final Optional<SpreadsheetLabelName> labelName = token.labelName();

        this.deleteAndCheck(
            token,
            HistoryToken.labelMappingDelete(
                token.id(),
                token.name(),
                labelName.get()
            )
        );
    }

    @Test
    public void testSetSaveValueCell() {
        final SpreadsheetLabelMappingHistoryToken token = this.createHistoryToken();
        final Optional<SpreadsheetLabelName> labelName = token.labelName();
        final SpreadsheetCellReference cell = SpreadsheetSelection.parseCell("B2");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            cell.toString(),
            HistoryToken.labelMappingSave(
                token.id(),
                token.name(),
                labelName.get()
                    .setLabelMappingReference(cell)
            )
        );
    }

    @Test
    public void testSetSaveValueCellRange() {
        final SpreadsheetLabelMappingHistoryToken token = this.createHistoryToken();
        final Optional<SpreadsheetLabelName> labelName = token.labelName();
        final SpreadsheetCellRangeReference cells = SpreadsheetSelection.parseCellRange("C3:D4");

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            cells.toString(),
            HistoryToken.labelMappingSave(
                token.id(),
                token.name(),
                labelName.get()
                    .setLabelMappingReference(cells)
            )
        );
    }

    @Override
    SpreadsheetLabelMappingSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                                 final SpreadsheetName name,
                                                                 final SpreadsheetLabelName label) {
        return SpreadsheetLabelMappingSelectHistoryToken.with(
            id,
            name,
            label
        );
    }

    @Override
    public Class<SpreadsheetLabelMappingSelectHistoryToken> type() {
        return SpreadsheetLabelMappingSelectHistoryToken.class;
    }
}
