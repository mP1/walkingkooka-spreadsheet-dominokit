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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public class SpreadsheetRowSelectHistoryToken extends SpreadsheetRowHistoryToken {

    static SpreadsheetRowSelectHistoryToken with(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetRowSelectHistoryToken(
                id,
                name,
                viewportSelection
        );
    }

    private SpreadsheetRowSelectHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final SpreadsheetViewportSelection viewportSelection) {
        super(
                id,
                name,
                viewportSelection
        );
    }

    @Override
    UrlFragment rowUrlFragment() {
        return SELECT;
    }

    @Override
    SpreadsheetHistoryToken setDifferentIdOrName(final SpreadsheetId id,
                                                 final SpreadsheetName name) {
        return new SpreadsheetRowSelectHistoryToken(
                id,
                name,
                this.viewportSelection()
        );
    }

    @Override
    void onHashChange0(final AppContext context) {
        // POST metadata with new row selection
        context.spreadsheetMetadataFetcher().patchMetadata(
                this.id(),
                SpreadsheetMetadataPropertyName.SELECTION,
                this.viewportSelection()
        );
    }
}
