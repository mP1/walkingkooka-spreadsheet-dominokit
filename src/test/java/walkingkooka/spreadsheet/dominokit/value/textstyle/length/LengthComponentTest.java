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

package walkingkooka.spreadsheet.dominokit.value.textstyle.length;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.tree.text.Length;

import java.util.Optional;

public final class LengthComponentTest implements ValueTextBoxComponentLikeTesting<LengthComponent, Length<?>> {

    private final static Length<?> LENGTH = Length.pixel(1.0);

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            LengthComponent.empty()
                .clearValue(),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icon=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testClearValueOptional() {
        this.treePrintAndCheck(
            LengthComponent.empty()
                .clearValue()
                .optional(),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icon=mdi-close-circle\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            LengthComponent.empty()
                .setValue(
                    Optional.of(LENGTH)
                ),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1px] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValuePixel() {
        this.treePrintAndCheck(
            LengthComponent.empty()
                .setStringValue(
                    Optional.of(
                        LENGTH.text()
                    )
                ),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1px] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueNoneLength() {
        this.treePrintAndCheck(
            LengthComponent.empty()
                .setStringValue(
                    Optional.of(
                        Length.none()
                            .text()
                    )
                ),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [none] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            LengthComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!] icon=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid number length \"Invalid123!\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public LengthComponent createComponent() {
        return LengthComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<LengthComponent> type() {
        return LengthComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
