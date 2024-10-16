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

package walkingkooka.spreadsheet.dominokit.pluginfoset;

import walkingkooka.convert.provider.ConverterInfo;
import walkingkooka.convert.provider.ConverterInfoSet;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.naming.Name;
import walkingkooka.plugin.PluginInfoLike;
import walkingkooka.plugin.PluginInfoSetLike;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfo;
import walkingkooka.spreadsheet.export.SpreadsheetExporterInfoSet;
import walkingkooka.spreadsheet.export.SpreadsheetExporterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfo;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfo;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterInfoSet;
import walkingkooka.spreadsheet.importer.SpreadsheetImporterName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfo;
import walkingkooka.tree.expression.function.provider.ExpressionFunctionInfoSet;

/**
 * A collection of factory methods to create {@link PluginInfoSetDialogComponentContext}.
 */
public final class PluginInfoSetDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetComparators}
     */
    public static PluginInfoSetDialogComponentContext<SpreadsheetComparatorName, SpreadsheetComparatorInfo, SpreadsheetComparatorInfoSet> comparators(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasic.comparators(
                context
        );
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicConverters}
     */
    public static PluginInfoSetDialogComponentContext<ConverterName, ConverterInfo, ConverterInfoSet> converters(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasic.converters(
                context
        );
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetExporters}
     */
    public static PluginInfoSetDialogComponentContext<SpreadsheetExporterName, SpreadsheetExporterInfo, SpreadsheetExporterInfoSet> exporters(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasic.exporters(
                context
        );
    }

    /**
     * {@see FakePluginInfoSetDialogComponentContext}
     */
    public static <N extends Name & Comparable<N>, I extends PluginInfoLike<I, N>, IS extends PluginInfoSetLike<N, I, IS>> PluginInfoSetDialogComponentContext<N, I, IS> fake() {
        return new FakePluginInfoSetDialogComponentContext<>();
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetExpressionFunctions}
     */
    public static PluginInfoSetDialogComponentContext<ExpressionFunctionName, ExpressionFunctionInfo, ExpressionFunctionInfoSet> expressionFunctions(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasic.expressionFunctions(
                context
        );
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetFormatters}
     */
    public static PluginInfoSetDialogComponentContext<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet> formatters(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasic.formatters(
                context
        );
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetImporters}
     */
    public static PluginInfoSetDialogComponentContext<SpreadsheetImporterName, SpreadsheetImporterInfo, SpreadsheetImporterInfoSet> importers(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasic.importers(
                context
        );
    }

    /**
     * {@see PluginInfoSetDialogComponentContextBasicSpreadsheetParsers}
     */
    public static PluginInfoSetDialogComponentContext<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet> parsers(final AppContext context) {
        return PluginInfoSetDialogComponentContextBasic.parsers(
                context
        );
    }

    /**
     * Stop creation
     */
    private PluginInfoSetDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
