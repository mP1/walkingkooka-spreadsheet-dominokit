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

package walkingkooka.spreadsheet.dominokit.value.textstyle.margin;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Margin;

import java.util.Optional;

public final class MarginBoxComponentTest implements ValueComponentTesting<HTMLDivElement, Margin, MarginBoxComponent> {
    
    private final static Margin MARGIN = Margin.parse("1px 2px -3px 4.5px");

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "MarginBoxComponent\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(MARGIN)
                ),
            "MarginBoxComponent\n" +
                "  Margin\n" +
                "    ALL\n" +
                "      TextStyle\n" +
                "        margin-bottom=-3px\n" +
                "        margin-left=4.5px\n" +
                "        margin-right=2px\n" +
                "        margin-top=1px\n"
        );
    }

    @Test
    public void testSetValueWithMarginTop() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        MARGIN.setEdge(BoxEdge.TOP)
                    )
                ),
            "MarginBoxComponent\n" +
                "  Margin\n" +
                "    TOP\n" +
                "      TextStyle\n" +
                "        margin-top=1px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: #444444; border-bottom-style: DOTTED; border-bottom-width: 3px; border-color: black; border-left-color: #444444; border-left-style: DOTTED; border-left-width: 3px; border-right-color: #444444; border-right-style: DOTTED; border-right-width: 3px; border-style: solid; border-top-color: black; border-top-style: SOLID; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testSetValueWithMarginRight() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        MARGIN.setEdge(BoxEdge.RIGHT)
                    )
                ),
            "MarginBoxComponent\n" +
                "  Margin\n" +
                "    RIGHT\n" +
                "      TextStyle\n" +
                "        margin-right=2px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: #444444; border-bottom-style: DOTTED; border-bottom-width: 3px; border-color: black; border-left-color: #444444; border-left-style: DOTTED; border-left-width: 3px; border-right-color: black; border-right-style: SOLID; border-right-width: 3px; border-style: solid; border-top-color: #444444; border-top-style: DOTTED; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testSetValueWithMarginBottom() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        MARGIN.setEdge(BoxEdge.BOTTOM)
                    )
                ),
            "MarginBoxComponent\n" +
                "  Margin\n" +
                "    BOTTOM\n" +
                "      TextStyle\n" +
                "        margin-bottom=-3px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: black; border-bottom-style: SOLID; border-bottom-width: 3px; border-color: black; border-left-color: #444444; border-left-style: DOTTED; border-left-width: 3px; border-right-color: #444444; border-right-style: DOTTED; border-right-width: 3px; border-style: solid; border-top-color: #444444; border-top-style: DOTTED; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testSetValueWithMarginLeft() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        MARGIN.setEdge(BoxEdge.LEFT)
                    )
                ),
            "MarginBoxComponent\n" +
                "  Margin\n" +
                "    LEFT\n" +
                "      TextStyle\n" +
                "        margin-left=4.5px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: #444444; border-bottom-style: DOTTED; border-bottom-width: 3px; border-color: black; border-left-color: black; border-left-style: SOLID; border-left-width: 3px; border-right-color: #444444; border-right-style: DOTTED; border-right-width: 3px; border-style: solid; border-top-color: #444444; border-top-style: DOTTED; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testSetValueWithMarginRightLeft() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Margin.parse("right: 1px; left: 2px;")
                    )
                ),
            "MarginBoxComponent\n" +
                "  Margin\n" +
                "    ALL\n" +
                "      TextStyle\n" +
                "        margin-left=2px\n" +
                "        margin-right=1px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: #444444; border-bottom-style: DOTTED; border-bottom-width: 3px; border-color: black; border-left-color: black; border-left-style: SOLID; border-left-width: 3px; border-right-color: black; border-right-style: SOLID; border-right-width: 3px; border-style: solid; border-top-color: #444444; border-top-style: DOTTED; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    private void setValueAndCheck(final MarginBoxComponent component,
                                  final String expectedTreePrint,
                                  final String expectedComponent) {
        this.treePrintAndCheck(
            component,
            expectedTreePrint
        );

        this.treePrintAndCheck(
            component.component,
            expectedComponent
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public MarginBoxComponent createComponent() {
        return MarginBoxComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<MarginBoxComponent> type() {
        return MarginBoxComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
