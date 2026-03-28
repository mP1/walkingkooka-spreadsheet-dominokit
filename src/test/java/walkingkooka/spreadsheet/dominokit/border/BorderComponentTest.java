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

package walkingkooka.spreadsheet.dominokit.border;

import org.junit.jupiter.api.Test;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.BorderStyle;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

public final class BorderComponentTest implements ValueTextBoxComponentLikeTesting<BorderComponent, Border> {

    private final static BoxEdge BOX_EDGE = BoxEdge.TOP;

    private final static Border BORDER = TextStyle.EMPTY.setBorder(
        Color.BLACK,
        BorderStyle.DASHED,
        Length.pixel(1.0)
    ).border(BOX_EDGE);

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            BorderComponent.empty(BOX_EDGE)
                .clearValue(),
            "BorderComponent\n" +
                "  TOP\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] REQUIRED\n"
        );
    }

    @Test
    public void testSetValueTop() {
        this.treePrintAndCheck(
            BorderComponent.empty(BOX_EDGE)
                .setValue(
                    Optional.of(BORDER)
                ),
            "BorderComponent\n" +
                "  TOP\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [black DASHED 1px] REQUIRED\n"
        );
    }

    @Test
    public void testSetValueAll() {
        final BoxEdge boxEdge = BoxEdge.ALL;

        this.treePrintAndCheck(
            BorderComponent.empty(boxEdge)
                .setValue(
                    Optional.of(
                        TextStyle.EMPTY.setBorder(
                            Color.WHITE,
                            BorderStyle.SOLID,
                            Length.pixel(123.0)
                        ).border(boxEdge)
                    )
                ),
            "BorderComponent\n" +
                "  ALL\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [white SOLID 123px] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueTop() {
        this.treePrintAndCheck(
            BorderComponent.empty(BOX_EDGE)
                .setStringValue(
                    Optional.of(
                        BORDER.text()
                    )
                ),
            "BorderComponent\n" +
                "  TOP\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [black DASHED 1px] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueAll() {
        this.treePrintAndCheck(
            BorderComponent.empty(BoxEdge.ALL)
                .setStringValue(
                    Optional.of(
                        BoxEdge.ALL.setBorder(
                            Optional.of(Color.WHITE),
                            Optional.of(BorderStyle.SOLID),
                            Optional.of(Length.pixel(123.0))
                        ).text()
                    )
                ),
            "BorderComponent\n" +
                "  ALL\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [white SOLID 123px] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            BorderComponent.empty(BOX_EDGE)
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "BorderComponent\n" +
                "  TOP\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [Invalid123!] REQUIRED\n" +
                "        Errors\n" +
                "          Unknown color name \"Invalid123!\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public BorderComponent createComponent() {
        return BorderComponent.empty(BOX_EDGE);
    }

    // class............................................................................................................

    @Override
    public Class<BorderComponent> type() {
        return BorderComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
