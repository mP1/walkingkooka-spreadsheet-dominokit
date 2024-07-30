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
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.TextNode;

import java.util.List;

public final class SpreadsheetFormatterTableComponentTest implements ClassTesting<SpreadsheetFormatterTableComponent>,
        SpreadsheetMetadataTesting,
        TreePrintableTesting {

    @Test
    public void testRefresh() {
        this.refreshAndCheck(
                Lists.of(
                        SpreadsheetFormatterSample.with(
                                "Label123",
                                SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/dd")
                                        .spreadsheetFormatterSelector(),
                                TextNode.text("1999/12/31")
                        )
                ),
                "SpreadsheetFormatterTableComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Label123\n" +
                        "            \"yyyy/mm/dd\" [#/2/Untitled/cell/D1/formatter/date/save/date-format-pattern%20yyyy/mm/dd] id=id123-Label123-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              1999/12/31\n"
        );
    }

    @Test
    public void testRefresh2() {
        this.refreshAndCheck(
                Lists.of(
                        SpreadsheetFormatterSample.with(
                                "Short",
                                SpreadsheetPattern.parseDateFormatPattern("yyyy/mm/dd")
                                        .spreadsheetFormatterSelector(),
                                TextNode.text("1999/12/31")
                        ),
                        SpreadsheetFormatterSample.with(
                                "Medium",
                                SpreadsheetPattern.parseDateFormatPattern("ddd/mm/yyyy")
                                        .spreadsheetFormatterSelector(),
                                TextNode.text("Monday 31/12/1999")
                        )
                ),
                "SpreadsheetFormatterTableComponent\n" +
                        "  SpreadsheetCard\n" +
                        "    Card\n" +
                        "      SpreadsheetDataTableComponent\n" +
                        "        ROW(S)\n" +
                        "          ROW 0\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Short\n" +
                        "            \"yyyy/mm/dd\" [#/2/Untitled/cell/D1/formatter/date/save/date-format-pattern%20yyyy/mm/dd] id=id123-Short-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              1999/12/31\n" +
                        "          ROW 1\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Medium\n" +
                        "            \"ddd/mm/yyyy\" [#/2/Untitled/cell/D1/formatter/date/save/date-format-pattern%20ddd/mm/yyyy] id=id123-Medium-Link\n" +
                        "            SpreadsheetTextNodeComponent\n" +
                        "              Monday 31/12/1999\n"
        );
    }

    private void refreshAndCheck(final List<SpreadsheetFormatterSample> samples,
                                 final String expected) {
        final SpreadsheetFormatterTableComponent component = SpreadsheetFormatterTableComponent.empty(
                "id123-",
                new FakeSpreadsheetFormatterTableComponentContext() {
                    @Override
                    public HistoryToken historyToken() {
                        return HistoryToken.parseString("/2/Untitled/cell/D1/formatter/date");
                    }

                    @Override
                    public String saveText(final String text) {
                        return text;
                    }
                }
        );
        component.refresh(samples);
        this.treePrintAndCheck(
                component,
                expected
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetFormatterTableComponent> type() {
        return SpreadsheetFormatterTableComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
