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

package walkingkooka.spreadsheet.dominokit.currency;

import walkingkooka.currency.CurrencyContextTesting;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContextTesting;

import java.util.Currency;
import java.util.Optional;

public interface CurrencyDialogComponentContextTesting<C extends CurrencyDialogComponentContext> extends DialogComponentContextTesting<C>,
    CurrencyContextTesting,
    ComponentLifecycleMatcherTesting {

    default void dialogTitleAndCheck(final C context,
                                     final String expected) {
        this.checkEquals(
            expected,
            context.dialogTitle()
        );
    }

    // undoCurrency.......................................................................................................

    default void undoCurrencyAndCheck(final C context) {
        this.undoCurrencyAndCheck(
            context,
            Optional.empty()
        );
    }

    default void undoCurrencyAndCheck(final C context,
                                      final Currency expected) {
        this.undoCurrencyAndCheck(
            context,
            Optional.ofNullable(expected)
        );
    }

    default void undoCurrencyAndCheck(final C context,
                                      final Optional<Currency> expected) {
        this.checkEquals(
            expected,
            context.undoCurrency()
        );
    }

    // type.............................................................................................................

    @Override
    default String typeNameSuffix() {
        return CurrencyDialogComponentContext.class.getSimpleName();
    }
}
