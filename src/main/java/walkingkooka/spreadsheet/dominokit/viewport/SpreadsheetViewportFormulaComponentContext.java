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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.log.LoggingContext;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProvider;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public interface SpreadsheetViewportFormulaComponentContext extends HistoryContext,
    LoggingContext,
    HasSpreadsheetDeltaFetcher,
    HasSpreadsheetViewportCache,
    HasSpreadsheetMetadata,
    HasSpreadsheetMetadataFetcher,
    SpreadsheetParserProvider,
    ProviderContext {

    @Override
    default SpreadsheetViewportFormulaComponentContext cloneEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetViewportFormulaComponentContext setLocale(final Locale locale) {
        Objects.requireNonNull(locale, "locale");
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetViewportFormulaComponentContext setUser(final Optional<EmailAddress> user) {
        throw new UnsupportedOperationException();
    }

    @Override
    default <T> SpreadsheetViewportFormulaComponentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                                               final T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetViewportFormulaComponentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        throw new UnsupportedOperationException();
    }
}
