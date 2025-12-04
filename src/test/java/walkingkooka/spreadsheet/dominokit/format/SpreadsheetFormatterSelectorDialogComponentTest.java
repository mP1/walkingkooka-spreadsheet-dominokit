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

import org.junit.jupiter.api.Test;
import walkingkooka.Either;
import walkingkooka.color.Color;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.provider.ConverterName;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.plugin.ProviderContext;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetFormatterFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatter;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterInfoSet;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProviderSamplesContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelectorToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetFormatterSelectorDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetFormatterSelectorDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    private final static AnchoredSpreadsheetSelection CELL = SpreadsheetSelection.A1.setDefaultAnchor();

    // cell / date......................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellDateFormatterNameMissingPattern() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/formatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetFormatterName.DATE + "",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/cell/A1/formatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/cell/A1/formatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/cell/A1/formatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/cell/A1/formatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/cell/A1/formatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d/m/yy] id=SpreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d%20mmm%20yyyy] id=SpreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d%20mmmm%20yyyy] id=SpreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dddd,%20d%20mmmm%20yyyy] id=SpreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    ERROR Empty \"text\"\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date] id=SpreadsheetFormatterSelector-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeCellDate() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/formatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetFormatterName.DATE + " dd/mm/yyyy",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/cell/A1/formatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/cell/A1/formatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/cell/A1/formatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/cell/A1/formatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/cell/A1/formatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d/m/yy] id=SpreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d%20mmm%20yyyy] id=SpreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d%20mmmm%20yyyy] id=SpreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dddd,%20d%20mmmm%20yyyy] id=SpreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyy] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"d\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyyd] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyydd] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyyddd] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyydddd] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyym] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyymm] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyymmm] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyymmmm] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyymmmmm] id=SpreadsheetFormatterSelector-appender-append-8-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20/mm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20d/mm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20ddd/mm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dddd/mm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20ddmm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd//yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/m/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mmm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mmmm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mmmmm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mmyyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date dd/mm/yyyy] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyy] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // cell / date-time.................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellDateTime() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/formatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetFormatterName.DATE_TIME + " dd/mm/yyyy hh:mm:ss",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/cell/A1/formatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/cell/A1/formatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/cell/A1/formatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/cell/A1/formatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/cell/A1/formatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20d/m/yy,%20h:mm%20AM/PM] id=SpreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99, 12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=SpreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999, 12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=SpreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999 at 12:58:00 PM\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=SpreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999 at 12:58:00 PM\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999 12:58:00\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss.] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss0] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssA/P] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssa/p] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssam/pm] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"d\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssd] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssdd] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssddd] id=SpreadsheetFormatterSelector-appender-append-8-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssdddd] id=SpreadsheetFormatterSelector-appender-append-9-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssh] id=SpreadsheetFormatterSelector-appender-append-10-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:sshh] id=SpreadsheetFormatterSelector-appender-append-11-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssm] id=SpreadsheetFormatterSelector-appender-append-12-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmm] id=SpreadsheetFormatterSelector-appender-append-13-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmm] id=SpreadsheetFormatterSelector-appender-append-14-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmmm] id=SpreadsheetFormatterSelector-appender-append-15-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=SpreadsheetFormatterSelector-appender-append-16-Link\n" +
                "                  \"yy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssyy] id=SpreadsheetFormatterSelector-appender-append-17-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssyyyy] id=SpreadsheetFormatterSelector-appender-append-18-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20d/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20ddd/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dddd/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20ddmm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd//yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/m/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mmm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mmmm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mmmmm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mmyyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "                  \" \" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyyhh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-6-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20h:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-6-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hhmm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-7-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh::ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-8-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:m:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-8-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mmss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-9-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:] id=SpreadsheetFormatterSelector-removeOrReplace-remove-10-Link\n" +
                "                      \"s\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:s] id=SpreadsheetFormatterSelector-removeOrReplace-remove-10-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-time dd/mm/yyyy hh:mm:ss] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // cell / number....................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellNumber() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/formatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetFormatterName.NUMBER + " $#0.00",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/cell/A1/formatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/cell/A1/formatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/cell/A1/formatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/cell/A1/formatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    123.5\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -123.5\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0.\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    124\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -124\n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0\n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12,350%\n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -12,350%\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0%\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $123*50\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $-123*50\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $0*00\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"$#0.00\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $1234*50\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"#\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00%23] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"$\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00$] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"%\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00%25] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \",\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00,] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00.] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00/] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"?\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00?] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"E\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00E] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"$\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20%230.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \"#\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$0.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%23.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%23000] id=SpreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.0] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.0] id=SpreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [number $#0.00] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/number%20$%230.00] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // cell / text......................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellText() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/formatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetFormatterName.TEXT + " @ \"Hello\"",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/cell/A1/formatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/cell/A1/formatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/cell/A1/formatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/cell/A1/formatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/cell/A1/formatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Default\n" +
                "                  \"@\" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20@] id=SpreadsheetFormatterSelector-Default-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Hello 123\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"@ \"Hello\"\" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20@%20%22Hello%22] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Hello World 123 Hello\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"* \" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20@%20%22Hello%22*%20] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"@\" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20@%20%22Hello%22@] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"_ \" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20@%20%22Hello%22_%20] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"@\" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20%20%22Hello%22] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \" \" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20@%22Hello%22] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"\"Hello\"\" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20@%20] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [text @ \"Hello\"] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/text%20@%20%22Hello%22] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // cell / time......................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellTime() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/formatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetFormatterName.TIME + " hh:mm",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/cell/A1/formatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/cell/A1/formatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/cell/A1/formatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/cell/A1/formatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/cell/A1/formatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"h:mm AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20h:mm%20AM/PM] id=SpreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20h:mm:ss%20AM/PM] id=SpreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"hh:mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mm] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mm.] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mm0] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mmA/P] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mmAM/PM] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mma/p] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mmam/pm] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mmh] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mmhh] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"s\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mms] id=SpreadsheetFormatterSelector-appender-append-8-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mmss] id=SpreadsheetFormatterSelector-appender-append-9-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20:mm] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/cell/A1/formatter/save/time%20h:mm] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hhmm] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:m] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [time hh:mm] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/time%20hh:mm] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // metadata / date..................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataDate() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/dateFormatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetFormatterName.DATE + " dd/mm/yyyy",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Date Formatter (dateFormatter)\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        TabsComponent\n" +
                "          TAB 0 SELECTED\n" +
                "            \"Date\" DISABLED id=SpreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=SpreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=SpreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=SpreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=SpreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=SpreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20d/m/yy] id=SpreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20d%20mmm%20yyyy] id=SpreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20d%20mmmm%20yyyy] id=SpreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dddd,%20d%20mmmm%20yyyy] id=SpreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyy] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"d\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyyd] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyydd] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyyddd] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyydddd] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyym] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyymm] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyymmm] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyymmmm] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyymmmmm] id=SpreadsheetFormatterSelector-appender-append-8-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20/mm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20d/mm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20ddd/mm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dddd/mm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20ddmm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd//yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/m/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mmm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mmmm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mmmmm/yyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mmyyyy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yy] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date dd/mm/yyyy] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyy] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20yyyy/mm/dd] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // metadata / date-time.............................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataDateTime() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/dateTimeFormatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetFormatterName.DATE_TIME + " dd/mm/yyyy hh:mm:ss",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Date Time Formatter (dateTimeFormatter)\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        TabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=SpreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1 SELECTED\n" +
                "            \"Date Time\" DISABLED id=SpreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=SpreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=SpreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=SpreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=SpreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20d/m/yy,%20h:mm%20AM/PM] id=SpreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99, 12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=SpreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999, 12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=SpreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999 at 12:58:00 PM\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=SpreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999 at 12:58:00 PM\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999 12:58:00\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss.] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss0] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssA/P] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssa/p] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssam/pm] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"d\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssd] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssdd] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssddd] id=SpreadsheetFormatterSelector-appender-append-8-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssdddd] id=SpreadsheetFormatterSelector-appender-append-9-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssh] id=SpreadsheetFormatterSelector-appender-append-10-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:sshh] id=SpreadsheetFormatterSelector-appender-append-11-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssm] id=SpreadsheetFormatterSelector-appender-append-12-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmm] id=SpreadsheetFormatterSelector-appender-append-13-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmm] id=SpreadsheetFormatterSelector-appender-append-14-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmmm] id=SpreadsheetFormatterSelector-appender-append-15-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=SpreadsheetFormatterSelector-appender-append-16-Link\n" +
                "                  \"yy\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssyy] id=SpreadsheetFormatterSelector-appender-append-17-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssyyyy] id=SpreadsheetFormatterSelector-appender-append-18-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20d/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20ddd/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dddd/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20ddmm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd//yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/m/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mmm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mmmm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mmmmm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mmyyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yy%20hh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "                  \" \" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyyhh:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-6-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20h:mm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-6-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hhmm:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-7-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh::ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-8-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:m:ss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-8-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mmss] id=SpreadsheetFormatterSelector-removeOrReplace-remove-9-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:] id=SpreadsheetFormatterSelector-removeOrReplace-remove-10-Link\n" +
                "                      \"s\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:s] id=SpreadsheetFormatterSelector-removeOrReplace-remove-10-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-time dd/mm/yyyy hh:mm:ss] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20yyyy/mm/dd%20hh:mm] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // metadata / error.................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataError() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/errorFormatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetFormatterName.NUMBER + " $#0.00",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Error Formatter (errorFormatter)\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        TabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=SpreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=SpreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2 SELECTED\n" +
                "            \"Error\" DISABLED id=SpreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=SpreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=SpreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=SpreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    123.5\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -123.5\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0.\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    124\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -124\n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0\n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12,350%\n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -12,350%\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0%\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $123*50\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $-123*50\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $0*00\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"$#0.00\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $1234*50\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00%23] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00$] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"%\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00%25] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \",\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00,] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00.] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00/] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"?\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00?] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"E\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00E] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20%230.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$0.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%23.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%23000] id=SpreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.0] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.0] id=SpreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [number $#0.00] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number%20$%230.00] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/badge-error%20text%20@] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // metadata / number................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataNumber() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/numberFormatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetFormatterName.NUMBER + " $#0.00",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Number Formatter (numberFormatter)\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        TabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=SpreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=SpreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=SpreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3 SELECTED\n" +
                "            \"Number\" DISABLED id=SpreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=SpreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=SpreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    123.5\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -123.5\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230.%23%23%23] id=SpreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0.\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    124\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -124\n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230] id=SpreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0\n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12,350%\n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -12,350%\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%23,%23%230%25] id=SpreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0%\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $123*50\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $-123*50\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%23,%23%230.00] id=SpreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $0*00\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"$#0.00\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $1234*50\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00%23] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00$] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"%\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00%25] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \",\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00,] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00.] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00/] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"?\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00?] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"E\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00E] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20%230.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$0.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%23.00] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%23000] id=SpreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.0] id=SpreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.0] id=SpreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [number $#0.00] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%20$%230.00] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number%200.%23;0.%23;0] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // metadata / text..................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataText() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/textFormatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetFormatterName.TEXT + " @ \"Hello\"",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Text Formatter (textFormatter)\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        TabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=SpreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=SpreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=SpreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=SpreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4 SELECTED\n" +
                "            \"Text\" DISABLED id=SpreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=SpreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/time] id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Default\n" +
                "                  \"@\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@] id=SpreadsheetFormatterSelector-Default-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Hello 123\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"@ \"Hello\"\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@%20%22Hello%22] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Hello World 123 Hello\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"* \" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@%20%22Hello%22*%20] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"@\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@%20%22Hello%22@] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"_ \" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@%20%22Hello%22_%20] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"@\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20%20%22Hello%22] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \" \" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@%22Hello%22] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"\"Hello\"\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@%20] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [text @ \"Hello\"] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@%20%22Hello%22] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text%20@] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // metadata / time..................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataTime() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/timeFormatter")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetFormatterSelectorDialogComponent.with(
                AppContextSpreadsheetFormatterSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetFormatterName.TIME + " hh:mm",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Time Formatter (timeFormatter)\n" +
                "    id=SpreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        TabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=SpreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=SpreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=SpreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=SpreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=SpreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5 SELECTED\n" +
                "            \"Time\" DISABLED id=SpreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Accounting\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/accounting] id=SpreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/automatic] id=SpreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/badge-error] id=SpreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/collection] id=SpreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Currency\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/currency] id=SpreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/date] id=SpreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/date-time] id=SpreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/default-text] id=SpreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/expression] id=SpreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Full Date\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/full-date] id=SpreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Full Date Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/full-date-time] id=SpreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Full Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/full-time] id=SpreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/general] id=SpreadsheetFormatterSelector-formatterNames-12-Link\n" +
                "                  \"Long Date\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/long-date] id=SpreadsheetFormatterSelector-formatterNames-13-Link\n" +
                "                  \"Long Date Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/long-date-time] id=SpreadsheetFormatterSelector-formatterNames-14-Link\n" +
                "                  \"Long Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/long-time] id=SpreadsheetFormatterSelector-formatterNames-15-Link\n" +
                "                  \"Medium Date\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/medium-date] id=SpreadsheetFormatterSelector-formatterNames-16-Link\n" +
                "                  \"Medium Date Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/medium-date-time] id=SpreadsheetFormatterSelector-formatterNames-17-Link\n" +
                "                  \"Medium Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/medium-time] id=SpreadsheetFormatterSelector-formatterNames-18-Link\n" +
                "                  \"Number\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/number] id=SpreadsheetFormatterSelector-formatterNames-19-Link\n" +
                "                  \"Percent\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/percent] id=SpreadsheetFormatterSelector-formatterNames-20-Link\n" +
                "                  \"Scientific\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/scientific] id=SpreadsheetFormatterSelector-formatterNames-21-Link\n" +
                "                  \"Short Date\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/short-date] id=SpreadsheetFormatterSelector-formatterNames-22-Link\n" +
                "                  \"Short Date Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/short-date-time] id=SpreadsheetFormatterSelector-formatterNames-23-Link\n" +
                "                  \"Short Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/short-time] id=SpreadsheetFormatterSelector-formatterNames-24-Link\n" +
                "                  \"Text\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/text] id=SpreadsheetFormatterSelector-formatterNames-25-Link\n" +
                "                  \"Time\" DISABLED id=SpreadsheetFormatterSelector-formatterNames-26-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=SpreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"h:mm AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20h:mm%20AM/PM] id=SpreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20h:mm:ss%20AM/PM] id=SpreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"hh:mm\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mm] id=SpreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mm.] id=SpreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mm0] id=SpreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mmA/P] id=SpreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mmAM/PM] id=SpreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mma/p] id=SpreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mmam/pm] id=SpreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mmh] id=SpreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mmhh] id=SpreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"s\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mms] id=SpreadsheetFormatterSelector-appender-append-8-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mmss] id=SpreadsheetFormatterSelector-appender-append-9-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20:mm] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20h:mm] id=SpreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hhmm] id=SpreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:m] id=SpreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [time hh:mm] id=SpreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mm] id=SpreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/] id=SpreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time%20hh:mm:ss] id=SpreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=SpreadsheetFormatterSelector-close-Link\n"
        );
    }

    // dialog...........................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataDateDialogClose() {
        final SpreadsheetMetadataPropertyName<SpreadsheetFormatterSelector> property = SpreadsheetMetadataPropertyName.DATE_FORMATTER;

        this.closeAndCheck(
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                property
            ),
            HistoryToken.metadataSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME
            )
        );
    }

    @Test
    public void testOnHistoryTokenChangeMetadataDateDialogSave() {
        final SpreadsheetMetadataPropertyName<SpreadsheetFormatterSelector> property = SpreadsheetMetadataPropertyName.DATE_FORMATTER;
        final SpreadsheetFormatterSelector value = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
            .spreadsheetFormatterSelector();

        this.setSaveValueAndCheck(
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                property
            ),
            Optional.of(value),
            HistoryToken.metadataPropertySave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                property,
                Optional.of(value)
            )
        );
    }

    @Test
    public void testOnHistoryTokenChangeMetadataDateDialogRemove() {
        final SpreadsheetMetadataPropertyName<SpreadsheetFormatterSelector> property = SpreadsheetMetadataPropertyName.DATE_FORMATTER;

        this.setSaveValueAndCheck(
            HistoryToken.metadataPropertySelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                property
            ),
            Optional.empty(),
            HistoryToken.metadataPropertySave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                property,
                Optional.empty()
            )
        );
    }

    @Test
    public void testOnHistoryTokenChangeCellDateDialogClose() {
        this.closeAndCheck(
            HistoryToken.cellFormatterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            HistoryToken.cellSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            )
        );
    }

    @Test
    public void testOnHistoryTokenChangeCellDateDialogSave() {
        final SpreadsheetFormatterSelector value = SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy")
            .spreadsheetFormatterSelector();

        this.setSaveValueAndCheck(
            HistoryToken.cellFormatterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.of(value),
            HistoryToken.cellFormatterSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL,
                Optional.of(value)
            )
        );
    }

    @Test
    public void testOnHistoryTokenChangeCellDateDialogRemove() {
        this.setSaveValueAndCheck(
            HistoryToken.cellFormatterSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(),
            HistoryToken.cellFormatterSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL,
                Optional.empty()
            )
        );
    }

    private void onHistoryTokenChangeAndSetTextAndCheck(final SpreadsheetFormatterSelectorDialogComponent dialog,
                                                        final String text,
                                                        final AppContext context,
                                                        final String expected) {
        this.checkEquals(
            false,
            dialog.isMatch(NOT_MATCHED),
            () -> "should not be matched " + NOT_MATCHED
        );

        dialog.onHistoryTokenChange(
            NOT_MATCHED,
            context
        );

        dialog.setText(text);

        this.treePrintAndCheck(
            dialog,
            expected
        );
    }

    private AppContext appContext(final HistoryToken historyToken) {
        return new FakeAppContext() {

            @Override
            public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                return null;
            }

            @Override
            public void giveFocus(final Runnable focus) {
                // ignore
            }

            @Override
            public HistoryToken historyToken() {
                return historyToken;
            }

            @Override
            public SpreadsheetViewportCache spreadsheetViewportCache() {
                return SpreadsheetViewportCache.empty(this);
            }

            @Override
            public <C extends ConverterContext> Converter<C> converter(final ConverterSelector selector,
                                                                       final ProviderContext context) {
                return SpreadsheetMetadataTesting.CONVERTER_PROVIDER.converter(
                    selector,
                    context
                );
            }

            @Override
            public <C extends ConverterContext> Converter<C> converter(final ConverterName converterSpreadsheet_Name,
                                                                       final List<?> values,
                                                                       final ProviderContext context) {
                return SpreadsheetMetadataTesting.CONVERTER_PROVIDER.converter(
                    converterSpreadsheet_Name,
                    values,
                    context
                );
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Runnable addSpreadsheetFormatterFetcherWatcher(final SpreadsheetFormatterFetcherWatcher watcher) {
                return null;
            }

            @Override
            public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterSelector selector,
                                                             final ProviderContext context) {
                return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatter(
                    selector,
                    context
                );
            }

            @Override
            public SpreadsheetFormatter spreadsheetFormatter(final SpreadsheetFormatterName spreadsheet_name,
                                                             final List<?> values,
                                                             final ProviderContext context) {
                return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatter(
                    spreadsheet_name,
                    values,
                    context
                );
            }

            @Override
            public Optional<SpreadsheetFormatterSelectorToken> spreadsheetFormatterNextToken(final SpreadsheetFormatterSelector selector) {
                return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterNextToken(selector);
            }

            @Override
            public List<SpreadsheetFormatterSample> spreadsheetFormatterSamples(final SpreadsheetFormatterSelector selector,
                                                                                final boolean includeSamples,
                                                                                final SpreadsheetFormatterProviderSamplesContext context) {
                return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterSamples(
                    selector,
                    includeSamples,
                    context
                );
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                    SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                    SPREADSHEET_ID
                );
            }

            @Override
            public Runnable addSpreadsheetMetadataFetcherWatcher(final SpreadsheetMetadataFetcherWatcher watcher) {
                return null;
            }

            @Override
            public LocalDateTime now() {
                return HAS_NOW.now();
            }

            @Override
            public Locale locale() {
                return SpreadsheetFormatterSelectorDialogComponentTest.LOCALE;
            }

            // SpreadsheetFormatterContext......................................................................................

            @Override
            public Optional<SpreadsheetCell> cell() {
                return Optional.empty();
            }

            @Override
            public boolean canConvert(final Object value,
                                      final Class<?> type) {
                return SPREADSHEET_FORMATTER_CONTEXT.canConvert(
                    value,
                    type
                );
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> type) {
                return SPREADSHEET_FORMATTER_CONTEXT.convert(
                    value,
                    type
                );
            }

            @Override
            public Converter<SpreadsheetConverterContext> converter() {
                return SPREADSHEET_FORMATTER_CONTEXT.converter();
            }

            @Override
            public Optional<TextNode> formatValue(final Optional<Object> value) {
                return SPREADSHEET_FORMATTER_CONTEXT.formatValue(value);
            }

            @Override
            public int cellCharacterWidth() {
                return SPREADSHEET_FORMATTER_CONTEXT.cellCharacterWidth();
            }

            @Override
            public Optional<Color> colorNumber(final int number) {
                return SPREADSHEET_FORMATTER_CONTEXT.colorNumber(number);
            }

            @Override
            public Optional<Color> colorName(final SpreadsheetColorName name) {
                return SPREADSHEET_FORMATTER_CONTEXT.colorName(name);
            }

            @Override
            public long dateOffset() {
                return SPREADSHEET_FORMATTER_CONTEXT.dateOffset();
            }

            @Override
            public List<String> ampms() {
                return SPREADSHEET_FORMATTER_CONTEXT.ampms();
            }

            @Override
            public String ampm(final int hourOfDay) {
                return SPREADSHEET_FORMATTER_CONTEXT.ampm(hourOfDay);
            }

            @Override
            public List<String> monthNames() {
                return SPREADSHEET_FORMATTER_CONTEXT.monthNames();
            }

            @Override
            public String monthName(final int month) {
                return SPREADSHEET_FORMATTER_CONTEXT.monthName(month);
            }

            @Override
            public List<String> monthNameAbbreviations() {
                return SPREADSHEET_FORMATTER_CONTEXT.monthNameAbbreviations();
            }

            @Override
            public String monthNameAbbreviation(final int month) {
                return SPREADSHEET_FORMATTER_CONTEXT.monthNameAbbreviation(month);
            }

            @Override
            public List<String> weekDayNames() {
                return SPREADSHEET_FORMATTER_CONTEXT.weekDayNames();
            }

            @Override
            public String weekDayName(final int day) {
                return SPREADSHEET_FORMATTER_CONTEXT.weekDayName(day);
            }

            @Override
            public List<String> weekDayNameAbbreviations() {
                return SPREADSHEET_FORMATTER_CONTEXT.weekDayNameAbbreviations();
            }

            @Override
            public String weekDayNameAbbreviation(final int day) {
                return SPREADSHEET_FORMATTER_CONTEXT.weekDayNameAbbreviation(day);
            }

            @Override
            public int defaultYear() {
                return SPREADSHEET_FORMATTER_CONTEXT.defaultYear();
            }

            @Override
            public int twoDigitYear() {
                return SPREADSHEET_FORMATTER_CONTEXT.twoDigitYear();
            }

            @Override
            public int twoToFourDigitYear(final int year) {
                return SPREADSHEET_FORMATTER_CONTEXT.twoToFourDigitYear(year);
            }

            @Override
            public int generalFormatNumberDigitCount() {
                return SPREADSHEET_FORMATTER_CONTEXT.generalFormatNumberDigitCount();
            }

            @Override
            public MathContext mathContext() {
                return SPREADSHEET_FORMATTER_CONTEXT.mathContext();
            }

            @Override
            public ExpressionNumberKind expressionNumberKind() {
                return SPREADSHEET_FORMATTER_CONTEXT.expressionNumberKind();
            }

            @Override
            public String currencySymbol() {
                return SPREADSHEET_FORMATTER_CONTEXT.currencySymbol();
            }

            @Override
            public char decimalSeparator() {
                return SPREADSHEET_FORMATTER_CONTEXT.decimalSeparator();
            }

            @Override
            public String exponentSymbol() {
                return SPREADSHEET_FORMATTER_CONTEXT.exponentSymbol();
            }

            @Override
            public char groupSeparator() {
                return SPREADSHEET_FORMATTER_CONTEXT.groupSeparator();
            }

            @Override
            public char monetaryDecimalSeparator() {
                return '*';
            }

            @Override
            public char percentSymbol() {
                return SPREADSHEET_FORMATTER_CONTEXT.percentSymbol();
            }

            @Override
            public char negativeSign() {
                return SPREADSHEET_FORMATTER_CONTEXT.negativeSign();
            }

            @Override
            public char positiveSign() {
                return SPREADSHEET_FORMATTER_CONTEXT.positiveSign();
            }

            @Override
            public char zeroDigit() {
                return SPREADSHEET_FORMATTER_CONTEXT.zeroDigit();
            }

            // SpreadsheetFormatterProvider.............................................................................

            @Override
            public SpreadsheetFormatterInfoSet spreadsheetFormatterInfos() {
                return SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterInfos();
            }
        };
    }

    @Override
    public SpreadsheetFormatterSelectorDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetFormatterSelectorDialogComponent.with(
            AppContextSpreadsheetFormatterSelectorDialogComponentContextCell.with(
                this.appContext(historyToken)
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetFormatterSelectorDialogComponent> type() {
        return SpreadsheetFormatterSelectorDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
