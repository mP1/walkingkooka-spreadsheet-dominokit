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

package walkingkooka.spreadsheet.dominokit.parser;

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProviderDelegator;

import java.util.Optional;

final class SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext implements SpreadsheetParserNameLinkListComponentContext,
    HistoryContextDelegator,
    SpreadsheetParserProviderDelegator {

    static SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext with(final HistoryContext historyContext,
                                                                                                      final SpreadsheetParserProvider spreadsheetParserProvider,
                                                                                                      final Optional<SpreadsheetParserName> parserName) {
        return new SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext(
            historyContext,
            spreadsheetParserProvider,
            parserName
        );
    }

    private SpreadsheetParserSelectorDialogComponentSpreadsheetParserNameLinkListComponentContext(final HistoryContext historyContext,
                                                                                                  final SpreadsheetParserProvider spreadsheetParserProvider,
                                                                                                  final Optional<SpreadsheetParserName> parserName) {
        this.historyContext = historyContext;
        this.spreadsheetParserProvider = spreadsheetParserProvider;
        this.parserName = parserName;
    }

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

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
