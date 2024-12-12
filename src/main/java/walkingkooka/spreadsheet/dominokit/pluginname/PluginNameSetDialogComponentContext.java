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

package walkingkooka.spreadsheet.dominokit.pluginname;


import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.fetcher.HasPluginFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.fetcher.HasSpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;

/**
 * A {@link walkingkooka.Context} tht accompanies a {@link PluginNameSetDialogComponent} providing custom values and functionality.
 */
public interface PluginNameSetDialogComponentContext
        extends SpreadsheetDialogComponentContext,
        AddPluginNameSetComponentContext,
        RemovePluginNameSetComponentContext,
        HasPluginFetcherWatchers,
        HasSpreadsheetMetadataFetcherWatchers,
        HasSpreadsheetMetadata{
    /**
     * Returns the dialog title.
     */
    String dialogTitle();

    /**
     * Loads an existing spreadsheet
     */
    void loadSpreadsheetMetadata(final SpreadsheetId id);

    void pluginFilter(final String query,
                      final int offset,
                      final int count);
}
