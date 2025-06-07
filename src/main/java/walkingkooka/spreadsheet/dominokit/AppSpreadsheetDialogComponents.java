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
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellReferencesDialogComponent;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellReferencesDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellValueDialogComponent;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellValueDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellValueDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponent;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.datetimesymbols.DateTimeSymbolsDialogComponent;
import walkingkooka.spreadsheet.dominokit.datetimesymbols.DateTimeSymbolsDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.decimalnumbersymbols.DecimalNumberSymbolsDialogComponent;
import walkingkooka.spreadsheet.dominokit.decimalnumbersymbols.DecimalNumberSymbolsDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetFindDialogComponent;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetFindDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.insert.SpreadsheetColumnRowInsertCountDialogComponent;
import walkingkooka.spreadsheet.dominokit.insert.SpreadsheetColumnRowInsertCountDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingDialogComponent;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingListDialogComponent;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.parser.SpreadsheetParserSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.plugin.JarEntryInfoListDialogComponent;
import walkingkooka.spreadsheet.dominokit.plugin.JarEntryInfoListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.plugin.PluginFileViewDialogComponent;
import walkingkooka.spreadsheet.dominokit.plugin.PluginFileViewDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.plugin.PluginNameSetDialogComponent;
import walkingkooka.spreadsheet.dominokit.plugin.PluginNameSetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.plugin.PluginSetDialogComponent;
import walkingkooka.spreadsheet.dominokit.plugin.PluginSetDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.plugin.PluginUploadDialogComponent;
import walkingkooka.spreadsheet.dominokit.plugin.PluginUploadDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.pluginaliassetlike.PluginAliasSetLikeDialogComponent;
import walkingkooka.spreadsheet.dominokit.pluginaliassetlike.PluginAliasSetLikeDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.sort.SpreadsheetSortDialogComponent;
import walkingkooka.spreadsheet.dominokit.sort.SpreadsheetSortDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListDialogComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetDateComponent;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextBox;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTimeComponent;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Responsible for creating and the registry of all {@link walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent}.
 */
final class AppSpreadsheetDialogComponents implements PublicStaticHelper {

    static void register(final AppContext context) {
        plugin(context);

        spreadsheet(context);

        metadata(context);

        cellValue(context);

        dateTimeSymbols(context);

        decimalNumberSymbols(context);

        columnAndRow(context);

        cellReferences(context);

        find(context);

        formatterParser(context);

        label(context);

        sort(context);
    }

    private static void cellReferences(final AppContext context) {
        SpreadsheetCellReferencesDialogComponent.with(
            SpreadsheetCellReferencesDialogComponentContexts.appContext(context)
        );
    }

    private static void cellValue(final AppContext context) {
        {
            final SpreadsheetCellValueDialogComponentContext<LocalDate> dateContext = SpreadsheetCellValueDialogComponentContexts.date(
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // JsonNodeMarshallContext
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                SpreadsheetDateComponent.empty(
                    dateContext.id(),
                    context.now()::toLocalDate // HasNow
                ),
                dateContext
            );
        }
        {
            final SpreadsheetCellValueDialogComponentContext<String> textContext = SpreadsheetCellValueDialogComponentContexts.text(
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // JsonNodeMarshallContext
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                SpreadsheetTextBox.empty()
                    .setId(
                        textContext.id()
                    ),
                textContext
            );
        }
        {
            final SpreadsheetCellValueDialogComponentContext<LocalTime> timeContext = SpreadsheetCellValueDialogComponentContexts.time(
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // JsonNodeMarshallContext
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                SpreadsheetTimeComponent.empty(
                    timeContext.id(),
                    context.now()::toLocalTime // HasNow
                ),
                timeContext
            );
        }
    }

    private static void columnAndRow(final AppContext context) {
        SpreadsheetColumnRowInsertCountDialogComponent.with(
            SpreadsheetColumnRowInsertCountDialogComponentContexts.appContext(context)
        );
    }

