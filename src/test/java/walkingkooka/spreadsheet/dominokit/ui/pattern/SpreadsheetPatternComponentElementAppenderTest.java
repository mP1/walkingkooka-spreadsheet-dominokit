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
                "/1/Untitled/cell/A1/format-pattern/date", // historyToken
                "dd/mm/yyyy",
                SpreadsheetPattern::parseDateFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"dd\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyydd] id=pattern-append-0-Link\n" +
                        "      \"d\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyyd] id=pattern-append-1-Link\n" +
                        "      \"ddd\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyyddd] id=pattern-append-2-Link\n" +
                        "      \"dddd\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyydddd] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyymm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyym] id=pattern-append-5-Link\n" +
                        "      \"mmm\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyymmm] id=pattern-append-6-Link\n" +
                        "      \"mmmm\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyymmmm] id=pattern-append-7-Link\n" +
                        "      \"mmmmm\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyymmmmm] id=pattern-append-8-Link\n" +
                        "      \"yy\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yy] id=pattern-append-9-Link\n" +
                        "      \"yyyy\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyy] id=pattern-append-10-Link\n" +
                        "      \";\" [#/1/Untitled/cell/A1/format-pattern/date/save/dd/mm/yyyy%3B] id=pattern-append-11-Link\n"
        );
    }

    @Test
    public void testMetadataDateFormat() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/date-format-pattern", // historyToken
                "dd/mm/yyyy",
                SpreadsheetPattern::parseDateFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"dd\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyydd] id=pattern-append-0-Link\n" +
                        "      \"d\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyyd] id=pattern-append-1-Link\n" +
                        "      \"ddd\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyyddd] id=pattern-append-2-Link\n" +
                        "      \"dddd\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyydddd] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyymm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyym] id=pattern-append-5-Link\n" +
                        "      \"mmm\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyymmm] id=pattern-append-6-Link\n" +
                        "      \"mmmm\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyymmmm] id=pattern-append-7-Link\n" +
                        "      \"mmmmm\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyymmmmm] id=pattern-append-8-Link\n" +
                        "      \"yy\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yy] id=pattern-append-9-Link\n" +
                        "      \"yyyy\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyy] id=pattern-append-10-Link\n" +
                        "      \";\" [#/1/Untitled/metadata/date-format-pattern/save/dd/mm/yyyy%3B] id=pattern-append-11-Link\n"
        );
    }

    // date parse....................................................................................................

    @Test
    public void testCellDateParse() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/parse-pattern/date", // historyToken
                "dd/mm/yyyy",
                SpreadsheetPattern::parseDateParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"dd\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyydd] id=pattern-append-0-Link\n" +
                        "      \"d\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyyd] id=pattern-append-1-Link\n" +
                        "      \"ddd\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyyddd] id=pattern-append-2-Link\n" +
                        "      \"dddd\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyydddd] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyymm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyym] id=pattern-append-5-Link\n" +
                        "      \"mmm\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyymmm] id=pattern-append-6-Link\n" +
                        "      \"mmmm\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyymmmm] id=pattern-append-7-Link\n" +
                        "      \"mmmmm\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyymmmmm] id=pattern-append-8-Link\n" +
                        "      \"yy\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yy] id=pattern-append-9-Link\n" +
                        "      \"yyyy\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyy] id=pattern-append-10-Link\n" +
                        "      \";\" [#/1/Untitled/cell/A1/parse-pattern/date/save/dd/mm/yyyy%3B] id=pattern-append-11-Link\n"
        );
    }

    @Test
    public void testMetadataDateParse() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/date-parse-pattern", // historyToken
                "dd/mm/yyyy",
                SpreadsheetPattern::parseDateParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"dd\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyydd] id=pattern-append-0-Link\n" +
                        "      \"d\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyyd] id=pattern-append-1-Link\n" +
                        "      \"ddd\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyyddd] id=pattern-append-2-Link\n" +
                        "      \"dddd\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyydddd] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyymm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyym] id=pattern-append-5-Link\n" +
                        "      \"mmm\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyymmm] id=pattern-append-6-Link\n" +
                        "      \"mmmm\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyymmmm] id=pattern-append-7-Link\n" +
                        "      \"mmmmm\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyymmmmm] id=pattern-append-8-Link\n" +
                        "      \"yy\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yy] id=pattern-append-9-Link\n" +
                        "      \"yyyy\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyy] id=pattern-append-10-Link\n" +
                        "      \";\" [#/1/Untitled/metadata/date-parse-pattern/save/dd/mm/yyyy%3B] id=pattern-append-11-Link\n"
        );
    }

    // dateTime format....................................................................................................

    @Test
    public void testCellDateTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/format-pattern/date-time", // historyToken
                "dd/mm/yyyy hh:mm:ss",
                SpreadsheetPattern::parseDateTimeFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"dd\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssdd] id=pattern-append-0-Link\n" +
                        "      \"d\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssd] id=pattern-append-1-Link\n" +
                        "      \"ddd\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssddd] id=pattern-append-2-Link\n" +
                        "      \"dddd\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssdddd] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "      \"mmm\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmmm] id=pattern-append-6-Link\n" +
                        "      \"mmmm\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmmmm] id=pattern-append-7-Link\n" +
                        "      \"mmmmm\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmmmmm] id=pattern-append-8-Link\n" +
                        "      \"yy\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssyy] id=pattern-append-9-Link\n" +
                        "      \"yyyy\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssyyyy] id=pattern-append-10-Link\n" +
                        "      \"0\" DISABLED id=pattern-append-11-Link\n" +
                        "      \".\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss.] id=pattern-append-12-Link\n" +
                        "      \"hh\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:sshh] id=pattern-append-13-Link\n" +
                        "      \"h\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssh] id=pattern-append-14-Link\n" +
                        "      \"mm\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmm] id=pattern-append-15-Link\n" +
                        "      \"m\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssm] id=pattern-append-16-Link\n" +
                        "      \"ss\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss] id=pattern-append-17-Link\n" +
                        "      \"s\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:s] id=pattern-append-18-Link\n" +
                        "      \"am/pm\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssam/pm] id=pattern-append-19-Link\n" +
                        "      \"AM/PM\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssAM/PM] id=pattern-append-20-Link\n" +
                        "      \"a/p\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssa/p] id=pattern-append-21-Link\n" +
                        "      \"A/P\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssA/P] id=pattern-append-22-Link\n" +
                        "      \";\" [#/1/Untitled/cell/A1/format-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss%3B] id=pattern-append-23-Link\n"
        );
    }

    @Test
    public void testMetadataDateTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/date-time-format-pattern", // historyToken
                "dd/mm/yyyy hh:mm:ss",
                SpreadsheetPattern::parseDateTimeFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"dd\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssdd] id=pattern-append-0-Link\n" +
                        "      \"d\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssd] id=pattern-append-1-Link\n" +
                        "      \"ddd\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssddd] id=pattern-append-2-Link\n" +
                        "      \"dddd\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssdddd] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "      \"mmm\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssmmm] id=pattern-append-6-Link\n" +
                        "      \"mmmm\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssmmmm] id=pattern-append-7-Link\n" +
                        "      \"mmmmm\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssmmmmm] id=pattern-append-8-Link\n" +
                        "      \"yy\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssyy] id=pattern-append-9-Link\n" +
                        "      \"yyyy\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssyyyy] id=pattern-append-10-Link\n" +
                        "      \"0\" DISABLED id=pattern-append-11-Link\n" +
                        "      \".\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss.] id=pattern-append-12-Link\n" +
                        "      \"hh\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:sshh] id=pattern-append-13-Link\n" +
                        "      \"h\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssh] id=pattern-append-14-Link\n" +
                        "      \"mm\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssmm] id=pattern-append-15-Link\n" +
                        "      \"m\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssm] id=pattern-append-16-Link\n" +
                        "      \"ss\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss] id=pattern-append-17-Link\n" +
                        "      \"s\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:s] id=pattern-append-18-Link\n" +
                        "      \"am/pm\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssam/pm] id=pattern-append-19-Link\n" +
                        "      \"AM/PM\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssAM/PM] id=pattern-append-20-Link\n" +
                        "      \"a/p\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssa/p] id=pattern-append-21-Link\n" +
                        "      \"A/P\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ssA/P] id=pattern-append-22-Link\n" +
                        "      \";\" [#/1/Untitled/metadata/date-time-format-pattern/save/dd/mm/yyyy+hh:mm:ss%3B] id=pattern-append-23-Link\n"
        );
    }

    // dateTime parse....................................................................................................

    @Test
    public void testCellDateTimeParse() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/parse-pattern/date-time", // historyToken
                "dd/mm/yyyy hh:mm:ss",
                SpreadsheetPattern::parseDateTimeParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"dd\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssdd] id=pattern-append-0-Link\n" +
                        "      \"d\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssd] id=pattern-append-1-Link\n" +
                        "      \"ddd\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssddd] id=pattern-append-2-Link\n" +
                        "      \"dddd\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssdddd] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "      \"mmm\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmmm] id=pattern-append-6-Link\n" +
                        "      \"mmmm\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmmmm] id=pattern-append-7-Link\n" +
                        "      \"mmmmm\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmmmmm] id=pattern-append-8-Link\n" +
                        "      \"yy\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssyy] id=pattern-append-9-Link\n" +
                        "      \"yyyy\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssyyyy] id=pattern-append-10-Link\n" +
                        "      \"0\" DISABLED id=pattern-append-11-Link\n" +
                        "      \".\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss.] id=pattern-append-12-Link\n" +
                        "      \"hh\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:sshh] id=pattern-append-13-Link\n" +
                        "      \"h\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssh] id=pattern-append-14-Link\n" +
                        "      \"mm\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssmm] id=pattern-append-15-Link\n" +
                        "      \"m\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssm] id=pattern-append-16-Link\n" +
                        "      \"ss\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss] id=pattern-append-17-Link\n" +
                        "      \"s\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:s] id=pattern-append-18-Link\n" +
                        "      \"am/pm\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssam/pm] id=pattern-append-19-Link\n" +
                        "      \"AM/PM\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssAM/PM] id=pattern-append-20-Link\n" +
                        "      \"a/p\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssa/p] id=pattern-append-21-Link\n" +
                        "      \"A/P\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ssA/P] id=pattern-append-22-Link\n" +
                        "      \";\" [#/1/Untitled/cell/A1/parse-pattern/date-time/save/dd/mm/yyyy+hh:mm:ss%3B] id=pattern-append-23-Link\n"
        );
    }

    @Test
    public void testMetadataDateTimeParse() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/date-time-parse-pattern", // historyToken
                "dd/mm/yyyy hh:mm:ss",
                SpreadsheetPattern::parseDateTimeParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"dd\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssdd] id=pattern-append-0-Link\n" +
                        "      \"d\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssd] id=pattern-append-1-Link\n" +
                        "      \"ddd\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssddd] id=pattern-append-2-Link\n" +
                        "      \"dddd\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssdddd] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "      \"mmm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssmmm] id=pattern-append-6-Link\n" +
                        "      \"mmmm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssmmmm] id=pattern-append-7-Link\n" +
                        "      \"mmmmm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssmmmmm] id=pattern-append-8-Link\n" +
                        "      \"yy\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssyy] id=pattern-append-9-Link\n" +
                        "      \"yyyy\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssyyyy] id=pattern-append-10-Link\n" +
                        "      \"0\" DISABLED id=pattern-append-11-Link\n" +
                        "      \".\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss.] id=pattern-append-12-Link\n" +
                        "      \"hh\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:sshh] id=pattern-append-13-Link\n" +
                        "      \"h\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssh] id=pattern-append-14-Link\n" +
                        "      \"mm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssmm] id=pattern-append-15-Link\n" +
                        "      \"m\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssm] id=pattern-append-16-Link\n" +
                        "      \"ss\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss] id=pattern-append-17-Link\n" +
                        "      \"s\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:s] id=pattern-append-18-Link\n" +
                        "      \"am/pm\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssam/pm] id=pattern-append-19-Link\n" +
                        "      \"AM/PM\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssAM/PM] id=pattern-append-20-Link\n" +
                        "      \"a/p\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssa/p] id=pattern-append-21-Link\n" +
                        "      \"A/P\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ssA/P] id=pattern-append-22-Link\n" +
                        "      \";\" [#/1/Untitled/metadata/date-time-parse-pattern/save/dd/mm/yyyy+hh:mm:ss%3B] id=pattern-append-23-Link\n"
        );
    }
    
    // number format....................................................................................................

    @Test
    public void testCellNumberFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/format-pattern/number", // historyToken
                "$#.00",
                SpreadsheetPattern::parseNumberFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"$\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00%24] id=pattern-append-0-Link\n" +
                        "      \"#\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00%23] id=pattern-append-1-Link\n" +
                        "      \"?\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00?] id=pattern-append-2-Link\n" +
                        "      \"0\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00] id=pattern-append-3-Link\n" +
                        "      \",\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00%2C] id=pattern-append-4-Link\n" +
                        "      \".\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00.] id=pattern-append-5-Link\n" +
                        "      \"E\" DISABLED id=pattern-append-6-Link\n" +
                        "      \"/\" DISABLED id=pattern-append-7-Link\n" +
                        "      \"%\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00%25] id=pattern-append-8-Link\n" +
                        "      \";\" [#/1/Untitled/cell/A1/format-pattern/number/save/%24%23.00%3B] id=pattern-append-9-Link\n"
        );
    }

    @Test
    public void testMetadataNumberFormat() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/number-format-pattern", // historyToken
                "$#.00",
                SpreadsheetPattern::parseNumberFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"$\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.00%24] id=pattern-append-0-Link\n" +
                        "      \"#\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.00%23] id=pattern-append-1-Link\n" +
                        "      \"?\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.00?] id=pattern-append-2-Link\n" +
                        "      \"0\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.00] id=pattern-append-3-Link\n" +
                        "      \",\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.00%2C] id=pattern-append-4-Link\n" +
                        "      \".\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.00.] id=pattern-append-5-Link\n" +
                        "      \"E\" DISABLED id=pattern-append-6-Link\n" +
                        "      \"/\" DISABLED id=pattern-append-7-Link\n" +
                        "      \"%\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.00%25] id=pattern-append-8-Link\n" +
                        "      \";\" [#/1/Untitled/metadata/number-format-pattern/save/%24%23.00%3B] id=pattern-append-9-Link\n"
        );
    }

    // number parse....................................................................................................

    @Test
    public void testCellNumberParse() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/parse-pattern/number", // historyToken
                "$#.00",
                SpreadsheetPattern::parseNumberParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"$\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00%24] id=pattern-append-0-Link\n" +
                        "      \"#\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00%23] id=pattern-append-1-Link\n" +
                        "      \"?\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00?] id=pattern-append-2-Link\n" +
                        "      \"0\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00] id=pattern-append-3-Link\n" +
                        "      \",\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00%2C] id=pattern-append-4-Link\n" +
                        "      \".\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00.] id=pattern-append-5-Link\n" +
                        "      \"E\" DISABLED id=pattern-append-6-Link\n" +
                        "      \"/\" DISABLED id=pattern-append-7-Link\n" +
                        "      \"%\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00%25] id=pattern-append-8-Link\n" +
                        "      \";\" [#/1/Untitled/cell/A1/parse-pattern/number/save/%24%23.00%3B] id=pattern-append-9-Link\n"
        );
    }

    @Test
    public void testMetadataNumberParse() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/number-parse-pattern", // historyToken
                "$#.00",
                SpreadsheetPattern::parseNumberParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"$\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.00%24] id=pattern-append-0-Link\n" +
                        "      \"#\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.00%23] id=pattern-append-1-Link\n" +
                        "      \"?\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.00?] id=pattern-append-2-Link\n" +
                        "      \"0\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.00] id=pattern-append-3-Link\n" +
                        "      \",\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.00%2C] id=pattern-append-4-Link\n" +
                        "      \".\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.00.] id=pattern-append-5-Link\n" +
                        "      \"E\" DISABLED id=pattern-append-6-Link\n" +
                        "      \"/\" DISABLED id=pattern-append-7-Link\n" +
                        "      \"%\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.00%25] id=pattern-append-8-Link\n" +
                        "      \";\" [#/1/Untitled/metadata/number-parse-pattern/save/%24%23.00%3B] id=pattern-append-9-Link\n"
        );
    }

    // text format......................................................................................................

    @Test
    public void testCellTextFormatAtSign() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/format-pattern/text", // historyToken
                "@",
                SpreadsheetPattern::parseTextFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"@\" [#/1/Untitled/cell/A1/format-pattern/text/save/@] id=pattern-append-0-Link\n" +
                        "      \"* \" [#/1/Untitled/cell/A1/format-pattern/text/save/@*+] id=pattern-append-1-Link\n" +
                        "      \"_ \" [#/1/Untitled/cell/A1/format-pattern/text/save/@_+] id=pattern-append-2-Link\n"
        );
    }

    @Test
    public void testCellTextFormatIncludesLiteral() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/format-pattern/text", // historyToken
                "\"Hello\" @",
                SpreadsheetPattern::parseTextFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"@\" [#/1/Untitled/cell/A1/format-pattern/text/save/%22Hello%22+@] id=pattern-append-0-Link\n" +
                        "      \"* \" [#/1/Untitled/cell/A1/format-pattern/text/save/%22Hello%22+@*+] id=pattern-append-1-Link\n" +
                        "      \"_ \" [#/1/Untitled/cell/A1/format-pattern/text/save/%22Hello%22+@_+] id=pattern-append-2-Link\n" // expected
        );
    }

    @Test
    public void testMetadataTextFormatAtSign() {
        //http://localhost:12345/index.html#/1/Untitled/metadata/date-format-pattern
        this.refreshAndCheck(
                "/1/Untitled/metadata/text-format-pattern", // historyToken
                "@",
                SpreadsheetPattern::parseTextFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"@\" [#/1/Untitled/metadata/text-format-pattern/save/@] id=pattern-append-0-Link\n" +
                        "      \"* \" [#/1/Untitled/metadata/text-format-pattern/save/@*+] id=pattern-append-1-Link\n" +
                        "      \"_ \" [#/1/Untitled/metadata/text-format-pattern/save/@_+] id=pattern-append-2-Link\n"
        );
    }

    // time format......................................................................................................

    @Test
    public void testCellTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/format-pattern/time", // historyToken
                "hh:mm:ss",
                SpreadsheetPattern::parseTimeFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"0\" DISABLED id=pattern-append-0-Link\n" +
                        "      \".\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ss.] id=pattern-append-1-Link\n" +
                        "      \"hh\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:sshh] id=pattern-append-2-Link\n" +
                        "      \"h\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ssh] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "      \"ss\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ss] id=pattern-append-6-Link\n" +
                        "      \"s\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:s] id=pattern-append-7-Link\n" +
                        "      \"am/pm\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ssam/pm] id=pattern-append-8-Link\n" +
                        "      \"AM/PM\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ssAM/PM] id=pattern-append-9-Link\n" +
                        "      \"a/p\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ssa/p] id=pattern-append-10-Link\n" +
                        "      \"A/P\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ssA/P] id=pattern-append-11-Link\n" +
                        "      \";\" [#/1/Untitled/cell/A1/format-pattern/time/save/hh:mm:ss%3B] id=pattern-append-12-Link\n"
        );
    }

    @Test
    public void testMetadataTimeFormat() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/time-format-pattern", // historyToken
                "hh:mm:ss",
                SpreadsheetPattern::parseTimeFormatPattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"0\" DISABLED id=pattern-append-0-Link\n" +
                        "      \".\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ss.] id=pattern-append-1-Link\n" +
                        "      \"hh\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:sshh] id=pattern-append-2-Link\n" +
                        "      \"h\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ssh] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "      \"ss\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ss] id=pattern-append-6-Link\n" +
                        "      \"s\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:s] id=pattern-append-7-Link\n" +
                        "      \"am/pm\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ssam/pm] id=pattern-append-8-Link\n" +
                        "      \"AM/PM\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ssAM/PM] id=pattern-append-9-Link\n" +
                        "      \"a/p\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ssa/p] id=pattern-append-10-Link\n" +
                        "      \"A/P\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ssA/P] id=pattern-append-11-Link\n" +
                        "      \";\" [#/1/Untitled/metadata/time-format-pattern/save/hh:mm:ss%3B] id=pattern-append-12-Link\n"
        );
    }

    // time parse......................................................................................................

    @Test
    public void testCellTimeParse() {
        this.refreshAndCheck(
                "/1/Untitled/cell/A1/parse-pattern/time", // historyToken
                "hh:mm:ss",
                SpreadsheetPattern::parseTimeParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"0\" DISABLED id=pattern-append-0-Link\n" +
                        "      \".\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ss.] id=pattern-append-1-Link\n" +
                        "      \"hh\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:sshh] id=pattern-append-2-Link\n" +
                        "      \"h\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ssh] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "      \"ss\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ss] id=pattern-append-6-Link\n" +
                        "      \"s\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:s] id=pattern-append-7-Link\n" +
                        "      \"am/pm\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ssam/pm] id=pattern-append-8-Link\n" +
                        "      \"AM/PM\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ssAM/PM] id=pattern-append-9-Link\n" +
                        "      \"a/p\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ssa/p] id=pattern-append-10-Link\n" +
                        "      \"A/P\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ssA/P] id=pattern-append-11-Link\n" +
                        "      \";\" [#/1/Untitled/cell/A1/parse-pattern/time/save/hh:mm:ss%3B] id=pattern-append-12-Link\n"
        );
    }

    @Test
    public void testMetadataTimeParse() {
        this.refreshAndCheck(
                "/1/Untitled/metadata/time-parse-pattern", // historyToken
                "hh:mm:ss",
                SpreadsheetPattern::parseTimeParsePattern,
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    Append new component(s)\n" +
                        "      \"0\" DISABLED id=pattern-append-0-Link\n" +
                        "      \".\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss.] id=pattern-append-1-Link\n" +
                        "      \"hh\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:sshh] id=pattern-append-2-Link\n" +
                        "      \"h\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ssh] id=pattern-append-3-Link\n" +
                        "      \"mm\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ssmm] id=pattern-append-4-Link\n" +
                        "      \"m\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ssm] id=pattern-append-5-Link\n" +
                        "      \"ss\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss] id=pattern-append-6-Link\n" +
                        "      \"s\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:s] id=pattern-append-7-Link\n" +
                        "      \"am/pm\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ssam/pm] id=pattern-append-8-Link\n" +
                        "      \"AM/PM\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ssAM/PM] id=pattern-append-9-Link\n" +
                        "      \"a/p\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ssa/p] id=pattern-append-10-Link\n" +
                        "      \"A/P\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ssA/P] id=pattern-append-11-Link\n" +
                        "      \";\" [#/1/Untitled/metadata/time-parse-pattern/save/hh:mm:ss%3B] id=pattern-append-12-Link\n"
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
                return HistoryToken.parse(
                        UrlFragment.parse(token)
                );
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
