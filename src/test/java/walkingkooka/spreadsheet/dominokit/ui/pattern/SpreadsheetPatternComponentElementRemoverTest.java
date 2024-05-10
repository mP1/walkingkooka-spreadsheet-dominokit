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
import walkingkooka.net.UrlFragment;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
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
                "/1/Untitled/cell/A1/format-pattern/date",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"dd\" [#/1/Untitled/cell/A1/format-pattern/date/save//mm/yyyy]\n" +
                        "            pattern-alternative-0-MenuItem \"d\" [/1/Untitled/cell/A1/format-pattern/date/save/d/mm/yyyy]\n" +
                        "            pattern-alternative-1-MenuItem \"dd\" [/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyy]\n" +
                        "            pattern-alternative-2-MenuItem \"ddd\" [/1/Untitled/cell/A1/format-pattern/date/save/ddd/mm/yyyy]\n" +
                        "            pattern-alternative-3-MenuItem \"dddd\" [/1/Untitled/cell/A1/format-pattern/date/save/dddd/mm/yyyy]\n" +
                        "        \"/\" [#/1/Untitled/cell/A1/format-pattern/date/save/ddmm/yyyy]\n" +
                        "        \"mm\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd//yyyy]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/cell/A1/format-pattern/date/save/dd/m/yyyy]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyy]\n" +
                        "            pattern-alternative-2-MenuItem \"mmm\" [/1/Untitled/cell/A1/format-pattern/date/save/dd/mmm/yyyy]\n" +
                        "            pattern-alternative-3-MenuItem \"mmmm\" [/1/Untitled/cell/A1/format-pattern/date/save/dd/mmmm/yyyy]\n" +
                        "            pattern-alternative-4-MenuItem \"mmmmm\" [/1/Untitled/cell/A1/format-pattern/date/save/dd/mmmmm/yyyy]\n" +
                        "        \"/\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mmyyyy]\n" +
                        "        \"yyyy\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/]\n" +
                        "            pattern-alternative-0-MenuItem \"yy\" [/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yy]\n" +
                        "            pattern-alternative-1-MenuItem \"yyyy\" [/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyy]\n"
        );
    }

    @Test
    public void testMetadataDateFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateFormatPattern("dd/mm/yyyy"),
                "",
                "/1/Untitled/metadata/date-format-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"dd\" [#/1/Untitled/metadata/date-format-pattern/save//mm/yyyy]\n" +
                        "            pattern-alternative-0-MenuItem \"d\" [/1/Untitled/metadata/date-format-pattern/save/d/mm/yyyy]\n" +
                        "            pattern-alternative-1-MenuItem \"dd\" [/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyy]\n" +
                        "            pattern-alternative-2-MenuItem \"ddd\" [/1/Untitled/metadata/date-format-pattern/save/ddd/mm/yyyy]\n" +
                        "            pattern-alternative-3-MenuItem \"dddd\" [/1/Untitled/metadata/date-format-pattern/save/dddd/mm/yyyy]\n" +
                        "        \"/\" [#/1/Untitled/metadata/date-format-pattern/save/ddmm/yyyy]\n" +
                        "        \"mm\" [#/1/Untitled/metadata/date-format-pattern/save/dd//yyyy]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/metadata/date-format-pattern/save/dd/m/yyyy]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyy]\n" +
                        "            pattern-alternative-2-MenuItem \"mmm\" [/1/Untitled/metadata/date-format-pattern/save/dd/mmm/yyyy]\n" +
                        "            pattern-alternative-3-MenuItem \"mmmm\" [/1/Untitled/metadata/date-format-pattern/save/dd/mmmm/yyyy]\n" +
                        "            pattern-alternative-4-MenuItem \"mmmmm\" [/1/Untitled/metadata/date-format-pattern/save/dd/mmmmm/yyyy]\n" +
                        "        \"/\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mmyyyy]\n" +
                        "        \"yyyy\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/]\n" +
                        "            pattern-alternative-0-MenuItem \"yy\" [/1/Untitled/metadata/date-format-pattern/save/dd/mm/yy]\n" +
                        "            pattern-alternative-1-MenuItem \"yyyy\" [/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyy]\n"
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
                        "        \"dd\" [#/1/Untitled/cell/A1/parse-pattern/date/save//mm/yyyy]\n" +
                        "            pattern-alternative-0-MenuItem \"d\" [/1/Untitled/cell/A1/parse-pattern/date/save/d/mm/yyyy]\n" +
                        "            pattern-alternative-1-MenuItem \"dd\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyy]\n" +
                        "            pattern-alternative-2-MenuItem \"ddd\" [/1/Untitled/cell/A1/parse-pattern/date/save/ddd/mm/yyyy]\n" +
                        "            pattern-alternative-3-MenuItem \"dddd\" [/1/Untitled/cell/A1/parse-pattern/date/save/dddd/mm/yyyy]\n" +
                        "        \"/\" [#/1/Untitled/cell/A1/parse-pattern/date/save/ddmm/yyyy]\n" +
                        "        \"mm\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd//yyyy]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/m/yyyy]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyy]\n" +
                        "            pattern-alternative-2-MenuItem \"mmm\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mmm/yyyy]\n" +
                        "            pattern-alternative-3-MenuItem \"mmmm\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mmmm/yyyy]\n" +
                        "            pattern-alternative-4-MenuItem \"mmmmm\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mmmmm/yyyy]\n" +
                        "        \"/\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mmyyyy]\n" +
                        "        \"yyyy\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/]\n" +
                        "            pattern-alternative-0-MenuItem \"yy\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yy]\n" +
                        "            pattern-alternative-1-MenuItem \"yyyy\" [/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyy]\n"
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
                        "        \"dd\" [#/1/Untitled/metadata/date-parse-pattern/save//mm/yyyy]\n" +
                        "            pattern-alternative-0-MenuItem \"d\" [/1/Untitled/metadata/date-parse-pattern/save/d/mm/yyyy]\n" +
                        "            pattern-alternative-1-MenuItem \"dd\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyy]\n" +
                        "            pattern-alternative-2-MenuItem \"ddd\" [/1/Untitled/metadata/date-parse-pattern/save/ddd/mm/yyyy]\n" +
                        "            pattern-alternative-3-MenuItem \"dddd\" [/1/Untitled/metadata/date-parse-pattern/save/dddd/mm/yyyy]\n" +
                        "        \"/\" [#/1/Untitled/metadata/date-parse-pattern/save/ddmm/yyyy]\n" +
                        "        \"mm\" [#/1/Untitled/metadata/date-parse-pattern/save/dd//yyyy]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/metadata/date-parse-pattern/save/dd/m/yyyy]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyy]\n" +
                        "            pattern-alternative-2-MenuItem \"mmm\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mmm/yyyy]\n" +
                        "            pattern-alternative-3-MenuItem \"mmmm\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mmmm/yyyy]\n" +
                        "            pattern-alternative-4-MenuItem \"mmmmm\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mmmmm/yyyy]\n" +
                        "        \"/\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mmyyyy]\n" +
                        "        \"yyyy\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/]\n" +
                        "            pattern-alternative-0-MenuItem \"yy\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yy]\n" +
                        "            pattern-alternative-1-MenuItem \"yyyy\" [/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyy]\n"
        );
    }

    // date-time format.................................................................................................

    @Test
    public void testCellDateTimeFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateTimeFormatPattern("dd/mm/yyyy hh:mm:ss AM/PM"),
                "",
                "/1/Untitled/cell/A2/format-pattern/date-time",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"dd\" [#/1/Untitled/cell/A2/format-pattern/date-time/save//mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"d\" [/1/Untitled/cell/A2/format-pattern/date-time/save/d/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"dd\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"ddd\" [/1/Untitled/cell/A2/format-pattern/date-time/save/ddd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-3-MenuItem \"dddd\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dddd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"/\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/ddmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"mm\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd//yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/m/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"mmm\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-3-MenuItem \"mmmm\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mmmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-4-MenuItem \"mmmmm\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mmmmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"/\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mmyyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"yyyy\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"yy\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"yyyy\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \" \" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyyhh:mm:ss+AM/PM]\n" +
                        "        \"hh\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"h\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+h:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"hh\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \":\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hhmm:ss+AM/PM]\n" +
                        "        \"mm\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh::ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:m:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \":\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mmss+AM/PM]\n" +
                        "        \"ss\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"s\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:s+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"ss\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \" \" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssAM/PM]\n" +
                        "        \"AM/PM\" [#/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+]\n" +
                        "            pattern-alternative-0-MenuItem \"A/P\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+A/P]\n" +
                        "            pattern-alternative-1-MenuItem \"AM/PM\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"a/p\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+a/p]\n" +
                        "            pattern-alternative-3-MenuItem \"am/pm\" [/1/Untitled/cell/A2/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+am/pm]\n"
        );
    }

    @Test
    public void testMetadataDateTimeFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseDateTimeFormatPattern("dd/mm/yyyy hh:mm:ss AM/PM"),
                "",
                "/1/Untitled/metadata/date-time-format-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"dd\" [#/1/Untitled/metadata/date-time-format-pattern/save//mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"d\" [/1/Untitled/metadata/date-time-format-pattern/save/d/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"dd\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"ddd\" [/1/Untitled/metadata/date-time-format-pattern/save/ddd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-3-MenuItem \"dddd\" [/1/Untitled/metadata/date-time-format-pattern/save/dddd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"/\" [#/1/Untitled/metadata/date-time-format-pattern/save/ddmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"mm\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd//yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/m/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"mmm\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-3-MenuItem \"mmmm\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mmmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-4-MenuItem \"mmmmm\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mmmmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"/\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mmyyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"yyyy\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"yy\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"yyyy\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \" \" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyyhh:mm:ss+AM/PM]\n" +
                        "        \"hh\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"h\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+h:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"hh\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \":\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hhmm:ss+AM/PM]\n" +
                        "        \"mm\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh::ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:m:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \":\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mmss+AM/PM]\n" +
                        "        \"ss\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"s\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:s+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"ss\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \" \" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssAM/PM]\n" +
                        "        \"AM/PM\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+]\n" +
                        "            pattern-alternative-0-MenuItem \"A/P\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+A/P]\n" +
                        "            pattern-alternative-1-MenuItem \"AM/PM\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"a/p\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+a/p]\n" +
                        "            pattern-alternative-3-MenuItem \"am/pm\" [/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss+am/pm]\n"
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
                        "        \"dd\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save//mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"d\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/d/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"dd\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"ddd\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/ddd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-3-MenuItem \"dddd\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dddd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"/\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/ddmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"mm\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd//yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/m/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"mmm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-3-MenuItem \"mmmm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mmmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-4-MenuItem \"mmmmm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mmmmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"/\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mmyyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"yyyy\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"yy\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"yyyy\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \" \" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyyhh:mm:ss+AM/PM]\n" +
                        "        \"hh\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"h\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+h:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"hh\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \":\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hhmm:ss+AM/PM]\n" +
                        "        \"mm\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh::ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:m:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \":\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mmss+AM/PM]\n" +
                        "        \"ss\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"s\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:s+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"ss\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \" \" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssAM/PM]\n" +
                        "        \"AM/PM\" [#/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+]\n" +
                        "            pattern-alternative-0-MenuItem \"A/P\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+A/P]\n" +
                        "            pattern-alternative-1-MenuItem \"AM/PM\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"a/p\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+a/p]\n" +
                        "            pattern-alternative-3-MenuItem \"am/pm\" [/1/Untitled/cell/A2/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss+am/pm]\n"
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
                        "        \"dd\" [#/1/Untitled/metadata/date-time-parse-pattern/save//mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"d\" [/1/Untitled/metadata/date-time-parse-pattern/save/d/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"dd\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"ddd\" [/1/Untitled/metadata/date-time-parse-pattern/save/ddd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-3-MenuItem \"dddd\" [/1/Untitled/metadata/date-time-parse-pattern/save/dddd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"/\" [#/1/Untitled/metadata/date-time-parse-pattern/save/ddmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"mm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd//yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/m/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"mmm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-3-MenuItem \"mmmm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mmmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-4-MenuItem \"mmmmm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mmmmm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"/\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mmyyyy+hh:mm:ss+AM/PM]\n" +
                        "        \"yyyy\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"yy\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"yyyy\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \" \" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyyhh:mm:ss+AM/PM]\n" +
                        "        \"hh\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"h\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+h:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"hh\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \":\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hhmm:ss+AM/PM]\n" +
                        "        \"mm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh::ss+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:m:ss+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \":\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mmss+AM/PM]\n" +
                        "        \"ss\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:+AM/PM]\n" +
                        "            pattern-alternative-0-MenuItem \"s\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:s+AM/PM]\n" +
                        "            pattern-alternative-1-MenuItem \"ss\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "        \" \" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssAM/PM]\n" +
                        "        \"AM/PM\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+]\n" +
                        "            pattern-alternative-0-MenuItem \"A/P\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+A/P]\n" +
                        "            pattern-alternative-1-MenuItem \"AM/PM\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+AM/PM]\n" +
                        "            pattern-alternative-2-MenuItem \"a/p\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+a/p]\n" +
                        "            pattern-alternative-3-MenuItem \"am/pm\" [/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss+am/pm]\n"
        );
    }

    // number format....................................................................................................

    @Test
    public void testCellNumberFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseNumberFormatPattern("$#.00"),
                "",
                "/1/Untitled/cell/A1/format-pattern/number",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"$\" [#/1/Untitled/cell/A1/format-pattern/number/save/%23.00]\n" +
                        "            pattern-alternative-0-MenuItem \"$\" [/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00]\n" +
                        "        \"#\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24.00]\n" +
                        "            pattern-alternative-0-MenuItem \"#\" [/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00]\n" +
                        "        \".\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%2300]\n" +
                        "            pattern-alternative-0-MenuItem \".\" [/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00]\n" +
                        "        \"0\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.0]\n" +
                        "            pattern-alternative-0-MenuItem \"0\" [/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00]\n" +
                        "        \"0\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.0]\n" +
                        "            pattern-alternative-0-MenuItem \"0\" [/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00]\n"
        );
    }

    @Test
    public void testMetadataNumberFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseNumberFormatPattern("$#.00"),
                "",
                "/1/Untitled/metadata/number-format-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"$\" [#/1/Untitled/metadata/number-format-pattern/save/%23.00]\n" +
                        "            pattern-alternative-0-MenuItem \"$\" [/1/Untitled/metadata/number-format-pattern/save/%24%23.00]\n" +
                        "        \"#\" [#/1/Untitled/metadata/number-format-pattern/save/%24.00]\n" +
                        "            pattern-alternative-0-MenuItem \"#\" [/1/Untitled/metadata/number-format-pattern/save/%24%23.00]\n" +
                        "        \".\" [#/1/Untitled/metadata/number-format-pattern/save/%24%2300]\n" +
                        "            pattern-alternative-0-MenuItem \".\" [/1/Untitled/metadata/number-format-pattern/save/%24%23.00]\n" +
                        "        \"0\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.0]\n" +
                        "            pattern-alternative-0-MenuItem \"0\" [/1/Untitled/metadata/number-format-pattern/save/%24%23.00]\n" +
                        "        \"0\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.0]\n" +
                        "            pattern-alternative-0-MenuItem \"0\" [/1/Untitled/metadata/number-format-pattern/save/%24%23.00]\n"
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
                        "        \"$\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%23.00]\n" +
                        "            pattern-alternative-0-MenuItem \"$\" [/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00]\n" +
                        "        \"#\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24.00]\n" +
                        "            pattern-alternative-0-MenuItem \"#\" [/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00]\n" +
                        "        \".\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%2300]\n" +
                        "            pattern-alternative-0-MenuItem \".\" [/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00]\n" +
                        "        \"0\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.0]\n" +
                        "            pattern-alternative-0-MenuItem \"0\" [/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00]\n" +
                        "        \"0\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.0]\n" +
                        "            pattern-alternative-0-MenuItem \"0\" [/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00]\n"
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
                        "        \"$\" [#/1/Untitled/metadata/number-parse-pattern/save/%23.00]\n" +
                        "            pattern-alternative-0-MenuItem \"$\" [/1/Untitled/metadata/number-parse-pattern/save/%24%23.00]\n" +
                        "        \"#\" [#/1/Untitled/metadata/number-parse-pattern/save/%24.00]\n" +
                        "            pattern-alternative-0-MenuItem \"#\" [/1/Untitled/metadata/number-parse-pattern/save/%24%23.00]\n" +
                        "        \".\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%2300]\n" +
                        "            pattern-alternative-0-MenuItem \".\" [/1/Untitled/metadata/number-parse-pattern/save/%24%23.00]\n" +
                        "        \"0\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.0]\n" +
                        "            pattern-alternative-0-MenuItem \"0\" [/1/Untitled/metadata/number-parse-pattern/save/%24%23.00]\n" +
                        "        \"0\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.0]\n" +
                        "            pattern-alternative-0-MenuItem \"0\" [/1/Untitled/metadata/number-parse-pattern/save/%24%23.00]\n"
        );
    }

    // text format......................................................................................................

    @Test
    public void testCellTextFormatAtSign() {
        this.refreshAndCheck(
                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN,
                "",
                "/1/Untitled/cell/A1/format-pattern/text",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"@\" [#/1/Untitled/cell/A1/format-pattern/text/save/]\n" +
                        "            pattern-alternative-0-MenuItem \"@\" [/1/Untitled/cell/A1/format-pattern/text/save/@]\n"
        );
    }

    @Test
    public void testMetadataTextFormatAtSign() {
        this.refreshAndCheck(
                SpreadsheetPattern.DEFAULT_TEXT_FORMAT_PATTERN,
                "",
                "/1/Untitled/metadata/text-format-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"@\" [#/1/Untitled/metadata/text-format-pattern/save/]\n" +
                        "            pattern-alternative-0-MenuItem \"@\" [/1/Untitled/metadata/text-format-pattern/save/@]\n"
        );
    }

    @Test
    public void testMetadataTextFormatTextLiteral() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseTextFormatPattern("\"Hello\"@"),
                "",
                "/1/Untitled/metadata/text-format-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"\"Hello\"\" [#/1/Untitled/metadata/text-format-pattern/save/@]\n" +
                        "        \"@\" [#/1/Untitled/metadata/text-format-pattern/save/%22Hello%22]\n" +
                        "            pattern-alternative-0-MenuItem \"@\" [/1/Untitled/metadata/text-format-pattern/save/%22Hello%22@]\n"
        );
    }

    // time format......................................................................................................

    @Test
    public void testCellTimeFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseTimeFormatPattern("hh:mm:ss"),
                "",
                "/1/Untitled/cell/A4/format-pattern/time",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"hh\" [#/1/Untitled/cell/A4/format-pattern/time/save/:mm:ss]\n" +
                        "            pattern-alternative-0-MenuItem \"h\" [/1/Untitled/cell/A4/format-pattern/time/save/h:mm:ss]\n" +
                        "            pattern-alternative-1-MenuItem \"hh\" [/1/Untitled/cell/A4/format-pattern/time/save/hh:mm:ss]\n" +
                        "        \":\" [#/1/Untitled/cell/A4/format-pattern/time/save/hhmm:ss]\n" +
                        "        \"mm\" [#/1/Untitled/cell/A4/format-pattern/time/save/hh::ss]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/cell/A4/format-pattern/time/save/hh:m:ss]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/cell/A4/format-pattern/time/save/hh:mm:ss]\n" +
                        "        \":\" [#/1/Untitled/cell/A4/format-pattern/time/save/hh:mmss]\n" +
                        "        \"ss\" [#/1/Untitled/cell/A4/format-pattern/time/save/hh:mm:]\n" +
                        "            pattern-alternative-0-MenuItem \"s\" [/1/Untitled/cell/A4/format-pattern/time/save/hh:mm:s]\n" +
                        "            pattern-alternative-1-MenuItem \"ss\" [/1/Untitled/cell/A4/format-pattern/time/save/hh:mm:ss]\n"
        );
    }

    @Test
    public void testMetadataTimeFormat() {
        this.refreshAndCheck(
                SpreadsheetPattern.parseTimeFormatPattern("hh:mm:ss"),
                "",
                "/1/Untitled/metadata/time-format-pattern",
                "SpreadsheetPatternComponentElementRemover\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      Remove individual component(s)\n" +
                        "        \"hh\" [#/1/Untitled/metadata/time-format-pattern/save/:mm:ss]\n" +
                        "            pattern-alternative-0-MenuItem \"h\" [/1/Untitled/metadata/time-format-pattern/save/h:mm:ss]\n" +
                        "            pattern-alternative-1-MenuItem \"hh\" [/1/Untitled/metadata/time-format-pattern/save/hh:mm:ss]\n" +
                        "        \":\" [#/1/Untitled/metadata/time-format-pattern/save/hhmm:ss]\n" +
                        "        \"mm\" [#/1/Untitled/metadata/time-format-pattern/save/hh::ss]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/metadata/time-format-pattern/save/hh:m:ss]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/metadata/time-format-pattern/save/hh:mm:ss]\n" +
                        "        \":\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mmss]\n" +
                        "        \"ss\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:]\n" +
                        "            pattern-alternative-0-MenuItem \"s\" [/1/Untitled/metadata/time-format-pattern/save/hh:mm:s]\n" +
                        "            pattern-alternative-1-MenuItem \"ss\" [/1/Untitled/metadata/time-format-pattern/save/hh:mm:ss]\n"
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
                        "        \"hh\" [#/1/Untitled/cell/A4/parse-pattern/time/save/:mm:ss]\n" +
                        "            pattern-alternative-0-MenuItem \"h\" [/1/Untitled/cell/A4/parse-pattern/time/save/h:mm:ss]\n" +
                        "            pattern-alternative-1-MenuItem \"hh\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:ss]\n" +
                        "        \":\" [#/1/Untitled/cell/A4/parse-pattern/time/save/hhmm:ss]\n" +
                        "        \"mm\" [#/1/Untitled/cell/A4/parse-pattern/time/save/hh::ss]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:m:ss]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:ss]\n" +
                        "        \":\" [#/1/Untitled/cell/A4/parse-pattern/time/save/hh:mmss]\n" +
                        "        \"ss\" [#/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:]\n" +
                        "            pattern-alternative-0-MenuItem \"s\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:s]\n" +
                        "            pattern-alternative-1-MenuItem \"ss\" [/1/Untitled/cell/A4/parse-pattern/time/save/hh:mm:ss]\n"
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
                        "        \"hh\" [#/1/Untitled/metadata/time-parse-pattern/save/:mm:ss]\n" +
                        "            pattern-alternative-0-MenuItem \"h\" [/1/Untitled/metadata/time-parse-pattern/save/h:mm:ss]\n" +
                        "            pattern-alternative-1-MenuItem \"hh\" [/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss]\n" +
                        "        \":\" [#/1/Untitled/metadata/time-parse-pattern/save/hhmm:ss]\n" +
                        "        \"mm\" [#/1/Untitled/metadata/time-parse-pattern/save/hh::ss]\n" +
                        "            pattern-alternative-0-MenuItem \"m\" [/1/Untitled/metadata/time-parse-pattern/save/hh:m:ss]\n" +
                        "            pattern-alternative-1-MenuItem \"mm\" [/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss]\n" +
                        "        \":\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mmss]\n" +
                        "        \"ss\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:]\n" +
                        "            pattern-alternative-0-MenuItem \"s\" [/1/Untitled/metadata/time-parse-pattern/save/hh:mm:s]\n" +
                        "            pattern-alternative-1-MenuItem \"ss\" [/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss]\n"
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
                    public HistoryToken historyToken() {
                        return HistoryToken.parse(
                                UrlFragment.parse(historyToken)
                        );
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
