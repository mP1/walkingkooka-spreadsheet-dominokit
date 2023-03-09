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
import walkingkooka.text.cursor.TextCursor;

abstract public class SpreadsheetSelectionHistoryHashToken extends SpreadsheetNameHistoryHashToken {

    SpreadsheetSelectionHistoryHashToken(final SpreadsheetId id,
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
    HistoryHashToken parse0(final String component,
                            final TextCursor cursor) {
        HistoryHashToken result = this;

        switch(component) {
            case "clear":
                result = this.clear();
                break;
            case "delete":
                result = this.delete();
                break;
            case "formula":
                result = this.formulaHistoryHashToken();
                break;
            case "freeze":
                result = this.freeze();
                break;
            case "menu":
                result = this.menu();
                break;
            case "pattern":
                result = this.parsePattern(cursor);
                break;
            case "save":
                result = this.parseSave(cursor);
                break;
            case "style":
                result = this.parseStyle(cursor);
                break;
            case "unfreeze":
                result = unfreeze();
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }
}
