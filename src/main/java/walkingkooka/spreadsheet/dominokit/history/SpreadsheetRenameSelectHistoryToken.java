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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.text.cursor.TextCursor;

/**
 * This history token opens/displays a dialog after the user clicks the spreadsheet name in the toolbar for editing.
 */
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
        return this;
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return new SpreadsheetRenameSelectHistoryToken(
            id,
            name
        );
    }

    @Override
    UrlFragment spreadsheetRenameUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        final HistoryToken result;

        switch (component) {
            case SAVE_STRING:
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

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetRenameSelect(
            this.id,
            this.name
        );
    }
}
