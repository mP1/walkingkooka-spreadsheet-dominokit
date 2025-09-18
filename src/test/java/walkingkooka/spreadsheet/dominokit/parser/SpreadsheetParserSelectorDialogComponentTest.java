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

package walkingkooka.spreadsheet.dominokit.parser;

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
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterProviderSamplesContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelectorToken;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetParserSelectorDialogComponentTest implements DialogComponentLifecycleTesting<SpreadsheetParserSelectorDialogComponent>,
    HistoryTokenTesting,
    SpreadsheetMetadataTesting {

    private final static AnchoredSpreadsheetSelection CELL = SpreadsheetSelection.A1.setDefaultAnchor();

    // cell / date......................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellDateMissingPattern() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetParserName.DATE_PARSER_PATTERN + "",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Parser\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern] id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern] id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern] id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20d/m/yy] id=spreadsheetParserSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20d%20mmm%20yyyy] id=spreadsheetParserSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20d%20mmmm%20yyyy] id=spreadsheetParserSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=spreadsheetParserSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    ERROR Empty \"text\"\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-parse-pattern] id=spreadsheetParserSelector-TextBox\n" +
                "            Errors\n" +
                "              Empty \"text\"\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" DISABLED id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    @Test
    public void testOnHistoryTokenChangeCellDate() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetParserName.DATE_PARSER_PATTERN + " dd/mm/yyyy",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Parser\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern] id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern] id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern] id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20d/m/yy] id=spreadsheetParserSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20d%20mmm%20yyyy] id=spreadsheetParserSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20d%20mmmm%20yyyy] id=spreadsheetParserSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=spreadsheetParserSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyy] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"d\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyyd] id=spreadsheetParserSelector-appender-append-0-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyydd] id=spreadsheetParserSelector-appender-append-1-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyyddd] id=spreadsheetParserSelector-appender-append-2-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyydddd] id=spreadsheetParserSelector-appender-append-3-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyym] id=spreadsheetParserSelector-appender-append-4-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyymm] id=spreadsheetParserSelector-appender-append-5-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyymmm] id=spreadsheetParserSelector-appender-append-6-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyymmmm] id=spreadsheetParserSelector-appender-append-7-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyymmmmm] id=spreadsheetParserSelector-appender-append-8-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20/mm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20d/mm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20ddd/mm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dddd/mm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20ddmm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd//yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/m/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mmm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mmmm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mmmmm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mmyyyy] id=spreadsheetParserSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yy] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-parse-pattern dd/mm/yyyy] id=spreadsheetParserSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyy] id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    // cell / date-time.................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellDateTime() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetParserName.DATE_TIME_PARSER_PATTERN + " dd/mm/yyyy hh:mm:ss",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Parser\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern] id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern] id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern] id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=spreadsheetParserSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99, 12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=spreadsheetParserSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999, 12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=spreadsheetParserSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999 at 12:58:00 PM\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=spreadsheetParserSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999 at 12:58:00 PM\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999 12:58:00\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss.] id=spreadsheetParserSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss0] id=spreadsheetParserSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssA/P] id=spreadsheetParserSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=spreadsheetParserSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssa/p] id=spreadsheetParserSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssam/pm] id=spreadsheetParserSelector-appender-append-5-Link\n" +
                "                  \"d\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssd] id=spreadsheetParserSelector-appender-append-6-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdd] id=spreadsheetParserSelector-appender-append-7-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssddd] id=spreadsheetParserSelector-appender-append-8-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdddd] id=spreadsheetParserSelector-appender-append-9-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssh] id=spreadsheetParserSelector-appender-append-10-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:sshh] id=spreadsheetParserSelector-appender-append-11-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=spreadsheetParserSelector-appender-append-12-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=spreadsheetParserSelector-appender-append-13-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmm] id=spreadsheetParserSelector-appender-append-14-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmm] id=spreadsheetParserSelector-appender-append-15-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=spreadsheetParserSelector-appender-append-16-Link\n" +
                "                  \"yy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyy] id=spreadsheetParserSelector-appender-append-17-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyyyy] id=spreadsheetParserSelector-appender-append-18-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20d/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20ddd/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dddd/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20ddmm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd//yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/m/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mmm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mmmm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mmmmm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mmyyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "                  \" \" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyyhh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-5-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-6-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20h:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-6-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hhmm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-7-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh::ss] id=spreadsheetParserSelector-removeOrReplace-remove-8-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:m:ss] id=spreadsheetParserSelector-removeOrReplace-remove-8-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mmss] id=spreadsheetParserSelector-removeOrReplace-remove-9-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:] id=spreadsheetParserSelector-removeOrReplace-remove-10-Link\n" +
                "                      \"s\" [/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:s] id=spreadsheetParserSelector-removeOrReplace-remove-10-Link-replace-0-MenuItem\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-time-parse-pattern dd/mm/yyyy hh:mm:ss] id=spreadsheetParserSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    // cell / number....................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellNumber() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetParserName.NUMBER_PARSER_PATTERN + " $#0.00",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Parser\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern] id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern] id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern] id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=spreadsheetParserSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    123.5\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=spreadsheetParserSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -123.5\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=spreadsheetParserSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0.\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230] id=spreadsheetParserSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    124\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230] id=spreadsheetParserSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -124\n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230] id=spreadsheetParserSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0\n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230%25] id=spreadsheetParserSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12,350%\n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230%25] id=spreadsheetParserSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -12,350%\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%23,%23%230%25] id=spreadsheetParserSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0%\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%23,%23%230.00] id=spreadsheetParserSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $123.50\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%23,%23%230.00] id=spreadsheetParserSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $-123.50\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%23,%23%230.00] id=spreadsheetParserSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $0.00\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"$#0.00\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $1234.50\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"#\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00%23] id=spreadsheetParserSelector-appender-append-0-Link\n" +
                "                  \"$\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00$] id=spreadsheetParserSelector-appender-append-1-Link\n" +
                "                  \"%\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00%25] id=spreadsheetParserSelector-appender-append-2-Link\n" +
                "                  \",\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00,] id=spreadsheetParserSelector-appender-append-3-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00.] id=spreadsheetParserSelector-appender-append-4-Link\n" +
                "                  \"/\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00/] id=spreadsheetParserSelector-appender-append-5-Link\n" +
                "                  \"?\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00?] id=spreadsheetParserSelector-appender-append-6-Link\n" +
                "                  \"E\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00E] id=spreadsheetParserSelector-appender-append-7-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"$\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20%230.00] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link\n" +
                "                  \"#\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$0.00] id=spreadsheetParserSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%23.00] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%23000] id=spreadsheetParserSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.0] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.0] id=spreadsheetParserSelector-removeOrReplace-remove-5-Link\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [number-parse-pattern $#0.00] id=spreadsheetParserSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern%20$%230.00] id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    // cell / time......................................................................................................

    @Test
    public void testOnHistoryTokenChangeCellTime() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextCell.with(context)
            ),
            SpreadsheetParserName.TIME_PARSER_PATTERN + " hh:mm",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    A1: Parser\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/date-parse-pattern] id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/date-time-parse-pattern] id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" [#/1/Spreadsheet1/cell/A1/parser/save/number-parse-pattern] id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"h:mm AM/PM\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20h:mm%20AM/PM] id=spreadsheetParserSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=spreadsheetParserSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"hh:mm\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mm] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mm.] id=spreadsheetParserSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mm0] id=spreadsheetParserSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mmA/P] id=spreadsheetParserSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mmAM/PM] id=spreadsheetParserSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mma/p] id=spreadsheetParserSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mmam/pm] id=spreadsheetParserSelector-appender-append-5-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mmh] id=spreadsheetParserSelector-appender-append-6-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mmhh] id=spreadsheetParserSelector-appender-append-7-Link\n" +
                "                  \"s\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mms] id=spreadsheetParserSelector-appender-append-8-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mmss] id=spreadsheetParserSelector-appender-append-9-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"hh\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20:mm] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20h:mm] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hhmm] id=spreadsheetParserSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:m] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [time-parse-pattern hh:mm] id=spreadsheetParserSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/cell/A1/parser/save/time-parse-pattern%20hh:mm] id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/cell/A1/parser/save/] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/cell/A1] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    // metadata / date..................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataDate() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/dateParser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetParserName.DATE_PARSER_PATTERN + " dd/mm/yyyy",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Date Parser (dateParser)\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0 SELECTED\n" +
                "            \"Date\" DISABLED id=spreadsheetParserSelector-Tabs-dateParser\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser] id=spreadsheetParserSelector-Tabs-dateTimeParser\n" +
                "          TAB 2\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberParser] id=spreadsheetParserSelector-Tabs-numberParser\n" +
                "          TAB 3\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeParser] id=spreadsheetParserSelector-Tabs-timeParser\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-time-parse-pattern] id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/number-parse-pattern] id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/time-parse-pattern] id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20d/m/yy] id=spreadsheetParserSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20d%20mmm%20yyyy] id=spreadsheetParserSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20d%20mmmm%20yyyy] id=spreadsheetParserSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=spreadsheetParserSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyy] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"d\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyyd] id=spreadsheetParserSelector-appender-append-0-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyydd] id=spreadsheetParserSelector-appender-append-1-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyyddd] id=spreadsheetParserSelector-appender-append-2-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyydddd] id=spreadsheetParserSelector-appender-append-3-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyym] id=spreadsheetParserSelector-appender-append-4-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyymm] id=spreadsheetParserSelector-appender-append-5-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyymmm] id=spreadsheetParserSelector-appender-append-6-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyymmmm] id=spreadsheetParserSelector-appender-append-7-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyymmmmm] id=spreadsheetParserSelector-appender-append-8-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20/mm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20d/mm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20ddd/mm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dddd/mm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20ddmm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd//yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/m/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mmm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mmmm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mmmmm/yyyy] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mmyyyy] id=spreadsheetParserSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yy] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-parse-pattern dd/mm/yyyy] id=spreadsheetParserSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20dd/mm/yyyy] id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/dateParser/save/date-parse-pattern%20yyyy/mm/dd] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    // metadata / date-time.............................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataDateTime() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/dateTimeParser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetParserName.DATE_TIME_PARSER_PATTERN + " dd/mm/yyyy hh:mm:ss",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Date Time Parser (dateTimeParser)\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateParser] id=spreadsheetParserSelector-Tabs-dateParser\n" +
                "          TAB 1 SELECTED\n" +
                "            \"Date Time\" DISABLED id=spreadsheetParserSelector-Tabs-dateTimeParser\n" +
                "          TAB 2\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberParser] id=spreadsheetParserSelector-Tabs-numberParser\n" +
                "          TAB 3\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeParser] id=spreadsheetParserSelector-Tabs-timeParser\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-parse-pattern] id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/number-parse-pattern] id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/time-parse-pattern] id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=spreadsheetParserSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/99, 12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Medium\n" +
                "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=spreadsheetParserSelector-Medium-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 Dec. 1999, 12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=spreadsheetParserSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31 December 1999 at 12:58:00 PM\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Full\n" +
                "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=spreadsheetParserSelector-Full-Link\n" +
                "                  TextNodeComponent\n" +
                "                    Friday, 31 December 1999 at 12:58:00 PM\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"dd/mm/yyyy hh:mm:ss\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    31/12/1999 12:58:00\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss.] id=spreadsheetParserSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss0] id=spreadsheetParserSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssA/P] id=spreadsheetParserSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=spreadsheetParserSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssa/p] id=spreadsheetParserSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssam/pm] id=spreadsheetParserSelector-appender-append-5-Link\n" +
                "                  \"d\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssd] id=spreadsheetParserSelector-appender-append-6-Link\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdd] id=spreadsheetParserSelector-appender-append-7-Link\n" +
                "                  \"ddd\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssddd] id=spreadsheetParserSelector-appender-append-8-Link\n" +
                "                  \"dddd\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdddd] id=spreadsheetParserSelector-appender-append-9-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssh] id=spreadsheetParserSelector-appender-append-10-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:sshh] id=spreadsheetParserSelector-appender-append-11-Link\n" +
                "                  \"m\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=spreadsheetParserSelector-appender-append-12-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=spreadsheetParserSelector-appender-append-13-Link\n" +
                "                  \"mmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmm] id=spreadsheetParserSelector-appender-append-14-Link\n" +
                "                  \"mmmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmm] id=spreadsheetParserSelector-appender-append-15-Link\n" +
                "                  \"mmmmm\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=spreadsheetParserSelector-appender-append-16-Link\n" +
                "                  \"yy\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyy] id=spreadsheetParserSelector-appender-append-17-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyyyy] id=spreadsheetParserSelector-appender-append-18-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"dd\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"d\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20d/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                      \"ddd\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20ddd/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                "                      \"dddd\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dddd/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20ddmm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd//yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/m/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "                      \"mmm\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mmm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                "                      \"mmmm\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mmmm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                "                      \"mmmmm\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mmmmm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mmyyyy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"yyyy\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link\n" +
                "                      \"yy\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yy%20hh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                "                  \" \" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyyhh:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-5-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-6-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20h:mm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-6-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hhmm:ss] id=spreadsheetParserSelector-removeOrReplace-remove-7-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh::ss] id=spreadsheetParserSelector-removeOrReplace-remove-8-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:m:ss] id=spreadsheetParserSelector-removeOrReplace-remove-8-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mmss] id=spreadsheetParserSelector-removeOrReplace-remove-9-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:] id=spreadsheetParserSelector-removeOrReplace-remove-10-Link\n" +
                "                      \"s\" [/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:s] id=spreadsheetParserSelector-removeOrReplace-remove-10-Link-replace-0-MenuItem\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [date-time-parse-pattern dd/mm/yyyy hh:mm:ss] id=spreadsheetParserSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser/save/date-time-parse-pattern%20yyyy/mm/dd%20hh:mm] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    // metadata / number................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataNumber() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/numberParser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetParserName.NUMBER_PARSER_PATTERN + " $#0.00",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Number Parser (numberParser)\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateParser] id=spreadsheetParserSelector-Tabs-dateParser\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser] id=spreadsheetParserSelector-Tabs-dateTimeParser\n" +
                "          TAB 2 SELECTED\n" +
                "            \"Number\" DISABLED id=spreadsheetParserSelector-Tabs-numberParser\n" +
                "          TAB 3\n" +
                "            \"Time\" [#/1/Spreadsheet1/spreadsheet/timeParser] id=spreadsheetParserSelector-Tabs-timeParser\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/date-parse-pattern] id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/date-time-parse-pattern] id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/time-parse-pattern] id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=spreadsheetParserSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    123.5\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=spreadsheetParserSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -123.5\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Number\n" +
                "                  \"#,##0.###\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=spreadsheetParserSelector-Number-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0.\n" +
                "                ROW 3\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230] id=spreadsheetParserSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    124\n" +
                "                ROW 4\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230] id=spreadsheetParserSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -124\n" +
                "                ROW 5\n" +
                "                  TextNodeComponent\n" +
                "                    Integer\n" +
                "                  \"#,##0\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230] id=spreadsheetParserSelector-Integer-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0\n" +
                "                ROW 6\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230%25] id=spreadsheetParserSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12,350%\n" +
                "                ROW 7\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230%25] id=spreadsheetParserSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    -12,350%\n" +
                "                ROW 8\n" +
                "                  TextNodeComponent\n" +
                "                    Percent\n" +
                "                  \"#,##0%\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%23,%23%230%25] id=spreadsheetParserSelector-Percent-Link\n" +
                "                  TextNodeComponent\n" +
                "                    0%\n" +
                "                ROW 9\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%23,%23%230.00] id=spreadsheetParserSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $123.50\n" +
                "                ROW 10\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%23,%23%230.00] id=spreadsheetParserSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $-123.50\n" +
                "                ROW 11\n" +
                "                  TextNodeComponent\n" +
                "                    Currency\n" +
                "                  \"$#,##0.00\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%23,%23%230.00] id=spreadsheetParserSelector-Currency-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $0.00\n" +
                "                ROW 12\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"$#0.00\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    $1234.50\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00%23] id=spreadsheetParserSelector-appender-append-0-Link\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00$] id=spreadsheetParserSelector-appender-append-1-Link\n" +
                "                  \"%\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00%25] id=spreadsheetParserSelector-appender-append-2-Link\n" +
                "                  \",\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00,] id=spreadsheetParserSelector-appender-append-3-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00.] id=spreadsheetParserSelector-appender-append-4-Link\n" +
                "                  \"/\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00/] id=spreadsheetParserSelector-appender-append-5-Link\n" +
                "                  \"?\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00?] id=spreadsheetParserSelector-appender-append-6-Link\n" +
                "                  \"E\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00E] id=spreadsheetParserSelector-appender-append-7-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"$\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20%230.00] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link\n" +
                "                  \"#\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$0.00] id=spreadsheetParserSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%23.00] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%23000] id=spreadsheetParserSelector-removeOrReplace-remove-3-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.0] id=spreadsheetParserSelector-removeOrReplace-remove-4-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.0] id=spreadsheetParserSelector-removeOrReplace-remove-5-Link\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [number-parse-pattern $#0.00] id=spreadsheetParserSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%20$%230.00] id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/numberParser/save/number-parse-pattern%200.%23;0.%23;0] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    // metadata / time..................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataTime() {
        final AppContext context = this.appContext(
            HistoryToken.parseString("/1/Spreadsheet1/spreadsheet/timeParser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
            SpreadsheetParserSelectorDialogComponent.with(
                AppContextSpreadsheetParserSelectorDialogComponentContextMetadata.with(context)
            ),
            SpreadsheetParserName.TIME_PARSER_PATTERN + " hh:mm",
            context,
            "SpreadsheetParserSelectorDialogComponent\n" +
                "  DialogComponent\n" +
                "    Spreadsheet: Time Parser (timeParser)\n" +
                "    id=spreadsheetParserSelector-Dialog includeClose=true\n" +
                "      SpreadsheetMetadataPropertyNameTabsComponent\n" +
                "        SpreadsheetTabsComponent\n" +
                "          TAB 0\n" +
                "            \"Date\" [#/1/Spreadsheet1/spreadsheet/dateParser] id=spreadsheetParserSelector-Tabs-dateParser\n" +
                "          TAB 1\n" +
                "            \"Date Time\" [#/1/Spreadsheet1/spreadsheet/dateTimeParser] id=spreadsheetParserSelector-Tabs-dateTimeParser\n" +
                "          TAB 2\n" +
                "            \"Number\" [#/1/Spreadsheet1/spreadsheet/numberParser] id=spreadsheetParserSelector-Tabs-numberParser\n" +
                "          TAB 3 SELECTED\n" +
                "            \"Time\" DISABLED id=spreadsheetParserSelector-Tabs-timeParser\n" +
                "      SpreadsheetParserNameLinkListComponent\n" +
                "        CardLinkListComponent\n" +
                "          CardComponent\n" +
                "            Card\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"Date Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/date-parse-pattern] id=spreadsheetParserSelector-parserNames-0-Link\n" +
                "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/date-time-parse-pattern] id=spreadsheetParserSelector-parserNames-1-Link\n" +
                "                  \"Number Parse Pattern\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/number-parse-pattern] id=spreadsheetParserSelector-parserNames-2-Link\n" +
                "                  \"Time Parse Pattern\" DISABLED id=spreadsheetParserSelector-parserNames-3-Link\n" +
                "      SpreadsheetFormatterTableComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            DataTableComponent\n" +
                "              id=spreadsheetParserSelector-Table\n" +
                "              ROW(S)\n" +
                "                ROW 0\n" +
                "                  TextNodeComponent\n" +
                "                    Short\n" +
                "                  \"h:mm AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20h:mm%20AM/PM] id=spreadsheetParserSelector-Short-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58 PM\n" +
                "                ROW 1\n" +
                "                  TextNodeComponent\n" +
                "                    Long\n" +
                "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=spreadsheetParserSelector-Long-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58:00 PM\n" +
                "                ROW 2\n" +
                "                  TextNodeComponent\n" +
                "                    Sample\n" +
                "                  \"hh:mm\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mm] id=spreadsheetParserSelector-Sample-Link\n" +
                "                  TextNodeComponent\n" +
                "                    12:58\n" +
                "      AppendPluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Append component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \".\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mm.] id=spreadsheetParserSelector-appender-append-0-Link\n" +
                "                  \"0\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mm0] id=spreadsheetParserSelector-appender-append-1-Link\n" +
                "                  \"A/P\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mmA/P] id=spreadsheetParserSelector-appender-append-2-Link\n" +
                "                  \"AM/PM\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mmAM/PM] id=spreadsheetParserSelector-appender-append-3-Link\n" +
                "                  \"a/p\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mma/p] id=spreadsheetParserSelector-appender-append-4-Link\n" +
                "                  \"am/pm\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mmam/pm] id=spreadsheetParserSelector-appender-append-5-Link\n" +
                "                  \"h\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mmh] id=spreadsheetParserSelector-appender-append-6-Link\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mmhh] id=spreadsheetParserSelector-appender-append-7-Link\n" +
                "                  \"s\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mms] id=spreadsheetParserSelector-appender-append-8-Link\n" +
                "                  \"ss\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mmss] id=spreadsheetParserSelector-appender-append-9-Link\n" +
                "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                "        CardComponent\n" +
                "          Card\n" +
                "            Remove / Replace component(s)\n" +
                "              FlexLayoutComponent\n" +
                "                ROW\n" +
                "                  \"hh\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20:mm] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link\n" +
                "                      \"h\" [/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20h:mm] id=spreadsheetParserSelector-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                "                  \":\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hhmm] id=spreadsheetParserSelector-removeOrReplace-remove-1-Link\n" +
                "                  \"mm\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link\n" +
                "                      \"m\" [/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:m] id=spreadsheetParserSelector-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                "      SpreadsheetParserSelectorComponent\n" +
                "        ValueTextBoxComponent\n" +
                "          TextBoxComponent\n" +
                "            [time-parse-pattern hh:mm] id=spreadsheetParserSelector-TextBox\n" +
                "      AnchorListComponent\n" +
                "        FlexLayoutComponent\n" +
                "          ROW\n" +
                "            \"Save\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mm] id=spreadsheetParserSelector-save-Link\n" +
                "            \"Clear\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/] id=spreadsheetParserSelector-clear-Link\n" +
                "            \"Undo\" [#/1/Spreadsheet1/spreadsheet/timeParser/save/time-parse-pattern%20hh:mm:ss] id=spreadsheetParserSelector-undo-Link\n" +
                "            \"Close\" [#/1/Spreadsheet1/spreadsheet] id=spreadsheetParserSelector-close-Link\n"
        );
    }

    // dialog...........................................................................................................

    @Test
    public void testOnHistoryTokenChangeMetadataDateDialogClose() {
        final SpreadsheetMetadataPropertyName<SpreadsheetParserSelector> property = SpreadsheetMetadataPropertyName.DATE_PARSER;

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
        final SpreadsheetMetadataPropertyName<SpreadsheetParserSelector> property = SpreadsheetMetadataPropertyName.DATE_PARSER;
        final SpreadsheetParserSelector value = SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
            .spreadsheetParserSelector();

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
        final SpreadsheetMetadataPropertyName<SpreadsheetParserSelector> property = SpreadsheetMetadataPropertyName.DATE_PARSER;

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
            HistoryToken.cellParserSelect(
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
        final SpreadsheetParserSelector value = SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
            .spreadsheetParserSelector();

        this.setSaveValueAndCheck(
            HistoryToken.cellParserSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.of(value),
            HistoryToken.cellParserSave(
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
            HistoryToken.cellParserSelect(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL
            ),
            Optional.empty(),
            HistoryToken.cellParserSave(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                CELL,
                Optional.empty()
            )
        );
    }

    private void onHistoryTokenChangeAndSetTextAndCheck(final SpreadsheetParserSelectorDialogComponent dialog,
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
            public <C extends ConverterContext> Converter<C> converter(final ConverterName converterName,
                                                                       final List<?> values,
                                                                       final ProviderContext context) {
                return SpreadsheetMetadataTesting.CONVERTER_PROVIDER.converter(
                    converterName,
                    values,
                    context
                );
            }

            @Override
            public SpreadsheetParser spreadsheetParser(final SpreadsheetParserSelector selector,
                                                       final ProviderContext context) {
                return SpreadsheetMetadataTesting.SPREADSHEET_PARSER_PROVIDER.spreadsheetParser(
                    selector,
                    context
                );
            }

            @Override
            public SpreadsheetParser spreadsheetParser(final SpreadsheetParserName name,
                                                       final List<?> values,
                                                       final ProviderContext context) {
                return SpreadsheetMetadataTesting.SPREADSHEET_PARSER_PROVIDER.spreadsheetParser(
                    name,
                    values,
                    context
                );
            }

            @Override
            public Optional<SpreadsheetParserSelectorToken> spreadsheetParserNextToken(final SpreadsheetParserSelector selector) {
                return SpreadsheetMetadataTesting.SPREADSHEET_PARSER_PROVIDER.spreadsheetParserNextToken(selector);
            }

            @Override
            public SpreadsheetParserInfoSet spreadsheetParserInfos() {
                return SPREADSHEET_PARSER_PROVIDER.spreadsheetParserInfos();
            }

            @Override
            public Runnable addSpreadsheetDeltaFetcherWatcher(final SpreadsheetDeltaFetcherWatcher watcher) {
                return null;
            }

            @Override
            public Optional<SpreadsheetFormatterSelector> spreadsheetFormatterSelector(final SpreadsheetParserSelector selector) {
                return SPREADSHEET_PARSER_PROVIDER.spreadsheetFormatterSelector(selector);
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
            public void giveFocus(final Runnable focus) {
                // ignore
            }

            @Override
            public LocalDateTime now() {
                return NOW.now();
            }

            @Override
            public Locale locale() {
                return LOCALE;
            }

            @Override
            public Optional<SpreadsheetCell> cell() {
                return Optional.empty();
            }

            // SpreadsheetParserContext......................................................................................

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
                return SPREADSHEET_FORMATTER_CONTEXT.monetaryDecimalSeparator();
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
        };
    }

    @Override
    public SpreadsheetParserSelectorDialogComponent createSpreadsheetDialogComponentLifecycle(final HistoryToken historyToken) {
        return SpreadsheetParserSelectorDialogComponent.with(
            AppContextSpreadsheetParserSelectorDialogComponentContextCell.with(
                this.appContext(historyToken)
            )
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetParserSelectorDialogComponent> type() {
        return SpreadsheetParserSelectorDialogComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
