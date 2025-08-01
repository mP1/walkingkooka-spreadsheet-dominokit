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
import walkingkooka.spreadsheet.SpreadsheetUrlFragments;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;

import java.util.Optional;

abstract public class SpreadsheetRowHistoryToken extends SpreadsheetAnchoredSelectionHistoryToken {

    SpreadsheetRowHistoryToken(final SpreadsheetId id,
                               final SpreadsheetName name,
                               final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
            id,
            name,
            anchoredSelection
        );

        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isRowOrRowRange()) {
            throw new IllegalArgumentException("Got " + selection + " expected row or row-range");
        }
    }

    @Override //
    final UrlFragment selectionUrlFragment() {
        return SpreadsheetUrlFragments.ROW.appendSlashThen(
            this.anchoredSelection.urlFragment()
        ).appendSlashThen(this.rowUrlFragment());
    }

    abstract UrlFragment rowUrlFragment();

    @Override
    public final HistoryToken clearAction() {
        return this.selectionSelect();
    }

    // parse............................................................................................................

    @Override final HistoryToken parseNext(final String component,
                                           final TextCursor cursor) {
        final HistoryToken result;

        switch (component) {
            case CLEAR_STRING:
                result = this.clear();
                break;
            case DELETE_STRING:
                result = this.delete();
                break;
            case FREEZE_STRING:
                result = this.freeze();
                break;
            case INSERT_AFTER_STRING:
                result = this.insertAfter(
                    parseCount(cursor)
                );
                break;
            case INSERT_BEFORE_STRING:
                result = this.insertBefore(
                    parseCount(cursor)
                );
                break;
            case MENU_STRING:
                result = this.menu(
                    Optional.empty(), // no selection
                    SpreadsheetLabelNameResolvers.fake()
                );
                break;
            case SORT_STRING:
                result = this.parseSort(cursor);
                break;
            case UNFREEZE_STRING:
                result = this.unfreeze();
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }
}
