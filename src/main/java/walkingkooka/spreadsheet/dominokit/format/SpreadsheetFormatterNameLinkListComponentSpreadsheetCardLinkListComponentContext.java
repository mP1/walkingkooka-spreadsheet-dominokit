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
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetCardLinkListComponentContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;

import java.util.Optional;

final class SpreadsheetFormatterNameLinkListComponentSpreadsheetCardLinkListComponentContext implements SpreadsheetCardLinkListComponentContext,
    HistoryContextDelegator {

    static SpreadsheetFormatterNameLinkListComponentSpreadsheetCardLinkListComponentContext with(final Optional<SpreadsheetFormatterName> name,
                                                                                                 final HistoryContext context) {
        return new SpreadsheetFormatterNameLinkListComponentSpreadsheetCardLinkListComponentContext(
            name,
            context
        );
    }

    private SpreadsheetFormatterNameLinkListComponentSpreadsheetCardLinkListComponentContext(final Optional<SpreadsheetFormatterName> name,
                                                                                             final HistoryContext context) {
        this.name = name;
        this.context = context;
    }

    @Override
    public boolean isDisabled(final String text) {
        Optional<SpreadsheetFormatterName> name = this.name;

        return name.isPresent() &&
            SpreadsheetFormatterName.CASE_SENSITIVITY.equals(
                name.map(SpreadsheetFormatterName::value)
                    .orElse(null),
                text
            );
    }

    private final Optional<SpreadsheetFormatterName> name;

    @Override
    public String saveValueText(final String text) {
        return text; // will be a SpreadsheetFormatterName
    }

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    private final HistoryContext context;
}
