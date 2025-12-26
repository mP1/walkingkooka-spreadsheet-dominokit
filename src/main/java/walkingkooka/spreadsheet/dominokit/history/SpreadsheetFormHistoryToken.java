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
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.validation.form.FormName;

import java.util.Objects;
import java.util.Optional;

/**
 * Base class for various form definition CRUD operations
 * <pre>
 * #/SpreadsheetId/SpreadsheetName/form/FormName
 * #/SpreadsheetId/SpreadsheetName/form/FormName/delete
 * #/SpreadsheetId/SpreadsheetName/form/FormName/save/XXX
 * </pre>
 */
public abstract class SpreadsheetFormHistoryToken extends SpreadsheetNameHistoryToken {

    SpreadsheetFormHistoryToken(final SpreadsheetId id,
                                final SpreadsheetName name) {
        super(
            id,
            name
        );
    }

    public abstract Optional<FormName> formName();

    @Override
    public HistoryToken setSaveValue(final Optional<?> value) {
        Objects.requireNonNull(value, "value");

        return this;
    }

    @Override //
    final UrlFragment spreadsheetNameUrlFragment() {
        return FORM.appendSlashThen(
            this.formUrlFragment()
        );
    }

    abstract UrlFragment formUrlFragment();

    // parse............................................................................................................

    @Override final HistoryToken parseNext(final String component,
                                           final TextCursor cursor) {
        final HistoryToken historyToken;

        switch (component) {
            case WILDCARD_STRING:
                historyToken = this.parseOffsetAndCount(cursor);
                break;
            case DELETE_STRING:
                historyToken = this.delete();
                break;
            case SAVE_STRING:
                historyToken = this.parseSave(cursor);
                break;
            default:
                cursor.end();
                historyToken = this; // ignore
                break;
        }

        return historyToken;
    }

}
