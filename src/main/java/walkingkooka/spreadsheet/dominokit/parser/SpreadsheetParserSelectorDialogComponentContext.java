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

package walkingkooka.spreadsheet.dominokit.parser;

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetParserFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetParserFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.focus.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.meta.SpreadsheetMetadataPropertyNameTabsComponentContext;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEditContext;

import java.util.Optional;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link SpreadsheetParserSelectorDialogComponent} provided various inputs.
 */
public interface SpreadsheetParserSelectorDialogComponentContext extends CanGiveFocus,
    ComponentLifecycleMatcher,
    HasSpreadsheetParserFetcherWatchers,
    DialogComponentContext,
    SpreadsheetParserProvider,
    SpreadsheetMetadataPropertyNameTabsComponentContext,
    SpreadsheetParserSelectorEditContext {

    /**
     * Tabs should only be shown when editing a metadata formatter.
     */
    boolean shouldShowTabs();

    /**
     * Invokes the server end point
     * <br>
     * /api/spreadsheet/1/parser/&#47;/edit
     */
    void loadSpreadsheetParsersEdit(final String text);

    /**
     * Provides the UNDO.
     */
    Optional<SpreadsheetParserSelector> undo();

    /**
     * Adds a {@link SpreadsheetDeltaFetcherWatcher}.
     */
    Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetParserFetcherWatcher}.
     */
    @Override
    Runnable addSpreadsheetParserFetcherWatcher(final SpreadsheetParserFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);
}
