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
import walkingkooka.spreadsheet.reference.SpreadsheetViewport;

public class SpreadsheetRowDeleteHistoryToken extends SpreadsheetRowHistoryToken {

    static SpreadsheetRowDeleteHistoryToken with(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final SpreadsheetViewport viewport) {
        return new SpreadsheetRowDeleteHistoryToken(
                id,
                name,
                viewport
        );
    }

    private SpreadsheetRowDeleteHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final SpreadsheetViewport viewport) {
        super(
                id,
                name,
                viewport
        );
    }

    @Override
    UrlFragment rowUrlFragment() {
        return DELETE;
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.viewport()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.spreadsheetDeltaFetcher()
                .deleteDelta(
                        this.id(),
                        this.viewport()
                );
    }
}
