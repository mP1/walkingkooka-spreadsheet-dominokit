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

import java.util.Optional;

public final class SpreadsheetCellLabelSelectHistoryTokenTest extends SpreadsheetCellLabelHistoryTokenTestCase<SpreadsheetCellLabelSelectHistoryToken> {

    @Test
    public void testSetLabelName() {
        this.setLabelNameAndCheck(
            this.createHistoryToken(),
            Optional.of(LABEL),
            HistoryToken.cellLabelSave(
                ID,
                NAME,
                SELECTION,
                LABEL
            )
        );
    }

    // clear...........................................................................................................

    @Test
    public void testClear() {
        this.clearActionAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellLabelSelect(
                ID,
                NAME,
                SELECTION
            )
        );
    }

    // saveValue........................................................................................................

    @Test
    public void testSetSaveValueWithEmpty() {
        this.saveValueAndCheck(
            this.createHistoryToken(),
            ""
        );
    }

    @Test
    public void testSetSaveValueNonEmpty() {
        final String labelName = "Hello";

        this.saveValueAndCheck(
            this.createHistoryToken(),
            labelName,
            HistoryToken.cellLabelSave(
                ID,
                NAME,
                SELECTION,
                SpreadsheetSelection.labelName(labelName)
            )
        );
    }

    @Test
    public void testUrlFragment() {
        this.urlFragmentAndCheck(
            "/123/SpreadsheetName456/cell/A1/label"
        );
    }

    @Override
    SpreadsheetCellLabelSelectHistoryToken createHistoryToken(final SpreadsheetId id,
                                                              final SpreadsheetName name,
                                                              final AnchoredSpreadsheetSelection anchoredSelection) {
        return SpreadsheetCellLabelSelectHistoryToken.with(
            id,
            name,
            anchoredSelection
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellLabelSelectHistoryToken> type() {
        return SpreadsheetCellLabelSelectHistoryToken.class;
    }
}
