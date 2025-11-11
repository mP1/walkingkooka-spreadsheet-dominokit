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
import walkingkooka.text.cursor.TextCursor;

/**
 * Displays the rename spreadsheet dialog for editing.
 * <pre>
 * /rename/spreadsheet-id-123
 * </pre>
 */
public final class SpreadsheetListRenameSelectHistoryToken extends SpreadsheetListRenameHistoryToken {

    static SpreadsheetListRenameSelectHistoryToken with(final SpreadsheetId id) {
        return new SpreadsheetListRenameSelectHistoryToken(
            id
        );
    }

    private SpreadsheetListRenameSelectHistoryToken(final SpreadsheetId id) {
        super(
            id
        );
    }

    @Override
    public UrlFragment renameUrlFragment() {
        return UrlFragment.EMPTY;
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    HistoryToken parseNext(final String component,
                           final TextCursor cursor) {
        HistoryToken historyToken = this;

        switch (component) {
            case SAVE_STRING:
                historyToken = this.setSaveStringValue(
                    parseComponentOrEmpty(cursor)
                );
                break;
            default:
                break;
        }

        return historyToken;
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitSpreadsheetListRenameSelect(this.id);
    }
}
