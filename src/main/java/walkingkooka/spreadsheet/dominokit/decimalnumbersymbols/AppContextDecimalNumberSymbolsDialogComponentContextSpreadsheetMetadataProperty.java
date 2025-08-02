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

package walkingkooka.spreadsheet.dominokit.decimalnumbersymbols;

import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;

import java.util.Objects;
import java.util.Optional;

public class AppContextDecimalNumberSymbolsDialogComponentContextSpreadsheetMetadataProperty extends AppContextDecimalNumberSymbolsDialogComponentContext {

    static AppContextDecimalNumberSymbolsDialogComponentContextSpreadsheetMetadataProperty with(final AppContext context) {
        return new AppContextDecimalNumberSymbolsDialogComponentContextSpreadsheetMetadataProperty(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextDecimalNumberSymbolsDialogComponentContextSpreadsheetMetadataProperty(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return this.spreadsheetMetadataPropertyNameDialogTitle(SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS);
    }

    @Override
    public Optional<DecimalNumberSymbols> copyDecimalNumberSymbols() {
        return Optional.empty();
    }

    @Override
    public Optional<DecimalNumberSymbols> loadDecimalNumberSymbols() {
        return this.context.spreadsheetMetadata()
            .get(SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS);
    }

    @Override
    public void save(final Optional<DecimalNumberSymbols> symbols) {
        final AppContext context = this.context;
        context.spreadsheetMetadataFetcher()
            .patchMetadata(
                context.spreadsheetMetadata()
                    .getOrFail(SpreadsheetMetadataPropertyName.SPREADSHEET_ID),
                SpreadsheetMetadata.EMPTY.setOrRemove(
                    SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS,
                    symbols.orElse(null)
                )
            );
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetMetadataPropertySelectHistoryToken &&
            token.cast(SpreadsheetMetadataPropertySelectHistoryToken.class).propertyName() == SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS;
    }
}
