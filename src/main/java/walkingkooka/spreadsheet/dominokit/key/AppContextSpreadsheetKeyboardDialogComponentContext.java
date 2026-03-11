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

package walkingkooka.spreadsheet.dominokit.key;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.RefreshContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportComponentKeyBindings;

import java.util.Objects;

abstract class AppContextSpreadsheetKeyboardDialogComponentContext implements SpreadsheetKeyboardDialogComponentContext,
    RefreshContextDelegator {

    AppContextSpreadsheetKeyboardDialogComponentContext(final AppContext context) {
        super();
        this.context = Objects.requireNonNull(context, "context");
    }

    // HasSpreadsheetViewportComponentKeyBindings.......................................................................

    @Override
    public final SpreadsheetViewportComponentKeyBindings spreadsheetViewportComponentKeyBindings() {
        return context.spreadsheetViewportComponentKeyBindings();
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public final boolean shouldIgnore(final HistoryToken token) {
        return token.isSave();
    }

    // RefreshContextDelegator..........................................................................................

    @Override
    public final RefreshContext refreshContext() {
        return this.context;
    }

    private final AppContext context;

    // toString.........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
