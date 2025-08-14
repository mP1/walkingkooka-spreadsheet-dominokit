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
import walkingkooka.spreadsheet.dominokit.flex.FlexLayoutComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.text.TextComponent;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.IndentingPrinters;
import walkingkooka.text.printer.Printers;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.text.TextNode;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class HtmlElementComponentTest implements ClassTesting<HtmlElementComponent<?, ?>>,
    TreePrintableTesting {

    // setId............................................................................................................

    @Test
    public void testSetIdWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
                .setId(null)
        );
    }

    @Test
    public void testSetIdWithEmptyFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> HtmlElementComponent.div()
                .setId("")
        );
    }

    @Test
    public void testSetId() {
        final String id = "id123";

        this.checkEquals(
            id,
            HtmlElementComponent.div()
                .setId(id)
                .id()
        );
    }

    // setCssProperty............................................................................................................

    @Test
    public void testSetCssPropertyWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
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
            () -> HtmlElementComponent.div()
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
            () -> HtmlElementComponent.div()
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
            () -> HtmlElementComponent.div()
                .setCssText(null)
        );
    }

    // appendCssClasses.................................................................................................

    @Test
    public void testAppendCssClassesWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
                .addCssClasses(null)
        );
    }

    // removeCssClasses.................................................................................................

    @Test
    public void testRemoveCssClassesWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
                .addCssClasses(null)
        );
    }

    // appendNode.......................................................................................................

    @Test
    public void testAppendChildTextNodeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
                .appendChild(
                    (TextNode)null
                )
        );
    }

    @Test
    public void testAppendChildNodeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
                .appendChild(
                    (Node)null
                )
        );
    }

    @Test
    public void testAppendChildNodeWithIsElementFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
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
            () -> HtmlElementComponent.div()
                .appendText(null)
        );
    }

    // removeNode.......................................................................................................

    @Test
    public void testRemoveChildNodeWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
                .removeChild(
                    (Node)null
                )
        );
    }

    @Test
    public void testRemoveChildNodeWithIsElementFails() {
        assertThrows(
            NullPointerException.class,
            () -> HtmlElementComponent.div()
                .removeChild(
                    (IsElement<?>) null
                )
        );
    }

    // setText..........................................................................................................

    @Test
    public void testSetTextAndTreePrint() {
        this.treePrintAndCheck(
            HtmlElementComponent.div()
                .setText("Hello"),
            "DIV\n" +
                "  \"Hello\"\n"
        );
    }

    @Test
    public void testSetTextIncludingAttributesAndTreePrint() {
        this.treePrintAndCheck(
            HtmlElementComponent.div()
                .setId("id123")
                .setColor("red")
                .setText("Hello"),
            "DIV\n" +
                "  id=\"id123\" style=\"color: red;\"\n" +
                "    \"Hello\"\n"
        );
    }

    @Test
    public void testSetTextClearsChildrenAndTreePrint() {
        this.treePrintAndCheck(
            HtmlElementComponent.div()
                .appendChild(
                    HtmlElementComponent.div()
                        .setId("Lost")
                        .setColor("red")
                        .setText("LostText")
                )
                .setText("Hello"),
            "DIV\n" +
                "  \"Hello\"\n"
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
            HtmlElementComponent.div()
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
        this.treePrintAndCheck(
            HtmlElementComponent.table()
                .setId("tableId123")
                .setCssText("color: #123456;")
            ,
            "TABLE\n" +
                "  id=\"tableId123\" style=\"color: #123456;\"\n"
        );
    }


    @Test
    public void testCssClasses() {
        this.treePrintAndCheck(
            HtmlElementComponent.div()
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
            HtmlElementComponent.div()
                .appendText("Hello")
            ,
            "DIV\n" +
                "  \"Hello\"\n"
        );
    }

    @Test
    public void testTreePrintWithColorBlack() {
        this.treePrintAndCheck(
            HtmlElementComponent.div()
                .setColor("black"),
            "DIV\n" +
                "  style=\"color: black;\"\n"
        );
    }

    @Test
    public void testTreePrintWithColorWithoutWebColorName() {
        this.treePrintAndCheck(
            HtmlElementComponent.div()
                .setColor("#123456"),
            "DIV\n" +
                "  style=\"color: #123456;\"\n"
        );
    }

    @Test
    public void testTable() {
        this.treePrintAndCheck(
            HtmlElementComponent.table()
                .setId("tableId123")
                .appendChild(
                    HtmlElementComponent.thead()
                        .appendChild(
                            HtmlElementComponent.th()
                                .setId("th111")
                                .setColor("green")
                                .setCssText("background-color: white; color: black")
                                .setCssProperty("background-color", "#123456")
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("A")
                                    )
                                )
                        ).appendChild(
                            HtmlElementComponent.th()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("B")
                                    )
                                )
                        ).appendChild(
                            HtmlElementComponent.th()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("C")
                                    )
                                )
                        )
                ).appendChild(
                    HtmlElementComponent.tr()
                        .appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("A1")
                                    )
                                )
                        ).appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("B1")
                                    )
                                )
                        ).appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("C1")
                                    )
                                )
                        )
                ).appendChild(
                    HtmlElementComponent.tr()
                        .appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("A2")
                                    )
                                )
                        ).appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("B2")
                                    )
                                )
                        ).appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("C2")
                                    )
                                )
                        )
                ).appendChild(
                    HtmlElementComponent.tbody()
                        .appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("Footer A")
                                    )
                                )
                        ).appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("Footer B")
                                    )
                                )
                        ).appendChild(
                            HtmlElementComponent.td()
                                .appendChild(
                                    TextComponent.with(
                                        Optional.of("Footer C")
                                    )
                                )
                        )
                ),
            "TABLE\n" +
                "  id=\"tableId123\"\n" +
                "    THEAD\n" +
                "      TH\n" +
                "        id=\"th111\" style=\"background-color: #123456; color: black;\"\n" +
                "          TextComponent\n" +
                "            \"A\"\n" +
                "      TH\n" +
                "        TextComponent\n" +
                "          \"B\"\n" +
                "      TH\n" +
                "        TextComponent\n" +
                "          \"C\"\n" +
                "    TR\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"A1\"\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"B1\"\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"C1\"\n" +
                "    TR\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"A2\"\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"B2\"\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"C2\"\n" +
                "    TBODY\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"Footer A\"\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"Footer B\"\n" +
                "      TD\n" +
                "        TextComponent\n" +
                "          \"Footer C\"\n"
        );
    }

    // printTreeChildren................................................................................................

    @Test
    public void testPrintTreeChildren() {
        final DivComponent component = HtmlElementComponent.div();
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
        final DivComponent component = HtmlElementComponent.div();
        component.appendChild(
            HistoryTokenAnchorComponent.empty()
                .setTextContent("Hello111")
        ).appendChild(
            HistoryTokenAnchorComponent.empty()
                .setTextContent("Hello222")
        ).appendChild(
            FlexLayoutComponent.row()
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
    public Class<HtmlElementComponent<?, ?>> type() {
        return Cast.to(HtmlElementComponent.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
