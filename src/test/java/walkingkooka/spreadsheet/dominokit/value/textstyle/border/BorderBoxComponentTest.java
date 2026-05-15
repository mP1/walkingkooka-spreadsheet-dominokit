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

package walkingkooka.spreadsheet.dominokit.value.textstyle.border;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.tree.text.Border;

import java.util.Optional;

public final class BorderBoxComponentTest implements ValueComponentTesting<HTMLDivElement, Border, BorderBoxComponent>,
    ToStringTesting<BorderBoxComponent> {

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            this.createComponent(),
            "BorderBoxComponent\n"
        );
    }

    @Test
    public void testSetValue() {
        final BorderBoxComponent component = this.createComponent();

        this.treePrintAndCheck(
            component.setValue(
                    Optional.of(
                        Border.parse("WHITE DOTTED 2px")
                    )
                ),
            "BorderBoxComponent\n" +
                "  Border\n" +
                "    ALL\n" +
                "      TextStyle\n" +
                "        border-bottom-color=white\n" +
                "        border-bottom-style=DOTTED\n" +
                "        border-bottom-width=2px\n" +
                "        border-left-color=white\n" +
                "        border-left-style=DOTTED\n" +
                "        border-left-width=2px\n" +
                "        border-right-color=white\n" +
                "        border-right-style=DOTTED\n" +
                "        border-right-width=2px\n" +
                "        border-top-color=white\n" +
                "        border-top-style=DOTTED\n" +
                "        border-top-width=2px\n"
        );

        this.treePrintAndCheck(
            component.component,
            "DIV\n" +
                "  style=\"border-bottom-color: white; border-bottom-style: DOTTED; border-bottom-width: 2px; border-left-color: white; border-left-style: DOTTED; border-left-width: 2px; border-right-color: white; border-right-style: DOTTED; border-right-width: 2px; border-top-color: white; border-top-style: DOTTED; border-top-width: 2px; display: ; height: 20px; width: 20px;\"\n"
        );
    }

    @Test
    public void testClearValue() {
        final BorderBoxComponent component = this.createComponent();

        this.treePrintAndCheck(
            component.clearValue(),
            "BorderBoxComponent\n"
        );

        this.treePrintAndCheck(
            component.component,
            "DIV\n" +
                "  style=\"display: none; height: 20px; width: 20px;\"\n"
        );
    }

    @Override
    public BorderBoxComponent createComponent() {
        return BorderBoxComponent.empty();
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
    public void testToStringWithBorder() {
        this.toStringAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Border.parse("RED SOLID 1px")
                    )
                ),
            "border ALL red SOLID 1px"
        );
    }

    // class............................................................................................................

    @Override
    public Class<BorderBoxComponent> type() {
        return BorderBoxComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
