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

package walkingkooka.spreadsheet.dominokit.label;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenOffsetAndCount;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public final class AppContextSpreadsheetLabelMappingListDialogComponentContextLabelTest implements SpreadsheetLabelMappingListDialogComponentContextTesting<AppContextSpreadsheetLabelMappingListDialogComponentContextLabel> {

    // isMatch..........................................................................................................

    @Test
    public void testIsMatchWithSpreadsheetCellLabelsHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellLabels(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                HistoryTokenOffsetAndCount.EMPTY
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetCellSelectHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor()
            ),
            false
        );
    }

    @Test
    public void testIsMatchWithSpreadsheetLabelMappingListHistoryToken() {
        this.isMatchAndCheck(
            this.createContext(),
            HistoryToken.labelMappingList(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                HistoryTokenOffsetAndCount.EMPTY
            ),
            true
        );
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AppContextSpreadsheetLabelMappingListDialogComponentContextLabel createContext() {
        return AppContextSpreadsheetLabelMappingListDialogComponentContextLabel.with(
            new FakeAppContext()
        );
    }

    @Override
    public Class<AppContextSpreadsheetLabelMappingListDialogComponentContextLabel> type() {
        return AppContextSpreadsheetLabelMappingListDialogComponentContextLabel.class;
    }
}
