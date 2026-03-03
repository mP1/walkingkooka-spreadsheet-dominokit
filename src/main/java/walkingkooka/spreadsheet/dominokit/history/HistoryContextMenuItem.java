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

import walkingkooka.currency.CurrencyContext;
import walkingkooka.currency.CurrencyContexts;
import walkingkooka.locale.LocaleContexts;

import java.util.Currency;
import java.util.Locale;

final class HistoryContextMenuItem {

    static {
        final Locale locale = Locale.getDefault();

        CURRENCY_CONTEXT = CurrencyContexts.jre(
            Currency.getInstance(locale),
            (Currency from, Currency to) -> {
                throw new UnsupportedOperationException();
            },
            LocaleContexts.jre(locale)
        );
    }

    final static CurrencyContext CURRENCY_CONTEXT;

    private HistoryContextMenuItem() {
        throw new UnsupportedOperationException();
    }
}
