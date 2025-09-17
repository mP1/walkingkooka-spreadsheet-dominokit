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
import walkingkooka.spreadsheet.dominokit.selector.AppendPluginSelectorTokenComponentContext;
import walkingkooka.spreadsheet.dominokit.selector.RemoveOrReplacePluginSelectorTokenComponentContext;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;

final class SpreadsheetParserSelectorAppendPluginSelectorTokenComponentContextRemoveOrReplacePluginSelectorTokenComponentContext implements AppendPluginSelectorTokenComponentContext,
    RemoveOrReplacePluginSelectorTokenComponentContext,
    HistoryContextDelegator {

    static SpreadsheetParserSelectorAppendPluginSelectorTokenComponentContextRemoveOrReplacePluginSelectorTokenComponentContext with(final SpreadsheetParserName name,
                                                                                                                                     final HistoryContext context) {
        return new SpreadsheetParserSelectorAppendPluginSelectorTokenComponentContextRemoveOrReplacePluginSelectorTokenComponentContext(
            name,
            context
        );
    }

    private SpreadsheetParserSelectorAppendPluginSelectorTokenComponentContextRemoveOrReplacePluginSelectorTokenComponentContext(final SpreadsheetParserName name,
                                                                                                                                 final HistoryContext context) {
        this.name = name;
        this.context = context;
    }

    @Override
    public String saveText(final String text) {
        final SpreadsheetParserName name = this.name;

        return null == name ?
            text :
            name.setValueText(text)
                .toString();
    }

    private final SpreadsheetParserName name;

    // HistoryContext..............................................................................................

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    private final HistoryContext context;
}
