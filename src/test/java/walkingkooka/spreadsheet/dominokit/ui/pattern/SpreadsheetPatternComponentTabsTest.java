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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.text.printer.TreePrintableTesting;

public final class SpreadsheetPatternComponentTabsTest implements ClassTesting<SpreadsheetPatternComponentTabs>,
        TreePrintableTesting {

    @Test
    public void testFormat() {
        this.treePrintAndCheck(
                SpreadsheetPatternComponentTabs.empty(
                        new FakeSpreadsheetPatternDialogComponentContext() {
                            @Override
                            public SpreadsheetPatternKind[] filteredPatternKinds() {
                                return SpreadsheetPatternKind.formatValues();
                            }
                        }
                ),
                "SpreadsheetPatternComponentTabs\n" +
                        "  SpreadsheetTabsComponent\n" +
                        "    TAB 0\n" +
                        "      \"Date\" DISABLED id=pattern-date-format\n" +
                        "    TAB 1\n" +
                        "      \"Date Time\" DISABLED id=pattern-date-time-format\n" +
                        "    TAB 2\n" +
                        "      \"Number\" DISABLED id=pattern-number-format\n" +
                        "    TAB 3\n" +
                        "      \"Text\" DISABLED id=pattern-text-format\n" +
                        "    TAB 4\n" +
                        "      \"Time\" DISABLED id=pattern-time-format\n"
        );
    }

    @Test
    public void testParse() {
        this.treePrintAndCheck(
                SpreadsheetPatternComponentTabs.empty(
                        new FakeSpreadsheetPatternDialogComponentContext() {
                            @Override
                            public SpreadsheetPatternKind[] filteredPatternKinds() {
                                return SpreadsheetPatternKind.parseValues();
                            }
                        }
                ),
                "SpreadsheetPatternComponentTabs\n" +
                        "  SpreadsheetTabsComponent\n" +
                        "    TAB 0\n" +
                        "      \"Date\" DISABLED id=pattern-date-parse\n" +
                        "    TAB 1\n" +
                        "      \"Date Time\" DISABLED id=pattern-date-time-parse\n" +
                        "    TAB 2\n" +
                        "      \"Number\" DISABLED id=pattern-number-parse\n" +
                        "    TAB 3\n" +
                        "      \"Time\" DISABLED id=pattern-time-parse\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetPatternComponentTabs> type() {
        return SpreadsheetPatternComponentTabs.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
