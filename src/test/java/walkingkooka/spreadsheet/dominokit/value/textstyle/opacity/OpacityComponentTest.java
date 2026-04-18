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

package walkingkooka.spreadsheet.dominokit.value.textstyle.opacity;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.tree.text.Opacity;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class OpacityComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, Opacity, OpacityComponent>,
    ValueTextBoxComponentLikeTesting<OpacityComponent, Opacity> {
    
    private final static Opacity OPACITY = Opacity.parse("50%");

    @Test
    public void testWithNullIdPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> OpacityComponent.with(null)
        );
    }

    @Test
    public void testWithEmptyIdPrefixFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> OpacityComponent.with("")
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "OpacityComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      Opacity [] icons=mdi-close-circle id=TestIdPrefix123-opacity-TextBox REQUIRED\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testClearValueOpacity() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .clearValue(),
            "OpacityComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      Opacity [] icons=mdi-close-circle id=TestIdPrefix123-opacity-TextBox\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(OPACITY)
                ),
            "OpacityComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      Opacity [0.5] icons=mdi-close-circle id=TestIdPrefix123-opacity-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "OpacityComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      Opacity [Invalid123!] icons=mdi-close-circle id=TestIdPrefix123-opacity-TextBox REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character 'I' at 0\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public OpacityComponent createComponent() {
        return OpacityComponent.with("TestIdPrefix123-");
    }

    // HasName..........................................................................................................

    @Test
    public void testName() {
        this.nameAndCheck(
            this.createComponent(),
            TextStylePropertyName.OPACITY
        );
    }

    // class............................................................................................................

    @Override
    public Class<OpacityComponent> type() {
        return OpacityComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
