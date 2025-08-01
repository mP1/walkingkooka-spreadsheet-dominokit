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
 * <pre>
 * /STAR/offset/1/count/10/
 * </pre>
 */
public final class SpreadsheetListSelectHistoryToken extends SpreadsheetListHistoryToken implements HistoryTokenWatcher {

    static SpreadsheetListSelectHistoryToken with(final HistoryTokenOffsetAndCount offsetAndCount) {
        return new SpreadsheetListSelectHistoryToken(
            offsetAndCount
        );
    }

    private SpreadsheetListSelectHistoryToken(final HistoryTokenOffsetAndCount offsetAndCount) {
        super(
            offsetAndCount
        );
    }

    // HasUrlFragment...................................................................................................

    @Override
    UrlFragment spreadsheetListUrlFragment() {
        return UrlFragment.EMPTY;
    }

    // HistoryToken.....................................................................................................

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final HistoryTokenOffsetAndCount offsetAndCount = this.offsetAndCount;
        final OptionalInt count = offsetAndCount.count();

        context.spreadsheetMetadataFetcher()
            .getSpreadsheetMetadatas(
                offsetAndCount.offset(),
                count.isPresent() ?
                    count :
                    context.spreadsheetListDialogComponentDefaultCount()
            );
    }
}
