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

    private final static Border BORDER = TextStyle.EMPTY.setBorder(
        Color.BLACK,
        BorderStyle.DASHED,
        Length.pixel(1.0)
    ).border(BoxEdge.TOP);

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            BorderComponent.empty()
                .clearValue(),
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] REQUIRED\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            BorderComponent.empty()
                .setValue(
                    Optional.of(BORDER)
                ),
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [top-color: black; top-style: dashed; top-width: 1px;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            BorderComponent.empty()
                .setStringValue(
                    Optional.of(
                        BORDER.text()
                    )
                ),
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [top-color: black; top-style: dashed; top-width: 1px;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            BorderComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!] REQUIRED\n" +
                "      Errors\n" +
                "        TextStyle: Invalid property \"Invalid123\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public BorderComponent createComponent() {
        return BorderComponent.empty();
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
