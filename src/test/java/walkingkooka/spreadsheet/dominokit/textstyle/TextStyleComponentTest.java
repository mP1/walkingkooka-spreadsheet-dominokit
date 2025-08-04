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

package walkingkooka.spreadsheet.dominokit.textstyle;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.tree.text.TextStyle;

import java.util.Optional;

public final class TextStyleComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, TextStyle, TextStyleComponent> {

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
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [color: rgb(17, 17, 17); text-align: left;]\n"
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
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [color: #111; text-align: left;]\n"
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
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [color: #111; text-align: XYZ;]\n" +
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
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      []\n"
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
