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
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

/**
 * Deletes the selected column or columns and any cells that may be present within them.
 * <pre>
 * /123/SpreadsheetName456/column/A/delete
 *
 * /spreadsheet-id/spreadsheet-name/column/column or column-range/delete
 * </pre>
 */
public class SpreadsheetColumnDeleteHistoryToken extends SpreadsheetColumnHistoryToken {

    static SpreadsheetColumnDeleteHistoryToken with(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetColumnDeleteHistoryToken(
            id,
            name,
            anchoredSelection
        );
    }

    private SpreadsheetColumnDeleteHistoryToken(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
            id,
            name,
            anchoredSelection
        );
    }

    @Override
    UrlFragment columnUrlFragment() {
        return DELETE;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        ).delete();
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(
            context.historyToken()
                .clearSelection()
        );
        context.spreadsheetDeltaFetcher()
            .deleteDelta(
                this.id(),
                this.anchoredSelection()
                    .selection()
            );
    }
}
