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

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponentSuggestionsProvider;

import java.util.Currency;
import java.util.Optional;

public interface CurrencyComponentContext<T> extends SuggestBoxComponentSuggestionsProvider<CurrencyComponentSuggestionsValue<T>>,
    Context {

    MenuItem<CurrencyComponentSuggestionsValue<T>> createMenuItem(final CurrencyComponentSuggestionsValue<T> value);

    /**
     * Creates a {@link MenuItem} with a save {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} with the
     * provided value.
     */
    default MenuItem<CurrencyComponentSuggestionsValue<T>> historyTokenMenuItem(final String id,
                                                                                final CurrencyComponentSuggestionsValue<T> value,
                                                                                final HistoryContext historyContext) {
        return historyContext.menuItem(
            id + "-suggestion-" + value.currency().getCurrencyCode() + SpreadsheetElementIds.OPTION, // id
            value.text(),
            Optional.of(
                historyContext.historyToken()
                    .setSaveValue(
                        Optional.of(
                            value.value()
                        )
                    )
            )
        );
    }

    Optional<CurrencyComponentSuggestionsValue<T>> toValue(final Currency currency);

    @Override
    default String menuItemKey(final CurrencyComponentSuggestionsValue<T> value) {
        return value.currency()
            .getCurrencyCode();
    }
}
