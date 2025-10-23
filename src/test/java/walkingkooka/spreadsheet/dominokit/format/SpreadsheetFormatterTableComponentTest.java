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

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.value.TableComponentTesting;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSample;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataTesting;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Optional;

public final class SpreadsheetFormatterTableComponentTest implements TableComponentTesting<HTMLDivElement, List<SpreadsheetFormatterSample>, SpreadsheetFormatterTableComponent>,
    SpreadsheetMetadataTesting {

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
            ), // SpreadsheetFormatterTableComponentContext#formatterTableHistoryTokenSave -> SpreadsheetFormatterSelector#toString + "@"
            "SpreadsheetFormatterTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=id123-Table\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextNodeComponent\n" +
                "              Label123\n" +
                "            \"yyyy/mm/dd\" [#/2/Untitled/cell/D1/formatter/save/date%20yyyy/mm/dd@] id=id123-Label123-Link\n" +
                "            TextNodeComponent\n" +
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
            ), // SpreadsheetFormatterTableComponentContext#formatterTableHistoryTokenSave -> SpreadsheetFormatterSelector#toString + "@"
            "SpreadsheetFormatterTableComponent\n" +
                "  CardComponent\n" +
                "    Card\n" +
                "      DataTableComponent\n" +
                "        id=id123-Table\n" +
                "        ROW(S)\n" +
                "          ROW 0\n" +
                "            TextNodeComponent\n" +
                "              Short\n" +
                "            \"yyyy/mm/dd\" [#/2/Untitled/cell/D1/formatter/save/date%20yyyy/mm/dd@] id=id123-Short-Link\n" +
                "            TextNodeComponent\n" +
                "              1999/12/31\n" +
                "          ROW 1\n" +
                "            TextNodeComponent\n" +
                "              Medium\n" +
                "            \"ddd/mm/yyyy\" [#/2/Untitled/cell/D1/formatter/save/date%20ddd/mm/yyyy@] id=id123-Medium-Link\n" +
                "            TextNodeComponent\n" +
                "              Monday 31/12/1999\n"
        );
    }

    private void refreshAndCheck(final List<SpreadsheetFormatterSample> samples,
                                 final String expected) {
        final SpreadsheetFormatterTableComponent component = SpreadsheetFormatterTableComponent.empty(
            "id123-"
        );

        component.setValue(
            Optional.ofNullable(
                samples.isEmpty() ?
                    null :
                    samples
            )
        );

        component.refresh(
            new FakeSpreadsheetFormatterTableComponentContext() {
                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/2/Untitled/cell/D1/formatter");
                }

                @Override
                public String formatterTableHistoryTokenSave(final SpreadsheetFormatterSelector selector) {
                    return selector.text() + "@";
                }
            }
        );

        this.treePrintAndCheck(
            component,
            expected
        );
    }

    @Override
    public SpreadsheetFormatterTableComponent createComponent() {
        return SpreadsheetFormatterTableComponent.empty(
            "id123-"
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
