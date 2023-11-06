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

import java.util.Optional;

public class SpreadsheetRowInsertBeforeHistoryToken extends SpreadsheetRowInsertHistoryToken {

    static SpreadsheetRowInsertBeforeHistoryToken with(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection selection,
                                                       final int count) {
        return new SpreadsheetRowInsertBeforeHistoryToken(
                id,
                name,
                selection,
                count
        );
    }

    private SpreadsheetRowInsertBeforeHistoryToken(final SpreadsheetId id,
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
    UrlFragment rowUrlFragment() {
        return INSERT_BEFORE.append(
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
        final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();
        final int count = this.count();

        context.spreadsheetDeltaFetcher()
                .insertBeforeColumn(
                        this.id(),
                        selection,
                        count,
                        Optional.of(
                                context.viewport(
                                        Optional.of(anchoredSpreadsheetSelection)
                                )
                        )
                );

        context.pushHistoryToken(
                this.clearAction()
                        .setRow(
                                selection.addSaturated(count)
                        )
        );
    }
}
