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

package walkingkooka.spreadsheet.dominokit.pluginfosetlink;

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
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorAlias;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorAliasSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfo;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorInfoSet;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorSelector;
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
import walkingkooka.spreadsheet.parser.SpreadsheetParserAlias;
import walkingkooka.spreadsheet.parser.SpreadsheetParserAliasSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfo;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;

/**
 * A collection of factory methods to create {@link PluginInfoSetLikeDialogComponentContext}.
 */
public final class PluginInfoSetLikeDialogComponentContexts implements PublicStaticHelper {

    /**
     * {@see PluginInfoSetLikeDialogComponentContextBasicSpreadsheetComparators}
     */
    public static PluginInfoSetLikeDialogComponentContext<SpreadsheetComparatorName, SpreadsheetComparatorInfo, SpreadsheetComparatorInfoSet, SpreadsheetComparatorSelector, SpreadsheetComparatorAlias, SpreadsheetComparatorAliasSet> comparators(final AppContext context) {
        return PluginInfoSetLikeDialogComponentContextBasic.comparators(
                context
        );
    }

    /**
     * {@see PluginInfoSetLikeDialogComponentContextBasicConverters}
     */
    public static PluginInfoSetLikeDialogComponentContext<ConverterName, ConverterInfo, ConverterInfoSet, ConverterSelector, ConverterAlias, ConverterAliasSet> converters(final AppContext context) {
        return PluginInfoSetLikeDialogComponentContextBasic.converters(
                context
        );
    }

    /**
     * {@see PluginInfoSetLikeDialogComponentContextBasicSpreadsheetExporters}
     */
    public static PluginInfoSetLikeDialogComponentContext<SpreadsheetExporterName, SpreadsheetExporterInfo, SpreadsheetExporterInfoSet, SpreadsheetExporterSelector, SpreadsheetExporterAlias, SpreadsheetExporterAliasSet> exporters(final AppContext context) {
        return PluginInfoSetLikeDialogComponentContextBasic.exporters(
                context
        );
    }

    /**
     * {@see FakePluginInfoSetLikeDialogComponentContext}
     */
    public static <N extends Name & Comparable<N>,
            I extends PluginInfoLike<I, N>,
            IS extends PluginInfoSetLike<N, I, IS, S, A, AS>,
            S extends PluginSelectorLike<N>,
            A extends PluginAliasLike<N, S, A>,
            AS extends PluginAliasSetLike<N, I, IS, S, A, AS>>
    PluginInfoSetLikeDialogComponentContext<N, I, IS, S, A, AS> fake() {
        return new FakePluginInfoSetLikeDialogComponentContext<>();
    }

    /**
     * {@see PluginInfoSetLikeDialogComponentContextBasicSpreadsheetFormatters}
     */
    public static PluginInfoSetLikeDialogComponentContext<SpreadsheetFormatterName, SpreadsheetFormatterInfo, SpreadsheetFormatterInfoSet, SpreadsheetFormatterSelector, SpreadsheetFormatterAlias, SpreadsheetFormatterAliasSet> formatters(final AppContext context) {
        return PluginInfoSetLikeDialogComponentContextBasic.formatters(
                context
        );
    }

    /**
     * {@see PluginInfoSetLikeDialogComponentContextBasicSpreadsheetImporters}
     */
    public static PluginInfoSetLikeDialogComponentContext<SpreadsheetImporterName, SpreadsheetImporterInfo, SpreadsheetImporterInfoSet, SpreadsheetImporterSelector, SpreadsheetImporterAlias, SpreadsheetImporterAliasSet> importers(final AppContext context) {
        return PluginInfoSetLikeDialogComponentContextBasic.importers(
                context
        );
    }

    /**
     * {@see PluginInfoSetLikeDialogComponentContextBasicSpreadsheetParsers}
     */
    public static PluginInfoSetLikeDialogComponentContext<SpreadsheetParserName, SpreadsheetParserInfo, SpreadsheetParserInfoSet, SpreadsheetParserSelector, SpreadsheetParserAlias, SpreadsheetParserAliasSet> parsers(final AppContext context) {
        return PluginInfoSetLikeDialogComponentContextBasic.parsers(
                context
        );
    }

    /**
     * Stop creation
     */
    private PluginInfoSetLikeDialogComponentContexts() {
        throw new UnsupportedOperationException();
    }
}
