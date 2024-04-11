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

    SpreadsheetListHistoryToken(final OptionalInt from,
                                final OptionalInt count) {
        super();
        this.from = from;
        this.count = count;
    }

    // from.............................................................................................................

    public final OptionalInt from() {
        return this.from;
    }

    public abstract SpreadsheetListHistoryToken setFrom(final OptionalInt from);

    final SpreadsheetListHistoryToken setFrom0(final OptionalInt from) {
        checkFrom(from);

        return this.from.equals(from) ?
                this :
                this.replaceFromAndCount(
                        from
                );
    }

    private final OptionalInt from;

    static OptionalInt checkFrom(final OptionalInt from) {
        Objects.requireNonNull(from, "from");

        from.ifPresent(value -> {
            if (value < 0) {
                throw new IllegalArgumentException("Invalid from < 0 got " + value);
            }
        });
        return from;
    }

    abstract SpreadsheetListHistoryToken replaceFromAndCount(final OptionalInt from);

    // count............................................................................................................

    final OptionalInt count;

    // HasUrlFragment...................................................................................................

    @Override
    public final UrlFragment urlFragment() {
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

        return this.listUrlFragment()
                .append(
                        urlFragment.length() == 0 ?
                                UrlFragment.SLASH :
                                UrlFragment.parse(
                                        urlFragment.toString()
                                )
                );
    }

    abstract UrlFragment listUrlFragment();

    // HistoryToken.....................................................................................................

    final HistoryToken parse0(final String component,
                              final TextCursor cursor) {
        HistoryToken historyToken = this;

        String nextComponent = component;

        switch (nextComponent) {
            case "count":
                historyToken = historyToken.setCount(
                        parseCount(cursor)
                );
                break;
            case "from":
                historyToken = historyToken.cast(SpreadsheetListHistoryToken.class)
                        .setFrom(
                                parseOptionalInt(cursor)
                        );
                break;
            case "reload":
                historyToken = historyToken.cast(SpreadsheetListSelectHistoryToken.class)
                        .reload();
            default:
                break;
        }

        nextComponent = parseComponent(cursor)
                .orElse("");

        return historyToken;
    }

    @Override
    public final HistoryToken setFormula() {
        return this; // should not happen
    }

    abstract HistoryToken reload();

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
                        this.from(),
                        count.isPresent() ?
                                count :
                                context.spreadsheetListDialogComponentDefaultCount()
                );
    }
}
