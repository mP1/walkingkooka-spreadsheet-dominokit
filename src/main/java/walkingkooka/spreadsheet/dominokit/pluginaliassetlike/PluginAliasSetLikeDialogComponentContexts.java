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

package walkingkooka.spreadsheet.dominokit.pluginaliassetlike;

import walkingkooka.convert.provider.ConverterAlias;
import walkingkooka.convert.provider.ConverterAliasSet;
import walkingkooka.convert.provider.ConverterInfo;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginAliasLike;
import walkingkooka.plugin.PluginAliasSetLike;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorAlias;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorAliasSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorSelector;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAlias;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterInfo;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterName;
import walkingkooka.spreadsheet.export.provider.SpreadsheetExporterSelector;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterAlias;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAlias;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterInfo;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterName;
import walkingkooka.spreadsheet.importer.provider.SpreadsheetImporterSelector;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserAlias;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;
import walkingkooka.validation.form.provider.FormHandlerAlias;
import walkingkooka.validation.form.provider.FormHandlerAliasSet;
import walkingkooka.validation.form.provider.FormHandlerInfo;
import walkingkooka.validation.form.provider.FormHandlerInfoSet;
import walkingkooka.validation.form.provider.FormHandlerName;
import walkingkooka.validation.form.provider.FormHandlerSelector;
import walkingkooka.validation.provider.ValidatorAlias;
import walkingkooka.validation.provider.ValidatorAliasSet;
import walkingkooka.validation.provider.ValidatorInfo;
import walkingkooka.validation.provider.ValidatorInfoSet;
import walkingkooka.validation.provider.ValidatorName;
import walkingkooka.validation.provider.ValidatorSelector;

/**
 * A collection of factory methods to create {@link PluginAliasSetLikeDialogComponentContext}.
 */
public final class PluginAliasSetLikeDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSetComparators}
     */
    public static PluginAliasSetLikeDialogComponentContext<SpreadsheetComparatorName, SpreadsheetComparatorInfo, SpreadsheetComparatorInfoSet, SpreadsheetComparatorSelector, SpreadsheetComparatorAlias, SpreadsheetComparatorAliasSet> comparators(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetComparatorAliasSetComparators.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSetConverters}
     */
    public static PluginAliasSetLikeDialogComponentContext<ConverterName, ConverterInfo, ConverterInfoSet, ConverterSelector, ConverterAlias, ConverterAliasSet> converters(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextConverterAliasSetConverters.with(context);
    }

    /**
     * {@see FakePluginAliasSetLikeDialogComponentContext}
     */
    public static <N extends Name & Comparable<N>,
        I extends PluginInfoLike<I, N>,
        IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
        S extends PluginSelectorLike<N>,
        A extends PluginAliasLike<N, S, A>,
        AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
    PluginAliasSetLikeDialogComponentContext<N, I, IS, S, A, AS> fake() {
        return new FakePluginAliasSetLikeDialogComponentContext<>();
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSetExporters}
     */
    public static PluginAliasSetLikeDialogComponentContext<SpreadsheetExporterName, SpreadsheetExporterInfo, SpreadsheetExporterInfoSet, SpreadsheetExporterSelector, SpreadsheetExporterAlias, SpreadsheetExporterAliasSet> exporters(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetExporterAliasSetExporters.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSet}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> findFunctions(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFindFunctions.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSetFormatters}
     */
    public static PluginAliasSetLikeDialogComponentContext<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet, SpreadsheetFormatterSelector, SpreadsheetFormatterAlias, SpreadsheetFormatterAliasSet> formatters(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetFormatterAliasSetFormatters.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFormattingFunctions}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> formattingFunctions(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFormattingFunctions.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSetComparators}
     */
    public static PluginAliasSetLikeDialogComponentContext<FormHandlerName, FormHandlerInfo, FormHandlerInfoSet, FormHandlerSelector, FormHandlerAlias, FormHandlerAliasSet> formHandlers(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextFormHandlerAliasSetFormHandlers.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFormulaFunctions}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> formulaFunctions(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFormulaFunctions.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFunctions}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> functions(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetFunctions.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSetImporters}
     */
    public static PluginAliasSetLikeDialogComponentContext<SpreadsheetImporterName, SpreadsheetImporterInfo, SpreadsheetImporterInfoSet, SpreadsheetImporterSelector, SpreadsheetImporterAlias, SpreadsheetImporterAliasSet> importers(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetImporterAliasSetImporters.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers}
     */
    public static PluginAliasSetLikeDialogComponentContext<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet> parsers(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextSpreadsheetParserAliasSetParsers.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSet}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> scriptingFunctions(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetScriptingFunctions.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidators}
     */
    public static PluginAliasSetLikeDialogComponentContext<ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet> validators(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidators.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetValidationFunctions}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> validatorFunctions(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextExpressionFunctionAliasSetValidationFunctions.with(context);
    }

    /**
     * {@see AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidationValidators}
     */
    public static PluginAliasSetLikeDialogComponentContext<ValidatorName, ValidatorInfo, ValidatorInfoSet, ValidatorSelector, ValidatorAlias, ValidatorAliasSet> validatorValidators(final AppContext context) {
        return AppContextPluginAliasSetLikeDialogComponentContextValidatorAliasSetValidationValidators.with(context);
    }

    /**
     * Stop creation
     */
    private PluginAliasSetLikeDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
