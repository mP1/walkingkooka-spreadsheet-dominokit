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

import java.util.Objects;

/**
 * Opens a dialog which lists references for the current selection.
 * <pre>
 * /1/SpreadsheetName222/cell/A1/references
 * /1/SpreadsheetName222/cell/A1/references/offset/1/count/2
 * </pre>
 */
public final class SpreadsheetCellReferenceListHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellReferenceListHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                                         final HistoryTokenOffsetAndCount offsetAndCount) {
        return new SpreadsheetCellReferenceListHistoryToken(
            id,
            name,
            anchoredSelection,
            offsetAndCount
        );
    }

    private SpreadsheetCellReferenceListHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final HistoryTokenOffsetAndCount offsetAndCount) {
        super(
            id,
            name,
            anchoredSelection
        );
        this.offsetAndCount = Objects.requireNonNull(
            offsetAndCount,
            "offsetAndCount"
        );
    }

    final HistoryTokenOffsetAndCount offsetAndCount;

    // /references/offset/1/count/2
    @Override
    UrlFragment cellUrlFragment() {
        return REFERENCES.append(
            this.offsetAndCount.urlFragment()
        );
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        ).references(this.offsetAndCount);
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // references dialog will open
    }
}
