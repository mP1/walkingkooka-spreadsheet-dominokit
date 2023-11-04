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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Optional;

public class SpreadsheetColumnInsertAfterHistoryToken extends SpreadsheetColumnInsertHistoryToken {

    static SpreadsheetColumnInsertAfterHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection selection,
                                                         final int count) {
        return new SpreadsheetColumnInsertAfterHistoryToken(
                id,
                name,
                selection,
                count
        );
    }

    private SpreadsheetColumnInsertAfterHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection selection,
                                                     final int count) {
        super(
                id,
                name,
                selection,
                count
        );
    }

    @Override
    UrlFragment columnUrlFragment() {
        return INSERT_AFTER.append(
                this.countUrlFragment()
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.selection(),
                this.count()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = this.selection();

        context.spreadsheetDeltaFetcher()
                .insertBeforeColumn(
                        this.id(),
                        anchoredSpreadsheetSelection
                                .selection(),
                        this.count(),
                        Optional.of(
                                context.viewport(
                                        Optional.of(anchoredSpreadsheetSelection)
                                )
                        )
                );
    }
}
