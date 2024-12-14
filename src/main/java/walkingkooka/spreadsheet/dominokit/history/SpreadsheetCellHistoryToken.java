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
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolvers;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;
import java.util.OptionalInt;

abstract public class SpreadsheetCellHistoryToken extends SpreadsheetAnchoredSelectionHistoryToken {

    SpreadsheetCellHistoryToken(final SpreadsheetId id,
                                final SpreadsheetName name,
                                final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );

        final SpreadsheetSelection selection = anchoredSelection.selection();
        if (false == (selection.isCellReference() || selection.isCellRangeReference() || selection.isLabelName())) {
            throw new IllegalArgumentException("Got " + selection + " expected cell, cell-range or label");
        }
    }

    @Override //
    final UrlFragment anchoredSelectionUrlFragment() {
        return this.cellUrlFragment();
    }

    abstract UrlFragment cellUrlFragment();

    @Override //
    final HistoryToken setClear0() {
        return this; // clear cell not supported
    }

    @Override //
    final HistoryToken setDelete0() {

        // deleting a pattern will create a save pattern with empty string.
        return this instanceof SpreadsheetCellFormatterHistoryToken || this instanceof SpreadsheetCellParserHistoryToken ?
                this.setSave("") :
                cellDelete(
                        this.id(),
                        this.name(),
                        this.anchoredSelection()
                );
    }

    final HistoryToken setFormula0() {
        return formula(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setFreeze0() {
        return cellFreeze(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken setInsertAfter0(final OptionalInt count) {
        return this;
    }

    @Override //
    final HistoryToken setInsertBefore0(final OptionalInt count) {
        return this;
    }

    @Override //
    final HistoryToken setMenu1() {
        return cellMenu(
                this.id(),
                this.name(),
                this.anchoredSelection()
        );
    }

    @Override //
    final AnchoredSpreadsheetSelection setMenuSelection(final SpreadsheetSelection selection) {
        final AnchoredSpreadsheetSelection anchored = this.anchoredSelection();

        return selection.isCellReference() &&
                anchored.selection()
                        .testCell(selection.toCell()) ?
                anchored :
                selection.setDefaultAnchor();
    }

    // sort.............................................................................................................

    @Override //
    final HistoryToken setSortEdit0(final String comparators) {
        return HistoryToken.cellSortEdit(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                comparators
        );
    }

    @Override //
    final HistoryToken setSortSave0(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesList comparators) {
        return HistoryToken.cellSortSave(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                comparators
        );
    }

    // style............................................................................................................

    @Override //
    final HistoryToken setStyle0(final TextStylePropertyName<?> propertyName) {
        return cellStyle(
                this.id(),
                this.name(),
                this.anchoredSelection(),
                propertyName
        );
    }

    @Override //
    final HistoryToken setUnfreeze0() {
        return cellUnfreeze(
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
            case COPY_STRING:
                result = this.parseCopy(cursor);
                break;
            case CUT_STRING:
                result = this.parseCut(cursor);
                break;
            case DELETE_STRING:
                result = this.setDelete();
                break;
            case FIND_STRING:
                result = this.parseFind(cursor);
                break;
            case FREEZE_STRING:
                result = this.setFreeze();
                break;
            case FORMATTER_STRING:
                result = this.setFormatter();
                break;
            case FORMULA_STRING:
                result = this.setFormula();
                break;
            case MENU_STRING:
                result = this.setMenu(
                        Optional.empty(), // no selection
                        SpreadsheetLabelNameResolvers.fake()
                );
                break;
            case PARSER_STRING:
                result = this.setParser();
                break;
            case PASTE_STRING:
                result = this.parsePaste(cursor);
                break;
            case SAVE_STRING:
                result = this.parseSave(cursor);
                break;
            case SORT_STRING:
                result = this.parseSort(cursor);
                break;
            case STYLE_STRING:
                result = this.parseStyle(cursor);
                break;
            case TOOLBAR_STRING:
                result = this.setToolbar();
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

    private HistoryToken parseCopy(final TextCursor cursor) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            final SpreadsheetCellSelectHistoryToken cell = this.cast(SpreadsheetCellSelectHistoryToken.class);

            String component = parseComponentOrNull(cursor);
            if (null != component) {
                token = cell.setCellCopy(
                        SpreadsheetCellClipboardKind.parse(component)
                );
            }
        }

        return token;
    }

    private HistoryToken parsePaste(final TextCursor cursor) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            final SpreadsheetCellSelectHistoryToken cell = this.cast(SpreadsheetCellSelectHistoryToken.class);

            String component = parseComponentOrNull(cursor);
            if (null != component) {
                token = cell.setCellPaste(
                        SpreadsheetCellClipboardKind.parse(component)
                );
            }
        }

        return token;
    }

    private HistoryToken parseCut(final TextCursor cursor) {
        HistoryToken token = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            final SpreadsheetCellSelectHistoryToken cell = this.cast(SpreadsheetCellSelectHistoryToken.class);

            String component = parseComponentOrNull(cursor);
            if (null != component) {
                token = cell.setCellCut(
                        SpreadsheetCellClipboardKind.parse(component)
                );
            }
        }

        return token;
    }

    private HistoryToken parseFind(final TextCursor cursor) {
        final TextCursorSavePoint save = cursor.save();
        cursor.end();

        final String queryText = save.textBetween()
                .toString();

        return this.setQuery(
                SpreadsheetCellFindQuery.parse(queryText)
        );
    }

    private static String parseComponentOrNull(final TextCursor cursor) {
        return parseComponent(cursor)
                .orElse(null);
    }
}
