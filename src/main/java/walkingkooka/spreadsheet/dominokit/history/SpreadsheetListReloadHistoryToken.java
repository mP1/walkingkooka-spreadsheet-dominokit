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
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListDialogComponent;

import java.util.OptionalInt;

/**
 * A token that reloads the list shown by {@link SpreadsheetListDialogComponent}.
 * <pre>
 * /reload
 * </pre>
 */
public final class SpreadsheetListReloadHistoryToken extends SpreadsheetListHistoryToken {

    static SpreadsheetListReloadHistoryToken with(final OptionalInt offset,
                                                  final OptionalInt count) {
        return new SpreadsheetListReloadHistoryToken(
                checkOffset(offset),
                count
        );
    }

    private SpreadsheetListReloadHistoryToken(final OptionalInt offset,
                                              final OptionalInt count) {
        super(
                offset,
                count
        );
    }

    // offset.............................................................................................................

    @Override
    public SpreadsheetListReloadHistoryToken setOffset(final OptionalInt offset) {
        return this.setOffset0(offset)
                .cast(SpreadsheetListReloadHistoryToken.class);
    }

    @Override
    SpreadsheetListReloadHistoryToken replaceOffset(final OptionalInt offset) {
        return new SpreadsheetListReloadHistoryToken(
                offset,
                this.count
        );
    }

    // HasUrlFragment...................................................................................................

    @Override
    UrlFragment listUrlFragment() {
        return RELOAD;
    }

    // HistoryToken.....................................................................................................

    @Override
    public HistoryToken clearAction() {
        return HistoryToken.spreadsheetListSelect(
                this.offset(),
                this.count()
        );
    }

    // HistoryToken.....................................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.pushHistoryToken(
                this.clearAction()
        );

        // SpreadsheetListSelectHistoryToken#onHistoryTokenChange will do the actual reloading of data
    }
}
