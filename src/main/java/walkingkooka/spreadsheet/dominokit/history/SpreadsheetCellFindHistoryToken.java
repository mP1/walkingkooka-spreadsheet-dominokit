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
import walkingkooka.spreadsheet.engine.SpreadsheetCellFind;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Objects;


/**
 * Opens a dialog and displays a form which supports editing of a complex query to find and display cells that match.
 * <pre>
 * http://localhost:12345/index.html#/2/Untitled/cell/A1/find/path/LR-TB/offset/0/max/100/value-type/any/query/true()
 * /spreadsheet-id/spreadsheet-name/cell/cell-Or-cell-range-OR-label/find/path/LR-TB/offset/0/max/0/value-type/any/query/formula-expression-to-execute-for-each-candidate-cell.
 * </pre>
 */
public final class SpreadsheetCellFindHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellFindHistoryToken with(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                final SpreadsheetCellFind find) {
        return new SpreadsheetCellFindHistoryToken(
                id,
                name,
                anchoredSelection,
                find
        );
    }

    private SpreadsheetCellFindHistoryToken(final SpreadsheetId id,
                                            final SpreadsheetName name,
                                            final AnchoredSpreadsheetSelection anchoredSelection,
                                            final SpreadsheetCellFind find) {
        super(
                id,
                name,
                anchoredSelection
        );
        this.find = find;
    }

    public SpreadsheetCellFind find() {
        return this.find;
    }

    SpreadsheetCellFindHistoryToken setFind0(final SpreadsheetCellFind find) {
        Objects.requireNonNull(find, "find");

        return this.find.equals(find) ?
                this :
                new SpreadsheetCellFindHistoryToken(
                        this.id(),
                        this.name(),
                        this.anchoredSelection(),
                        find
                );
    }

    private final SpreadsheetCellFind find;

    @Override
    UrlFragment cellUrlFragment() {
        return FIND.appendSlashThen(this.find.urlFragment());
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override
    public HistoryToken setFormula() {
        return setFormula0();
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                id,
                name,
                anchoredSelection
        ).setFind(this.find);
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final SpreadsheetCellFind previousFind = context.lastCellFind();
        final SpreadsheetCellFind nextFind = this.find();
        if (previous.equals(nextFind)) {
            context.debug(this.getClass().getSimpleName() + ".onHistoryTokenChange0 saved same find=" + nextFind);
        } else {
            context.setLastCellFind(nextFind);
            context.debug(this.getClass().getSimpleName() + ".onHistoryTokenChange0 saved " + nextFind + " was " + previousFind);
        }
    }
}
