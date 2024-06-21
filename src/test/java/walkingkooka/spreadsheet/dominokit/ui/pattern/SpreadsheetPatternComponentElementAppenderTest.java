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
import java.util.function.Consumer;
import java.util.function.Function;

public final class SpreadsheetPatternComponentElementAppenderTest implements ClassTesting<SpreadsheetPatternComponentElementAppender>,
        TreePrintableTesting {

    private final static Consumer<String> RECREATE_NULL_CONSUMER = (s) -> {
    };

    // date format....................................................................................................

    @Test
    public void testCellDateFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/date", // historyToken
                "dd/mm/yyyy",
                SpreadsheetPattern::parseDateFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"dd\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyydd] id=pattern-append-0-Link\n" +
                        "          \"d\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyyd] id=pattern-append-1-Link\n" +
                        "          \"ddd\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyyddd] id=pattern-append-2-Link\n" +
                        "          \"dddd\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyydddd] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyymm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyym] id=pattern-append-5-Link\n" +
                        "          \"mmm\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyymmm] id=pattern-append-6-Link\n" +
                        "          \"mmmm\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyymmmm] id=pattern-append-7-Link\n" +
                        "          \"mmmmm\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyymmmmm] id=pattern-append-8-Link\n" +
                        "          \"yy\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yy] id=pattern-append-9-Link\n" +
                        "          \"yyyy\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy] id=pattern-append-10-Link\n" +
                        "          \";\" [#/1/Untitled/cell/A1/formatter/date/save/date-format-pattern%20dd/mm/yyyy;] id=pattern-append-11-Link\n"
        );
    }

    @Test
    public void testMetadataDateFormatter() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/date-formatter", // historyToken
                "dd/mm/yyyy",
                SpreadsheetPattern::parseDateFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"dd\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyydd] id=pattern-append-0-Link\n" +
                        "          \"d\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyyd] id=pattern-append-1-Link\n" +
                        "          \"ddd\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyyddd] id=pattern-append-2-Link\n" +
                        "          \"dddd\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyydddd] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyymm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyym] id=pattern-append-5-Link\n" +
                        "          \"mmm\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyymmm] id=pattern-append-6-Link\n" +
                        "          \"mmmm\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyymmmm] id=pattern-append-7-Link\n" +
                        "          \"mmmmm\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyymmmmm] id=pattern-append-8-Link\n" +
                        "          \"yy\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yy] id=pattern-append-9-Link\n" +
                        "          \"yyyy\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy] id=pattern-append-10-Link\n" +
                        "          \";\" [#/1/Untitled/metadata/date-formatter/save/date-format-pattern%20dd/mm/yyyy;] id=pattern-append-11-Link\n"
        );
    }

    // date parse....................................................................................................

    @Test
    public void testCellDateParser() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/parser/date", // historyToken
                "dd/mm/yyyy",
                SpreadsheetPattern::parseDateParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"dd\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyydd] id=pattern-append-0-Link\n" +
                        "          \"d\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyyd] id=pattern-append-1-Link\n" +
                        "          \"ddd\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyyddd] id=pattern-append-2-Link\n" +
                        "          \"dddd\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyydddd] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyymm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyym] id=pattern-append-5-Link\n" +
                        "          \"mmm\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyymmm] id=pattern-append-6-Link\n" +
                        "          \"mmmm\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyymmmm] id=pattern-append-7-Link\n" +
                        "          \"mmmmm\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyymmmmm] id=pattern-append-8-Link\n" +
                        "          \"yy\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yy] id=pattern-append-9-Link\n" +
                        "          \"yyyy\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-append-10-Link\n" +
                        "          \";\" [#/1/Untitled/cell/A1/parser/date/save/date-parse-pattern%20dd/mm/yyyy;] id=pattern-append-11-Link\n"
        );
    }

    @Test
    public void testMetadataDateParser() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/date-parser", // historyToken
                "dd/mm/yyyy",
                SpreadsheetPattern::parseDateParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"dd\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyydd] id=pattern-append-0-Link\n" +
                        "          \"d\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyyd] id=pattern-append-1-Link\n" +
                        "          \"ddd\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyyddd] id=pattern-append-2-Link\n" +
                        "          \"dddd\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyydddd] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyymm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyym] id=pattern-append-5-Link\n" +
                        "          \"mmm\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyymmm] id=pattern-append-6-Link\n" +
                        "          \"mmmm\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyymmmm] id=pattern-append-7-Link\n" +
                        "          \"mmmmm\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyymmmmm] id=pattern-append-8-Link\n" +
                        "          \"yy\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yy] id=pattern-append-9-Link\n" +
                        "          \"yyyy\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyy] id=pattern-append-10-Link\n" +
                        "          \";\" [#/1/Untitled/metadata/date-parser/save/date-parse-pattern%20dd/mm/yyyy;] id=pattern-append-11-Link\n"
        );
    }

    // dateTime format....................................................................................................

    @Test
    public void testCellDateTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/date-time", // historyToken
                "dd/mm/yyyy hh:mm:ss",
                SpreadsheetPattern::parseDateTimeFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"dd\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssdd] id=pattern-append-0-Link\n" +
                        "          \"d\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssd] id=pattern-append-1-Link\n" +
                        "          \"ddd\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssddd] id=pattern-append-2-Link\n" +
                        "          \"dddd\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssdddd] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "          \"mmm\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmmm] id=pattern-append-6-Link\n" +
                        "          \"mmmm\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmmmm] id=pattern-append-7-Link\n" +
                        "          \"mmmmm\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=pattern-append-8-Link\n" +
                        "          \"yy\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssyy] id=pattern-append-9-Link\n" +
                        "          \"yyyy\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssyyyy] id=pattern-append-10-Link\n" +
                        "          \"0\" DISABLED id=pattern-append-11-Link\n" +
                        "          \".\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss.] id=pattern-append-12-Link\n" +
                        "          \"hh\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:sshh] id=pattern-append-13-Link\n" +
                        "          \"h\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssh] id=pattern-append-14-Link\n" +
                        "          \"mm\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=pattern-append-15-Link\n" +
                        "          \"m\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=pattern-append-16-Link\n" +
                        "          \"ss\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-append-17-Link\n" +
                        "          \"s\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:s] id=pattern-append-18-Link\n" +
                        "          \"am/pm\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssam/pm] id=pattern-append-19-Link\n" +
                        "          \"AM/PM\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=pattern-append-20-Link\n" +
                        "          \"a/p\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssa/p] id=pattern-append-21-Link\n" +
                        "          \"A/P\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssA/P] id=pattern-append-22-Link\n" +
                        "          \";\" [#/1/Untitled/cell/A1/formatter/date-time/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss;] id=pattern-append-23-Link\n"
        );
    }

    @Test
    public void testMetadataDateTimeFormatter() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/date-time-formatter", // historyToken
                "dd/mm/yyyy hh:mm:ss",
                SpreadsheetPattern::parseDateTimeFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"dd\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssdd] id=pattern-append-0-Link\n" +
                        "          \"d\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssd] id=pattern-append-1-Link\n" +
                        "          \"ddd\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssddd] id=pattern-append-2-Link\n" +
                        "          \"dddd\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssdddd] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "          \"mmm\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmmm] id=pattern-append-6-Link\n" +
                        "          \"mmmm\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmmmm] id=pattern-append-7-Link\n" +
                        "          \"mmmmm\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=pattern-append-8-Link\n" +
                        "          \"yy\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssyy] id=pattern-append-9-Link\n" +
                        "          \"yyyy\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssyyyy] id=pattern-append-10-Link\n" +
                        "          \"0\" DISABLED id=pattern-append-11-Link\n" +
                        "          \".\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss.] id=pattern-append-12-Link\n" +
                        "          \"hh\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:sshh] id=pattern-append-13-Link\n" +
                        "          \"h\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssh] id=pattern-append-14-Link\n" +
                        "          \"mm\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=pattern-append-15-Link\n" +
                        "          \"m\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=pattern-append-16-Link\n" +
                        "          \"ss\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-append-17-Link\n" +
                        "          \"s\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:s] id=pattern-append-18-Link\n" +
                        "          \"am/pm\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssam/pm] id=pattern-append-19-Link\n" +
                        "          \"AM/PM\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=pattern-append-20-Link\n" +
                        "          \"a/p\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssa/p] id=pattern-append-21-Link\n" +
                        "          \"A/P\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ssA/P] id=pattern-append-22-Link\n" +
                        "          \";\" [#/1/Untitled/metadata/date-time-formatter/save/date-time-format-pattern%20dd/mm/yyyy%20hh:mm:ss;] id=pattern-append-23-Link\n"
        );
    }

    // dateTime parser..................................................................................................

    @Test
    public void testCellDateTimeParser() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/parser/date-time", // historyToken
                "dd/mm/yyyy hh:mm:ss",
                SpreadsheetPattern::parseDateTimeParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"dd\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdd] id=pattern-append-0-Link\n" +
                        "          \"d\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssd] id=pattern-append-1-Link\n" +
                        "          \"ddd\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssddd] id=pattern-append-2-Link\n" +
                        "          \"dddd\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdddd] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "          \"mmm\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmm] id=pattern-append-6-Link\n" +
                        "          \"mmmm\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmm] id=pattern-append-7-Link\n" +
                        "          \"mmmmm\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=pattern-append-8-Link\n" +
                        "          \"yy\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyy] id=pattern-append-9-Link\n" +
                        "          \"yyyy\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyyyy] id=pattern-append-10-Link\n" +
                        "          \"0\" DISABLED id=pattern-append-11-Link\n" +
                        "          \".\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss.] id=pattern-append-12-Link\n" +
                        "          \"hh\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:sshh] id=pattern-append-13-Link\n" +
                        "          \"h\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssh] id=pattern-append-14-Link\n" +
                        "          \"mm\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=pattern-append-15-Link\n" +
                        "          \"m\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=pattern-append-16-Link\n" +
                        "          \"ss\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-append-17-Link\n" +
                        "          \"s\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:s] id=pattern-append-18-Link\n" +
                        "          \"am/pm\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssam/pm] id=pattern-append-19-Link\n" +
                        "          \"AM/PM\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=pattern-append-20-Link\n" +
                        "          \"a/p\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssa/p] id=pattern-append-21-Link\n" +
                        "          \"A/P\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssA/P] id=pattern-append-22-Link\n" +
                        "          \";\" [#/1/Untitled/cell/A1/parser/date-time/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss;] id=pattern-append-23-Link\n"
        );
    }

    @Test
    public void testMetadataDateTimeParser() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/date-time-parser", // historyToken
                "dd/mm/yyyy hh:mm:ss",
                SpreadsheetPattern::parseDateTimeParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"dd\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdd] id=pattern-append-0-Link\n" +
                        "          \"d\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssd] id=pattern-append-1-Link\n" +
                        "          \"ddd\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssddd] id=pattern-append-2-Link\n" +
                        "          \"dddd\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssdddd] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "          \"mmm\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmm] id=pattern-append-6-Link\n" +
                        "          \"mmmm\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmm] id=pattern-append-7-Link\n" +
                        "          \"mmmmm\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmmmmm] id=pattern-append-8-Link\n" +
                        "          \"yy\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyy] id=pattern-append-9-Link\n" +
                        "          \"yyyy\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssyyyy] id=pattern-append-10-Link\n" +
                        "          \"0\" DISABLED id=pattern-append-11-Link\n" +
                        "          \".\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss.] id=pattern-append-12-Link\n" +
                        "          \"hh\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:sshh] id=pattern-append-13-Link\n" +
                        "          \"h\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssh] id=pattern-append-14-Link\n" +
                        "          \"mm\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssmm] id=pattern-append-15-Link\n" +
                        "          \"m\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssm] id=pattern-append-16-Link\n" +
                        "          \"ss\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss] id=pattern-append-17-Link\n" +
                        "          \"s\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:s] id=pattern-append-18-Link\n" +
                        "          \"am/pm\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssam/pm] id=pattern-append-19-Link\n" +
                        "          \"AM/PM\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssAM/PM] id=pattern-append-20-Link\n" +
                        "          \"a/p\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssa/p] id=pattern-append-21-Link\n" +
                        "          \"A/P\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ssA/P] id=pattern-append-22-Link\n" +
                        "          \";\" [#/1/Untitled/metadata/date-time-parser/save/date-time-parse-pattern%20dd/mm/yyyy%20hh:mm:ss;] id=pattern-append-23-Link\n"
        );
    }
    
    // number format....................................................................................................

    @Test
    public void testCellNumberFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/number", // historyToken
                "$#.00",
                SpreadsheetPattern::parseNumberFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"$\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00$] id=pattern-append-0-Link\n" +
                        "          \"#\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00%23] id=pattern-append-1-Link\n" +
                        "          \"?\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00?] id=pattern-append-2-Link\n" +
                        "          \"0\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00] id=pattern-append-3-Link\n" +
                        "          \",\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00,] id=pattern-append-4-Link\n" +
                        "          \".\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00.] id=pattern-append-5-Link\n" +
                        "          \"E\" DISABLED id=pattern-append-6-Link\n" +
                        "          \"/\" DISABLED id=pattern-append-7-Link\n" +
                        "          \"%\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00%25] id=pattern-append-8-Link\n" +
                        "          \";\" [#/1/Untitled/cell/A1/formatter/number/save/number-format-pattern%20$%23.00;] id=pattern-append-9-Link\n"
        );
    }

    @Test
    public void testMetadataNumberFormatter() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/number-formatter", // historyToken
                "$#.00",
                SpreadsheetPattern::parseNumberFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"$\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00$] id=pattern-append-0-Link\n" +
                        "          \"#\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00%23] id=pattern-append-1-Link\n" +
                        "          \"?\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00?] id=pattern-append-2-Link\n" +
                        "          \"0\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00] id=pattern-append-3-Link\n" +
                        "          \",\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00,] id=pattern-append-4-Link\n" +
                        "          \".\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00.] id=pattern-append-5-Link\n" +
                        "          \"E\" DISABLED id=pattern-append-6-Link\n" +
                        "          \"/\" DISABLED id=pattern-append-7-Link\n" +
                        "          \"%\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00%25] id=pattern-append-8-Link\n" +
                        "          \";\" [#/1/Untitled/metadata/number-formatter/save/number-format-pattern%20$%23.00;] id=pattern-append-9-Link\n"
        );
    }

    // number parse....................................................................................................

    @Test
    public void testCellNumberParse() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/parser/number", // historyToken
                "$#.00",
                SpreadsheetPattern::parseNumberParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"$\" [#/1/Untitled/cell/A1/parser/number/save/number-parse-pattern%20$%23.00$] id=pattern-append-0-Link\n" +
                        "          \"#\" [#/1/Untitled/cell/A1/parser/number/save/number-parse-pattern%20$%23.00%23] id=pattern-append-1-Link\n" +
                        "          \"?\" [#/1/Untitled/cell/A1/parser/number/save/number-parse-pattern%20$%23.00?] id=pattern-append-2-Link\n" +
                        "          \"0\" [#/1/Untitled/cell/A1/parser/number/save/number-parse-pattern%20$%23.00] id=pattern-append-3-Link\n" +
                        "          \",\" [#/1/Untitled/cell/A1/parser/number/save/number-parse-pattern%20$%23.00,] id=pattern-append-4-Link\n" +
                        "          \".\" [#/1/Untitled/cell/A1/parser/number/save/number-parse-pattern%20$%23.00.] id=pattern-append-5-Link\n" +
                        "          \"E\" DISABLED id=pattern-append-6-Link\n" +
                        "          \"/\" DISABLED id=pattern-append-7-Link\n" +
                        "          \"%\" [#/1/Untitled/cell/A1/parser/number/save/number-parse-pattern%20$%23.00%25] id=pattern-append-8-Link\n" +
                        "          \";\" [#/1/Untitled/cell/A1/parser/number/save/number-parse-pattern%20$%23.00;] id=pattern-append-9-Link\n"
        );
    }

    @Test
    public void testMetadataNumberParse() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/number-parser", // historyToken
                "$#.00",
                SpreadsheetPattern::parseNumberParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"$\" [#/1/Untitled/metadata/number-parser/save/number-parse-pattern%20$%23.00$] id=pattern-append-0-Link\n" +
                        "          \"#\" [#/1/Untitled/metadata/number-parser/save/number-parse-pattern%20$%23.00%23] id=pattern-append-1-Link\n" +
                        "          \"?\" [#/1/Untitled/metadata/number-parser/save/number-parse-pattern%20$%23.00?] id=pattern-append-2-Link\n" +
                        "          \"0\" [#/1/Untitled/metadata/number-parser/save/number-parse-pattern%20$%23.00] id=pattern-append-3-Link\n" +
                        "          \",\" [#/1/Untitled/metadata/number-parser/save/number-parse-pattern%20$%23.00,] id=pattern-append-4-Link\n" +
                        "          \".\" [#/1/Untitled/metadata/number-parser/save/number-parse-pattern%20$%23.00.] id=pattern-append-5-Link\n" +
                        "          \"E\" DISABLED id=pattern-append-6-Link\n" +
                        "          \"/\" DISABLED id=pattern-append-7-Link\n" +
                        "          \"%\" [#/1/Untitled/metadata/number-parser/save/number-parse-pattern%20$%23.00%25] id=pattern-append-8-Link\n" +
                        "          \";\" [#/1/Untitled/metadata/number-parser/save/number-parse-pattern%20$%23.00;] id=pattern-append-9-Link\n"
        );
    }

    // text format......................................................................................................

    @Test
    public void testCellTextFormatAtSign() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/text", // historyToken
                "@",
                SpreadsheetPattern::parseTextFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"@\" [#/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20@] id=pattern-append-0-Link\n" +
                        "          \"* \" [#/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20@*%20] id=pattern-append-1-Link\n" +
                        "          \"_ \" [#/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20@_%20] id=pattern-append-2-Link\n"
        );
    }

    @Test
    public void testCellTextFormatIncludesLiteral() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/text", // historyToken
                "\"Hello\" @",
                SpreadsheetPattern::parseTextFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"@\" [#/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20%22Hello%22%20@] id=pattern-append-0-Link\n" +
                        "          \"* \" [#/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20%22Hello%22%20@*%20] id=pattern-append-1-Link\n" +
                        "          \"_ \" [#/1/Untitled/cell/A1/formatter/text/save/text-format-pattern%20%22Hello%22%20@_%20] id=pattern-append-2-Link\n" // expected
        );
    }

    @Test
    public void testMetadataTextFormatterAtSign() {
        //http://localhost:12345/index.html#/1/Untitled/metadata/date-formatter
        this.refreshAndCheck(
                "/1/Untitled/metadata/text-formatter", // historyToken
                "@",
                SpreadsheetPattern::parseTextFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"@\" [#/1/Untitled/metadata/text-formatter/save/text-format-pattern%20@] id=pattern-append-0-Link\n" +
                        "          \"* \" [#/1/Untitled/metadata/text-formatter/save/text-format-pattern%20@*%20] id=pattern-append-1-Link\n" +
                        "          \"_ \" [#/1/Untitled/metadata/text-formatter/save/text-format-pattern%20@_%20] id=pattern-append-2-Link\n"
        );
    }

    // time format......................................................................................................

    @Test
    public void testCellTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/formatter/time", // historyToken
                "hh:mm:ss",
                SpreadsheetPattern::parseTimeFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"0\" DISABLED id=pattern-append-0-Link\n" +
                        "          \".\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ss.] id=pattern-append-1-Link\n" +
                        "          \"hh\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:sshh] id=pattern-append-2-Link\n" +
                        "          \"h\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ssh] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "          \"ss\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ss] id=pattern-append-6-Link\n" +
                        "          \"s\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:s] id=pattern-append-7-Link\n" +
                        "          \"am/pm\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ssam/pm] id=pattern-append-8-Link\n" +
                        "          \"AM/PM\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ssAM/PM] id=pattern-append-9-Link\n" +
                        "          \"a/p\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ssa/p] id=pattern-append-10-Link\n" +
                        "          \"A/P\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ssA/P] id=pattern-append-11-Link\n" +
                        "          \";\" [#/1/Untitled/cell/A1/formatter/time/save/time-format-pattern%20hh:mm:ss;] id=pattern-append-12-Link\n"
        );
    }

    @Test
    public void testMetadataTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/time-formatter", // historyToken
                "hh:mm:ss",
                SpreadsheetPattern::parseTimeFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"0\" DISABLED id=pattern-append-0-Link\n" +
                        "          \".\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ss.] id=pattern-append-1-Link\n" +
                        "          \"hh\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:sshh] id=pattern-append-2-Link\n" +
                        "          \"h\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ssh] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "          \"ss\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ss] id=pattern-append-6-Link\n" +
                        "          \"s\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:s] id=pattern-append-7-Link\n" +
                        "          \"am/pm\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ssam/pm] id=pattern-append-8-Link\n" +
                        "          \"AM/PM\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ssAM/PM] id=pattern-append-9-Link\n" +
                        "          \"a/p\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ssa/p] id=pattern-append-10-Link\n" +
                        "          \"A/P\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ssA/P] id=pattern-append-11-Link\n" +
                        "          \";\" [#/1/Untitled/metadata/time-formatter/save/time-format-pattern%20hh:mm:ss;] id=pattern-append-12-Link\n"
        );
    }

    // time parse......................................................................................................

    @Test
    public void testCellTimeParse() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/parser/time", // historyToken
                "hh:mm:ss",
                SpreadsheetPattern::parseTimeParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"0\" DISABLED id=pattern-append-0-Link\n" +
                        "          \".\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ss.] id=pattern-append-1-Link\n" +
                        "          \"hh\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:sshh] id=pattern-append-2-Link\n" +
                        "          \"h\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ssh] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "          \"ss\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ss] id=pattern-append-6-Link\n" +
                        "          \"s\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:s] id=pattern-append-7-Link\n" +
                        "          \"am/pm\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ssam/pm] id=pattern-append-8-Link\n" +
                        "          \"AM/PM\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ssAM/PM] id=pattern-append-9-Link\n" +
                        "          \"a/p\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ssa/p] id=pattern-append-10-Link\n" +
                        "          \"A/P\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ssA/P] id=pattern-append-11-Link\n" +
                        "          \";\" [#/1/Untitled/cell/A1/parser/time/save/time-parse-pattern%20hh:mm:ss;] id=pattern-append-12-Link\n"
        );
    }

    @Test
    public void testMetadataTimeParse() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/time-parser", // historyToken
                "hh:mm:ss",
                SpreadsheetPattern::parseTimeParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      SpreadsheetFlexLayout\n" +
                        "        ROW\n" +
                        "          \"0\" DISABLED id=pattern-append-0-Link\n" +
                        "          \".\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ss.] id=pattern-append-1-Link\n" +
                        "          \"hh\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:sshh] id=pattern-append-2-Link\n" +
                        "          \"h\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ssh] id=pattern-append-3-Link\n" +
                        "          \"mm\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "          \"m\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "          \"ss\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ss] id=pattern-append-6-Link\n" +
                        "          \"s\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:s] id=pattern-append-7-Link\n" +
                        "          \"am/pm\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ssam/pm] id=pattern-append-8-Link\n" +
                        "          \"AM/PM\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ssAM/PM] id=pattern-append-9-Link\n" +
                        "          \"a/p\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ssa/p] id=pattern-append-10-Link\n" +
                        "          \"A/P\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ssA/P] id=pattern-append-11-Link\n" +
                        "          \";\" [#/1/Untitled/metadata/time-parser/save/time-parse-pattern%20hh:mm:ss;] id=pattern-append-12-Link\n"
        );
    }

    // helpers..........................................................................................................

    private void refreshAndCheck(final String historyToken,
                                 final String patternText,
                                 final Function<String, SpreadsheetPattern> patternParser,
                                 final String expected) {
        final SpreadsheetPatternComponentElementAppender component = SpreadsheetPatternComponentElementAppender.empty();
        final SpreadsheetPatternDialogComponentContext context = this.context(historyToken);

        component.refresh(
                RECREATE_NULL_CONSUMER,
                context
        );

        component.refreshLinks(
                patternText, // pattern
                patternParser.apply(patternText),
                context
        );

        this.treePrintAndCheck(
                component,
                expected
        );
    }

    private SpreadsheetPatternDialogComponentContext context(final String token) {
        return new FakeSpreadsheetPatternDialogComponentContext() {
            @Override
            public HistoryToken historyToken() {
                return HistoryToken.parseString(token);
            }

            @Override
            public SpreadsheetPatternKind patternKind() {
                return this.historyToken()
                        .patternKind()
                        .get();
            }

            @Override
            public void debug(final Object... values) {
                System.out.println(Arrays.toString(values));
            }
        };
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetPatternComponentElementAppender> type() {
        return SpreadsheetPatternComponentElementAppender.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
