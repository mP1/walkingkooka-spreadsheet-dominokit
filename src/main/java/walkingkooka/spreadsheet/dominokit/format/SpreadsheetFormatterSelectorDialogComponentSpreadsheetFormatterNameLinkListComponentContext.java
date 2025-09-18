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

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProviderDelegator;

import java.util.Optional;

final class SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext implements SpreadsheetFormatterNameLinkListComponentContext,
    HistoryContextDelegator,
    SpreadsheetFormatterProviderDelegator {

    static SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext with(final HistoryContext historyContext,
                                                                                                            final SpreadsheetFormatterProvider spreadsheetFormatterProvider,
                                                                                                            final Optional<SpreadsheetFormatterName> formatterName) {
        return new SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext(
            historyContext,
            spreadsheetFormatterProvider,
            formatterName
        );
    }

    private SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext(final HistoryContext historyContext,
                                                                                                        final SpreadsheetFormatterProvider spreadsheetFormatterProvider,
                                                                                                        final Optional<SpreadsheetFormatterName> formatterName) {
        this.historyContext = historyContext;
        this.spreadsheetFormatterProvider = spreadsheetFormatterProvider;
        this.formatterName = formatterName;
    }

    @Override
    public HistoryContext historyContext() {
        return this.historyContext;
    }

    private final HistoryContext historyContext;

    @Override
    public SpreadsheetFormatterProvider spreadsheetFormatterProvider() {
        return this.spreadsheetFormatterProvider;
    }

    private final SpreadsheetFormatterProvider spreadsheetFormatterProvider;

    @Override
    public Optional<SpreadsheetFormatterName> formatterName() {
        return this.formatterName;
    }

    private final Optional<SpreadsheetFormatterName> formatterName;
}
