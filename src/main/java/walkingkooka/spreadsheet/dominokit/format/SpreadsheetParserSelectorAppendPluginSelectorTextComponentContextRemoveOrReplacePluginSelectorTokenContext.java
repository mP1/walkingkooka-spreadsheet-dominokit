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
import walkingkooka.spreadsheet.dominokit.selector.AppendPluginSelectorTokenContext;
import walkingkooka.spreadsheet.dominokit.selector.RemoveOrReplacePluginSelectorTextComponentContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;

final class SpreadsheetParserSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTokenContext implements AppendPluginSelectorTokenContext,
        RemoveOrReplacePluginSelectorTextComponentContext,
        HistoryTokenContextDelegator {

    static SpreadsheetParserSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTokenContext with(final SpreadsheetParserName name,
                                                                                                                           final HistoryTokenContext context) {
        return new SpreadsheetParserSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTokenContext(
                name,
                context
        );
    }

    private SpreadsheetParserSelectorAppendPluginSelectorTextComponentContextRemoveOrReplacePluginSelectorTokenContext(final SpreadsheetParserName name,
                                                                                                                       final HistoryTokenContext context) {
        this.name = name;
        this.context = context;
    }

    @Override
    public String saveText(final String text) {
        final SpreadsheetParserName name = this.name;

        return null == name ?
                text :
                name.setText(text)
                        .toString();
    }

    private final SpreadsheetParserName name;

    // HistoryTokenContext..............................................................................................

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    private final HistoryTokenContext context;
}
