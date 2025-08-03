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

import org.dominokit.domino.ui.style.SpacingCss;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.text.SpreadsheetTextComponent;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetElementComponentTest implements ClassTesting<SpreadsheetElementComponent<?, ?>>,
    TreePrintableTesting {

    // TreePrintable....................................................................................................

    @Test
    public void testTableWithCssTextWithTrailingSemiColon() {
        SpreadsheetElementComponent.tr();

        this.treePrintAndCheck(
            SpreadsheetElementComponent.table()
                .setId("tableId123")
                .setCssText("color: black;")
            ,
            "TABLE\n" +
                "  id=\"tableId123\" style=\"color: black;\"\n"
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
                "        id=\"th111\" style=\"background-color: yellow; color: black;\"\n" +
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
