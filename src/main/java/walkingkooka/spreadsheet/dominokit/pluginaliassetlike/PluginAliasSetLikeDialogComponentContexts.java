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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.export.SpreadsheetExporterAlias;
import walkingkooka.spreadsheet.export.SpreadsheetExporterAliasSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfo;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterName;
import walkingkooka.spreadsheet.export.SpreadsheetExporterSelector;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAlias;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterAliasSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterAlias;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterAliasSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfo;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterName;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterSelector;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAlias;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionAliasSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionSelector;

/**
 * A collection of factory methods to create {@link PluginAliasSetLikeDialogComponentContext}.
 */
public final class PluginAliasSetLikeDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see PluginAliasSetLikeDialogComponentContextBasicConverterAliasSetConverters}
     */
    public static PluginAliasSetLikeDialogComponentContext<ConverterName, ConverterInfo, ConverterInfoSet, ConverterSelector, ConverterAlias, ConverterAliasSet> converters(final AppContext context) {
        return PluginAliasSetLikeDialogComponentContextBasicConverterAliasSetConverters.with(context);
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
     * {@see PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExporterAliasSetExporters}
     */
    public static PluginAliasSetLikeDialogComponentContext<SpreadsheetExporterName, SpreadsheetExporterInfo, SpreadsheetExporterInfoSet, SpreadsheetExporterSelector, SpreadsheetExporterAlias, SpreadsheetExporterAliasSet> exporters(final AppContext context) {
        return PluginAliasSetLikeDialogComponentContextBasicSpreadsheetExporterAliasSetExporters.with(context);
    }
    
    /**
     * {@see PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSet}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> findFunctions(final AppContext context) {
        return PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFindFunctions.with(context);
    }

    /**
     * {@see PluginAliasSetLikeDialogComponentContextBasicSpreadsheetFormatterAliasSetFormatters}
     */
    public static PluginAliasSetLikeDialogComponentContext<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet, SpreadsheetFormatterSelector, SpreadsheetFormatterAlias, SpreadsheetFormatterAliasSet> formatters(final AppContext context) {
        return PluginAliasSetLikeDialogComponentContextBasicSpreadsheetFormatterAliasSetFormatters.with(context);
    }
    
    /**
     * {@see PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFormulaFunctions}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> formulaFunctions(final AppContext context) {
        return PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFormulaFunctions.with(context);
    }

    /**
     * {@see PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFunctions}
     */
    public static PluginAliasSetLikeDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet, ExpressionFunctionSelector, ExpressionFunctionAlias, ExpressionFunctionAliasSet> functions(final AppContext context) {
        return PluginAliasSetLikeDialogComponentContextBasicExpressionFunctionAliasSetFunctions.with(context);
    }

    /**
     * {@see PluginAliasSetLikeDialogComponentContextBasicSpreadsheetImporterAliasSetImporters}
     */
    public static PluginAliasSetLikeDialogComponentContext<SpreadsheetImporterName, SpreadsheetImporterInfo, SpreadsheetImporterInfoSet, SpreadsheetImporterSelector, SpreadsheetImporterAlias, SpreadsheetImporterAliasSet> importers(final AppContext context) {
        return PluginAliasSetLikeDialogComponentContextBasicSpreadsheetImporterAliasSetImporters.with(context);
    }

    /**
     * Stop creation
     */
    private PluginAliasSetLikeDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
