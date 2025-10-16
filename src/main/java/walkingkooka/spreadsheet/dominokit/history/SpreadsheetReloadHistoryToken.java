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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;
import java.util.Optional;

/**
 * A token that represents a reload spreadsheet action.
 * <pre>
 * /1/reload
 * /spreadsheet-id/reload
 * </pre>
 */
public final class SpreadsheetReloadHistoryToken extends SpreadsheetNameHistoryToken {

    static SpreadsheetReloadHistoryToken with(final SpreadsheetId id,
                                              final SpreadsheetName name) {
        return new SpreadsheetReloadHistoryToken(
            id,
            name
        );
    }

    private SpreadsheetReloadHistoryToken(final SpreadsheetId id,
                                          final SpreadsheetName name) {
        super(
            id,
            name
        );
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        return this; // ignores whatever is after /spreadsheet-id/spreadsheet-name/reload
    }

    @Override //
    public HistoryToken clearAction() {
        return spreadsheetSelect(
            this.id,
            this.name
        );
    }


    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return new SpreadsheetReloadHistoryToken(
            id,
            name
        );
    }

    @Override
    public HistoryToken setSaveValue(final Optional<?> value) {
        Objects.requireNonNull(value, "value");

        return this;
    }

    @Override
    UrlFragment spreadsheetNameUrlFragment() {
        return RELOAD;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(
            previous
        );
        context.reload();
    }
}
