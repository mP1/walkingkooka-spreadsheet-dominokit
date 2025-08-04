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

package walkingkooka.spreadsheet.dominokit.locale;

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.Context;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponentSuggestionsProvider;

import java.util.Locale;
import java.util.Optional;

public interface SpreadsheetLocaleComponentContext<T> extends SuggestBoxComponentSuggestionsProvider<SpreadsheetLocaleComponentSuggestionsValue<T>>,
    Context {

    MenuItem<SpreadsheetLocaleComponentSuggestionsValue<T>> createMenuItem(final SpreadsheetLocaleComponentSuggestionsValue<T> value);

    /**
     * Creates a {@link MenuItem} with a save {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} with the
     * provided value.
     */
    default MenuItem<SpreadsheetLocaleComponentSuggestionsValue<T>> historyTokenMenuItem(final String id,
                                                                                         final SpreadsheetLocaleComponentSuggestionsValue<T> value,
                                                                                         final HistoryContext historyContext) {
        return historyContext.menuItem(
            id + "-suggestion-" + value.locale().toLanguageTag() + SpreadsheetElementIds.OPTION, // id
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

    Optional<SpreadsheetLocaleComponentSuggestionsValue<T>> toValue(final Locale locale);

    @Override
    default String menuItemKey(final SpreadsheetLocaleComponentSuggestionsValue<T> value) {
        return value.locale()
            .toLanguageTag();
    }
}
