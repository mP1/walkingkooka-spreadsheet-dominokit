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

import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.locale.LocaleContext;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellLinksComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetFormatterFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetFormatterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.recent.RecentValueSavesContext;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.text.LineEnding;

import java.util.Locale;
import java.util.Optional;

public interface SpreadsheetViewportComponentContext extends HasSpreadsheetDeltaFetcher,
    HasSpreadsheetFormatterFetcher,
    HasSpreadsheetFormatterFetcherWatchers,
    HasSpreadsheetMetadata,
    HasSpreadsheetMetadataFetcherWatchers,
    HasSpreadsheetViewportCache,
    LocaleContext,
    RefreshContext,
    SpreadsheetComparatorProvider,
    SpreadsheetLabelNameResolver,
    SpreadsheetViewportFormulaComponentContext,
    SpreadsheetCellLinksComponentContext,
    RecentValueSavesContext,
    SpreadsheetViewportContext {

    @Override
    default SpreadsheetViewportComponentContext cloneEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetViewportComponentContext setEnvironmentContext(final EnvironmentContext environmentContext) {
        throw new UnsupportedOperationException();
    }

    @Override
    default LineEnding lineEnding() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetViewportComponentContext setLineEnding(final LineEnding lineEnding) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetViewportComponentContext setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetViewportComponentContext setUser(final Optional<EmailAddress> user) {
        throw new UnsupportedOperationException();
    }

    @Override
    <T> SpreadsheetViewportComponentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                                final T value);

    @Override
    SpreadsheetViewportComponentContext removeEnvironmentValue(final EnvironmentValueName<?> name);

    /**
     * Helper that gets the {@link SpreadsheetCellReference home} for the viewport or fails
     */
    default SpreadsheetCellReference home() {
        return this.spreadsheetMetadata()
            .getOrFail(SpreadsheetMetadataPropertyName.VIEWPORT_HOME);
    }
}
