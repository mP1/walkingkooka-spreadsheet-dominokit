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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontweight;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.tree.text.FontWeight;

import java.util.Optional;

public final class FontWeightComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, FontWeight, FontWeightComponent>,
    ValueTextBoxComponentLikeTesting<FontWeightComponent, FontWeight> {

    @Test
    public void testSetLabelFromPropertyName() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabelFromPropertyName(),
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        Font Weight [] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n" +
                "        Errors\n" +
                "          Empty \"text\"\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n" +
                "        Errors\n" +
                "          Empty \"text\"\n"
        );
    }

    @Test
    public void testSetValueWithBold() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(FontWeight.BOLD)
                ),
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [BOLD] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithNormal() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(FontWeight.NORMAL)
                ),
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [NORMAL] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithNumber() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        FontWeight.with(123)
                    )
                ),
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [123] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueBold() {
        final FontWeightComponent component = this.createComponent();

        final String text = "BolD";

        this.setStringValueAndCheck(
            component,
            text,
            FontWeight.parse(text)
        );

        this.treePrintAndCheck(
            component,
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [BolD] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueNormal() {
        final FontWeightComponent component = this.createComponent();

        final String text = "normAL";

        this.setStringValueAndCheck(
            component,
            text,
            FontWeight.parse(text)
        );

        this.treePrintAndCheck(
            component,
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [normAL] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueNumber() {
        final FontWeightComponent component = this.createComponent();

        final String text = "123";

        this.setStringValueAndCheck(
            component,
            text,
            FontWeight.parse(text)
        );

        this.treePrintAndCheck(
            component,
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [123] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "Invalid"
                    )
                ),
            "FontWeightComponent\n" +
                "    ValueTextBoxComponent\n" +
                "      TextBoxComponent\n" +
                "        [Invalid] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n" +
                "        Errors\n" +
                "          Invalid character 'I' at 0\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public FontWeightComponent createComponent() {
        return FontWeightComponent.with("TestIdPrefix123-");
    }

    // class............................................................................................................

    @Override
    public Class<FontWeightComponent> type() {
        return FontWeightComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
