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

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

public abstract class SpreadsheetCellDateTimeSymbolsHistoryToken extends SpreadsheetCellHistoryToken {

    SpreadsheetCellDateTimeSymbolsHistoryToken(final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final AnchoredSpreadsheetSelection anchoredSelection,
                                               final Optional<DateTimeSymbols> dateTimeSymbols) {
        super(id, name, anchoredSelection);
        this.dateTimeSymbols = Objects.requireNonNull(dateTimeSymbols, "dateTimeSymbols");
    }

    final Optional<DateTimeSymbols> dateTimeSymbols;

    @Override // /cell/A1/dateTimeSymbols
    final UrlFragment cellUrlFragment() {
        return DATE_TIME_SYMBOLS.appendSlashThen(
            this.dateTimeSymbolsUrlFragment()
        );
    }

    abstract UrlFragment dateTimeSymbolsUrlFragment();
}
