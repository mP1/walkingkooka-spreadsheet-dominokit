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

import walkingkooka.collect.list.Lists;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.compare.SpreadsheetCellSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.dominokit.clipboard.SpreadsheetCellClipboardKind;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetCellFind;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorSavePoint;

import java.util.Optional;
import java.util.OptionalInt;

abstract public class SpreadsheetSelectionHistoryToken extends SpreadsheetNameHistoryToken {

    SpreadsheetSelectionHistoryToken(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    // HasUrlFragment...................................................................................................

    @Override //
    final UrlFragment spreadsheetNameUrlFragment() {
        return this.selectionUrlFragment();
    }

    abstract UrlFragment selectionUrlFragment();

    // parse............................................................................................................

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        final HistoryToken result;

        switch (component) {
            case "clear":
                result = this.setClear();
                break;
            case "copy":
                result = this.parseCopy(cursor);
                break;
            case "cut":
                result = this.parseCut(cursor);
                break;
            case "delete":
                result = this.setDelete();
                break;
            case "find":
                result = this.parseFind(cursor);
                break;
            case "format-pattern":
                result = this.parseFormatPattern(cursor);
                break;
            case "formula":
                result = this.setFormula();
                break;
            case "freeze":
                result = this.setFreeze();
                break;
            case "highlight":
                result = this.parseHighlight(cursor);
                break;
            case "insertAfter":
                result = this.setInsertAfter(
                        parseCount(cursor)
                );
                break;
            case "insertBefore":
                result = this.setInsertBefore(
                        parseCount(cursor)
                );
                break;
            case "menu":
                result = this.setMenu(Optional.empty());
                break;
            case "parse-pattern":
                result = this.parseParsePattern(cursor);
                break;
            case "paste":
                result = this.parsePaste(cursor);
                break;
            case "save":
                result = this.parseSave(cursor);
                break;
            case "sort":
                result = this.parseSort(cursor);
                break;
            case "style":
                result = this.parseStyle(cursor);
                break;
            case "unfreeze":
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
        SpreadsheetCellFind find = SpreadsheetCellFind.empty();

        String component = parseComponentOrNull(cursor);
        if (null != component) {
            if ("path".equals(component)) {
                find = find.setPath(
                        parseComponent(cursor)
                                .map(SpreadsheetCellRangeReferencePath::valueOf)
                );

                component = parseComponentOrNull(cursor);
            }
            if ("offset".equals(component)) {
                find = find.setOffset(
                        parseComponent(cursor)
                        .map(Integer::parseInt)
                        .map(OptionalInt::of)
                        .orElseGet(
                                OptionalInt::empty
                        )
                );
                component = parseComponentOrNull(cursor);
            }
            if ("max".equals(component)) {
                find = find.setMax(
                        parseComponent(cursor)
                        .map(Integer::parseInt)
                        .map(OptionalInt::of)
                        .orElseGet(
                                OptionalInt::empty
                        )
                );
                component = parseComponentOrNull(cursor);
            }
            if ("value-type".equals(component)) {
                find = find.setValueType(
                        parseComponent(cursor)
                );
                component = parseComponentOrNull(cursor);
            }
            if ("query".equals(component)) {
                cursor.next();

                final TextCursorSavePoint save = cursor.save();
                cursor.end();

                final String queryText = save.textBetween()
                        .toString();
                find = find.setQuery(
                        queryText.isEmpty() ?
                        Optional.empty() :
                        Optional.of(
                                queryText
                        )
                );
            }
        }

        return this.setFind(find);
    }

    private HistoryToken parseHighlight(final TextCursor cursor) {
        final HistoryToken token = this.setHighlight();
        return token.parse(cursor);
    }

    private HistoryToken parseSort(final TextCursor cursor) {
        HistoryToken historyToken = this;

        if (this instanceof SpreadsheetCellSelectHistoryToken) {
            final SpreadsheetCellSelectHistoryToken cellSelectHistoryToken = this.cast(SpreadsheetCellSelectHistoryToken.class);

            final String component = parseComponent(cursor)
                    .orElse("");
            switch (component) {
                case "edit":
                    final String comparators = parseComponent(cursor)
                            .orElse("");

                    historyToken = HistoryToken.cellSortEdit(
                            cellSelectHistoryToken.id(),
                            cellSelectHistoryToken.name(),
                            cellSelectHistoryToken.anchoredSelection(),
                            comparators.isEmpty() ?
                                    Lists.empty() :
                                    SpreadsheetCellSpreadsheetComparatorNames.parseList(comparators)
                    );
                    break;
                case "save":
                    historyToken = HistoryToken.cellSortSave(
                            cellSelectHistoryToken.id(),
                            cellSelectHistoryToken.name(),
                            cellSelectHistoryToken.anchoredSelection(),
                            SpreadsheetCellSpreadsheetComparatorNames.parseList(
                                    parseComponent(cursor)
                                            .orElse("")
                            )
                    );
                    break;
                default:
                    break;
            }
        }

        return historyToken;
    }

    private static String parseComponentOrNull(final TextCursor cursor) {
        return parseComponent(cursor)
                .orElse(null);
    }
}
