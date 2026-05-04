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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.naming.HasName;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyEnumComponentDelegatorTest.TestTextStylePropertyEnumComponentDelegator;
import walkingkooka.spreadsheet.dominokit.value.textstyle.filter.TextStylePropertyFilterKind;
import walkingkooka.tree.text.TextAlign;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public final class TextStylePropertyEnumComponentDelegatorTest implements TextStylePropertyEnumComponentTesting<TextAlign, TestTextStylePropertyEnumComponentDelegator> {

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            new TestTextStylePropertyEnumComponentDelegator(),
            "TestTextStylePropertyEnumComponentDelegator\n" +
                "  TextStylePropertyEnumComponent\n" +
                "    AnchorListComponent\n" +
                "      FlexLayoutComponent\n" +
                "        ROW\n" +
                "          \"Clear\" [#/1/SpreadsheetName111/spreadsheet/style/text-align/save/] id=Test-textAlign-Link\n" +
                "          \"LEFT\" [#/1/SpreadsheetName111/spreadsheet/style/text-align/save/LEFT] id=Test-textAlign-LEFT-Link\n" +
                "          \"RIGHT\" [#/1/SpreadsheetName111/spreadsheet/style/text-align/save/RIGHT] id=Test-textAlign-RIGHT-Link\n"
        );
    }

    @Override
    public TestTextStylePropertyEnumComponentDelegator createComponent() {
        return new TestTextStylePropertyEnumComponentDelegator();
    }

    @Override
    public List<TextAlign> enumValues() {
        return Lists.of(
            TextAlign.LEFT,
            TextAlign.RIGHT
        );
    }

    // class............................................................................................................

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<TestTextStylePropertyEnumComponentDelegator> type() {
        return TestTextStylePropertyEnumComponentDelegator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }

    final static class TestTextStylePropertyEnumComponentDelegator implements TextStylePropertyEnumComponentDelegator<TextAlign, TestTextStylePropertyEnumComponentDelegator>,
        HasName<TextStylePropertyName<TextAlign>> {

        @Override
        public Set<TextStylePropertyFilterKind> textStylePropertyFilterKinds() {
            return EnumSet.of(
                TextStylePropertyFilterKind.TEXT
            );
        }

        @Override
        public TextStylePropertyEnumComponent<TextAlign> textStylePropertyNameEnumHistoryTokenAnchorListComponent() {
            return this.component;
        }

        private final TextStylePropertyEnumComponent<TextAlign> component = TextStylePropertyEnumComponent.with(
            "Test-",
            TextStylePropertyName.TEXT_ALIGN,
            Lists.of(
                null,
                TextAlign.LEFT,
                TextAlign.RIGHT
            ),
            (n) -> n.map(TextAlign::name).orElse("Clear"),
            (n) -> Optional.empty(),
            EnumSet.of(
                TextStylePropertyFilterKind.TEXT
            ),
            new FakeTextStylePropertyEnumComponentContext() {
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
