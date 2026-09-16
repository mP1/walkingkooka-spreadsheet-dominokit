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

package walkingkooka.spreadsheet.dominokit.value.plugin.currencyexchangerater;

import walkingkooka.Cast;
import walkingkooka.currency.provider.CurrencyExchangeRaterSelector;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Optional;

final class AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata extends AppContextCurrencyExchangeRaterSelectorDialogComponentContext {

    static AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata with(final AppContext context) {
        return new AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata(context);
    }

    private AppContextCurrencyExchangeRaterSelectorDialogComponentContextMetadata(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(
            this.propertyName()
                .orElse(null)
        );
    }

    @Override
    public Optional<CurrencyExchangeRaterSelector> undo() {
        return this.propertyName()
            .flatMap(p -> this.context.spreadsheetMetadata()
                .getIgnoringDefaults(p)
            );
    }

    private Optional<SpreadsheetMetadataPropertyName<CurrencyExchangeRaterSelector>> propertyName() {
        return Cast.to(
            this.historyToken()
                .metadataPropertyName()
        );
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token.metadataPropertyName()
            .map(SpreadsheetMetadataPropertyName::isCurrencyExchangeRaterSelector)
            .orElse(false) &&
            false == token.isSave();
    }
}
