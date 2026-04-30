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

package walkingkooka.spreadsheet.dominokit.value.textstyle.color;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.color.Color;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;

import java.util.Optional;

public final class ColorBoxComponentTest implements ValueComponentTesting<HTMLDivElement, Color, ColorBoxComponent>,
    ToStringTesting<ColorBoxComponent> {

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            this.createComponent(),
            "ColorBoxComponent\n"
        );
    }

    @Test
    public void testSetValue() {
        final ColorBoxComponent component = this.createComponent();

        this.treePrintAndCheck(
            component.setValue(
                    Optional.of(
                        Color.BLACK
                    )
                ),
            "ColorBoxComponent\n" +
                "  black\n"
        );

        this.treePrintAndCheck(
            component.component,
            "DIV\n" +
                "  style=\"background-color: black; border-color: black; border-style: solid; border-width: 1px; display: ;\"\n"
        );
    }

    @Test
    public void testClearValue() {
        final ColorBoxComponent component = this.createComponent();

        this.treePrintAndCheck(
            component.clearValue(),
            "ColorBoxComponent\n"
        );

        this.treePrintAndCheck(
            component.component,
            "DIV\n" +
                "  style=\"border-color: black; border-style: solid; border-width: 1px; display: none;\"\n"
        );
    }

    @Override
    public ColorBoxComponent createComponent() {
        return ColorBoxComponent.empty();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createComponent(),
            ""
        );
    }

    @Test
    public void testToStringWithColor() {
        this.toStringAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(Color.BLACK)
                ),
            "black"
        );
    }

    // class............................................................................................................

    @Override
    public Class<ColorBoxComponent> type() {
        return ColorBoxComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
