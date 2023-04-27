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

/**
 * A token that represents a spreadsheet create action.
 */
public final class SpreadsheetCreateHistoryToken extends SpreadsheetHistoryToken {

    static SpreadsheetCreateHistoryToken with() {
        return new SpreadsheetCreateHistoryToken();
    }

    private SpreadsheetCreateHistoryToken() {
        super();
    }

    @Override
    public UrlFragment urlFragment() {
        return UrlFragment.SLASH;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return component.length() > 0 ?
                spreadsheetLoad(
                        SpreadsheetId.parse(component)
                ).parse(cursor) :
                this;
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.spreadsheetMetadataFetcher()
                .createSpreadsheetMetadata();
    }

    @Override
    public HistoryToken formulaHistoryToken() {
        return this; // should not happen
    }

    @Override
    public HistoryToken formulaSaveHistoryToken(final String text) {
        throw new UnsupportedOperationException();
    }
}
