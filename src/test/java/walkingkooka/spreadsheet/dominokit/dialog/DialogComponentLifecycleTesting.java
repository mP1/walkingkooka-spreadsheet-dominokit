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

package walkingkooka.spreadsheet.dominokit.dialog;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.HistoryTokenAwareComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSelectHistoryToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public interface DialogComponentLifecycleTesting<T extends DialogComponentLifecycle> extends HistoryTokenAwareComponentLifecycleTesting<T> {

    @Test
    default void testShouldIgnoreWithSpreadsheetCellSelectHistoryToken() {
        final SpreadsheetCellSelectHistoryToken historyToken = HistoryToken.cellSelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        this.shouldIgnoreAndCheck(
            this.createSpreadsheetDialogComponentLifecycle(historyToken),
            historyToken,
            false
        );
    }

    @Test
    default void testIsMatchWithSpreadsheetCellSelectHistoryToken() {
        final SpreadsheetCellSelectHistoryToken historyToken = HistoryToken.cellSelect(
            SPREADSHEET_ID,
            SPREADSHEET_NAME,
            SpreadsheetSelection.A1.setDefaultAnchor()
        );

        this.isMatchAndCheck(
            this.createSpreadsheetDialogComponentLifecycle(historyToken),
            historyToken,
            false
        );
    }

    T createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken);
}
