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

import java.util.OptionalInt;

/**
 * A token that represents a spreadsheet list files dialog.
 */
public final class SpreadsheetListSelectHistoryToken extends SpreadsheetListHistoryToken {

    static SpreadsheetListSelectHistoryToken with(final OptionalInt from,
                                                  final OptionalInt count) {
        return new SpreadsheetListSelectHistoryToken(
                checkFrom(from),
                count
        );
    }

    private SpreadsheetListSelectHistoryToken(final OptionalInt from,
                                              final OptionalInt count) {
        super(
                from,
                count
        );
    }

    // from.............................................................................................................

    @Override
    public SpreadsheetListSelectHistoryToken setFrom(final OptionalInt from) {
        return this.setFrom0(from)
                .cast(SpreadsheetListSelectHistoryToken.class);
    }

    @Override
    SpreadsheetListSelectHistoryToken replaceFromAndCount(final OptionalInt from) {
        return new SpreadsheetListSelectHistoryToken(
                from,
                this.count
        );
    }

    // HasUrlFragment...................................................................................................

    @Override
    UrlFragment listUrlFragment() {
        return UrlFragment.EMPTY;
    }

    // HistoryToken.....................................................................................................

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    // HistoryToken.....................................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final OptionalInt count = this.count();

        context.spreadsheetMetadataFetcher()
                .getSpreadsheetMetadatas(
                        this.from(),
                        count.isPresent() ?
                                count :
                                context.spreadsheetListDialogComponentDefaultCount()
                );
    }
}
