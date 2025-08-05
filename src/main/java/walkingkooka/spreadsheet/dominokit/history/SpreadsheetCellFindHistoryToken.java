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
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Objects;


/**
 * Opens a dialog and displays a form which supports editing of a complex query to find and display cells that match.
 * <pre>
 * http://localhost:12345/index.html#/2/Untitled/cell/A1/find/path/LR-TB/offset/0/count/100/value-type/any/query/true()
 * http://localhost:12345/api/spreadsheet-id/spreadsheet-name/cell/cell-Or-cell-range-OR-label/find/path/LR-TB/offset/0/count/0/value-type/any/query/formula-expression-to-execute-for-each-candidate-cell.
 * </pre>
 */
public final class SpreadsheetCellFindHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellFindHistoryToken with(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                final SpreadsheetCellFindQuery query) {
        return new SpreadsheetCellFindHistoryToken(
            id,
            name,
            anchoredSelection,
            query
        );
    }

    private SpreadsheetCellFindHistoryToken(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final AnchoredSpreadsheetSelection anchoredSelection,
                                            final SpreadsheetCellFindQuery query) {
        super(
            id,
            name,
            anchoredSelection
        );
        this.query = query;
    }

    public SpreadsheetCellFindQuery query() {
        return this.query;
    }

    SpreadsheetCellFindHistoryToken setQuery0(final SpreadsheetCellFindQuery query) {
        Objects.requireNonNull(query, "query");

        return this.query.equals(query) ?
            this :
            new SpreadsheetCellFindHistoryToken(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                query
            );
    }

    private final SpreadsheetCellFindQuery query;

    @Override
    UrlFragment cellUrlFragment() {
        return FIND.append(this.query.urlFragment());
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
        ).setQuery(this.query);
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // previously SpreadsheetMetadataPropertyName#FIND_QUERY was patched
        // which is now wrong.
    }
}
