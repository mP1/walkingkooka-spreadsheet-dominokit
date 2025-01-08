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

package walkingkooka.spreadsheet.dominokit.format;

import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetFormatterFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.focus.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.patternkind.SpreadsheetPatternKindTabsComponentContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProvider;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorEditContext;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link SpreadsheetFormatterSelectorDialogComponent} provided various inputs.
 */
public interface SpreadsheetFormatterSelectorDialogComponentContext extends CanGiveFocus,
    ComponentLifecycleMatcher,
    HasSpreadsheetFormatterFetcherWatchers,
    SpreadsheetDialogComponentContext,
    SpreadsheetFormatterProvider,
    SpreadsheetPatternKindTabsComponentContext,
    SpreadsheetFormatterSelectorEditContext,
    SpreadsheetFormatterTableComponentContext {

    /**
     * Logic to provide the dialog title. In some cases the title might not be currently enabled as it is extracted from
     * the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} which will change after the dialog is actually created.
     */
    String dialogTitle();

    /**
     * Tabs should only be shown when editing a metadata formatter.
     */
    boolean shouldShowTabs();

    /**
     * Provides the UNDO text.
     */
    String undo();

    /**
     * Invokes the server end point
     * <br>
     * /api/spreadsheet/1/formatter/&#47;/edit
     */
    void loadSpreadsheetFormattersEdit(final String text);

    /**
     * Adds a {@link SpreadsheetDeltaFetcherWatcher}.
     */
    Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetFormatterFetcherWatcher}.
     */
    @Override Runnable addSpreadsheetFormatterFetcherWatcher(final SpreadsheetFormatterFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);
}
