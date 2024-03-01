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

import java.util.OptionalInt;

public class SpreadsheetRowInsertBeforeHistoryToken extends SpreadsheetRowInsertHistoryToken {

    static SpreadsheetRowInsertBeforeHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection,
                                                       final OptionalInt count) {
        return new SpreadsheetRowInsertBeforeHistoryToken(
                id,
                name,
                anchoredSelection,
                count
        );
    }

    private SpreadsheetRowInsertBeforeHistoryToken(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final OptionalInt count) {
        super(
                id,
                name,
                anchoredSelection,
                count
        );
    }

    @Override
    UrlFragment rowUrlFragment() {
        return INSERT_BEFORE.append(
                this.countUrlFragment()
        );
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                id,
                name,
                anchoredSelection
        ).setInsertBefore(
                this.count()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final OptionalInt count = this.count();
        if (count.isPresent()) {
            this.callServer(
                    count.getAsInt(),
                    previous,
                    context
            );
        }
    }

    private void callServer(final int count,
                            final HistoryToken previous,
                            final AppContext context) {
        final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = this.anchoredSelection();
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

        context.spreadsheetDeltaFetcher()
                .insertBeforeRow(
                        this.id(),
                        selection,
                        count
                );
        context.pushHistoryToken(
                previous.clearAction()
                        .setAnchoredSelection(
                                previous.anchoredSelectionOrEmpty()
                                        .map(
                                                a -> a.selection()
                                                        .addSaturated(
                                                                0,
                                                                count
                                                        ).setAnchor(a.anchor())
                                        )
                        )
        );
    }
}
