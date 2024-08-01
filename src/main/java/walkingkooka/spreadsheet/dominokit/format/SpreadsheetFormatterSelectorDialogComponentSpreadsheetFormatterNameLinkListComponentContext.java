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
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviderDelegator;

import java.util.Optional;

final class SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext implements SpreadsheetFormatterNameLinkListComponentContext,
        HistoryTokenContextDelegator,
        SpreadsheetFormatterProviderDelegator {

    static SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext with(final HistoryTokenContext historyTokenContext,
                                                                                                            final SpreadsheetFormatterProvider spreadsheetFormatterProvider,
                                                                                                            final Optional<SpreadsheetFormatterName> formatterName) {
        return new SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext(
                historyTokenContext,
                spreadsheetFormatterProvider,
                formatterName
        );
    }

    private SpreadsheetFormatterSelectorDialogComponentSpreadsheetFormatterNameLinkListComponentContext(final HistoryTokenContext historyTokenContext,
                                                                                                        final SpreadsheetFormatterProvider spreadsheetFormatterProvider,
                                                                                                        final Optional<SpreadsheetFormatterName> formatterName) {
        this.historyTokenContext = historyTokenContext;
        this.spreadsheetFormatterProvider = spreadsheetFormatterProvider;
        this.formatterName = formatterName;
    }

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.historyTokenContext;
    }

    private final HistoryTokenContext historyTokenContext;

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