    private static void dateTimeSymbols(final AppContext context) {
        DateTimeSymbolsDialogComponent.with(
            DateTimeSymbolsDialogComponentContexts.cell(context)
        );
        DateTimeSymbolsDialogComponent.with(
            DateTimeSymbolsDialogComponentContexts.metadata(context)
        );
    }

    private static void decimalNumberSymbols(final AppContext context) {
        DecimalNumberSymbolsDialogComponent.with(
            DecimalNumberSymbolsDialogComponentContexts.cell(context)
        );
        DecimalNumberSymbolsDialogComponent.with(
            DecimalNumberSymbolsDialogComponentContexts.metadata(context)
        );
    }

    private static void formatterParser(final AppContext context) {
        formatter(context);
        parser(context);
    }

    private static void formatter(final AppContext context) {
        SpreadsheetFormatterSelectorDialogComponent.with(
            SpreadsheetFormatterSelectorDialogComponentContexts.cell(context)
        );

        SpreadsheetFormatterSelectorDialogComponent.with(
            SpreadsheetFormatterSelectorDialogComponentContexts.metadata(context)
        );
    }

    private static void find(final AppContext context) {
        SpreadsheetFindDialogComponent.with(
            SpreadsheetFindDialogComponentContexts.appContext(context)
        );
    }

    private static void label(final AppContext context) {
        SpreadsheetLabelMappingDialogComponent.with(
            SpreadsheetLabelMappingDialogComponentContexts.appContext(context)
        );

        SpreadsheetLabelMappingListDialogComponent.with(
            SpreadsheetLabelMappingListDialogComponentContexts.appContextCell(context)
        );

        SpreadsheetLabelMappingListDialogComponent.with(
            SpreadsheetLabelMappingListDialogComponentContexts.appContextLabel(context)
        );
    }

    private static void metadata(final AppContext context) {
        SpreadsheetComparatorNameListDialogComponent.with(
            SpreadsheetComparatorNameListDialogComponentContexts.sortComparators(context)
        );

        metadataPlugin(context);
    }

    private static void metadataPlugin(final AppContext context) {
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
        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.validators(context)
        );
        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.validatorValidators(context)
        );
    }

    private static void parser(final AppContext context) {
        SpreadsheetParserSelectorDialogComponent.with(
            SpreadsheetParserSelectorDialogComponentContexts.cell(context)
        );

        SpreadsheetParserSelectorDialogComponent.with(
            SpreadsheetParserSelectorDialogComponentContexts.metadata(context)
        );
    }

    private static void plugin(final AppContext context) {
        PluginSetDialogComponent.with(
            PluginSetDialogComponentContexts.appContext(context)
        );
        JarEntryInfoListDialogComponent.with(
            JarEntryInfoListDialogComponentContexts.appContext(context)
        );
        PluginFileViewDialogComponent.with(
            PluginFileViewDialogComponentContexts.appContext(context)
        );
        PluginUploadDialogComponent.with(
            PluginUploadDialogComponentContexts.appContext(context)
        );
    }

    private static void sort(final AppContext context) {
        SpreadsheetSortDialogComponent.with(
            SpreadsheetSortDialogComponentContexts.appContext(context)
        );
    }

    private static void spreadsheet(final AppContext context) {
        SpreadsheetListDialogComponent.with(
            SpreadsheetListDialogComponentContexts.appContext(context)
        );

        spreadsheetName(context);
    }

    private static void spreadsheetName(final AppContext context) {
        SpreadsheetNameDialogComponent.with(
            SpreadsheetNameDialogComponentContexts.spreadsheetListRename(context)
        );

        SpreadsheetNameDialogComponent.with(
            SpreadsheetNameDialogComponentContexts.spreadsheetRename(context)
        );
    }

    /**
     * Stop creation
     */
    private AppSpreadsheetDialogComponents() {
        throw new UnsupportedOperationException();
    }
}
