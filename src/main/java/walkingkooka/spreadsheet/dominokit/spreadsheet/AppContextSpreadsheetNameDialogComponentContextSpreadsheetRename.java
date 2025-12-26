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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRenameHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRenameSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRenameSelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Objects;
import java.util.Optional;

final class AppContextSpreadsheetNameDialogComponentContextSpreadsheetRename extends AppContextNameDialogComponentContext {

    static AppContextSpreadsheetNameDialogComponentContextSpreadsheetRename with(final AppContext context) {
        return new AppContextSpreadsheetNameDialogComponentContextSpreadsheetRename(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetNameDialogComponentContextSpreadsheetRename(final AppContext context) {
        super(context);
    }

    @Override
    public boolean shouldLoadSpreadsheetMetadata() {
        return false;
    }

    @Override
    public SpreadsheetId spreadsheetId() {
        return this.historyToken().cast(SpreadsheetRenameHistoryToken.class)
            .id();
    }

    @Override
    public Optional<SpreadsheetName> spreadsheetName() {
        return Optional.of(
            this.historyToken()
                .cast(SpreadsheetRenameSelectHistoryToken.class)
                .name()
        );
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetRenameSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetRenameSelectHistoryToken;
    }
}
