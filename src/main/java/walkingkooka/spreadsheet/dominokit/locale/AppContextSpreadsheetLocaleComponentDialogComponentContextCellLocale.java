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

package walkingkooka.spreadsheet.dominokit.locale;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellLocaleSelectHistoryToken;

import java.util.Objects;

final class AppContextSpreadsheetLocaleComponentDialogComponentContextCellLocale extends AppContextSpreadsheetLocaleComponentDialogComponentContext {

    static AppContextSpreadsheetLocaleComponentDialogComponentContextCellLocale with(final AppContext context) {
        return new AppContextSpreadsheetLocaleComponentDialogComponentContextCellLocale(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetLocaleComponentDialogComponentContextCellLocale(final AppContext context) {
        super(context);
    }

    // ComponentLifecycleMatcher........................................................................................

    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/delete
    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/save/Locale
    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false == this.isMatch(token);
    }

    // /spreadsheet/1/SpreadsheetName/cell/A1/Locale/save
    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellLocaleSelectHistoryToken;
    }
}
