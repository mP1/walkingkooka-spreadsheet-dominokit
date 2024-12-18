/*
 * Copyright 2023 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use context file except in compliance with the License.
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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponent;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatchers;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetFindDialogComponent;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetFindDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.plugin.PluginNameSetDialogComponent;
import walkingkooka.spreadsheet.dominokit.plugin.PluginNameSetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.pluginaliassetlike.PluginAliasSetLikeDialogComponent;
import walkingkooka.spreadsheet.dominokit.pluginaliassetlike.PluginAliasSetLikeDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetColumnRowInsertCountDialogComponent;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetColumnRowInsertCountDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetLabelMappingDialogComponent;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetLabelMappingDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.sort.SpreadsheetSortDialogComponent;
import walkingkooka.spreadsheet.dominokit.sort.SpreadsheetSortDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListComponentContexts;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListDialogComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponentContexts;

/**
 * Responsible for creating and the registry of all {@link walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent}.
 */
final class AppSpreadsheetDialogComponents implements PublicStaticHelper {

    static void register(final SpreadsheetMetadataFetcherWatchers metadataFetcherWatchers,
                         final AppContext context) {
        SpreadsheetListDialogComponent.with(
                SpreadsheetListComponentContexts.appContext(context)
        );

        PluginNameSetDialogComponent.with(
                PluginNameSetDialogComponentContexts.appContext(context)
        );

        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.findFunctions(context)
        );
        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.formulaFunctions(context)
        );

        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.converters(context)
        );
        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.comparators(context)
        );
        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.exporters(context)
        );
        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.functions(context)
        );
        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.formatters(context)
        );
        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.importers(context)
        );
        PluginAliasSetLikeDialogComponent.with(
                PluginAliasSetLikeDialogComponentContexts.parsers(context)
        );

        SpreadsheetComparatorNameListDialogComponent.with(
                SpreadsheetComparatorNameListDialogComponentContexts.sortComparators(context)
        );

        SpreadsheetNameDialogComponent.with(
                SpreadsheetNameDialogComponentContexts.spreadsheetListRename(context)
        );

        SpreadsheetNameDialogComponent.with(
                SpreadsheetNameDialogComponentContexts.spreadsheetRename(context)
        );

        SpreadsheetColumnRowInsertCountDialogComponent.with(
                SpreadsheetColumnRowInsertCountDialogComponentContexts.appContext(context)
        );

        SpreadsheetFindDialogComponent.with(
                SpreadsheetFindDialogComponentContexts.appContext(context)
        );

        SpreadsheetLabelMappingDialogComponent.with(
                SpreadsheetLabelMappingDialogComponentContexts.appContext(context)
        );

        SpreadsheetFormatterSelectorDialogComponent.with(
                SpreadsheetFormatterSelectorDialogComponentContexts.cell(context)
        );

        SpreadsheetParserSelectorDialogComponent.with(
                SpreadsheetParserSelectorDialogComponentContexts.cell(context)
        );

        SpreadsheetFormatterSelectorDialogComponent.with(
                SpreadsheetFormatterSelectorDialogComponentContexts.metadata(context)
        );
        SpreadsheetParserSelectorDialogComponent.with(
                SpreadsheetParserSelectorDialogComponentContexts.metadata(context)
        );

        SpreadsheetSortDialogComponent.with(
                SpreadsheetSortDialogComponentContexts.basic(
                        context, // SpreadsheetComparatorProvider
                        context, // HistoryTokenContext
                        context // loggingContext
                )
        );
    }

    /**
     * Stop creation
     */
    private AppSpreadsheetDialogComponents() {
        throw new UnsupportedOperationException();
    }
}
