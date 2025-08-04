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

package walkingkooka.spreadsheet.dominokit.dom;

import elemental2.dom.HTMLTableElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;
import walkingkooka.spreadsheet.dominokit.dom.HtmlElementComponentDelegatorTest.TestSpreadsheetElementComponentDelegator;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextComponent;
import walkingkooka.text.printer.IndentingPrinter;

import java.util.Optional;

public class HtmlElementComponentDelegatorTest implements HtmlComponentTesting<TestSpreadsheetElementComponentDelegator, HTMLTableElement> {

    @Override
    public void testTestNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testClassName() {
        this.checkEquals(
            HtmlElementComponent.class.getSimpleName() + "Delegator",
            HtmlElementComponentDelegator.class.getSimpleName()
        );
    }

    @Test
    public void testTreePrint() {
        final TestSpreadsheetElementComponentDelegator component = new TestSpreadsheetElementComponentDelegator();
        component.table.appendChild(
            HtmlElementComponent.thead()
                .appendChild(
                    HtmlElementComponent.th()
                        .appendChild(
                            SpreadsheetTextComponent.with(
                                Optional.of("A")
                            )
                        )
                ).appendChild(
                    HtmlElementComponent.th()
                        .appendChild(
                            SpreadsheetTextComponent.with(
                                Optional.of("B")
                            )
                        )
                )
        ).appendChild(
            HtmlElementComponent.tr()
                .appendChild(
                    HtmlElementComponent.td()
                        .appendChild(
                            SpreadsheetTextComponent.with(
                                Optional.of("A1=1+2")
                            )
                        )
                ).appendChild(
                    HtmlElementComponent.td()
                        .appendChild(
                            SpreadsheetTextComponent.with(
                                Optional.of("B1=333")
                            )
                        )
                )
        );

        this.treePrintAndCheck(
            component,
            "TestSpreadsheetElementComponentDelegator\n" +
                "  TABLE\n" +
                "    THEAD\n" +
                "      TH\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"A\"\n" +
                "      TH\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"B\"\n" +
                "    TR\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"A1=1+2\"\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"B1=333\"\n"
        );
    }

    final static class TestSpreadsheetElementComponentDelegator implements HtmlElementComponentDelegator<HTMLTableElement, TestSpreadsheetElementComponentDelegator> {

        @Override
        public TableComponent htmlElementComponent() {
            return this.table;
        }

        private final TableComponent table = HtmlElementComponent.table();

        @Override
        public boolean isEditing() {
            return false;
        }

        @Override
        public void printTree(final IndentingPrinter printer) {
            printer.println(this.getClass().getSimpleName());

            printer.indent();
            {
                this.table.printTree(printer);
            }
            printer.outdent();
        }
    }

    // class............................................................................................................

    @Override
    public Class<TestSpreadsheetElementComponentDelegator> type() {
        return TestSpreadsheetElementComponentDelegator.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
