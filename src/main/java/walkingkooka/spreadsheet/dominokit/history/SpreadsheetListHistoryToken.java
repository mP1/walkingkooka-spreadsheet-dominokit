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

/**
 * A token that represents a spreadsheet list files dialog.
 */
public final class SpreadsheetListHistoryToken extends SpreadsheetHistoryToken {

    static SpreadsheetListHistoryToken with(final OptionalInt from,
                                            final OptionalInt count) {
        return new SpreadsheetListHistoryToken(
                checkFrom(from),
                count
        );
    }

    private SpreadsheetListHistoryToken(final OptionalInt from,
                                        final OptionalInt count) {
        super();
        this.from = from;
        this.count = count;
    }

    // from.............................................................................................................

    public OptionalInt from() {
        return this.from;
    }

    public SpreadsheetListHistoryToken setFrom(final OptionalInt from) {
        checkFrom(from);

        return this.from.equals(from) ?
                this :
                new SpreadsheetListHistoryToken(
                        from,
                        this.count
                );
    }

    private final OptionalInt from;

    private static OptionalInt checkFrom(final OptionalInt from) {
        Objects.requireNonNull(from, "from");

        from.ifPresent(value -> {
            if (value < 0) {
                throw new IllegalArgumentException("Invalid from < 0 got " + value);
            }
        });
        return from;
    }

    // count............................................................................................................

    final OptionalInt count;

    // HasUrlFragment...................................................................................................

    @Override
    public UrlFragment urlFragment() {
        StringBuilder urlFragment = new StringBuilder();

        {
            final OptionalInt from = this.from;
            if (from.isPresent()) {
                urlFragment.append("/from/")
                        .append(from.getAsInt());
            }
        }

        {
            final OptionalInt count = this.count;
            if (count.isPresent()) {
                urlFragment.append("/count/")
                        .append(count.getAsInt());
            }
        }

        return urlFragment.length() == 0 ?
                UrlFragment.SLASH :
                UrlFragment.parse(
                        urlFragment.toString()
                );
    }

    // HistoryToken.....................................................................................................

    @Override
    HistoryToken parse0(final String component,
                        final TextCursor cursor) {
        HistoryToken historyToken = this;

        String nextComponent = component;
        do {
            switch (nextComponent) {
                case "count":
                    historyToken = historyToken.setCount(
                            parseCount(cursor)
                    );
                    break;
                case "from":
                    historyToken = historyToken.cast(SpreadsheetListHistoryToken.class).setFrom(
                            parseOptionalInt(cursor)
                    );
                    break;
                default:
                    break;
            }

            nextComponent = parseComponent(cursor)
                    .orElse("");
        } while (false == cursor.isEmpty());

        return historyToken;
    }

    @Override
    public HistoryToken clearAction() {
        return this;
    }

    @Override
    public HistoryToken setFormula() {
        return this; // should not happen
    }

    @Override //
    HistoryToken replaceIdAndName(final SpreadsheetId id,
                                  final SpreadsheetName name) {
        return spreadsheetSelect(
                id,
                name
        );
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        context.spreadsheetMetadataFetcher()
                .getSpreadsheetMetadatas(
                        this.from(),
                        this.count()
                );
    }
}
