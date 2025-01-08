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

import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponentContext;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;

import java.util.Optional;

final class SpreadsheetParserNameLinkListComponentSpreadsheetLinkListComponentContext implements SpreadsheetLinkListComponentContext,
    HistoryTokenContextDelegator {

    static SpreadsheetParserNameLinkListComponentSpreadsheetLinkListComponentContext with(final Optional<SpreadsheetParserName> name,
                                                                                          final HistoryTokenContext context) {
        return new SpreadsheetParserNameLinkListComponentSpreadsheetLinkListComponentContext(
            name,
            context
        );
    }

    private SpreadsheetParserNameLinkListComponentSpreadsheetLinkListComponentContext(final Optional<SpreadsheetParserName> name,
                                                                                      final HistoryTokenContext context) {
        this.name = name;
        this.context = context;
    }

    @Override
    public boolean isDisabled(final String text) {
        Optional<SpreadsheetParserName> name = this.name;

        return name.isPresent() &&
            SpreadsheetParserName.CASE_SENSITIVITY.equals(
                name.map(SpreadsheetParserName::value)
                    .orElse(null),
                text
            );
    }

    private final Optional<SpreadsheetParserName> name;

    @Override
    public String saveText(final String text) {
        return text; // will be a SpreadsheetParserName
    }

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    private final HistoryTokenContext context;
}
