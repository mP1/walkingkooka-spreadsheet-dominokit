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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.Cast;
import walkingkooka.net.AbsoluteUrl;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.SpreadsheetValueType;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellReferencesDialogComponent;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellReferencesDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.cell.value.SpreadsheetCellValueDialogComponent;
import walkingkooka.spreadsheet.dominokit.cell.value.SpreadsheetCellValueDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.cell.value.SpreadsheetCellValueDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponent;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetComparatorNameListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.convert.ConverterSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.convert.ConverterSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.datetime.DateComponent;
import walkingkooka.spreadsheet.dominokit.datetime.DateTimeComponent;
import walkingkooka.spreadsheet.dominokit.datetime.TimeComponent;
import walkingkooka.spreadsheet.dominokit.datetimesymbols.DateTimeSymbolsDialogComponent;
import walkingkooka.spreadsheet.dominokit.datetimesymbols.DateTimeSymbolsDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.decimalnumbersymbols.DecimalNumberSymbolsDialogComponent;
import walkingkooka.spreadsheet.dominokit.decimalnumbersymbols.DecimalNumberSymbolsDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.email.EmailAddressComponent;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetCellFindDialogComponent;
import walkingkooka.spreadsheet.dominokit.find.SpreadsheetCellFindDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.format.SpreadsheetFormatterSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.formhandler.FormHandlerSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.formhandler.FormHandlerSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.insert.SpreadsheetColumnRowInsertCountDialogComponent;
import walkingkooka.spreadsheet.dominokit.insert.SpreadsheetColumnRowInsertCountDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingDialogComponent;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingListDialogComponent;
import walkingkooka.spreadsheet.dominokit.label.SpreadsheetLabelMappingListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.locale.LocaleDialogComponent;
import walkingkooka.spreadsheet.dominokit.locale.LocaleDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.navigate.SpreadsheetNavigateDialogComponent;
import walkingkooka.spreadsheet.dominokit.navigate.SpreadsheetNavigateDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.number.NumberComponent;
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
import walkingkooka.spreadsheet.dominokit.sort.SpreadsheetCellSortDialogComponent;
import walkingkooka.spreadsheet.dominokit.sort.SpreadsheetCellSortDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListDialogComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetListDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponent;
import walkingkooka.spreadsheet.dominokit.spreadsheet.SpreadsheetNameDialogComponentContexts;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.spreadsheet.dominokit.url.AbsoluteUrlComponent;
import walkingkooka.spreadsheet.dominokit.validator.ValidatorSelectorDialogComponent;
import walkingkooka.spreadsheet.dominokit.validator.ValidatorSelectorDialogComponentContexts;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.tree.expression.ExpressionNumber;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Responsible for creating and the registry of all {@link DialogComponent}.
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

        locale(context);

        navigate(context);

        sort(context);

        validator(context);
    }

    private static void cellReferences(final AppContext context) {
        SpreadsheetCellReferencesDialogComponent.with(
            SpreadsheetCellReferencesDialogComponentContexts.appContext(context)
        );
    }

    private static void cellValue(final AppContext context) {
        {
            final SpreadsheetCellValueDialogComponentContext<LocalDate> dateContext = SpreadsheetCellValueDialogComponentContexts.basic(
                SpreadsheetValueType.DATE,
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                DateComponent.empty(
                    dateContext.id(),
                    context.now()::toLocalDate // HasNow
                ),
                dateContext
            );
        }
        {
            final SpreadsheetCellValueDialogComponentContext<LocalDateTime> dateTimeContext = SpreadsheetCellValueDialogComponentContexts.basic(
                SpreadsheetValueType.DATE_TIME,
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                DateTimeComponent.empty(
                    dateTimeContext.id(),
                    context::now // HasNow
                ),
                dateTimeContext
            );
        }

        {
            final SpreadsheetCellValueDialogComponentContext<EmailAddress> emailContext = SpreadsheetCellValueDialogComponentContexts.basic(
                SpreadsheetValueType.EMAIL_ADDRESS,
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                EmailAddressComponent.empty()
                    .setId(emailContext.id())
                    .optional(),
                emailContext
            );
        }

        {
            final SpreadsheetCellValueDialogComponentContext<ExpressionNumber> numberContext = SpreadsheetCellValueDialogComponentContexts.basic(
                SpreadsheetValueType.NUMBER,
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                NumberComponent.empty(
                    numberContext.id() + "-value" + SpreadsheetElementIds.TEXT_BOX,
                    context
                ).optional(),
                numberContext
            );
        }

        {
            final SpreadsheetCellValueDialogComponentContext<String> textContext = SpreadsheetCellValueDialogComponentContexts.basic(
                SpreadsheetValueType.TEXT,
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                TextBoxComponent.empty()
                    .setId(
                        textContext.id() + "-value" + SpreadsheetElementIds.TEXT_BOX
                    ),
                textContext
            );
        }
        {
            final SpreadsheetCellValueDialogComponentContext<LocalTime> timeContext = SpreadsheetCellValueDialogComponentContexts.basic(
                SpreadsheetValueType.TIME,
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                TimeComponent.empty(
                    timeContext.id(),
                    context.now()::toLocalTime // HasNow
                ),
                timeContext
            );
        }

        {
            final SpreadsheetCellValueDialogComponentContext<AbsoluteUrl> urlContext = SpreadsheetCellValueDialogComponentContexts.basic(
                SpreadsheetValueType.URL,
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                AbsoluteUrlComponent.empty()
                    .setId(urlContext.id())
                    .optional(),
                urlContext
            );
        }

        {
            final SpreadsheetCellValueDialogComponentContext<ExpressionNumber> wholeNumberContext = SpreadsheetCellValueDialogComponentContexts.basic(
                SpreadsheetValueType.WHOLE_NUMBER,
                context.spreadsheetViewportCache(),
                context, // SpreadsheetDeltaFetcherWatcher
                context, // HistoryContext
                context // LoggingContext
            );

            SpreadsheetCellValueDialogComponent.with(
                NumberComponent.empty(
                    wholeNumberContext.id() + "-value" + SpreadsheetElementIds.TEXT_BOX,
                    context
                ).optional(),
                wholeNumberContext
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
        SpreadsheetCellFindDialogComponent.with(
            SpreadsheetCellFindDialogComponentContexts.appContext(context)
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

    private static void locale(final AppContext context) {
        LocaleDialogComponent.with(
            LocaleDialogComponentContexts.appContextCellLocale(context)
        );

        LocaleDialogComponent.with(
            LocaleDialogComponentContexts.appContextSpreadsheetMetadataLocale(context)
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

        for (final SpreadsheetMetadataPropertyName<?> selector : SpreadsheetMetadataPropertyName.ALL) {
            if (selector.isConverterSelector()) {
                ConverterSelectorDialogComponent.with(
                    ConverterSelectorDialogComponentContexts.appContext(
                        Cast.to(selector),
                        context
                    )
                );
            }
        }

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
            PluginAliasSetLikeDialogComponentContexts.formattingFunctions(context)
        );

        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.formHandlers(context)
        );
        FormHandlerSelectorDialogComponent.with(
            FormHandlerSelectorDialogComponentContexts.defaultFormHandler(context)
        );

        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.importers(context)
        );
        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.parsers(context)
        );
        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.scriptingFunctions(context)
        );
        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.validators(context)
        );
        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.validatorFunctions(context)
        );
        PluginAliasSetLikeDialogComponent.with(
            PluginAliasSetLikeDialogComponentContexts.validatorValidators(context)
        );
    }

    private static void navigate(final AppContext context) {
        SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.cellNavigate(context)
        );

        SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.columnNavigate(context)
        );

        SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.navigate(context)
        );

        SpreadsheetNavigateDialogComponent.with(
            SpreadsheetNavigateDialogComponentContexts.rowNavigate(context)
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
        SpreadsheetCellSortDialogComponent.with(
            SpreadsheetCellSortDialogComponentContexts.appContext(context)
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

    private static void validator(final AppContext context) {
        ValidatorSelectorDialogComponent.with(
            ValidatorSelectorDialogComponentContexts.appContext(context)
        );
    }

    /**
     * Stop creation
     */
    private AppSpreadsheetDialogComponents() {
        throw new UnsupportedOperationException();
    }
}
