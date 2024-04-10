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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetname;

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListRenameHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListRenameSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetListRenameSelectHistoryToken;

import java.util.Objects;
import java.util.Optional;

final class BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename extends BasicSpreadsheetNameDialogComponentContext {

    static BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename with(final AppContext context) {
        return new BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename(
                Objects.requireNonNull(context, "context")
        );
    }

    private BasicSpreadsheetNameDialogComponentContextSpreadsheetListRename(final AppContext context) {
        super(context);
    }

    @Override
    public boolean shouldLoadSpreadsheetMetadata() {
        return true; // need to load SpreadsheetMetadata to get SpreadsheetName when opened
    }

    @Override
    public SpreadsheetId spreadsheetId() {
        return this.historyToken().cast(SpreadsheetListRenameHistoryToken.class)
                .id();
    }

    @Override
    public Optional<SpreadsheetName> spreadsheetName() {
        return Optional.empty();
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetListRenameSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetListRenameSelectHistoryToken;
    }
}
