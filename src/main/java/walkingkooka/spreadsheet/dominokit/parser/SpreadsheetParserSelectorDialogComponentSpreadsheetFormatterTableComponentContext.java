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

import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterTableComponentContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;

final class SpreadsheetParserSelectorDialogComponentSpreadsheetFormatterTableComponentContext implements SpreadsheetFormatterTableComponentContext,
    HistoryContextDelegator {

    static SpreadsheetParserSelectorDialogComponentSpreadsheetFormatterTableComponentContext with(final SpreadsheetParserSelector selector,
                                                                                                  final HistoryContext context) {
        return new SpreadsheetParserSelectorDialogComponentSpreadsheetFormatterTableComponentContext(
            selector,
            context
        );
    }

    private SpreadsheetParserSelectorDialogComponentSpreadsheetFormatterTableComponentContext(final SpreadsheetParserSelector selector,
                                                                                              final HistoryContext context) {
        this.selector = selector;
        this.context = context;
    }

    @Override
    public String formatterTableHistoryTokenSave(final SpreadsheetFormatterSelector selector) {
        return this.selector.setValueText(selector.valueText())
            .toString();
    }

    private final SpreadsheetParserSelector selector;

    // HistoryContext..............................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    private final HistoryContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.selector + " " + this.context;
    }
}
