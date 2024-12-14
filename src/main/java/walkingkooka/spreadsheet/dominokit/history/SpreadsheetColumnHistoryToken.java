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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.OptionalInt;

abstract public class SpreadsheetColumnHistoryToken extends SpreadsheetAnchoredSelectionHistoryToken {

    SpreadsheetColumnHistoryToken(final SpreadsheetId id,
                                  final SpreadsheetName name,
                                  final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );

        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == selection.isColumnReference() && false == selection.isColumnRangeReference()) {
            throw new IllegalArgumentException("Got " + selection + " expected column or column-range");
        }
    }

    @Override //
    final UrlFragment anchoredSelectionUrlFragment() {
        return this.columnUrlFragment();
    }

    abstract UrlFragment columnUrlFragment();

    @Override
    public final HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override //
    final HistoryToken setClear0() {
        return columnClear(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setDelete0() {
        return columnDelete(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setFreeze0() {
        return columnFreeze(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setInsertAfter0(final OptionalInt count) {
        return columnInsertAfter(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                count
        );
    }
    
    @Override //
    final HistoryToken setInsertBefore0(final OptionalInt count) {
        return columnInsertBefore(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                count
        );
    }

    @Override //
    final HistoryToken setMenu1() {
        return columnMenu(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        final AnchoredSpreadsheetSelection anchored = this.anchoredSelection();

        return selection.isColumnReference() &&
                anchored
                        .selection()
                        .testColumn(selection.toColumn()) ?
                anchored :
                selection.setDefaultAnchor();
    }

    @Override //
    final HistoryToken setSave0(final String value) {
        HistoryToken historyToken = this;

        if (historyToken instanceof SpreadsheetColumnSortHistoryToken) {
            historyToken = HistoryToken.columnSortSave(
                    this.id(),
                    this.name(),
                    this.anchoredSelection(),
                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(value)
            );
        }

        return historyToken;
    }

    // sort.............................................................................................................

    @Override //
    final HistoryToken setSortEdit0(final String comparators) {
        return HistoryToken.columnSortEdit(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                comparators
        );
    }

    @Override //
    final HistoryToken setSortSave0(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparators) {
        return HistoryToken.columnSortSave(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                comparators
        );
    }

    // style............................................................................................................

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return this; // column/A/style not currently supported
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return columnUnfreeze(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    // parse............................................................................................................

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        final HistoryToken result;

        switch (component) {
            case CLEAR_STRING:
                result = this.setClear();
                break;
            case DELETE_STRING:
                result = this.setDelete();
                break;
            case FREEZE_STRING:
                result = this.setFreeze();
                break;
            case INSERT_AFTER_STRING:
                result = this.setInsertAfter(
                        parseCount(cursor)
                );
                break;
            case INSERT_BEFORE_STRING:
                result = this.setInsertBefore(
                        parseCount(cursor)
                );
                break;
            case MENU_STRING:
                result = this.setMenu(
                        Optional.empty(), // no selection
                        SpreadsheetLabelNameResolvers.fake()
                );
                break;
            case SORT_STRING:
                result = this.parseSort(cursor);
                break;
            case UNFREEZE_STRING:
                result = this.setUnfreeze();
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }
}
