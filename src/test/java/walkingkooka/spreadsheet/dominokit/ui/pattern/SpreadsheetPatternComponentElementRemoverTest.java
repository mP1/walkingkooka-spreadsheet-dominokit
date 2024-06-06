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

package walkingkooka.spreadsheet.dominokit.ui.pattern;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Arrays;

public final class SpreadsheetPatternComponentElementRemoverTest implements ClassTesting<SpreadsheetPatternComponentElementRemover>,
        TreePrintableTesting {

    // date format......................................................................................................

    @Test
    public void testCellDateFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy"),
                "",
                "/1/Untitled/cell/A1/formatter/date",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"dd\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20/mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                \"d\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"dd\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                \"ddd\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                \"dddd\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                \"mmm\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                \"mmmm\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                \"mmmmm\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "            \"yyyy\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/] id=pattern-remove-4-Link\n" +
                        "                \"yy\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"yyyy\" [/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n"
        );
    }

    @Test
    public void testMetadataDateFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy"),
                "",
                "/1/Untitled/metadata/date-formatter",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"dd\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20/mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                \"d\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"dd\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                \"ddd\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                \"dddd\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                \"mmm\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                \"mmmm\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                \"mmmmm\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "            \"yyyy\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/] id=pattern-remove-4-Link\n" +
                        "                \"yy\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"yyyy\" [/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n"
        );
    }

    // date parse.......................................................................................................

    @Test
    public void testCellDateParse() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy"),
                "",
                "/1/Untitled/cell/A1/parse-pattern/date",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"dd\" [#/1/Untitled/cell/A1/parse-pattern/date/save//mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                \"d\" [/1/Untitled/cell/A1/parse-pattern/date/save/d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"dd\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                \"ddd\" [/1/Untitled/cell/A1/parse-pattern/date/save/ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                \"dddd\" [/1/Untitled/cell/A1/parse-pattern/date/save/dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/cell/A1/parse-pattern/date/save/ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                \"mmm\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                \"mmmm\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                \"mmmmm\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "            \"yyyy\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/] id=pattern-remove-4-Link\n" +
                        "                \"yy\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"yyyy\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n"
        );
    }

    @Test
    public void testMetadataDateParse() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateParsePattern("dd/mm/yyyy"),
                "",
                "/1/Untitled/metadata/date-parse-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"dd\" [#/1/Untitled/metadata/date-parse-pattern/save//mm/yyyy] id=pattern-remove-0-Link\n" +
                        "                \"d\" [/1/Untitled/metadata/date-parse-pattern/save/d/mm/yyyy] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"dd\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                \"ddd\" [/1/Untitled/metadata/date-parse-pattern/save/ddd/mm/yyyy] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                \"dddd\" [/1/Untitled/metadata/date-parse-pattern/save/dddd/mm/yyyy] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/metadata/date-parse-pattern/save/ddmm/yyyy] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/metadata/date-parse-pattern/save/dd//yyyy] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/metadata/date-parse-pattern/save/dd/m/yyyy] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                \"mmm\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mmm/yyyy] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                \"mmmm\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mmmm/yyyy] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                \"mmmmm\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mmmmm/yyyy] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mmyyyy] id=pattern-remove-3-Link\n" +
                        "            \"yyyy\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/] id=pattern-remove-4-Link\n" +
                        "                \"yy\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yy] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"yyyy\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-remove-4-alt-1-MenuItem\n"
        );
    }

    // date-time format.................................................................................................

    @Test
    public void testCellDateTimeFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateTimeFormatPattern("dd/mm/yyyy hh:mm:ss AM/PM"),
                "",
                "/1/Untitled/cell/A2/formatter/date-time",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"dd\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-Link\n" +
                        "                \"d\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20d/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"dd\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                \"ddd\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20ddd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                \"dddd\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dddd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20ddmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd//yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/m/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                \"mmm\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                \"mmmm\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mmmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                \"mmmmm\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mmmmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mmyyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-3-Link\n" +
                        "            \"yyyy\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/%20hh:mm:ss%20AM/PM] id=pattern-remove-4-Link\n" +
                        "                \"yy\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yy%20hh:mm:ss%20AM/PM] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"yyyy\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "            \" \" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyyhh:mm:ss%20AM/PM] id=pattern-remove-5-Link\n" +
                        "            \"hh\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20:mm:ss%20AM/PM] id=pattern-remove-6-Link\n" +
                        "                \"h\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20h:mm:ss%20AM/PM] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                \"hh\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hhmm:ss%20AM/PM] id=pattern-remove-7-Link\n" +
                        "            \"mm\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh::ss%20AM/PM] id=pattern-remove-8-Link\n" +
                        "                \"m\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:m:ss%20AM/PM] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mmss%20AM/PM] id=pattern-remove-9-Link\n" +
                        "            \"ss\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:%20AM/PM] id=pattern-remove-10-Link\n" +
                        "                \"s\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:s%20AM/PM] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                \"ss\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "            \" \" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=pattern-remove-11-Link\n" +
                        "            \"AM/PM\" [#/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20] id=pattern-remove-12-Link\n" +
                        "                \"A/P\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20A/P] id=pattern-remove-12-alt-0-MenuItem\n" +
                        "                \"AM/PM\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-12-alt-1-MenuItem\n" +
                        "                \"a/p\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20a/p] id=pattern-remove-12-alt-2-MenuItem\n" +
                        "                \"am/pm\" [/1/Untitled/cell/A2/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20am/pm] id=pattern-remove-12-alt-3-MenuItem\n"
        );
    }

    @Test
    public void testMetadataDateTimeFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateTimeFormatPattern("dd/mm/yyyy hh:mm:ss AM/PM"),
                "",
                "/1/Untitled/metadata/date-time-formatter",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"dd\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-Link\n" +
                        "                \"d\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20d/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"dd\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                \"ddd\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20ddd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                \"dddd\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dddd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20ddmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd//yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/m/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                \"mmm\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                \"mmmm\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mmmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                \"mmmmm\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mmmmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mmyyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-3-Link\n" +
                        "            \"yyyy\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/%20hh:mm:ss%20AM/PM] id=pattern-remove-4-Link\n" +
                        "                \"yy\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yy%20hh:mm:ss%20AM/PM] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"yyyy\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "            \" \" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyyhh:mm:ss%20AM/PM] id=pattern-remove-5-Link\n" +
                        "            \"hh\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20:mm:ss%20AM/PM] id=pattern-remove-6-Link\n" +
                        "                \"h\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20h:mm:ss%20AM/PM] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                \"hh\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hhmm:ss%20AM/PM] id=pattern-remove-7-Link\n" +
                        "            \"mm\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh::ss%20AM/PM] id=pattern-remove-8-Link\n" +
                        "                \"m\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:m:ss%20AM/PM] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mmss%20AM/PM] id=pattern-remove-9-Link\n" +
                        "            \"ss\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:%20AM/PM] id=pattern-remove-10-Link\n" +
                        "                \"s\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:s%20AM/PM] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                \"ss\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "            \" \" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=pattern-remove-11-Link\n" +
                        "            \"AM/PM\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20] id=pattern-remove-12-Link\n" +
                        "                \"A/P\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20A/P] id=pattern-remove-12-alt-0-MenuItem\n" +
                        "                \"AM/PM\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-12-alt-1-MenuItem\n" +
                        "                \"a/p\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20a/p] id=pattern-remove-12-alt-2-MenuItem\n" +
                        "                \"am/pm\" [/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss%20am/pm] id=pattern-remove-12-alt-3-MenuItem\n"
        );
    }

    // date-time parse..................................................................................................

    @Test
    public void testCellDateTimeParse() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateTimeParsePattern("dd/mm/yyyy hh:mm:ss AM/PM"),
                "",
                "/1/Untitled/cell/A2/parse-pattern/date-time",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"dd\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save//mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-Link\n" +
                        "                \"d\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/d/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"dd\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                \"ddd\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/ddd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                \"dddd\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dddd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/ddmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd//yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/m/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                \"mmm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                \"mmmm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mmmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                \"mmmmm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mmmmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mmyyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-3-Link\n" +
                        "            \"yyyy\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/%20hh:mm:ss%20AM/PM] id=pattern-remove-4-Link\n" +
                        "                \"yy\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yy%20hh:mm:ss%20AM/PM] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"yyyy\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "            \" \" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyyhh:mm:ss%20AM/PM] id=pattern-remove-5-Link\n" +
                        "            \"hh\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20:mm:ss%20AM/PM] id=pattern-remove-6-Link\n" +
                        "                \"h\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20h:mm:ss%20AM/PM] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                \"hh\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hhmm:ss%20AM/PM] id=pattern-remove-7-Link\n" +
                        "            \"mm\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh::ss%20AM/PM] id=pattern-remove-8-Link\n" +
                        "                \"m\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:m:ss%20AM/PM] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mmss%20AM/PM] id=pattern-remove-9-Link\n" +
                        "            \"ss\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:%20AM/PM] id=pattern-remove-10-Link\n" +
                        "                \"s\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:s%20AM/PM] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                \"ss\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "            \" \" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ssAM/PM] id=pattern-remove-11-Link\n" +
                        "            \"AM/PM\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20] id=pattern-remove-12-Link\n" +
                        "                \"A/P\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20A/P] id=pattern-remove-12-alt-0-MenuItem\n" +
                        "                \"AM/PM\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-12-alt-1-MenuItem\n" +
                        "                \"a/p\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20a/p] id=pattern-remove-12-alt-2-MenuItem\n" +
                        "                \"am/pm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy%20hh:mm:ss%20am/pm] id=pattern-remove-12-alt-3-MenuItem\n"
        );
    }

    @Test
    public void testMetadataDateTimeParse() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateTimeParsePattern("dd/mm/yyyy hh:mm:ss AM/PM"),
                "",
                "/1/Untitled/metadata/date-time-parse-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"dd\" [#/1/Untitled/metadata/date-time-parse-pattern/save//mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-Link\n" +
                        "                \"d\" [/1/Untitled/metadata/date-time-parse-pattern/save/d/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"dd\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "                \"ddd\" [/1/Untitled/metadata/date-time-parse-pattern/save/ddd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-2-MenuItem\n" +
                        "                \"dddd\" [/1/Untitled/metadata/date-time-parse-pattern/save/dddd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-0-alt-3-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/metadata/date-time-parse-pattern/save/ddmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd//yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/m/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "                \"mmm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-2-MenuItem\n" +
                        "                \"mmmm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mmmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-3-MenuItem\n" +
                        "                \"mmmmm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mmmmm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-2-alt-4-MenuItem\n" +
                        "            \"/\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mmyyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-3-Link\n" +
                        "            \"yyyy\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/%20hh:mm:ss%20AM/PM] id=pattern-remove-4-Link\n" +
                        "                \"yy\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yy%20hh:mm:ss%20AM/PM] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"yyyy\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-4-alt-1-MenuItem\n" +
                        "            \" \" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyyhh:mm:ss%20AM/PM] id=pattern-remove-5-Link\n" +
                        "            \"hh\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20:mm:ss%20AM/PM] id=pattern-remove-6-Link\n" +
                        "                \"h\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20h:mm:ss%20AM/PM] id=pattern-remove-6-alt-0-MenuItem\n" +
                        "                \"hh\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-6-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hhmm:ss%20AM/PM] id=pattern-remove-7-Link\n" +
                        "            \"mm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh::ss%20AM/PM] id=pattern-remove-8-Link\n" +
                        "                \"m\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:m:ss%20AM/PM] id=pattern-remove-8-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-8-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mmss%20AM/PM] id=pattern-remove-9-Link\n" +
                        "            \"ss\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:%20AM/PM] id=pattern-remove-10-Link\n" +
                        "                \"s\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:s%20AM/PM] id=pattern-remove-10-alt-0-MenuItem\n" +
                        "                \"ss\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-10-alt-1-MenuItem\n" +
                        "            \" \" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ssAM/PM] id=pattern-remove-11-Link\n" +
                        "            \"AM/PM\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20] id=pattern-remove-12-Link\n" +
                        "                \"A/P\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20A/P] id=pattern-remove-12-alt-0-MenuItem\n" +
                        "                \"AM/PM\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20AM/PM] id=pattern-remove-12-alt-1-MenuItem\n" +
                        "                \"a/p\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20a/p] id=pattern-remove-12-alt-2-MenuItem\n" +
                        "                \"am/pm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy%20hh:mm:ss%20am/pm] id=pattern-remove-12-alt-3-MenuItem\n"
        );
    }

    // number format....................................................................................................

    @Test
    public void testCellNumberFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseNumberFormatPattern("$#.00"),
                "",
                "/1/Untitled/cell/A1/formatter/number",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"$\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20%23.00] id=pattern-remove-0-Link\n" +
                        "                \"$\" [/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "            \"#\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$.00] id=pattern-remove-1-Link\n" +
                        "                \"#\" [/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "            \".\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%2300] id=pattern-remove-2-Link\n" +
                        "                \".\" [/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "            \"0\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.0] id=pattern-remove-3-Link\n" +
                        "                \"0\" [/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "            \"0\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.0] id=pattern-remove-4-Link\n" +
                        "                \"0\" [/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00] id=pattern-remove-4-alt-0-MenuItem\n"
        );
    }

    @Test
    public void testMetadataNumberFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseNumberFormatPattern("$#.00"),
                "",
                "/1/Untitled/metadata/number-formatter",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"$\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20%23.00] id=pattern-remove-0-Link\n" +
                        "                \"$\" [/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "            \"#\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$.00] id=pattern-remove-1-Link\n" +
                        "                \"#\" [/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "            \".\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%2300] id=pattern-remove-2-Link\n" +
                        "                \".\" [/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "            \"0\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.0] id=pattern-remove-3-Link\n" +
                        "                \"0\" [/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "            \"0\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.0] id=pattern-remove-4-Link\n" +
                        "                \"0\" [/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00] id=pattern-remove-4-alt-0-MenuItem\n"
        );
    }

    // number parse.....................................................................................................

    @Test
    public void testCellNumberParse() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseNumberParsePattern("$#.00"),
                "",
                "/1/Untitled/cell/A1/parse-pattern/number",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"$\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%23.00] id=pattern-remove-0-Link\n" +
                        "                \"$\" [/1/Untitled/cell/A1/parse-pattern/number/save/$%23.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "            \"#\" [#/1/Untitled/cell/A1/parse-pattern/number/save/$.00] id=pattern-remove-1-Link\n" +
                        "                \"#\" [/1/Untitled/cell/A1/parse-pattern/number/save/$%23.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "            \".\" [#/1/Untitled/cell/A1/parse-pattern/number/save/$%2300] id=pattern-remove-2-Link\n" +
                        "                \".\" [/1/Untitled/cell/A1/parse-pattern/number/save/$%23.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "            \"0\" [#/1/Untitled/cell/A1/parse-pattern/number/save/$%23.0] id=pattern-remove-3-Link\n" +
                        "                \"0\" [/1/Untitled/cell/A1/parse-pattern/number/save/$%23.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "            \"0\" [#/1/Untitled/cell/A1/parse-pattern/number/save/$%23.0] id=pattern-remove-4-Link\n" +
                        "                \"0\" [/1/Untitled/cell/A1/parse-pattern/number/save/$%23.00] id=pattern-remove-4-alt-0-MenuItem\n"
        );
    }

    @Test
    public void testMetadataNumberParse() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseNumberParsePattern("$#.00"),
                "",
                "/1/Untitled/metadata/number-parse-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"$\" [#/1/Untitled/metadata/number-parse-pattern/save/%23.00] id=pattern-remove-0-Link\n" +
                        "                \"$\" [/1/Untitled/metadata/number-parse-pattern/save/$%23.00] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "            \"#\" [#/1/Untitled/metadata/number-parse-pattern/save/$.00] id=pattern-remove-1-Link\n" +
                        "                \"#\" [/1/Untitled/metadata/number-parse-pattern/save/$%23.00] id=pattern-remove-1-alt-0-MenuItem\n" +
                        "            \".\" [#/1/Untitled/metadata/number-parse-pattern/save/$%2300] id=pattern-remove-2-Link\n" +
                        "                \".\" [/1/Untitled/metadata/number-parse-pattern/save/$%23.00] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "            \"0\" [#/1/Untitled/metadata/number-parse-pattern/save/$%23.0] id=pattern-remove-3-Link\n" +
                        "                \"0\" [/1/Untitled/metadata/number-parse-pattern/save/$%23.00] id=pattern-remove-3-alt-0-MenuItem\n" +
                        "            \"0\" [#/1/Untitled/metadata/number-parse-pattern/save/$%23.0] id=pattern-remove-4-Link\n" +
                        "                \"0\" [/1/Untitled/metadata/number-parse-pattern/save/$%23.00] id=pattern-remove-4-alt-0-MenuItem\n"
        );
    }

    // text format......................................................................................................

    @Test
    public void testCellTextFormatAtSign() {
        this.refreshAndCheck(
                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN,
                "",
                "/1/Untitled/cell/A1/formatter/text",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"@\" [#/1/Untitled/cell/A1/formatter/text/save/] id=pattern-remove-0-Link\n" +
                        "                \"@\" [/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20@] id=pattern-remove-0-alt-0-MenuItem\n"
        );
    }

    @Test
    public void testMetadataTextFormatAtSign() {
        this.refreshAndCheck(
                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN,
                "",
                "/1/Untitled/metadata/text-formatter",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"@\" [#/1/Untitled/metadata/text-formatter/save/] id=pattern-remove-0-Link\n" +
                        "                \"@\" [/1/Untitled/metadata/text-formatter/save/text-format-pattern%20@] id=pattern-remove-0-alt-0-MenuItem\n"
        );
    }

    @Test
    public void testMetadataTextFormatTextLiteral() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseTextFormatPattern("\"Hello\"@"),
                "",
                "/1/Untitled/metadata/text-formatter",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"\"Hello\"\" [#/1/Untitled/metadata/text-formatter/save/text-format-pattern%20@] id=pattern-remove-0-Link\n" +
                        "            \"@\" [#/1/Untitled/metadata/text-formatter/save/text-format-pattern%20%22Hello%22] id=pattern-remove-1-Link\n" +
                        "                \"@\" [/1/Untitled/metadata/text-formatter/save/text-format-pattern%20%22Hello%22@] id=pattern-remove-1-alt-0-MenuItem\n"
        );
    }

    // time format......................................................................................................

    @Test
    public void testCellTimeFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseTimeFormatPattern("hh:mm:ss"),
                "",
                "/1/Untitled/cell/A4/formatter/time",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"hh\" [#/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20:mm:ss] id=pattern-remove-0-Link\n" +
                        "                \"h\" [/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20h:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"hh\" [/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hhmm:ss] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hh::ss] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hh:m:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hh:mmss] id=pattern-remove-3-Link\n" +
                        "            \"ss\" [#/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hh:mm:] id=pattern-remove-4-Link\n" +
                        "                \"s\" [/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hh:mm:s] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"ss\" [/1/Untitled/cell/A4/formatter/time/save/time-format-pattern%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n"
        );
    }

    @Test
    public void testMetadataTimeFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseTimeFormatPattern("hh:mm:ss"),
                "",
                "/1/Untitled/metadata/time-formatter",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"hh\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20:mm:ss] id=pattern-remove-0-Link\n" +
                        "                \"h\" [/1/Untitled/metadata/time-formatter/save/time-format-pattern%20h:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"hh\" [/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hhmm:ss] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh::ss] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:m:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mmss] id=pattern-remove-3-Link\n" +
                        "            \"ss\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:] id=pattern-remove-4-Link\n" +
                        "                \"s\" [/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:s] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"ss\" [/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n"
        );
    }

    // time parse......................................................................................................

    @Test
    public void testCellTimeParse() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseTimeParsePattern("hh:mm:ss"),
                "",
                "/1/Untitled/cell/A4/parse-pattern/time",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"hh\" [#/1/Untitled/cell/A4/parse-pattern/time/save/:mm:ss] id=pattern-remove-0-Link\n" +
                        "                \"h\" [/1/Untitled/cell/A4/parse-pattern/time/save/h:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"hh\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/cell/A4/parse-pattern/time/save/hhmm:ss] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/cell/A4/parse-pattern/time/save/hh::ss] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:m:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/cell/A4/parse-pattern/time/save/hh:mmss] id=pattern-remove-3-Link\n" +
                        "            \"ss\" [#/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:] id=pattern-remove-4-Link\n" +
                        "                \"s\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:s] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"ss\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n"
        );
    }

    @Test
    public void testMetadataTimeParse() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseTimeParsePattern("hh:mm:ss"),
                "",
                "/1/Untitled/metadata/time-parse-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        SpreadsheetFlexLayout\n" +
                        "          ROW\n" +
                        "            \"hh\" [#/1/Untitled/metadata/time-parse-pattern/save/:mm:ss] id=pattern-remove-0-Link\n" +
                        "                \"h\" [/1/Untitled/metadata/time-parse-pattern/save/h:mm:ss] id=pattern-remove-0-alt-0-MenuItem\n" +
                        "                \"hh\" [/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss] id=pattern-remove-0-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/metadata/time-parse-pattern/save/hhmm:ss] id=pattern-remove-1-Link\n" +
                        "            \"mm\" [#/1/Untitled/metadata/time-parse-pattern/save/hh::ss] id=pattern-remove-2-Link\n" +
                        "                \"m\" [/1/Untitled/metadata/time-parse-pattern/save/hh:m:ss] id=pattern-remove-2-alt-0-MenuItem\n" +
                        "                \"mm\" [/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss] id=pattern-remove-2-alt-1-MenuItem\n" +
                        "            \":\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mmss] id=pattern-remove-3-Link\n" +
                        "            \"ss\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:] id=pattern-remove-4-Link\n" +
                        "                \"s\" [/1/Untitled/metadata/time-parse-pattern/save/hh:mm:s] id=pattern-remove-4-alt-0-MenuItem\n" +
                        "                \"ss\" [/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss] id=pattern-remove-4-alt-1-MenuItem\n"
        );
    }

    // helpers..........................................................................................................

    private void refreshAndCheck(final SpreadsheetPattern pattern,
                                 final String errorPattern,
                                 final String historyToken,
                                 final String expected) {
        final SpreadsheetPatternComponentElementRemover elements = SpreadsheetPatternComponentElementRemover.empty();

        elements.refresh(
                pattern,
                errorPattern,
                new FakeSpreadsheetPatternDialogComponentContext() {
                    @Override
                    public SpreadsheetPatternKind patternKind() {
                        return this.historyToken()
                                .patternKind()
                                .get();
                    }

                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString(historyToken);
                    }

                    @Override
                    public void debug(final Object... values) {
                        System.out.println(Arrays.toString(values));
                    }
                }
        );

        this.treePrintAndCheck(
                elements,
                expected
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetPatternComponentElementRemover> type() {
        return SpreadsheetPatternComponentElementRemover.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
