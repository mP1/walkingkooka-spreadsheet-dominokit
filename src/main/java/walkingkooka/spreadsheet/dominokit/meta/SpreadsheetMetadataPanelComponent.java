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

package walkingkooka.spreadsheet.dominokit.meta;

import elemental2.dom.HTMLTableElement;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.provider.ConverterAliasSet;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.environment.AuditInfo;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.plugin.PluginNameSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorAliasSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorNameList;
import walkingkooka.spreadsheet.dominokit.HtmlComponent;
import walkingkooka.spreadsheet.dominokit.HtmlComponentDelegator;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetFormComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.dom.TBodyComponent;
import walkingkooka.spreadsheet.dominokit.dom.TableComponent;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetMetadataPropertySelectHistoryToken;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.validation.form.provider.FormHandlerAliasSet;
import walkingkooka.validation.form.provider.FormHandlerSelector;
import walkingkooka.validation.provider.ValidatorAliasSet;

import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * A ui that displays numerous {@link SpreadsheetMetadata} properties with support for editing the individual values.
 * Note it does not contain any HistoryToken watching logic.
 */
public final class SpreadsheetMetadataPanelComponent implements SpreadsheetFormComponentLifecycle<HTMLTableElement, SpreadsheetMetadataPanelComponent>,
    LoadedSpreadsheetMetadataRequired,
    NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    HtmlComponentDelegator<HTMLTableElement, SpreadsheetMetadataPanelComponent> {

    public static SpreadsheetMetadataPanelComponent with(final SpreadsheetMetadataPanelComponentContext context) {
        return new SpreadsheetMetadataPanelComponent(
            Objects.requireNonNull(context, "context")
        );
    }

    private SpreadsheetMetadataPanelComponent(final SpreadsheetMetadataPanelComponentContext context) {
        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.context = context;

        final List<SpreadsheetMetadataPanelComponentItem<?, ?, ?>> items = Lists.array();
        items.add(this.spreadsheetId());
        items.add(this.spreadsheetName());
        items.add(this.createdBy());
        items.add(this.createdTimestamp());
        items.add(this.modifiedBy());
        items.add(this.modifiedTimestamp());
        items.add(this.locale());

        // Number
        items.add(this.expressionNumberKind());
        items.add(this.precision());
        items.add(this.roundingMode());

        items.add(this.valueSeparator());

        items.add(this.dateTimeOffset());
        items.add(this.defaultYear());
        items.add(this.twoDigitYear());

        items.add(this.decimalNumberDigitCount());

        items.add(this.dateTimeSymbols());
        items.add(this.dateFormatter());
        items.add(this.dateParser());

        items.add(this.dateTimeFormatter());
        items.add(this.dateTimeParser());

        items.add(this.decimalNumberSymbols());
        items.add(this.numberFormatter());
        items.add(this.numberParser());

        items.add(this.textFormatter());

        items.add(this.timeFormatter());
        items.add(this.timeParser());

        items.add(this.plugins());

        items.add(this.comparators());
        items.add(this.converters());
        items.add(this.exporters());
        items.add(this.expressionFunctions());
        items.add(this.formatters());
        items.add(this.formHandlers());
        items.add(this.importers());
        items.add(this.parsers());
        items.add(this.validators());

        items.add(this.findConverter());
        items.add(this.findFunctions());

        items.add(this.formulaConverter());
        items.add(this.formulaFunctions());

        items.add(this.formattingConverter());
        items.add(this.formattingFunctions());

        items.add(this.defaultFormHandler());

        items.add(this.scriptingFunctions());
        items.add(this.scriptingConverter());

        items.add(this.sortComparators());
        items.add(this.sortConverter());

        items.add(this.validationConverter());
        items.add(this.validationFunctions());
        items.add(this.validationValidators());

        final TBodyComponent tBody = HtmlElementComponent.tbody();
        this.table = HtmlElementComponent.table()
            .appendChild(tBody);

        for (final SpreadsheetMetadataPanelComponentItem<?, ?, ?> item : items) {
            tBody.appendChild(
                HtmlElementComponent.tr()
                    .appendChild(
                        HtmlElementComponent.td()
                            .setCssProperty("text-wrap", "nowrap")
                            .setPaddingTop(PADDING_TOP_BOTTOM)
                            .setPaddingBottom(PADDING_TOP_BOTTOM)
                            .setText(
                                item.label()
                            )
                    ).appendChild(
                        HtmlElementComponent.td()
                            .setPaddingTop(PADDING_TOP_BOTTOM)
                            .setPaddingBottom(PADDING_TOP_BOTTOM)
                            .appendChild(item)
                    )
            );

            // eg: /1/SpreadsheetName/metadata/SpreadsheetName
            item.addFocusListener(
                (e) -> context.pushHistoryToken(
                    item.historyToken(
                            context.historyToken()
                    )
                )
            );
        }

        this.items = items;
    }

    private final static String PADDING_TOP_BOTTOM = "3px";

    // factory methods to create the individual SpreadsheetMetadataPanelComponentItem for a given property.

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> spreadsheetId() {
        return readOnlyText(
            SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
            Optional.empty(), // no label
            SpreadsheetId::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> spreadsheetName() {
        return this.spreadsheetNameComponent();
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> createdBy() {
        return readOnlyText(
            SpreadsheetMetadataPropertyName.AUDIT_INFO,
            Optional.of("Created by"),
            (final AuditInfo a) -> a.createdBy().value()
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> createdTimestamp() {
        return readOnlyText(
            SpreadsheetMetadataPropertyName.AUDIT_INFO,
            Optional.of("Create Timestamp"),
            (final AuditInfo a) -> this.context.formatDateTime(a.createdTimestamp())
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> modifiedBy() {
        return readOnlyText(
            SpreadsheetMetadataPropertyName.AUDIT_INFO,
            Optional.of("Modified by"),
            (final AuditInfo a) -> a.createdBy().value()
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> modifiedTimestamp() {
        return readOnlyText(
            SpreadsheetMetadataPropertyName.AUDIT_INFO,
            Optional.of("Modified Timestamp"),
            (final AuditInfo a) -> this.context.formatDateTime(a.createdTimestamp())
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> locale() {
        return link(
            SpreadsheetMetadataPropertyName.LOCALE
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> expressionNumberKind() {
        return this.enumValue(
            SpreadsheetMetadataPropertyName.EXPRESSION_NUMBER_KIND,
            Lists.of(ExpressionNumberKind.values())
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> decimalNumberDigitCount() {
        return SpreadsheetMetadataPanelComponentItem.decimalNumberDigitCount(
            this.context
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> precision() {
        return SpreadsheetMetadataPanelComponentItem.precision(
            this.context
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> roundingMode() {
        return this.enumValue(
            SpreadsheetMetadataPropertyName.ROUNDING_MODE,
            Lists.of(RoundingMode.values())
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> valueSeparator() {
        return readOnlyText(
            SpreadsheetMetadataPropertyName.VALUE_SEPARATOR,
            Optional.empty(),
            Object::toString
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> dateTimeOffset() {
        return SpreadsheetMetadataPanelComponentItem.dateTimeOffset(
            this.context
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> defaultYear() {
        return number(
            SpreadsheetMetadataPropertyName.DEFAULT_YEAR,
            1800,
            2100
        );
    }

    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> twoDigitYear() {
        return number(
            SpreadsheetMetadataPropertyName.TWO_DIGIT_YEAR,
            0,
            99
        );
    }

    private SpreadsheetMetadataPanelComponentItem<DateTimeSymbols, ?, ?> dateTimeSymbols() {
        return link(
            SpreadsheetMetadataPropertyName.DATE_TIME_SYMBOLS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetFormatterSelector, ?, ?> dateFormatter() {
        return link(
            SpreadsheetMetadataPropertyName.DATE_FORMATTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetParserSelector, ?, ?> dateParser() {
        return link(
            SpreadsheetMetadataPropertyName.DATE_PARSER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetFormatterSelector, ?, ?> dateTimeFormatter() {
        return link(
            SpreadsheetMetadataPropertyName.DATE_TIME_FORMATTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetParserSelector, ?, ?> dateTimeParser() {
        return link(
            SpreadsheetMetadataPropertyName.DATE_TIME_PARSER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<DecimalNumberSymbols, ?, ?> decimalNumberSymbols() {
        return link(
            SpreadsheetMetadataPropertyName.DECIMAL_NUMBER_SYMBOLS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetFormatterSelector, ?, ?> numberFormatter() {
        return link(
            SpreadsheetMetadataPropertyName.NUMBER_FORMATTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetParserSelector, ?, ?> numberParser() {
        return link(
            SpreadsheetMetadataPropertyName.NUMBER_PARSER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetFormatterSelector, ?, ?> textFormatter() {
        return link(
            SpreadsheetMetadataPropertyName.TEXT_FORMATTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetFormatterSelector, ?, ?> timeFormatter() {
        return link(
            SpreadsheetMetadataPropertyName.TIME_FORMATTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetParserSelector, ?, ?> timeParser() {
        return link(
            SpreadsheetMetadataPropertyName.TIME_PARSER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<PluginNameSet, ?, ?> plugins() {
        return link(
            SpreadsheetMetadataPropertyName.PLUGINS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetComparatorAliasSet, ?, ?> comparators() {
        return link(
            SpreadsheetMetadataPropertyName.COMPARATORS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ConverterAliasSet, ?, ?> converters() {
        return link(
            SpreadsheetMetadataPropertyName.CONVERTERS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<FormHandlerSelector, ?, ?> defaultFormHandler() {
        return link(
            SpreadsheetMetadataPropertyName.DEFAULT_FORM_HANDLER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetExporterAliasSet, ?, ?> exporters() {
        return link(
            SpreadsheetMetadataPropertyName.EXPORTERS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ExpressionFunctionAliasSet, ?, ?> expressionFunctions() {
        return link(
            SpreadsheetMetadataPropertyName.FUNCTIONS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ConverterSelector, ?, ?> findConverter() {
        return link(
            SpreadsheetMetadataPropertyName.FIND_CONVERTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ExpressionFunctionAliasSet, ?, ?> findFunctions() {
        return link(
            SpreadsheetMetadataPropertyName.FIND_FUNCTIONS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ConverterSelector, ?, ?> formulaConverter() {
        return link(
            SpreadsheetMetadataPropertyName.FORMULA_CONVERTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ExpressionFunctionAliasSet, ?, ?> formulaFunctions() {
        return link(
            SpreadsheetMetadataPropertyName.FORMULA_FUNCTIONS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetFormatterAliasSet, ?, ?> formatters() {
        return link(
            SpreadsheetMetadataPropertyName.FORMATTERS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ConverterSelector, ?, ?> formattingConverter() {
        return link(
            SpreadsheetMetadataPropertyName.FORMATTING_CONVERTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ExpressionFunctionAliasSet, ?, ?> formattingFunctions() {
        return link(
            SpreadsheetMetadataPropertyName.FORMATTING_FUNCTIONS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<FormHandlerAliasSet, ?, ?> formHandlers() {
        return link(
            SpreadsheetMetadataPropertyName.FORM_HANDLERS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetImporterAliasSet, ?, ?> importers() {
        return link(
            SpreadsheetMetadataPropertyName.IMPORTERS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetParserAliasSet, ?, ?> parsers() {
        return link(
            SpreadsheetMetadataPropertyName.PARSERS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ConverterSelector, ?, ?> scriptingConverter() {
        return link(
            SpreadsheetMetadataPropertyName.SCRIPTING_CONVERTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ExpressionFunctionAliasSet, ?, ?> scriptingFunctions() {
        return link(
            SpreadsheetMetadataPropertyName.SCRIPTING_FUNCTIONS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<SpreadsheetComparatorNameList, ?, ?> sortComparators() {
        return link(
            SpreadsheetMetadataPropertyName.SORT_COMPARATORS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ConverterSelector, ?, ?> sortConverter() {
        return link(
            SpreadsheetMetadataPropertyName.SORT_CONVERTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ValidatorAliasSet, ?, ?> validators() {
        return link(
            SpreadsheetMetadataPropertyName.VALIDATORS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ConverterSelector, ?, ?> validationConverter() {
        return link(
            SpreadsheetMetadataPropertyName.VALIDATION_CONVERTER
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ExpressionFunctionAliasSet, ?, ?> validationFunctions() {
        return link(
            SpreadsheetMetadataPropertyName.VALIDATION_FUNCTIONS
        );
    }

    private SpreadsheetMetadataPanelComponentItem<ValidatorAliasSet, ?, ?> validationValidators() {
        return link(
            SpreadsheetMetadataPropertyName.VALIDATION_VALIDATORS
        );
    }

    /**
     * Factory that displays a slider with thumbs for each Enum value.
     */
    private <T extends Enum<T>> SpreadsheetMetadataPanelComponentItemEnum<T> enumValue(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                                       final List<T> values) {
        return SpreadsheetMetadataPanelComponentItem.enumValue(
            propertyName,
            values,
            this.context
        );
    }

    /**
     * Factory that creates a link that opens a dialog.
     */
    private <T> SpreadsheetMetadataPanelComponentItem<T, ?, ?> link(final SpreadsheetMetadataPropertyName<T> propertyName) {
        return SpreadsheetMetadataPanelComponentItem.link(
            propertyName,
            this.context
        );
    }

    /**
     * Factory that creates a number text field to edit a integer {@link SpreadsheetMetadataPropertyName} value.
     */
    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> number(final SpreadsheetMetadataPropertyName<Integer> propertyName,
                                                               final int min,
                                                               final int max) {
        return SpreadsheetMetadataPanelComponentItem.number(
            propertyName,
            min,
            max,
            this.context
        );
    }

    /**
     * Factory that creates a single ROW.
     */
    private <T> SpreadsheetMetadataPanelComponentItem<?, ?, ?> readOnlyText(final SpreadsheetMetadataPropertyName<T> propertyName,
                                                                         final Optional<String> label,
                                                                         final Function<T, String> formatter) {
        return SpreadsheetMetadataPanelComponentItem.readOnlyText(
            propertyName,
            label,
            formatter,
            this.context
        );
    }

    /**
     * Factory that creates a single ROW with a single {@link org.dominokit.domino.ui.forms.TextBox}.
     */
    private SpreadsheetMetadataPanelComponentItem<?, ?, ?> spreadsheetNameComponent() {
        return SpreadsheetMetadataPanelComponentItem.spreadsheetName(
            this.context
        );
    }

    /**
     * Factory that creates a single ROW with a single {@link org.dominokit.domino.ui.forms.TextBox}.
     */
    private <T> SpreadsheetMetadataPanelComponentItem<?, ?, ?> text(final SpreadsheetMetadataPropertyName<String> propertyName) {
        return SpreadsheetMetadataPanelComponentItem.text(
            propertyName,
            this.context
        );
    }

    private final SpreadsheetMetadataPanelComponentContext context;

    // HtmlComponentDelegator...........................................................................................

    @Override
    public HtmlComponent<HTMLTableElement, ?> htmlComponent() {
        return this.table;
    }

    private final TableComponent table;

    // SpreadsheetFormComponentLifecycle................................................................................

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void open(final RefreshContext context) {
        this.open = true;
    }

    @Override
    public void close(final RefreshContext context) {
        this.open = false;
    }

    private boolean open;

    @Override
    public void openGiveFocus(final RefreshContext context) {
        final HistoryToken token = context.historyToken();
        if (token instanceof SpreadsheetMetadataPropertySelectHistoryToken) {
            final SpreadsheetMetadataPropertyName<?> propertyName = token.metadataPropertyName()
                .orElse(null);

            final Optional<SpreadsheetMetadataPanelComponentItem<?, ?, ?>> maybeItem = this.items.stream()
                .filter(i -> i.propertyName.equals(propertyName))
                .findFirst();

            if (maybeItem.isPresent()) {
                context.giveFocus(
                    maybeItem.get()::focus
                );
            }
        }
    }

    @Override
    public void refresh(final RefreshContext context) {
        // Before refreshing verify a loaded Metadata with LOCALE and other properties are present otherwise
        // rendering the panel will fail complaining LOCALE Is absent.
        //
        // https://github.com/mP1/walkingkooka-spreadsheet-dominokit/issues/1018
        if (this.context.spreadsheetMetadata()
            .get(SpreadsheetMetadataPropertyName.LOCALE)
            .isPresent()) {
            this.items.forEach(i -> i.refresh(context));
        } else {
            context.debug(this.getClass().getSimpleName() + ".refresh metadata missing " + SpreadsheetMetadataPropertyName.LOCALE + " skip until loaded.");
        }
    }

    private final List<SpreadsheetMetadataPanelComponentItem<?, ?, ?>> items;

    @Override
    public boolean isEditing() {
        return false;
    }

    // SpreadsheetMetadataFetcherWatcher.............................................,.........................................

    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata) {
        this.refreshIfOpen(this.context);
    }

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas) {
        // IGNORE
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.element().toString();
    }

    // id...............................................................................................................

    static String id(final SpreadsheetMetadataPropertyName<?> propertyName) {
        return "metadata-" + propertyName.value();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            for (final SpreadsheetMetadataPanelComponentItem<?, ?, ?> item : this.items) {
                item.printTree(printer);
            }
        }
        printer.outdent();
    }
}
