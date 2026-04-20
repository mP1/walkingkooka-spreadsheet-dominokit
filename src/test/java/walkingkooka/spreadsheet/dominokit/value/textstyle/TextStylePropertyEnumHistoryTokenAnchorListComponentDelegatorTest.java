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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.HasName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumHistoryTokenAnchorListComponentDelegatorTest.TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class TextStylePropertyEnumHistoryTokenAnchorListComponentDelegatorTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, TextAlign, TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator> {

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            new TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator(),
            "TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator\n" +
                "  TextStylePropertyEnumHistoryTokenAnchorListComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"LEFT\" [#/1/SpreadsheetName111/spreadsheet/style/text-align/save/LEFT] id=Test-textAlign-LEFT-Link\n" +
                "          \"RIGHT\" [#/1/SpreadsheetName111/spreadsheet/style/text-align/save/RIGHT] id=Test-textAlign-RIGHT-Link\n" +
                "          \"CENTER\" [#/1/SpreadsheetName111/spreadsheet/style/text-align/save/CENTER] id=Test-textAlign-CENTER-Link\n" +
                "          \"JUSTIFY\" [#/1/SpreadsheetName111/spreadsheet/style/text-align/save/JUSTIFY] id=Test-textAlign-JUSTIFY-Link\n"
        );
    }

    @Override
    public TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator createComponent() {
        return new TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator();
    }

    // class............................................................................................................

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator> type() {
        return TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator implements TextStylePropertyEnumHistoryTokenAnchorListComponentDelegator<TextAlign, TestTextStylePropertyEnumHistoryTokenAnchorListComponentDelegator>,
        HasName<TextStylePropertyName<TextAlign>> {

        @Override
        public TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
            return this.component;
        }

        private final TextStylePropertyEnumHistoryTokenAnchorListComponent<TextAlign> component = TextStylePropertyEnumHistoryTokenAnchorListComponent.with(
            "Test-",
            TextStylePropertyName.TEXT_ALIGN,
            Lists.of(TextAlign.values()),
            (n) -> n.map(TextAlign::name).orElse("Clear"),
            (n) -> Optional.empty(),
            new FakeTextStylePropertyEnumHistoryTokenAnchorListComponentContext() {
                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return () -> {
                    };
                }

                @Override
                public HistoryToken historyToken() {
                    return HistoryToken.parseString("/1/SpreadsheetName111");
                }
            }
        );
    }
}
