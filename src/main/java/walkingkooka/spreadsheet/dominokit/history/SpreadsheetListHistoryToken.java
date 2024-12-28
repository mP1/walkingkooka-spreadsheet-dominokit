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

import java.util.OptionalInt;

public abstract class SpreadsheetListHistoryToken extends SpreadsheetHistoryToken {

    SpreadsheetListHistoryToken(final OptionalInt offset,
                                final OptionalInt count) {
        super();
        this.offset = offset;
        this.count = count;
    }

    // offset.............................................................................................................

    final OptionalInt offset;

    // count............................................................................................................

    final OptionalInt count;

    // HasUrlFragment...................................................................................................

    @Override //
    final UrlFragment spreadsheetUrlFragment() {
        return countAndOffsetUrlFragment(
                this.offset,
                this.count,
                this.spreadsheetListUrlFragment()
        );
    }

    abstract UrlFragment spreadsheetListUrlFragment();

    // HistoryToken.....................................................................................................

    @Override //
    final HistoryToken parse0(final String component,
                              final TextCursor cursor) {
        throw new UnsupportedOperationException();
    }

    @Override //
    final HistoryToken replaceIdAndName(final SpreadsheetId id,
                                        final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }
}
