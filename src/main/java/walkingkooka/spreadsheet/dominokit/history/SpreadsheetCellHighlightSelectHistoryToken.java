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

/**
 * Selects the highlight icon in the toolbar.
 * <pre>
 * /123/SpreadsheetName456/cell/A1/highlight
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/highlight
 * </pre>
 */
public final class SpreadsheetCellHighlightSelectHistoryToken extends SpreadsheetCellHighlightHistoryToken {

    static SpreadsheetCellHighlightSelectHistoryToken with(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellHighlightSelectHistoryToken(
                id,
                name,
                anchoredSelection
        );
    }

    private SpreadsheetCellHighlightSelectHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override //
    UrlFragment highlightUrlFragment() {
        return SELECT;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                id,
                name,
                anchoredSelection
        ).setHighlight();
    }

    @Override
    HistoryToken setSave0(final String value) {
        final boolean enabled = ENABLE.equals(value);

        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final AnchoredSpreadsheetSelection anchoredSelection = this.anchoredSelection();

        return enabled || DISABLE.equals(value) ?
                cellHighlightSave(
                        id,
                        name,
                        anchoredSelection,
                        enabled
                ) :
                cellHighlightSelect(
                        id,
                        name,
                        anchoredSelection
                );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // nop
    }
}
