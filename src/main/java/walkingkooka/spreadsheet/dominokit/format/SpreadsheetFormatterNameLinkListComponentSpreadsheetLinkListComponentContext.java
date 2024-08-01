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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.spreadsheet.dominokit.SpreadsheetLinkListComponentContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;

final class SpreadsheetFormatterNameLinkListComponentSpreadsheetLinkListComponentContext implements SpreadsheetLinkListComponentContext,
        HistoryTokenContextDelegator {

    static SpreadsheetFormatterNameLinkListComponentSpreadsheetLinkListComponentContext with(final HistoryTokenContext context) {
        return new SpreadsheetFormatterNameLinkListComponentSpreadsheetLinkListComponentContext(context);
    }

    private SpreadsheetFormatterNameLinkListComponentSpreadsheetLinkListComponentContext(final HistoryTokenContext context) {
        this.context = context;
    }

    @Override
    public String saveText(final String text) {
        return text; // will be a SpreadsheetFormatterName
    }

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    private final HistoryTokenContext context;
}
