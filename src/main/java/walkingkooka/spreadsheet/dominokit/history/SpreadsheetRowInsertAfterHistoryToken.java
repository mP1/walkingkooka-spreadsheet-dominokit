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
 * Displays a dialog to prompt the user for how many rows to insert if count is missing or inserts the requested rows.
 * <pre>
 * /123/SpreadsheetName456/row/1/insertAfter
 * /123/SpreadsheetName456/row/1/insertAfter/1
 *
 * /spreadsheet-id/spreadsheet-name/row/row or row-range/insertAfter
 * /spreadsheet-id/spreadsheet-name/row/row or row-range/insertAfter/row-count
 * </pre>
 */
public class SpreadsheetRowInsertAfterHistoryToken extends SpreadsheetRowInsertHistoryToken {

    static SpreadsheetRowInsertAfterHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final OptionalInt count) {
        return new SpreadsheetRowInsertAfterHistoryToken(
            id,
            name,
            anchoredSelection,
            count
        );
    }

    private SpreadsheetRowInsertAfterHistoryToken(final SpreadsheetId id,
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
        return INSERT_AFTER.append(
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
        ).insertAfter(this.count());
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final OptionalInt count = this.count();
        if (count.isPresent()) {
            final AnchoredSpreadsheetSelection anchoredSpreadsheetSelection = this.anchoredSelection();
            final SpreadsheetSelection selection = anchoredSpreadsheetSelection.selection();

            context.spreadsheetDeltaFetcher()
                .postInsertAfterRow(
                    this.id,
                    selection,
                    count.getAsInt()
                );

            context.pushHistoryToken(previous);
        }
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitRowInsertAfter(
            this.id,
            this.name,
            this.anchoredSelection,
            this.count
        );
    }
}
