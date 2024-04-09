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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.text.cursor.TextCursor;

import java.util.Objects;

public final class SpreadsheetRenameSaveHistoryToken extends SpreadsheetRenameHistoryToken implements Value<SpreadsheetName> {

    static SpreadsheetRenameSaveHistoryToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final SpreadsheetName value) {
        return new SpreadsheetRenameSaveHistoryToken(
                id,
                name,
                value
        );
    }

    private SpreadsheetRenameSaveHistoryToken(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final SpreadsheetName value) {
        super(
                id,
                name
        );
        this.value = Objects.requireNonNull(value, "value");
    }

    public SpreadsheetName value() {
        return this.value;
    }

    private final SpreadsheetName value;

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetRenameSelect(
                this.id(),
                this.name()
        );
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return new SpreadsheetRenameSaveHistoryToken(
                id,
                name,
                this.value
        );
    }

    @Override //
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    UrlFragment spreadsheetRenameUrlFragment() {
        return SAVE.append(
                this.value.urlFragment()
        );
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        return this;
    }

    @Override
    public void onHistoryTokenChange0(final HistoryToken previous,
                                      final AppContext context) {
        // NOP
    }
}
