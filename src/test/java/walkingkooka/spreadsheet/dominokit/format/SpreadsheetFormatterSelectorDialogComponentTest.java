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
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" DISABLED id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d/m/yy] id=spreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d%20mmm%20yyyy] id=spreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d%20mmmm%20yyyy] id=spreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dddd,%20d%20mmmm%20yyyy] id=spreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    ERROR Empty \"text\"\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date] id=spreadsheetFormatterSelector-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetFormatterSelector-close-Link\n"
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
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" DISABLED id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d/m/yy] id=spreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d%20mmm%20yyyy] id=spreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20d%20mmmm%20yyyy] id=spreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dddd,%20d%20mmmm%20yyyy] id=spreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyy] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"d\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyyd] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyydd] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyyddd] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyydddd] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyym] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyymm] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyymmm] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyymmmm] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyymmmmm] id=spreadsheetFormatterSelector-appender-append-8-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20/mm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20d/mm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20ddd/mm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dddd/mm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20ddmm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd//yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/m/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mmm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mmmm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mmmmm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mmyyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yy] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date dd/mm/yyyy] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/date%20dd/mm/yyyy] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetFormatterSelector-close-Link\n"
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
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" DISABLED id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20d/m/yy,%20h:mm%20AM/PM] id=spreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99, 12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=spreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999, 12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=spreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999 at 12:58:00 PM\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=spreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999 at 12:58:00 PM\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999 12:58:00\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss.] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss0] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssA/P] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssa/p] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssam/pm] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"d\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssd] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssdd] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssddd] id=spreadsheetFormatterSelector-appender-append-8-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssdddd] id=spreadsheetFormatterSelector-appender-append-9-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssh] id=spreadsheetFormatterSelector-appender-append-10-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:sshh] id=spreadsheetFormatterSelector-appender-append-11-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssm] id=spreadsheetFormatterSelector-appender-append-12-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmm] id=spreadsheetFormatterSelector-appender-append-13-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmm] id=spreadsheetFormatterSelector-appender-append-14-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmmm] id=spreadsheetFormatterSelector-appender-append-15-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=spreadsheetFormatterSelector-appender-append-16-Link\n" +
                "                  \"yy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssyy] id=spreadsheetFormatterSelector-appender-append-17-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssyyyy] id=spreadsheetFormatterSelector-appender-append-18-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20d/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20ddd/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dddd/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20ddmm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd//yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/m/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mmm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mmmm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mmmmm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mmyyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "                  \" \" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyyhh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-6-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20h:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-6-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hhmm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-7-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh::ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-8-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:m:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-8-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mmss] id=spreadsheetFormatterSelector-removeOrReplace-remove-9-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:] id=spreadsheetFormatterSelector-removeOrReplace-remove-10-Link\n" +
                "                      \"s\" [/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:s] id=spreadsheetFormatterSelector-removeOrReplace-remove-10-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-time dd/mm/yyyy hh:mm:ss] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetFormatterSelector-close-Link\n"
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
            SpreadsheetFormatterName.NUMBER_FORMAT_PATTERN + " $#0.00",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" DISABLED id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    123.5\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -123.5\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0.\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    124\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -124\n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0\n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12,350%\n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -12,350%\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0%\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $123*50\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $-123*50\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $0*00\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"$#0.00\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $1234*50\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"#\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00%23] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"$\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00$] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"%\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00%25] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \",\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00,] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00.] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00/] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"?\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00?] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"E\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00E] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"$\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20%230.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \"#\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$0.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%23.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%23000] id=spreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.0] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.0] id=spreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [number-format-pattern $#0.00] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern%20$%230.00] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetFormatterSelector-close-Link\n"
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
            SpreadsheetFormatterName.TEXT_FORMAT_PATTERN + " @ \"Hello\"",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" DISABLED id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Default\n" +
                "                  \"@\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20@] id=spreadsheetFormatterSelector-Default-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Hello 123\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"@ \"Hello\"\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20@%20%22Hello%22] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Hello World 123 Hello\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"* \" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20@%20%22Hello%22*%20] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"@\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20@%20%22Hello%22@] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"_ \" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20@%20%22Hello%22_%20] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"@\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20%20%22Hello%22] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \" \" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20@%22Hello%22] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"\"Hello\"\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20@%20] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [text-format-pattern @ \"Hello\"] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern%20@%20%22Hello%22] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetFormatterSelector-close-Link\n"
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
            SpreadsheetFormatterName.TIME_FORMAT_PATTERN + " hh:mm",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Formatter\n" +
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/cell/A1/formatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/cell/A1/formatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/cell/A1/formatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/cell/A1/formatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/cell/A1/formatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/cell/A1/formatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/cell/A1/formatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/cell/A1/formatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/cell/A1/formatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" DISABLED id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"h:mm AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20h:mm%20AM/PM] id=spreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20h:mm:ss%20AM/PM] id=spreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"hh:mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mm] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mm.] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mm0] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mmA/P] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mmAM/PM] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mma/p] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mmam/pm] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mmh] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mmhh] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"s\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mms] id=spreadsheetFormatterSelector-appender-append-8-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mmss] id=spreadsheetFormatterSelector-appender-append-9-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20:mm] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20h:mm] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hhmm] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:m] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [time-format-pattern hh:mm] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/formatter/save/time-format-pattern%20hh:mm] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/formatter/save/] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetFormatterSelector-close-Link\n"
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
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0 SELECTED\n" +
                "            \"Date\" DISABLED id=spreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=spreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=spreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=spreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=spreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=spreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" DISABLED id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20d/m/yy] id=spreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20d%20mmm%20yyyy] id=spreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20d%20mmmm%20yyyy] id=spreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dddd,%20d%20mmmm%20yyyy] id=spreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyy] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"d\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyyd] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyydd] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyyddd] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyydddd] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyym] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyymm] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyymmm] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyymmmm] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyymmmmm] id=spreadsheetFormatterSelector-appender-append-8-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20/mm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20d/mm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20ddd/mm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dddd/mm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20ddmm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd//yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/m/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mmm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mmmm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mmmmm/yyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mmyyyy] id=spreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yy] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date dd/mm/yyyy] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20dd/mm/yyyy] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/dateFormatter/save/date%20yyyy/mm/dd] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetFormatterSelector-close-Link\n"
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
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=spreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1 SELECTED\n" +
                "            \"Date Time\" DISABLED id=spreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=spreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=spreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=spreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=spreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" DISABLED id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20d/m/yy,%20h:mm%20AM/PM] id=spreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99, 12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=spreadsheetFormatterSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999, 12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=spreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999 at 12:58:00 PM\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=spreadsheetFormatterSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999 at 12:58:00 PM\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999 12:58:00\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss.] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss0] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssA/P] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssa/p] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssam/pm] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"d\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssd] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssdd] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssddd] id=spreadsheetFormatterSelector-appender-append-8-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssdddd] id=spreadsheetFormatterSelector-appender-append-9-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssh] id=spreadsheetFormatterSelector-appender-append-10-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:sshh] id=spreadsheetFormatterSelector-appender-append-11-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssm] id=spreadsheetFormatterSelector-appender-append-12-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmm] id=spreadsheetFormatterSelector-appender-append-13-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmm] id=spreadsheetFormatterSelector-appender-append-14-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmmm] id=spreadsheetFormatterSelector-appender-append-15-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=spreadsheetFormatterSelector-appender-append-16-Link\n" +
                "                  \"yy\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssyy] id=spreadsheetFormatterSelector-appender-append-17-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ssyyyy] id=spreadsheetFormatterSelector-appender-append-18-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20d/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20ddd/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dddd/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20ddmm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd//yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/m/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mmm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mmmm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mmmmm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mmyyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yy%20hh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "                  \" \" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyyhh:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-6-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20h:mm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-6-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hhmm:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-7-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh::ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-8-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:m:ss] id=spreadsheetFormatterSelector-removeOrReplace-remove-8-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mmss] id=spreadsheetFormatterSelector-removeOrReplace-remove-9-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:] id=spreadsheetFormatterSelector-removeOrReplace-remove-10-Link\n" +
                "                      \"s\" [/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:s] id=spreadsheetFormatterSelector-removeOrReplace-remove-10-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-time dd/mm/yyyy hh:mm:ss] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20dd/mm/yyyy%20hh:mm:ss] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter/save/date-time%20yyyy/mm/dd%20hh:mm] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetFormatterSelector-close-Link\n"
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
            SpreadsheetFormatterName.NUMBER_FORMAT_PATTERN + " $#0.00",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Error Formatter (errorFormatter)\n" +
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=spreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=spreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2 SELECTED\n" +
                "            \"Error\" DISABLED id=spreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=spreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=spreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=spreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" DISABLED id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    123.5\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -123.5\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0.\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    124\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -124\n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0\n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12,350%\n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -12,350%\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0%\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $123*50\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $-123*50\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $0*00\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"$#0.00\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $1234*50\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00%23] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00$] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"%\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00%25] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \",\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00,] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00.] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00/] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"?\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00?] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"E\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00E] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20%230.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$0.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%23.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%23000] id=spreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.0] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.0] id=spreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [number-format-pattern $#0.00] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/number-format-pattern%20$%230.00] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/errorFormatter/save/badge-error%20text-format-pattern%20@] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetFormatterSelector-close-Link\n"
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
            SpreadsheetFormatterName.NUMBER_FORMAT_PATTERN + " $#0.00",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Number Formatter (numberFormatter)\n" +
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=spreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=spreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=spreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3 SELECTED\n" +
                "            \"Number\" DISABLED id=spreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=spreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=spreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" DISABLED id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    123.5\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -123.5\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230.%23%23%23] id=spreadsheetFormatterSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0.\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    124\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -124\n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230] id=spreadsheetFormatterSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0\n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12,350%\n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -12,350%\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%23,%23%230%25] id=spreadsheetFormatterSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0%\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $123*50\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $-123*50\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%23,%23%230.00] id=spreadsheetFormatterSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $0*00\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"$#0.00\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $1234*50\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00%23] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00$] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"%\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00%25] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \",\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00,] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00.] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00/] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"?\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00?] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"E\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00E] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20%230.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$0.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%23.00] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%23000] id=spreadsheetFormatterSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.0] id=spreadsheetFormatterSelector-removeOrReplace-remove-4-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.0] id=spreadsheetFormatterSelector-removeOrReplace-remove-5-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [number-format-pattern $#0.00] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%20$%230.00] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/numberFormatter/save/number-format-pattern%200.%23;0.%23;0] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetFormatterSelector-close-Link\n"
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
            SpreadsheetFormatterName.TEXT_FORMAT_PATTERN + " @ \"Hello\"",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Text Formatter (textFormatter)\n" +
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=spreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=spreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=spreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=spreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4 SELECTED\n" +
                "            \"Text\" DISABLED id=spreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter] id=spreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" DISABLED id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/time-format-pattern] id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Default\n" +
                "                  \"@\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@] id=spreadsheetFormatterSelector-Default-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Hello 123\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"@ \"Hello\"\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@%20%22Hello%22] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Hello World 123 Hello\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"* \" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@%20%22Hello%22*%20] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"@\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@%20%22Hello%22@] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"_ \" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@%20%22Hello%22_%20] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"@\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20%20%22Hello%22] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                  \" \" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@%22Hello%22] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"\"Hello\"\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@%20] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [text-format-pattern @ \"Hello\"] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@%20%22Hello%22] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/textFormatter/save/text-format-pattern%20@] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetFormatterSelector-close-Link\n"
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
            SpreadsheetFormatterName.TIME_FORMAT_PATTERN + " hh:mm",
            context,
            "SpreadsheetFormatterSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Time Formatter (timeFormatter)\n" +
                "    id=spreadsheetFormatterSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateFormatter] id=spreadsheetFormatterSelector-Tabs-dateFormatter\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeFormatter] id=spreadsheetFormatterSelector-Tabs-dateTimeFormatter\n" +
                "          TAB 2\n" +
                "            \"Error\" [#/1/Spreadsheet1/spreadsheet/errorFormatter] id=spreadsheetFormatterSelector-Tabs-errorFormatter\n" +
                "          TAB 3\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberFormatter] id=spreadsheetFormatterSelector-Tabs-numberFormatter\n" +
                "          TAB 4\n" +
                "            \"Text\" [#/1/Spreadsheet1/spreadsheet/textFormatter] id=spreadsheetFormatterSelector-Tabs-textFormatter\n" +
                "          TAB 5 SELECTED\n" +
                "            \"Time\" DISABLED id=spreadsheetFormatterSelector-Tabs-timeFormatter\n" +
                "      SpreadsheetFormatterNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Automatic\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/automatic] id=spreadsheetFormatterSelector-formatterNames-0-Link\n" +
                "                  \"Badge Error\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/badge-error] id=spreadsheetFormatterSelector-formatterNames-1-Link\n" +
                "                  \"Collection\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/collection] id=spreadsheetFormatterSelector-formatterNames-2-Link\n" +
                "                  \"Date\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/date] id=spreadsheetFormatterSelector-formatterNames-3-Link\n" +
                "                  \"Date Time\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/date-time] id=spreadsheetFormatterSelector-formatterNames-4-Link\n" +
                "                  \"Default Text\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/default-text] id=spreadsheetFormatterSelector-formatterNames-5-Link\n" +
                "                  \"Expression\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/expression] id=spreadsheetFormatterSelector-formatterNames-6-Link\n" +
                "                  \"General\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/general] id=spreadsheetFormatterSelector-formatterNames-7-Link\n" +
                "                  \"Number Format Pattern\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/number-format-pattern] id=spreadsheetFormatterSelector-formatterNames-8-Link\n" +
                "                  \"Spreadsheet Pattern Collection\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/spreadsheet-pattern-collection] id=spreadsheetFormatterSelector-formatterNames-9-Link\n" +
                "                  \"Text Format Pattern\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/text-format-pattern] id=spreadsheetFormatterSelector-formatterNames-10-Link\n" +
                "                  \"Time Format Pattern\" DISABLED id=spreadsheetFormatterSelector-formatterNames-11-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetFormatterSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"h:mm AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20h:mm%20AM/PM] id=spreadsheetFormatterSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20h:mm:ss%20AM/PM] id=spreadsheetFormatterSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"hh:mm\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mm] id=spreadsheetFormatterSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mm.] id=spreadsheetFormatterSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mm0] id=spreadsheetFormatterSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mmA/P] id=spreadsheetFormatterSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mmAM/PM] id=spreadsheetFormatterSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mma/p] id=spreadsheetFormatterSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mmam/pm] id=spreadsheetFormatterSelector-appender-append-5-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mmh] id=spreadsheetFormatterSelector-appender-append-6-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mmhh] id=spreadsheetFormatterSelector-appender-append-7-Link\n" +
                "                  \"s\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mms] id=spreadsheetFormatterSelector-appender-append-8-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mmss] id=spreadsheetFormatterSelector-appender-append-9-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20:mm] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20h:mm] id=spreadsheetFormatterSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hhmm] id=spreadsheetFormatterSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:m] id=spreadsheetFormatterSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "      SpreadsheetFormatterSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [time-format-pattern hh:mm] id=spreadsheetFormatterSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mm] id=spreadsheetFormatterSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/] id=spreadsheetFormatterSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/timeFormatter/save/time-format-pattern%20hh:mm:ss] id=spreadsheetFormatterSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetFormatterSelector-close-Link\n"
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
                return NOW.now();
            }

            @Override
            public Locale locale() {
                return LOCALE;
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
