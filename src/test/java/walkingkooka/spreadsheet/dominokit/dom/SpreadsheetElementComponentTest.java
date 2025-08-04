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

import elemental2.dom.Node;
import org.dominokit.domino.ui.IsElement;
import org.dominokit.domino.ui.style.SpacingCss;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.color.Color;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextComponent;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.IndentingPrinters;
import walkingkooka.text.printer.Printers;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetElementComponentTest implements ClassTesting<SpreadsheetElementComponent<?, ?>>,
    TreePrintableTesting {

    // setId............................................................................................................

    @Test
    public void testSetIdWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .setId(null)
        );
    }

    @Test
    public void testSetIdWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetElementComponent.div()
                .setId("")
        );
    }

    @Test
    public void testSetId() {
        final String id = "id123";

        this.checkEquals(
            id,
            SpreadsheetElementComponent.div()
                .setId(id)
                .id()
        );
    }

    // setCssProperty............................................................................................................

    @Test
    public void testSetCssPropertyWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .setCssProperty(
                    null,
                    "123"
                )
        );
    }

    @Test
    public void testSetCssPropertyWithEmptyNameFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> SpreadsheetElementComponent.div()
                .setCssProperty(
                    "",
                    "123"
                )
        );
    }

    @Test
    public void testSetCssPropertyWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .setCssProperty(
                    "color",
                    null
                )
        );
    }

    // setCssText.......................................................................................................

    @Test
    public void testSetCssTextWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .setCssText(null)
        );
    }

    // appendCssClasses.................................................................................................

    @Test
    public void testAppendCssClassesWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .addCssClasses(null)
        );
    }

    // removeCssClasses.................................................................................................

    @Test
    public void testRemoveCssClassesWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .addCssClasses(null)
        );
    }

    // appendNode.......................................................................................................

    @Test
    public void testAppendChildTextNodeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .appendChild(
                    (TextNode)null
                )
        );
    }

    @Test
    public void testAppendChildNodeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .appendChild(
                    (Node)null
                )
        );
    }

    @Test
    public void testAppendChildNodeWithIsElementFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .appendChild(
                    (IsElement<?>) null
                )
        );
    }

    // appendText.......................................................................................................

    @Test
    public void testAppendTextWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .appendText(null)
        );
    }

    // removeNode.......................................................................................................

    @Test
    public void testRemoveChildNodeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .removeChild(
                    (Node)null
                )
        );
    }

    @Test
    public void testRemoveChildNodeWithIsElementFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetElementComponent.div()
                .removeChild(
                    (IsElement<?>) null
                )
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testStyleWithRgbColorFunction() {
        this.checkEquals(
            "#123456",
            Color.parse("#123456")
                .toString()
        );

        this.treePrintAndCheck(
            SpreadsheetElementComponent.div()
                .setId("divId123")
                .setCssProperty(
                    "color",
                    Color.parse("#123456")
                        .toString()
                ).setCssProperty(
                    "text-align",
                "left"
                )
            ,
            "DIV\n" +
                "  id=\"divId123\" style=\"color: #123456; text-align: left;\"\n"
        );
    }

    @Test
    public void testTableWithCssTextWithTrailingSemiColon() {
        SpreadsheetElementComponent.tr();

        this.treePrintAndCheck(
            SpreadsheetElementComponent.table()
                .setId("tableId123")
                .setCssText("color: black;")
            ,
            "TABLE\n" +
                "  id=\"tableId123\" style=\"color: #000000;\"\n"
        );
    }


    @Test
    public void testCssClasses() {
        SpreadsheetElementComponent.tr();

        this.treePrintAndCheck(
            SpreadsheetElementComponent.div()
                .setId("divId123")
                .addCssClasses(
                    SpacingCss.dui_flex_col,
                    SpacingCss.dui_align_middle
                )
            ,
            "DIV\n" +
                "  id=\"divId123\" class=\"dui-flex-col dui-align-middle\"\n"
        );
    }

    @Test
    public void testTreePrintAfterAppendText() {
        this.treePrintAndCheck(
            SpreadsheetElementComponent.div()
                .appendText("Hello")
            ,
            "DIV\n" +
                "  \"Hello\"\n"
        );
    }

    @Test
    public void testTable() {
        SpreadsheetElementComponent.tr();

        this.treePrintAndCheck(
            SpreadsheetElementComponent.table()
                .setId("tableId123")
                .appendChild(
                    SpreadsheetElementComponent.thead()
                        .appendChild(
                            SpreadsheetElementComponent.th()
                                .setId("th111")
                                .setColor("green")
                                .setCssText("background-color: white; color: black")
                                .setCssProperty("background-color", "yellow")
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("A")
                                    )
                                )
                        ).appendChild(
                            SpreadsheetElementComponent.th()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("B")
                                    )
                                )
                        ).appendChild(
                            SpreadsheetElementComponent.th()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("C")
                                    )
                                )
                        )
                ).appendChild(
                    SpreadsheetElementComponent.tr()
                        .appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("A1")
                                    )
                                )
                        ).appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("B1")
                                    )
                                )
                        ).appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("C1")
                                    )
                                )
                        )
                ).appendChild(
                    SpreadsheetElementComponent.tr()
                        .appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("A2")
                                    )
                                )
                        ).appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("B2")
                                    )
                                )
                        ).appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("C2")
                                    )
                                )
                        )
                ).appendChild(
                    SpreadsheetElementComponent.tbody()
                        .appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("Footer A")
                                    )
                                )
                        ).appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("Footer B")
                                    )
                                )
                        ).appendChild(
                            SpreadsheetElementComponent.td()
                                .appendChild(
                                    SpreadsheetTextComponent.with(
                                        Optional.of("Footer C")
                                    )
                                )
                        )
                ),
            "TABLE\n" +
                "  id=\"tableId123\"\n" +
                "    THEAD\n" +
                "      TH\n" +
                "        id=\"th111\" style=\"background-color: #ffff00; color: #000000;\"\n" +
                "          SpreadsheetTextComponent\n" +
                "            \"A\"\n" +
                "      TH\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"B\"\n" +
                "      TH\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"C\"\n" +
                "    TR\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"A1\"\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"B1\"\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"C1\"\n" +
                "    TR\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"A2\"\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"B2\"\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"C2\"\n" +
                "    TBODY\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"Footer A\"\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"Footer B\"\n" +
                "      TD\n" +
                "        SpreadsheetTextComponent\n" +
                "          \"Footer C\"\n"
        );
    }

    // printTreeChildren................................................................................................

    @Test
    public void testPrintTreeChildren() {
        final SpreadsheetDivComponent component = SpreadsheetElementComponent.div();
        component.appendChild(
            HistoryTokenAnchorComponent.empty()
                .setTextContent("Hello111")
        ).appendChild(
            HistoryTokenAnchorComponent.empty()
                .setTextContent("Hello222")
        );

        final StringBuilder b = new StringBuilder();
        final IndentingPrinter printer = IndentingPrinters.printer(
            Printers.stringBuilder(b, LineEnding.NL),
            Indentation.SPACES2
        );

        component.printTreeChildren(printer);

        printer.println("Last");

        this.checkEquals(
            "\"Hello111\" DISABLED\n" +
                "\"Hello222\" DISABLED\n" +
                "Last\n",
            b.toString()
        );
    }

    @Test
    public void testPrintTreeChildrenSkipEmpty() {
        final SpreadsheetDivComponent component = SpreadsheetElementComponent.div();
        component.appendChild(
            HistoryTokenAnchorComponent.empty()
                .setTextContent("Hello111")
        ).appendChild(
            HistoryTokenAnchorComponent.empty()
                .setTextContent("Hello222")
        ).appendChild(
            SpreadsheetFlexLayout.row()
        );

        final StringBuilder b = new StringBuilder();
        final IndentingPrinter printer = IndentingPrinters.printer(
            Printers.stringBuilder(b, LineEnding.NL),
            Indentation.SPACES2
        );

        component.printTreeChildren(printer);

        printer.println("Last");

        this.checkEquals(
            "\"Hello111\" DISABLED\n" +
                "\"Hello222\" DISABLED\n" +
                "Last\n",
            b.toString()
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetElementComponent<?, ?>> type() {
        return Cast.to(SpreadsheetElementComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
