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

import org.junit.jupiter.api.Test;
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;

import java.util.Optional;

public abstract class SpreadsheetCellCurrencyExchangeRaterHistoryTokenTestCase<T extends SpreadsheetCellCurrencyExchangeRaterHistoryToken> extends SpreadsheetCellHistoryTokenTestCase<T>
    implements SpreadsheetMetadataTesting {

    SpreadsheetCellCurrencyExchangeRaterHistoryTokenTestCase() {
        super();
    }

    // close............................................................................................................

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION
            )
        );
    }

    // navigation.......................................................................................................

    @Test
    public final void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }

    // setSaveValue.....................................................................................................

    @Test
    public final void testSetSaveValueWithInvalidOptionalValueFails() {
        this.setSaveValueFails(
            Optional.of(this)
        );
    }

    @Test
    public final void testSetSaveValueWithEmpty() {
        final Optional<CurrencyExchangeRaterSelector> value = Optional.empty();

        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            value,
            HistoryToken.cellCurrencyExchangeRaterSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION,
                value
            )
        );
    }

    @Test
    public final void testSetSaveValueWithNonEmpty() {
        this.setSaveValueAndCheck(
            this.createHistoryToken(),
            OPTIONAL_CURRENCY_EXCHANGE_RATER,
            HistoryToken.cellCurrencyExchangeRaterSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SELECTION,
                OPTIONAL_CURRENCY_EXCHANGE_RATER
            )
        );
    }

    // stylePropertyName................................................................................................

    @Test
    public final void testStylePropertyName() {
        this.stylePropertyNameAndCheck(
            this.createHistoryToken()
        );
    }
}
