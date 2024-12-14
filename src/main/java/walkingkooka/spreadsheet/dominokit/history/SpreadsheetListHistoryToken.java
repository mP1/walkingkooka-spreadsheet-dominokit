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
import java.util.OptionalInt;

public abstract class SpreadsheetListHistoryToken extends SpreadsheetHistoryToken {

    SpreadsheetListHistoryToken(final OptionalInt offset,
                                final OptionalInt count) {
        super();
        this.offset = offset;
        this.count = count;
    }

    // from.............................................................................................................

    public final OptionalInt offset() {
        return this.offset;
    }

    public abstract SpreadsheetListHistoryToken setOffset(final OptionalInt offset);

    final SpreadsheetListHistoryToken setOffset0(final OptionalInt offset) {
        checkOffset(offset);

        return this.offset.equals(offset) ?
                this :
                this.replaceOffset(
                        offset
                );
    }

    private final OptionalInt offset;

    static OptionalInt checkOffset(final OptionalInt offset) {
        Objects.requireNonNull(offset, "offset");

        offset.ifPresent(value -> {
            if (value < 0) {
                throw new IllegalArgumentException("Invalid offset < 0 got " + value);
            }
        });
        return offset;
    }

    abstract SpreadsheetListHistoryToken replaceOffset(final OptionalInt offset);

    // count............................................................................................................

    final OptionalInt count;

    // HasUrlFragment...................................................................................................

    @Override //
    final UrlFragment spreadsheetUrlFragment() {
        UrlFragment list = this.listUrlFragment();

        {
            final OptionalInt offset = this.offset;
            if (offset.isPresent()) {
                list = list.appendSlashThen(OFFSET)
                        .appendSlashThen(
                                UrlFragment.with(
                                        String.valueOf(offset.getAsInt())
                                )
                        );
            }
        }

        {
            final OptionalInt count = this.count;
            if (count.isPresent()) {
                list = list.appendSlashThen(COUNT)
                        .appendSlashThen(
                                UrlFragment.with(
                                        String.valueOf(count.getAsInt())
                                )
                        );
            }
        }

        return list;
    }


    abstract UrlFragment listUrlFragment();

    // HistoryToken.....................................................................................................

    final HistoryToken parse0(final String component,
                              final TextCursor cursor) {
        HistoryToken historyToken = this;

        String nextComponent = component;

        do {
            switch (nextComponent) {
                case COUNT_STRING:
                    historyToken = historyToken.setCount(
                            parseCount(cursor)
                    );
                    break;
                case OFFSET_STRING:
                    historyToken = historyToken.cast(SpreadsheetListHistoryToken.class)
                            .setOffset(
                                    parseOptionalInt(cursor)
                            );
                    break;
                default:
                    cursor.end();
                    break;
            }
            nextComponent = parseComponent(cursor)
                    .orElse("");
        } while (false == cursor.isEmpty());

        return historyToken;
    }

    @Override //
    final HistoryToken replaceIdAndName(final SpreadsheetId id,
                                        final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final OptionalInt count = this.count();

        context.spreadsheetMetadataFetcher()
                .getSpreadsheetMetadatas(
                        this.offset(),
                        count.isPresent() ?
                                count :
                                context.spreadsheetListDialogComponentDefaultCount()
                );
    }
}
