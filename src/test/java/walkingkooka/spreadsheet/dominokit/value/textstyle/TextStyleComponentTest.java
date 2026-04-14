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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

public final class TextStyleComponentTest implements ValueTextBoxComponentLikeTesting<TextStyleComponent, TextStyle> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setValue(
                    Optional.of(
                        TextStyle.parse("color: #111; text-align: left;")
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [color: #111; text-align: left;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "color: #111; text-align: left;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [color: #111; text-align: left;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueFontVariantSmallCaps() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "font-variant: SMALL_CAPS;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [font-variant: SMALL_CAPS;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueFontVariantSmallCapsMixedCase() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "font-variant: SMALL_caps;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [font-variant: SMALL_caps;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithExtraWhitespaceEtc() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "  color:  #111;  text-align:  left;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [  color:  #111;  text-align:  left;] REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidText() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .setStringValue(
                    Optional.of(
                        "color: #111; text-align: XYZ;"
                    )
                ),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [color: #111; text-align: XYZ;] REQUIRED\n" +
                "      Errors\n" +
                "        Unknown value \"XYZ\"\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            TextStyleComponent.empty()
                .clearValue(),
            "TextStyleComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] REQUIRED\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public TextStyleComponent createComponent() {
        return TextStyleComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<TextStyleComponent> type() {
        return TextStyleComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
