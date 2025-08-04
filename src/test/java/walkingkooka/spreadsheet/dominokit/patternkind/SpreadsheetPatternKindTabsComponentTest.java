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

package walkingkooka.spreadsheet.dominokit.patternkind;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;

public final class SpreadsheetPatternKindTabsComponentTest implements HtmlComponentTesting<SpreadsheetPatternKindTabsComponent, HTMLDivElement> {

    private final static String ID = "id123-";

    @Test
    public void testFormat() {
        this.treePrintAndCheck(
            tabs(SpreadsheetPatternKind.formatValues()),
            "SpreadsheetPatternKindTabsComponent\n" +
                "  SpreadsheetTabsComponent\n" +
                "    TAB 0\n" +
                "      \"Date\" DISABLED id=id123-date-format\n" +
                "    TAB 1\n" +
                "      \"Date Time\" DISABLED id=id123-date-time-format\n" +
                "    TAB 2\n" +
                "      \"Number\" DISABLED id=id123-number-format\n" +
                "    TAB 3\n" +
                "      \"Text\" DISABLED id=id123-text-format\n" +
                "    TAB 4\n" +
                "      \"Time\" DISABLED id=id123-time-format\n"
        );
    }

    @Test
    public void testParse() {
        this.treePrintAndCheck(
            tabs(SpreadsheetPatternKind.parseValues()),
            "SpreadsheetPatternKindTabsComponent\n" +
                "  SpreadsheetTabsComponent\n" +
                "    TAB 0\n" +
                "      \"Date\" DISABLED id=id123-date-parse\n" +
                "    TAB 1\n" +
                "      \"Date Time\" DISABLED id=id123-date-time-parse\n" +
                "    TAB 2\n" +
                "      \"Number\" DISABLED id=id123-number-parse\n" +
                "    TAB 3\n" +
                "      \"Time\" DISABLED id=id123-time-parse\n"
        );
    }

    private SpreadsheetPatternKindTabsComponent tabs(final SpreadsheetPatternKind[] kinds) {
        return SpreadsheetPatternKindTabsComponent.empty(
            ID,
            kinds,
            new FakeSpreadsheetPatternKindTabsComponentContext()
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetPatternKindTabsComponent> type() {
        return SpreadsheetPatternKindTabsComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
