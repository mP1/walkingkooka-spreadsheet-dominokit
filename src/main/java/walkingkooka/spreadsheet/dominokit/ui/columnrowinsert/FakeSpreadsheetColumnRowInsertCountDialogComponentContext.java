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

package walkingkooka.spreadsheet.dominokit.ui.columnrowinsert;

import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.dialog.FakeSpreadsheetDialogComponentContext;

public class FakeSpreadsheetColumnRowInsertCountDialogComponentContext extends FakeSpreadsheetDialogComponentContext implements SpreadsheetColumnRowInsertCountDialogComponentContext {
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String dialogTitle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void save(final int count) {
        throw new UnsupportedOperationException();
    }
}
