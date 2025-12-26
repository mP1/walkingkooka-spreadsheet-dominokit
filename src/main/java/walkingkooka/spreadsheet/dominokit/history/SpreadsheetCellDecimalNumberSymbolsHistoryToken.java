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

import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

public abstract class SpreadsheetCellDecimalNumberSymbolsHistoryToken extends SpreadsheetCellHistoryToken {

    SpreadsheetCellDecimalNumberSymbolsHistoryToken(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                    final Optional<DecimalNumberSymbols> decimalNumberSymbols) {
        super(id, name, anchoredSelection);
        this.decimalNumberSymbols = Objects.requireNonNull(decimalNumberSymbols, "decimalNumberSymbols");
    }

    final Optional<DecimalNumberSymbols> decimalNumberSymbols;

    @Override // /cell/A1/dateTimeSymbols
    final UrlFragment cellUrlFragment() {
        return DECIMAL_NUMBER_SYMBOLS.appendSlashThen(
            this.decimalNumberSymbolsUrlFragment()
        );
    }

    abstract UrlFragment decimalNumberSymbolsUrlFragment();
}
