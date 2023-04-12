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

import java.util.Objects;

public final class UnknownHistoryToken extends HistoryToken {

    static UnknownHistoryToken with(final UrlFragment fragment) {
        Objects.requireNonNull(fragment, "fragment");

        return new UnknownHistoryToken(fragment);
    }

    private UnknownHistoryToken(final UrlFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onHashChange(final HistoryToken previous,
                             final AppContext context) {
        // nop
    }

    @Override
    public HistoryToken idName(final SpreadsheetId id,
                               final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    @Override
    public UrlFragment urlFragment() {
        return this.fragment;
    }

    private final UrlFragment fragment;
}
