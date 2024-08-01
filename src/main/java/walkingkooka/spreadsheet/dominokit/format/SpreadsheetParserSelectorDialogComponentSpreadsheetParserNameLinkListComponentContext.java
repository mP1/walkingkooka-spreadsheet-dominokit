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

import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProviderDelegator;

import java.util.Optional;

final class SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext implements SpreadsheetParserNameLinkListComponentContext,
        HistoryTokenContextDelegator,
        SpreadsheetParserProviderDelegator {

    static SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext with(final HistoryTokenContext historyTokenContext,
                                                                                                      final SpreadsheetParserProvider spreadsheetParserProvider,
                                                                                                      final Optional<SpreadsheetParserName> parserName) {
        return new SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext(
                historyTokenContext,
                spreadsheetParserProvider,
                parserName
        );
    }

    private SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext(final HistoryTokenContext historyTokenContext,
                                                                                                  final SpreadsheetParserProvider spreadsheetParserProvider,
                                                                                                  final Optional<SpreadsheetParserName> parserName) {
        this.historyTokenContext = historyTokenContext;
        this.spreadsheetParserProvider = spreadsheetParserProvider;
        this.parserName = parserName;
    }

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.historyTokenContext;
    }

    private final HistoryTokenContext historyTokenContext;

    @Override
    public SpreadsheetParserProvider spreadsheetParserProvider() {
        return this.spreadsheetParserProvider;
    }

    private final SpreadsheetParserProvider spreadsheetParserProvider;

    @Override
    public Optional<SpreadsheetParserName> parserName() {
        return this.parserName;
    }

    private final Optional<SpreadsheetParserName> parserName;
}
