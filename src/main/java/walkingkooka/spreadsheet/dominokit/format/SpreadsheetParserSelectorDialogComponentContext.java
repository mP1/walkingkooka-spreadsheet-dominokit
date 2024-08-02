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

import walkingkooka.spreadsheet.dominokit.CanGiveFocus;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcher;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.parser.SpreadsheetParserProvider;
import walkingkooka.spreadsheet.server.parser.SpreadsheetParserSelectorEditContext;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link SpreadsheetParserSelectorDialogComponent} provided various inputs.
 */
public interface SpreadsheetParserSelectorDialogComponentContext extends CanGiveFocus,
        ComponentLifecycleMatcher,
        SpreadsheetDialogComponentContext,
        SpreadsheetParserProvider,
        SpreadsheetPatternKindTabsComponentContext,
        SpreadsheetParserSelectorEditContext {


    /**
     * Logic to provide the dialog title. In some cases the title might not be currently enabled as it is extracted from
     * the {@link walkingkooka.spreadsheet.dominokit.history.HistoryToken} which will change after the dialog is actually created.
     */
    String dialogTitle();

    /**
     * Provides the UNDO text.
     */
    String undo();

    /**
     * Adds a {@link SpreadsheetDeltaFetcherWatcher}.
     */
    Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher);

    /**
     * Adds a {@link SpreadsheetMetadataFetcherWatcher}.
     */
    Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher);
}
