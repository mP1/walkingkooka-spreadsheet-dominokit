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
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.convert.SpreadsheetConverterContext;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.FakeAppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycleTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.format.SpreadsheetColorName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterProviderSamplesContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.spreadsheet.parser.SpreadsheetParser;
import walkingkooka.spreadsheet.parser.SpreadsheetParserInfoSet;
import walkingkooka.spreadsheet.parser.SpreadsheetParserName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelectorToken;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.ExpressionNumberKind;
import walkingkooka.tree.text.TextNode;

import java.math.MathContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class SpreadsheetParserSelectorDialogComponentTest implements SpreadsheetDialogComponentLifecycleTesting<SpreadsheetParserSelectorDialogComponent>,
        HistoryTokenTesting,
        SpreadsheetMetadataTesting {

    private final static SpreadsheetId SPREADSHEET_ID = SpreadsheetId.with(1);

    private final static SpreadsheetName NAME = SpreadsheetName.with("Spreadsheet123");

    private final static AnchoredSpreadsheetSelection CELL = SpreadsheetSelection.A1.setDefaultAnchor();

    // cell / date......................................................................................................

    @Test
    public void testCellDateMissingPattern() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicCell.with(context)
                ),
                SpreadsheetParserName.DATE_PARSER_PATTERN + "",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" DISABLED [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Short\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20d/m/yy] id=SpreadsheetParserSelectorDialogComponent-Table-Short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Medium\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20d%20mmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Long\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20d%20mmmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Full\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "            Errors\n" +
                        "              Empty \"text\"\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" DISABLED [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/cell/A1] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    @Test
    public void testCellDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicCell.with(context)
                ),
                SpreadsheetParserName.DATE_PARSER_PATTERN + " dd/mm/yyyy",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" DISABLED [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Short\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20d/m/yy] id=SpreadsheetParserSelectorDialogComponent-Table-Short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Medium\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20d%20mmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Long\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20d%20mmmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Full\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"d\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyyd] id=SpreadsheetParserSelectorDialogComponent-appender-append-0-Link\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyydd] id=SpreadsheetParserSelectorDialogComponent-appender-append-1-Link\n" +
                        "                  \"ddd\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyyddd] id=SpreadsheetParserSelectorDialogComponent-appender-append-2-Link\n" +
                        "                  \"dddd\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyydddd] id=SpreadsheetParserSelectorDialogComponent-appender-append-3-Link\n" +
                        "                  \"m\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyym] id=SpreadsheetParserSelectorDialogComponent-appender-append-4-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyymm] id=SpreadsheetParserSelectorDialogComponent-appender-append-5-Link\n" +
                        "                  \"mmm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyymmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-6-Link\n" +
                        "                  \"mmmm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyymmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-7-Link\n" +
                        "                  \"mmmmm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyymmmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-8-Link\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove / Replace component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20d/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20ddd/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dddd/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20ddmm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd//yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/m/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mmm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mmmm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mmmmm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mmyyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [date-parse-pattern dd/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern%20dd/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/cell/A1] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    // cell / date-time.................................................................................................

    @Test
    public void testCellDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicCell.with(context)
                ),
                SpreadsheetParserName.DATE_TIME_PARSER_PATTERN + " dd/mm/yyyy hh:mm:ss",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" DISABLED [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Short\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Medium\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:00 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Long\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:00 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Full\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:00 PM\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \".\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss.] id=SpreadsheetParserSelectorDialogComponent-appender-append-0-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss0] id=SpreadsheetParserSelectorDialogComponent-appender-append-1-Link\n" +
                        "                  \"A/P\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssA/P] id=SpreadsheetParserSelectorDialogComponent-appender-append-2-Link\n" +
                        "                  \"AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=SpreadsheetParserSelectorDialogComponent-appender-append-3-Link\n" +
                        "                  \"a/p\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssa/p] id=SpreadsheetParserSelectorDialogComponent-appender-append-4-Link\n" +
                        "                  \"am/pm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssam/pm] id=SpreadsheetParserSelectorDialogComponent-appender-append-5-Link\n" +
                        "                  \"d\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssd] id=SpreadsheetParserSelectorDialogComponent-appender-append-6-Link\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdd] id=SpreadsheetParserSelectorDialogComponent-appender-append-7-Link\n" +
                        "                  \"ddd\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssddd] id=SpreadsheetParserSelectorDialogComponent-appender-append-8-Link\n" +
                        "                  \"dddd\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdddd] id=SpreadsheetParserSelectorDialogComponent-appender-append-9-Link\n" +
                        "                  \"h\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssh] id=SpreadsheetParserSelectorDialogComponent-appender-append-10-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:sshh] id=SpreadsheetParserSelectorDialogComponent-appender-append-11-Link\n" +
                        "                  \"m\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=SpreadsheetParserSelectorDialogComponent-appender-append-12-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-13-Link\n" +
                        "                  \"mmm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-14-Link\n" +
                        "                  \"mmmm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-15-Link\n" +
                        "                  \"mmmmm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-16-Link\n" +
                        "                  \"yy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyy] id=SpreadsheetParserSelectorDialogComponent-appender-append-17-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyyyy] id=SpreadsheetParserSelectorDialogComponent-appender-append-18-Link\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove / Replace component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20d/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20ddd/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dddd/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20ddmm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd//yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/m/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mmm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mmmm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mmmmm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mmyyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyyhh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20h:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-6-Link-replace-0-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hhmm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh::ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:m:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-8-Link-replace-0-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mmss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:s] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-10-Link-replace-0-MenuItem\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [date-time-parse-pattern dd/mm/yyyy hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/cell/A1] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    // cell / number....................................................................................................

    @Test
    public void testCellNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicCell.with(context)
                ),
                SpreadsheetParserName.NUMBER_PARSER_PATTERN + " $#0.00",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" DISABLED [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Number\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=SpreadsheetParserSelectorDialogComponent-Table-Number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123.5\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Number\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=SpreadsheetParserSelectorDialogComponent-Table-Number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -123.5\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Number\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=SpreadsheetParserSelectorDialogComponent-Table-Number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Integer\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230] id=SpreadsheetParserSelectorDialogComponent-Table-Integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    124\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Integer\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230] id=SpreadsheetParserSelectorDialogComponent-Table-Integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -124\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Integer\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230] id=SpreadsheetParserSelectorDialogComponent-Table-Integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Percent\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230%25] id=SpreadsheetParserSelectorDialogComponent-Table-Percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12,350%\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Percent\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230%25] id=SpreadsheetParserSelectorDialogComponent-Table-Percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -12,350%\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Percent\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%23,%23%230%25] id=SpreadsheetParserSelectorDialogComponent-Table-Percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 9\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Currency\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%23,%23%230.00] id=SpreadsheetParserSelectorDialogComponent-Table-Currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $123.50\n" +
                        "                ROW 10\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Currency\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%23,%23%230.00] id=SpreadsheetParserSelectorDialogComponent-Table-Currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-123.50\n" +
                        "                ROW 11\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Currency\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%23,%23%230.00] id=SpreadsheetParserSelectorDialogComponent-Table-Currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"#\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00%23] id=SpreadsheetParserSelectorDialogComponent-appender-append-0-Link\n" +
                        "                  \"$\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00$] id=SpreadsheetParserSelectorDialogComponent-appender-append-1-Link\n" +
                        "                  \"%\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00%25] id=SpreadsheetParserSelectorDialogComponent-appender-append-2-Link\n" +
                        "                  \",\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00,] id=SpreadsheetParserSelectorDialogComponent-appender-append-3-Link\n" +
                        "                  \".\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00.] id=SpreadsheetParserSelectorDialogComponent-appender-append-4-Link\n" +
                        "                  \"/\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00/] id=SpreadsheetParserSelectorDialogComponent-appender-append-5-Link\n" +
                        "                  \"?\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00?] id=SpreadsheetParserSelectorDialogComponent-appender-append-6-Link\n" +
                        "                  \"E\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00E] id=SpreadsheetParserSelectorDialogComponent-appender-append-7-Link\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove / Replace component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"$\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20%230.00] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link\n" +
                        "                  \"#\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$0.00] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-1-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%23.00] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link\n" +
                        "                  \".\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%23000] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-3-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.0] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.0] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-5-Link\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [number-parse-pattern $#0.00] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern%20$%230.00] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/cell/A1] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    // cell / time......................................................................................................

    @Test
    public void testCellTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/cell/A1/parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicCell.with(context)
                ),
                SpreadsheetParserName.TIME_PARSER_PATTERN + " hh:mm",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" [#/1/Spreadsheet123/cell/A1/parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" DISABLED [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Short\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20h:mm%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Long\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:00 PM\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \".\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mm.] id=SpreadsheetParserSelectorDialogComponent-appender-append-0-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mm0] id=SpreadsheetParserSelectorDialogComponent-appender-append-1-Link\n" +
                        "                  \"A/P\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mmA/P] id=SpreadsheetParserSelectorDialogComponent-appender-append-2-Link\n" +
                        "                  \"AM/PM\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mmAM/PM] id=SpreadsheetParserSelectorDialogComponent-appender-append-3-Link\n" +
                        "                  \"a/p\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mma/p] id=SpreadsheetParserSelectorDialogComponent-appender-append-4-Link\n" +
                        "                  \"am/pm\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mmam/pm] id=SpreadsheetParserSelectorDialogComponent-appender-append-5-Link\n" +
                        "                  \"h\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mmh] id=SpreadsheetParserSelectorDialogComponent-appender-append-6-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mmhh] id=SpreadsheetParserSelectorDialogComponent-appender-append-7-Link\n" +
                        "                  \"s\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mms] id=SpreadsheetParserSelectorDialogComponent-appender-append-8-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mmss] id=SpreadsheetParserSelectorDialogComponent-appender-append-9-Link\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove / Replace component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20:mm] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20h:mm] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hhmm] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:m] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [time-parse-pattern hh:mm] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/cell/A1/parser/save/time-parse-pattern%20hh:mm] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/cell/A1/parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/cell/A1] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    // metadata / date..................................................................................................

    @Test
    public void testMetadataDate() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicMetadata.with(context)
                ),
                SpreadsheetParserName.DATE_PARSER_PATTERN + " dd/mm/yyyy",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetPatternKindTabsComponent\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0 SELECTED\n" +
                        "            \"Date\" DISABLED id=SpreadsheetParserSelectorDialogComponent-Tabs-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" [#/1/Spreadsheet123/metadata/date-time-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" [#/1/Spreadsheet123/metadata/number-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" [#/1/Spreadsheet123/metadata/time-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-time-parse\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" DISABLED [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet123/metadata/date-parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" [#/1/Spreadsheet123/metadata/date-parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" [#/1/Spreadsheet123/metadata/date-parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Short\n" +
                        "                  \"d/m/yy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d/m/yy] id=SpreadsheetParserSelectorDialogComponent-Table-Short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Medium\n" +
                        "                  \"d mmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d%20mmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Long\n" +
                        "                  \"d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d%20mmmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Full\n" +
                        "                  \"dddd, d mmmm yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dddd,%20d%20mmmm%20yyyy] id=SpreadsheetParserSelectorDialogComponent-Table-Full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"d\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyyd] id=SpreadsheetParserSelectorDialogComponent-appender-append-0-Link\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyydd] id=SpreadsheetParserSelectorDialogComponent-appender-append-1-Link\n" +
                        "                  \"ddd\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyyddd] id=SpreadsheetParserSelectorDialogComponent-appender-append-2-Link\n" +
                        "                  \"dddd\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyydddd] id=SpreadsheetParserSelectorDialogComponent-appender-append-3-Link\n" +
                        "                  \"m\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyym] id=SpreadsheetParserSelectorDialogComponent-appender-append-4-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyymm] id=SpreadsheetParserSelectorDialogComponent-appender-append-5-Link\n" +
                        "                  \"mmm\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyymmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-6-Link\n" +
                        "                  \"mmmm\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyymmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-7-Link\n" +
                        "                  \"mmmmm\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyymmmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-8-Link\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove / Replace component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20d/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20ddd/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dddd/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20ddmm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd//yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/m/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mmm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mmmm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mmmmm/yyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mmyyyy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yy] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [date-parse-pattern dd/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyy] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/metadata/date-parser/save/date-parse-pattern%20yyyy/mm/dd] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/metadata/date-parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    // metadata / date-time.............................................................................................

    @Test
    public void testMetadataDateTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/date-time-parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicMetadata.with(context)
                ),
                SpreadsheetParserName.DATE_TIME_PARSER_PATTERN + " dd/mm/yyyy hh:mm:ss",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Date Time parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetPatternKindTabsComponent\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" [#/1/Spreadsheet123/metadata/date-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-date-parse\n" +
                        "          TAB 1 SELECTED\n" +
                        "            \"Date Time\" DISABLED id=SpreadsheetParserSelectorDialogComponent-Tabs-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" [#/1/Spreadsheet123/metadata/number-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" [#/1/Spreadsheet123/metadata/time-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-time-parse\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" DISABLED [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" [#/1/Spreadsheet123/metadata/date-time-parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" [#/1/Spreadsheet123/metadata/date-time-parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Short\n" +
                        "                  \"d/m/yy, h:mm AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d/m/yy,%20h:mm%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31/12/99, 12:58 PM\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Medium\n" +
                        "                  \"d mmm yyyy, h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d%20mmm%20yyyy,%20h:mm:ss%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Medium-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 Dec. 1999, 12:58:00 PM\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Long\n" +
                        "                  \"d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    31 December 1999 at 12:58:00 PM\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Full\n" +
                        "                  \"dddd, d mmmm yyyy \\a\\t h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dddd,%20d%20mmmm%20yyyy%20%5Ca%5Ct%20h:mm:ss%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Full-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Friday, 31 December 1999 at 12:58:00 PM\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \".\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss.] id=SpreadsheetParserSelectorDialogComponent-appender-append-0-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss0] id=SpreadsheetParserSelectorDialogComponent-appender-append-1-Link\n" +
                        "                  \"A/P\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssA/P] id=SpreadsheetParserSelectorDialogComponent-appender-append-2-Link\n" +
                        "                  \"AM/PM\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=SpreadsheetParserSelectorDialogComponent-appender-append-3-Link\n" +
                        "                  \"a/p\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssa/p] id=SpreadsheetParserSelectorDialogComponent-appender-append-4-Link\n" +
                        "                  \"am/pm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssam/pm] id=SpreadsheetParserSelectorDialogComponent-appender-append-5-Link\n" +
                        "                  \"d\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssd] id=SpreadsheetParserSelectorDialogComponent-appender-append-6-Link\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdd] id=SpreadsheetParserSelectorDialogComponent-appender-append-7-Link\n" +
                        "                  \"ddd\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssddd] id=SpreadsheetParserSelectorDialogComponent-appender-append-8-Link\n" +
                        "                  \"dddd\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdddd] id=SpreadsheetParserSelectorDialogComponent-appender-append-9-Link\n" +
                        "                  \"h\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssh] id=SpreadsheetParserSelectorDialogComponent-appender-append-10-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:sshh] id=SpreadsheetParserSelectorDialogComponent-appender-append-11-Link\n" +
                        "                  \"m\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=SpreadsheetParserSelectorDialogComponent-appender-append-12-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-13-Link\n" +
                        "                  \"mmm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-14-Link\n" +
                        "                  \"mmmm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-15-Link\n" +
                        "                  \"mmmmm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=SpreadsheetParserSelectorDialogComponent-appender-append-16-Link\n" +
                        "                  \"yy\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyy] id=SpreadsheetParserSelectorDialogComponent-appender-append-17-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyyyy] id=SpreadsheetParserSelectorDialogComponent-appender-append-18-Link\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove / Replace component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"dd\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link\n" +
                        "                      \"d\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20d/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                        "                      \"ddd\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20ddd/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-1-MenuItem\n" +
                        "                      \"dddd\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dddd/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-2-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20ddmm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd//yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/m/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                        "                      \"mmm\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mmm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-1-MenuItem\n" +
                        "                      \"mmmm\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mmmm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-2-MenuItem\n" +
                        "                      \"mmmmm\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mmmmm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-3-MenuItem\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mmyyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-3-Link\n" +
                        "                  \"yyyy\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link\n" +
                        "                      \"yy\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link-replace-0-MenuItem\n" +
                        "                  \" \" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyyhh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-5-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-6-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20h:mm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-6-Link-replace-0-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hhmm:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-7-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh::ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-8-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:m:ss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-8-Link-replace-0-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mmss] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-9-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-10-Link\n" +
                        "                      \"s\" [/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:s] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-10-Link-replace-0-MenuItem\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [date-time-parse-pattern dd/mm/yyyy hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/metadata/date-time-parser/save/date-time-parse-pattern%20yyyy/mm/dd%20hh:mm] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/metadata/date-time-parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    // metadata / number................................................................................................

    @Test
    public void testMetadataNumber() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/number-parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicMetadata.with(context)
                ),
                SpreadsheetParserName.NUMBER_PARSER_PATTERN + " $#0.00",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Number parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetPatternKindTabsComponent\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" [#/1/Spreadsheet123/metadata/date-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" [#/1/Spreadsheet123/metadata/date-time-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-date-time-parse\n" +
                        "          TAB 2 SELECTED\n" +
                        "            \"Number\" DISABLED id=SpreadsheetParserSelectorDialogComponent-Tabs-number-parse\n" +
                        "          TAB 3\n" +
                        "            \"Time\" [#/1/Spreadsheet123/metadata/time-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-time-parse\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" [#/1/Spreadsheet123/metadata/number-parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet123/metadata/number-parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" DISABLED [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" [#/1/Spreadsheet123/metadata/number-parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Number\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=SpreadsheetParserSelectorDialogComponent-Table-Number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    123.5\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Number\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=SpreadsheetParserSelectorDialogComponent-Table-Number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -123.5\n" +
                        "                ROW 2\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Number\n" +
                        "                  \"#,##0.###\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230.%23%23%23] id=SpreadsheetParserSelectorDialogComponent-Table-Number-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0.\n" +
                        "                ROW 3\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Integer\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230] id=SpreadsheetParserSelectorDialogComponent-Table-Integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    124\n" +
                        "                ROW 4\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Integer\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230] id=SpreadsheetParserSelectorDialogComponent-Table-Integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -124\n" +
                        "                ROW 5\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Integer\n" +
                        "                  \"#,##0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230] id=SpreadsheetParserSelectorDialogComponent-Table-Integer-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0\n" +
                        "                ROW 6\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Percent\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230%25] id=SpreadsheetParserSelectorDialogComponent-Table-Percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12,350%\n" +
                        "                ROW 7\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Percent\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230%25] id=SpreadsheetParserSelectorDialogComponent-Table-Percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    -12,350%\n" +
                        "                ROW 8\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Percent\n" +
                        "                  \"#,##0%\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%23,%23%230%25] id=SpreadsheetParserSelectorDialogComponent-Table-Percent-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    0%\n" +
                        "                ROW 9\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Currency\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%23,%23%230.00] id=SpreadsheetParserSelectorDialogComponent-Table-Currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $123.50\n" +
                        "                ROW 10\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Currency\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%23,%23%230.00] id=SpreadsheetParserSelectorDialogComponent-Table-Currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $-123.50\n" +
                        "                ROW 11\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Currency\n" +
                        "                  \"$#,##0.00\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%23,%23%230.00] id=SpreadsheetParserSelectorDialogComponent-Table-Currency-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    $0.00\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"#\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00%23] id=SpreadsheetParserSelectorDialogComponent-appender-append-0-Link\n" +
                        "                  \"$\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00$] id=SpreadsheetParserSelectorDialogComponent-appender-append-1-Link\n" +
                        "                  \"%\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00%25] id=SpreadsheetParserSelectorDialogComponent-appender-append-2-Link\n" +
                        "                  \",\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00,] id=SpreadsheetParserSelectorDialogComponent-appender-append-3-Link\n" +
                        "                  \".\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00.] id=SpreadsheetParserSelectorDialogComponent-appender-append-4-Link\n" +
                        "                  \"/\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00/] id=SpreadsheetParserSelectorDialogComponent-appender-append-5-Link\n" +
                        "                  \"?\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00?] id=SpreadsheetParserSelectorDialogComponent-appender-append-6-Link\n" +
                        "                  \"E\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00E] id=SpreadsheetParserSelectorDialogComponent-appender-append-7-Link\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove / Replace component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"$\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20%230.00] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link\n" +
                        "                  \"#\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$0.00] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-1-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%23.00] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link\n" +
                        "                  \".\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%23000] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-3-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.0] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-4-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.0] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-5-Link\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [number-parse-pattern $#0.00] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%20$%230.00] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/metadata/number-parser/save/number-parse-pattern%200.%23] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/metadata/number-parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    // metadata / time..................................................................................................

    @Test
    public void testMetadataTime() {
        final AppContext context = this.appContext(
                HistoryToken.parseString("/1/Spreadsheet123/metadata/time-parser")
        );

        this.onHistoryTokenChangeAndSetTextAndCheck(
                SpreadsheetParserSelectorDialogComponent.with(
                        SpreadsheetParserSelectorDialogComponentContextBasicMetadata.with(context)
                ),
                SpreadsheetParserName.TIME_PARSER_PATTERN + " hh:mm",
                context,
                "SpreadsheetParserSelectorDialogComponent\n" +
                        "  SpreadsheetDialogComponent\n" +
                        "    Time parser\n" +
                        "    id=SpreadsheetParserSelectorDialogComponent includeClose=true\n" +
                        "      SpreadsheetPatternKindTabsComponent\n" +
                        "        SpreadsheetTabsComponent\n" +
                        "          TAB 0\n" +
                        "            \"Date\" [#/1/Spreadsheet123/metadata/date-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-date-parse\n" +
                        "          TAB 1\n" +
                        "            \"Date Time\" [#/1/Spreadsheet123/metadata/date-time-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-date-time-parse\n" +
                        "          TAB 2\n" +
                        "            \"Number\" [#/1/Spreadsheet123/metadata/number-parser] id=SpreadsheetParserSelectorDialogComponent-Tabs-number-parse\n" +
                        "          TAB 3 SELECTED\n" +
                        "            \"Time\" DISABLED id=SpreadsheetParserSelectorDialogComponent-Tabs-time-parse\n" +
                        "      SpreadsheetParserNameLinkListComponent\n" +
                        "        SpreadsheetLinkListComponent\n" +
                        "          SpreadsheetCard\n" +
                        "            Card\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"Date Parse Pattern\" [#/1/Spreadsheet123/metadata/time-parser/save/date-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-0-Link\n" +
                        "                  \"Date Time Parse Pattern\" [#/1/Spreadsheet123/metadata/time-parser/save/date-time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-1-Link\n" +
                        "                  \"Number Parse Pattern\" [#/1/Spreadsheet123/metadata/time-parser/save/number-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-2-Link\n" +
                        "                  \"Time Parse Pattern\" DISABLED [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern] id=SpreadsheetParserSelectorDialogComponent-parserNames-3-Link\n" +
                        "      SpreadsheetFormatterTableComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            SpreadsheetDataTableComponent\n" +
                        "              ROW(S)\n" +
                        "                ROW 0\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Short\n" +
                        "                  \"h:mm AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Short-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58 PM\n" +
                        "                ROW 1\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    Long\n" +
                        "                  \"h:mm:ss AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm:ss%20AM/PM] id=SpreadsheetParserSelectorDialogComponent-Table-Long-Link\n" +
                        "                  SpreadsheetTextNodeComponent\n" +
                        "                    12:58:00 PM\n" +
                        "      AppendPluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Append component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \".\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mm.] id=SpreadsheetParserSelectorDialogComponent-appender-append-0-Link\n" +
                        "                  \"0\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mm0] id=SpreadsheetParserSelectorDialogComponent-appender-append-1-Link\n" +
                        "                  \"A/P\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mmA/P] id=SpreadsheetParserSelectorDialogComponent-appender-append-2-Link\n" +
                        "                  \"AM/PM\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mmAM/PM] id=SpreadsheetParserSelectorDialogComponent-appender-append-3-Link\n" +
                        "                  \"a/p\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mma/p] id=SpreadsheetParserSelectorDialogComponent-appender-append-4-Link\n" +
                        "                  \"am/pm\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mmam/pm] id=SpreadsheetParserSelectorDialogComponent-appender-append-5-Link\n" +
                        "                  \"h\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mmh] id=SpreadsheetParserSelectorDialogComponent-appender-append-6-Link\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mmhh] id=SpreadsheetParserSelectorDialogComponent-appender-append-7-Link\n" +
                        "                  \"s\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mms] id=SpreadsheetParserSelectorDialogComponent-appender-append-8-Link\n" +
                        "                  \"ss\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mmss] id=SpreadsheetParserSelectorDialogComponent-appender-append-9-Link\n" +
                        "      RemoveOrReplacePluginSelectorTokenComponent\n" +
                        "        SpreadsheetCard\n" +
                        "          Card\n" +
                        "            Remove / Replace component(s)\n" +
                        "              SpreadsheetFlexLayout\n" +
                        "                ROW\n" +
                        "                  \"hh\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20:mm] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link\n" +
                        "                      \"h\" [/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20h:mm] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-0-Link-replace-0-MenuItem\n" +
                        "                  \":\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hhmm] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-1-Link\n" +
                        "                  \"mm\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link\n" +
                        "                      \"m\" [/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:m] id=SpreadsheetParserSelectorDialogComponent-removeOrReplace-remove-2-Link-replace-0-MenuItem\n" +
                        "      SpreadsheetParserSelectorComponent\n" +
                        "        ValueSpreadsheetTextBox\n" +
                        "          SpreadsheetTextBox\n" +
                        "            [time-parse-pattern hh:mm] id=SpreadsheetParserSelectorDialogComponent-TextBox\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"Save\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mm] id=SpreadsheetParserSelectorDialogComponent-save-Link\n" +
                        "          \"Undo\" [#/1/Spreadsheet123/metadata/time-parser/save/time-parse-pattern%20hh:mm:ss] id=SpreadsheetParserSelectorDialogComponent-undo-Link\n" +
                        "          \"Clear\" [#/1/Spreadsheet123/metadata/time-parser/save/] id=SpreadsheetParserSelectorDialogComponent-clear-Link\n" +
                        "          \"Close\" [#/1/Spreadsheet123/metadata] id=SpreadsheetParserSelectorDialogComponent-close-Link\n"
        );
    }

    // dialog...........................................................................................................

    @Test
    public void testMetadataDateDialogClose() {
        final SpreadsheetMetadataPropertyName<SpreadsheetParserSelector> property = SpreadsheetMetadataPropertyName.DATE_PARSER;

        this.historyTokenCloseDialogAndCheck(
                HistoryToken.metadataPropertySelect(
                        SPREADSHEET_ID,
                        NAME,
                        property
                ),
                HistoryToken.metadataSelect(
                        SPREADSHEET_ID,
                        NAME
                )
        );
    }

    @Test
    public void testMetadataDateDialogSave() {
        final SpreadsheetMetadataPropertyName<SpreadsheetParserSelector> property = SpreadsheetMetadataPropertyName.DATE_PARSER;
        final SpreadsheetParserSelector value = SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                .spreadsheetParserSelector();

        this.historyTokenSaveValueAndCheck(
                HistoryToken.metadataPropertySelect(
                        SPREADSHEET_ID,
                        NAME,
                        property
                ),
                Optional.of(value),
                HistoryToken.metadataPropertySave(
                        SPREADSHEET_ID,
                        NAME,
                        property,
                        Optional.of(value)
                )
        );
    }

    @Test
    public void testMetadataDateDialogRemove() {
        final SpreadsheetMetadataPropertyName<SpreadsheetParserSelector> property = SpreadsheetMetadataPropertyName.DATE_PARSER;

        this.historyTokenRemoveValueAndCheck(
                HistoryToken.metadataPropertySelect(
                        SPREADSHEET_ID,
                        NAME,
                        property
                ),
                Optional.empty(),
                HistoryToken.metadataPropertySave(
                        SPREADSHEET_ID,
                        NAME,
                        property,
                        Optional.empty()
                )
        );
    }

    @Test
    public void testCellDateDialogClose() {
        this.historyTokenCloseDialogAndCheck(
                HistoryToken.cellParserSelect(
                        SPREADSHEET_ID,
                        NAME,
                        CELL
                ),
                HistoryToken.cell(
                        SPREADSHEET_ID,
                        NAME,
                        CELL
                )
        );
    }

    @Test
    public void testCellDateDialogSave() {
        final SpreadsheetParserSelector value = SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy")
                .spreadsheetParserSelector();

        this.historyTokenSaveValueAndCheck(
                HistoryToken.cellParserSelect(
                        SPREADSHEET_ID,
                        NAME,
                        CELL
                ),
                Optional.of(value),
                HistoryToken.cellParserSave(
                        SPREADSHEET_ID,
                        NAME,
                        CELL,
                        Optional.of(value)
                )
        );
    }

    @Test
    public void testCellDateDialogRemove() {
        this.historyTokenRemoveValueAndCheck(
                HistoryToken.cellParserSelect(
                        SPREADSHEET_ID,
                        NAME,
                        CELL
                ),
                Optional.empty(),
                HistoryToken.cellParserSave(
                        SPREADSHEET_ID,
                        NAME,
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

            @Test
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
            public List<SpreadsheetFormatterSample> spreadsheetFormatterSamples(final SpreadsheetFormatterName name,
                                                                                final SpreadsheetFormatterProviderSamplesContext context) {
                return SpreadsheetMetadataTesting.SPREADSHEET_FORMATTER_PROVIDER.spreadsheetFormatterSamples(
                        name,
                        context
                );
            }

            @Override
            public SpreadsheetMetadata spreadsheetMetadata() {
                return SpreadsheetMetadataTesting.METADATA_EN_AU.set(
                        SpreadsheetMetadataPropertyName.SPREADSHEET_ID,
                        SpreadsheetId.with(1)
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
                return NOW.get();
            }

            @Override
            public Locale locale() {
                return LOCALE;
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
            public Optional<TextNode> format(final Object value) {
                return SPREADSHEET_FORMATTER_CONTEXT.format(value);
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
            public char percentageSymbol() {
                return SPREADSHEET_FORMATTER_CONTEXT.percentageSymbol();
            }

            @Override
            public char negativeSign() {
                return SPREADSHEET_FORMATTER_CONTEXT.negativeSign();
            }

            @Override
            public char positiveSign() {
                return SPREADSHEET_FORMATTER_CONTEXT.positiveSign();
            }
        };
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
