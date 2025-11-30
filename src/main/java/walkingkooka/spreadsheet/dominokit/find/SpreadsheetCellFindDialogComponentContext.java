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

package walkingkooka.spreadsheet.dominokit.find;

import walkingkooka.datetime.HasNow;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaCellsTableComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetDeltaFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.valuetype.ValueTypeEditComponentContext;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.text.LineEnding;

import java.util.Locale;
import java.util.Optional;

/**
 * The {@link walkingkooka.Context} accompanying a {@link SpreadsheetCellFindDialogComponent}.
 */
public interface SpreadsheetCellFindDialogComponentContext extends HistoryContext,
    SpreadsheetDeltaCellsTableComponentContext,
    HasSpreadsheetDeltaFetcherWatchers,
    HasSpreadsheetMetadata,
    HasNow,
    DialogComponentContext,
    SpreadsheetCellRangeReferencePathComponentContext,
    ValueTypeEditComponentContext,
    SpreadsheetParserProvider,
    ProviderContext {

    /**
     * {@see SpeadsheetDeltaFetcher#getFindCells}
     */
    void findCells(final SpreadsheetId id,
                   final SpreadsheetCellRangeReference cells,
                   final SpreadsheetCellFindQuery find);

    @Override
    default SpreadsheetCellFindDialogComponentContext cloneEnvironment() {
        throw new UnsupportedOperationException();
    }

    @Override
    default LineEnding lineEnding() {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetCellFindDialogComponentContext setLineEnding(final LineEnding lineEnding) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetCellFindDialogComponentContext setLocale(final Locale locale) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetCellFindDialogComponentContext setUser(final Optional<EmailAddress> user) {
        throw new UnsupportedOperationException();
    }

    @Override
    default <T> SpreadsheetCellFindDialogComponentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                                              final T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    default SpreadsheetCellFindDialogComponentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        throw new UnsupportedOperationException();
    }
}
