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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.text.cursor.TextCursor;

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
    UrlFragment spreadsheetIdUrlFragment() {
        return UrlFragment.EMPTY;
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
    public HistoryToken idName(final SpreadsheetId id,
                               final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    @Override
    HistoryToken idNameViewportSelection0(final SpreadsheetId id,
                                          final SpreadsheetName name,
                                          final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetLoadHistoryToken(id); // dont care about the name, when loading a new id
    }

    @Override
    public void onHashChange(final HistoryToken previous,
                             final AppContext context) {
        context.spreadsheetMetadataFetcher()
                .loadSpreadsheetMetadata(this.id());
    }

    /**
     * When the spreadsheet is loaded and a new {@link SpreadsheetMetadata} is returned update the history token.
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        context.pushHistoryToken(
                spreadsheetSelect(
                        metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID),
                        metadata.getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_NAME)
                )
        );
    }
}
