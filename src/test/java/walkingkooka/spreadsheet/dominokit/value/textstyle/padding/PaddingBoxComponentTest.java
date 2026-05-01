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

package walkingkooka.spreadsheet.dominokit.value.textstyle.padding;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Padding;

import java.util.Optional;

public final class PaddingBoxComponentTest implements ValueComponentTesting<HTMLDivElement, Padding, PaddingBoxComponent> {
    
    private final static Padding PADDING = Padding.parse("1px 2px -3px 4.5px");

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "PaddingBoxComponent\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(PADDING)
                ),
            "PaddingBoxComponent\n" +
                "  Padding\n" +
                "    ALL\n" +
                "      TextStyle\n" +
                "        padding-bottom=-3px\n" +
                "        padding-left=4.5px\n" +
                "        padding-right=2px\n" +
                "        padding-top=1px\n"
        );
    }

    @Test
    public void testSetValueWithPaddingTop() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        PADDING.setEdge(BoxEdge.TOP)
                    )
                ),
            "PaddingBoxComponent\n" +
                "  Padding\n" +
                "    TOP\n" +
                "      TextStyle\n" +
                "        padding-top=1px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: #444444; border-bottom-style: DOTTED; border-bottom-width: 3px; border-color: black; border-left-color: #444444; border-left-style: DOTTED; border-left-width: 3px; border-right-color: #444444; border-right-style: DOTTED; border-right-width: 3px; border-style: solid; border-top-color: black; border-top-style: SOLID; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testSetValueWithPaddingRight() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        PADDING.setEdge(BoxEdge.RIGHT)
                    )
                ),
            "PaddingBoxComponent\n" +
                "  Padding\n" +
                "    RIGHT\n" +
                "      TextStyle\n" +
                "        padding-right=2px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: #444444; border-bottom-style: DOTTED; border-bottom-width: 3px; border-color: black; border-left-color: #444444; border-left-style: DOTTED; border-left-width: 3px; border-right-color: black; border-right-style: SOLID; border-right-width: 3px; border-style: solid; border-top-color: #444444; border-top-style: DOTTED; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testSetValueWithPaddingBottom() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        PADDING.setEdge(BoxEdge.BOTTOM)
                    )
                ),
            "PaddingBoxComponent\n" +
                "  Padding\n" +
                "    BOTTOM\n" +
                "      TextStyle\n" +
                "        padding-bottom=-3px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: black; border-bottom-style: SOLID; border-bottom-width: 3px; border-color: black; border-left-color: #444444; border-left-style: DOTTED; border-left-width: 3px; border-right-color: #444444; border-right-style: DOTTED; border-right-width: 3px; border-style: solid; border-top-color: #444444; border-top-style: DOTTED; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testSetValueWithPaddingLeft() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        PADDING.setEdge(BoxEdge.LEFT)
                    )
                ),
            "PaddingBoxComponent\n" +
                "  Padding\n" +
                "    LEFT\n" +
                "      TextStyle\n" +
                "        padding-left=4.5px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: #444444; border-bottom-style: DOTTED; border-bottom-width: 3px; border-color: black; border-left-color: black; border-left-style: SOLID; border-left-width: 3px; border-right-color: #444444; border-right-style: DOTTED; border-right-width: 3px; border-style: solid; border-top-color: #444444; border-top-style: DOTTED; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testSetValueWithPaddingRightLeft() {
        this.setValueAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Padding.parse("right: 1px; left: 2px;")
                    )
                ),
            "PaddingBoxComponent\n" +
                "  Padding\n" +
                "    ALL\n" +
                "      TextStyle\n" +
                "        padding-left=2px\n" +
                "        padding-right=1px\n",
            "DIV\n" +
                "  style=\"background-color: #dddddd; border-bottom-color: #444444; border-bottom-style: DOTTED; border-bottom-width: 3px; border-color: black; border-left-color: black; border-left-style: SOLID; border-left-width: 3px; border-right-color: black; border-right-style: SOLID; border-right-width: 3px; border-style: solid; border-top-color: #444444; border-top-style: DOTTED; border-top-width: 3px; border-width: 1px; height: 20px; width: 20px;\"\n"
        );
    }

    private void setValueAndCheck(final PaddingBoxComponent component,
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
    public PaddingBoxComponent createComponent() {
        return PaddingBoxComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<PaddingBoxComponent> type() {
        return PaddingBoxComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
