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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.OptionalInt;

/**
 * Displays a dialog to prompt the user for how many columns to insert if count is missing or inserts the requested columns.
 * <pre>
 * /123/SpreadsheetName456/column/A/insertBefore
 * /123/SpreadsheetName456/column/A/insertBefore/1
 *
 * /spreadsheet-id/spreadsheet-name/column/column or column-range/insertBefore
 * /spreadsheet-id/spreadsheet-name/column/column or column-range/insertBefore/column-count
 * </pre>
 */
public class SpreadsheetColumnInsertBeforeHistoryToken extends SpreadsheetColumnInsertHistoryToken {

    static SpreadsheetColumnInsertBeforeHistoryToken with(final SpreadsheetId id,
                                                          final SpreadsheetName name,
                                                          final AnchoredSpreadsheetSelection anchoredSelection,
                                                          final OptionalInt count) {
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
                                                      final OptionalInt count) {
        super(
            id,
            name,
            anchoredSelection,
            count
        );
    }

    @Override
    UrlFragment columnUrlFragment() {
        return INSERT_BEFORE.appendSlashThen(
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
        ).insertBefore(
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
            .postInsertBeforeColumn(
                this.id,
                selection,
                count
            );
        context.pushHistoryToken(
            previous.setSelection(
                previous.selection()
                    .map(
                        s -> s.addSaturated(
                            count,
                            0
                        )
                    )
            )
        );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitColumnInsertBefore(
            this.id,
            this.name,
            this.anchoredSelection,
            this.count
        );
    }
}
