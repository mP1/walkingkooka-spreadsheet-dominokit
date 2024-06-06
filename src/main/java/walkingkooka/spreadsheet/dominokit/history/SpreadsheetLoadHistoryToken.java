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

import java.util.OptionalInt;

public final class SpreadsheetLoadHistoryToken extends SpreadsheetIdHistoryToken {

    static SpreadsheetLoadHistoryToken with(final SpreadsheetId id) {
        return new SpreadsheetLoadHistoryToken(
                id
        );
    }

    private SpreadsheetLoadHistoryToken(final SpreadsheetId id) {
        super(id);
    }

    @Override
    UrlFragment spreadsheetUrlFragment() {
        return this.id().urlFragment();
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return spreadsheetSelect(
                this.id(),
                SpreadsheetName.with(component)
        ).parse(cursor);
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetListSelect(
                OptionalInt.empty(), // from
                OptionalInt.empty() // count
        );
    }

    @Override
    public HistoryToken setFormula() {
        return this; // should not happen
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.loadSpreadsheetMetadataAndPushPreviousIfFails(
                this.id(),
                previous
        );
    }
}
