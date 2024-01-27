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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

public class SpreadsheetColumnInsertBeforeHistoryToken extends SpreadsheetColumnInsertHistoryToken {

    static SpreadsheetColumnInsertBeforeHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                                          final int count) {
        return new SpreadsheetColumnInsertBeforeHistoryToken(
                id,
                name,
                anchoredSelection,
                count
        );
    }

    private SpreadsheetColumnInsertBeforeHistoryToken(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final int count) {
        super(
                id,
                name,
                anchoredSelection,
                count
        );
    }

    @Override
    UrlFragment columnUrlFragment() {
        return INSERT_BEFORE.append(
                this.countUrlFragment()
        );
    }

    @Override //
    HistoryToken setDifferentAnchoredSelection(final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                this.id(),
                this.name(),
                anchoredSelection
        ).setInsertBefore(
                this.count()
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.anchoredSelection(),
                this.count()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = this.anchoredSelection();
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();
        final int count = this.count();

        context.spreadsheetDeltaFetcher()
                .insertBeforeColumn(
                        this.id(),
                        selection,
                        count
                );
        context.pushHistoryToken(
                previous.clearAction()
                        .setAnchoredSelection(
                                previous.selectionOrEmpty()
                                        .map(
                                                a -> a.selection()
                                                        .addSaturated(
                                                                count,
                                                                0
                                                        ).setAnchor(a.anchor())
                                        )
                        )
        );
    }
}
