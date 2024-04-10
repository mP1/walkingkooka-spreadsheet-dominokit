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
import walkingkooka.text.cursor.TextCursor;

public final class SpreadsheetRenameSelectHistoryToken extends SpreadsheetRenameHistoryToken {

    static SpreadsheetRenameSelectHistoryToken with(final SpreadsheetId id,
                                                    final SpreadsheetName name) {
        return new SpreadsheetRenameSelectHistoryToken(
                id,
                name
        );
    }

    private SpreadsheetRenameSelectHistoryToken(final SpreadsheetId id,
                                                final SpreadsheetName name) {
        super(
                id,
                name
        );
    }

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetSelect(
                this.id(),
                this.name()
        );
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return new SpreadsheetRenameSelectHistoryToken(
                id,
                name
        );
    }

    @Override //
    HistoryToken setSave0(final String value) {
        return HistoryToken.spreadsheetRenameSave(
                this.id(),
                this.name(),
                SpreadsheetName.with(value)
        );
    }

    @Override
    UrlFragment spreadsheetRenameUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        final HistoryToken result;

        switch (component) {
            case "save":
                result = this.parseSave(cursor);
                break;
            default:
                cursor.end();
                result = this; // ignore
                break;
        }

        return result;
    }

    @Override
    public void onHistoryTokenChange0(final HistoryToken previous,
                                      final AppContext context) {
        // NOP
    }
}
