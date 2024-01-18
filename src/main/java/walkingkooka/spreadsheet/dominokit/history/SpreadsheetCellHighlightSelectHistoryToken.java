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

public final class SpreadsheetCellHighlightSelectHistoryToken extends SpreadsheetCellHighlightHistoryToken {

    static SpreadsheetCellHighlightSelectHistoryToken with(final SpreadsheetId id,
                                                           final SpreadsheetName name,
                                                           final AnchoredSpreadsheetSelection selection) {
        return new SpreadsheetCellHighlightSelectHistoryToken(
                id,
                name,
                selection
        );
    }

    private SpreadsheetCellHighlightSelectHistoryToken(final SpreadsheetId id,
                                                       final SpreadsheetName name,
                                                       final AnchoredSpreadsheetSelection selection) {
        super(id, name, selection);
    }

    @Override //
    HistoryToken setDifferentSelection(final AnchoredSpreadsheetSelection selection) {
        return selection(
                this.id(),
                this.name(),
                selection
        ).setHighlight();
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return new SpreadsheetCellHighlightSelectHistoryToken(
                id,
                name,
                this.selection()
        );
    }

    @Override //
    UrlFragment highlightUrlFragment() {
        return SELECT;
    }

    @Override
    HistoryToken setSave0(final String value) {
        final boolean enabled = ENABLED.equals(value);

        final SpreadsheetId id = this.id();
        final SpreadsheetName name = this.name();
        final AnchoredSpreadsheetSelection selection = this.selection();

        return enabled || DISABLED.equals(value) ?
                cellHighlightSave(
                        id,
                        name,
                        selection,
                        enabled
                ) :
                cellHighlightSelect(
                        id,
                        name,
                        selection
                );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // nop
    }
}
