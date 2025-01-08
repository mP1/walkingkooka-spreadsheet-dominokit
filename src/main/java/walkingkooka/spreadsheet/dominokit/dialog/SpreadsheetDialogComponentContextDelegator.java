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

import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContextDelegator;

public interface SpreadsheetDialogComponentContextDelegator extends SpreadsheetDialogComponentContext,
    HistoryTokenContextDelegator,
    LoggingContextDelegator {

    @Override
    default HistoryTokenContext historyTokenContext() {
        return this.spreadsheetDialogComponentContext();
    }

    @Override
    default LoggingContext loggingContext() {
        return this.spreadsheetDialogComponentContext();
    }

    SpreadsheetDialogComponentContext spreadsheetDialogComponentContext();
}
