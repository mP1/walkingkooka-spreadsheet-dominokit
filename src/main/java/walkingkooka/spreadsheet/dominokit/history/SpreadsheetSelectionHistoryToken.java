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
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangePath;
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

    @Override
    final UrlFragment spreadsheetUrlFragment() {
        return this.selectionUrlFragment();
    }

    abstract UrlFragment selectionUrlFragment();

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        final HistoryToken result;

        switch(component) {
            case "clear":
                result = this.setClear();
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
            case "insertAfter":
                result = this.setInsertAfter(
                        this.parseCount(cursor)
                );
                break;
            case "insertBefore":
                result = this.setInsertBefore(
                        this.parseCount(cursor)
                );
                break;
            case "menu":
                result = this.setMenu(Optional.empty());
                break;
            case "parse-pattern":
                result = this.parseParsePattern(cursor);
                break;
            case "save":
                result = this.parseSave(cursor);
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

    private int parseCount(final TextCursor cursor) {
        final Optional<String> count = parseComponent(cursor);
        if (false == count.isPresent()) {
            throw new IllegalArgumentException("Missing count");
        }
        return Integer.parseInt(count.get());
    }

    private HistoryToken parseFind(final TextCursor cursor) {
        final Optional<SpreadsheetCellRangePath> path = parseComponent(cursor)
                .map(SpreadsheetCellRangePath::valueOf);

        final OptionalInt offset = path.isPresent() ?
                parseComponent(cursor)
                        .map(Integer::parseInt)
                        .map(OptionalInt::of)
                        .orElseGet(
                                OptionalInt::empty
                        ) :
                OptionalInt.empty();

        final OptionalInt max = offset.isPresent() ?
                parseComponent(cursor)
                        .map(Integer::parseInt)
                        .map(OptionalInt::of)
                        .orElseGet(
                                OptionalInt::empty
                        ) :
                OptionalInt.empty();

        final Optional<String> valueType = max.isPresent() ?
                parseComponent(cursor) :
                Optional.empty();

        final Optional<String> query;
        if (valueType.isPresent() && false == cursor.isEmpty()) {
            cursor.next();

            final TextCursorSavePoint save = cursor.save();
            cursor.end();

            final String queryText = save.textBetween()
                    .toString();
            query = queryText.isEmpty() ?
                    Optional.empty() :
                    Optional.of(
                            queryText
                    );
        } else {
            query = Optional.empty();
        }

        return this.setFind(
                path,
                offset,
                max,
                valueType,
                query
        );
    }
}
