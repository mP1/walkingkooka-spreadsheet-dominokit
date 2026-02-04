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
import walkingkooka.validation.form.FormName;

import java.util.Optional;

public abstract class SpreadsheetFormHistoryTokenTestCase<T extends SpreadsheetFormHistoryToken> extends SpreadsheetNameHistoryTokenTestCase<T> {

    final static FormName FORM_NAME = FormName.with("FormName123");

    SpreadsheetFormHistoryTokenTestCase() {
        super();
    }

    // formName.........................................................................................................

    final void formNameAndCheck(final SpreadsheetFormHistoryToken token,
                                final FormName expected) {
        this.formNameAndCheck(
            token,
            Optional.of(expected)
        );
    }

    final void formNameAndCheck(final SpreadsheetFormHistoryToken token,
                                final Optional<FormName> expected) {
        this.checkEquals(
            expected,
            token.formName(),
            token.urlFragment()::toString
        );
    }

    // close............................................................................................................

    @Test
    public final void testClose() {
        this.closeAndCheck(
            this.createHistoryToken(),
            HistoryToken.spreadsheetSelect(
                ID,
                NAME
            )
        );
    }

    // dateTimeSymbols..................................................................................................

    @Test
    public final void testDateTimeSymbols() {
        this.dateTimeSymbolsAndCheck(
            this.createHistoryToken()
        );
    }

    // decimalNumberSymbols.............................................................................................

    @Test
    public final void testDecimalNumberSymbols() {
        this.decimalNumberSymbolsAndCheck(
            this.createHistoryToken()
        );
    }

    // locale...........................................................................................................

    @Test
    public final void testLocale() {
        this.localeAndCheck(
            this.createHistoryToken()
        );
    }

    // navigation.......................................................................................................

    @Test
    public final void testNavigation() {
        this.navigationAndCheck(
            this.createHistoryToken()
        );
    }
}
