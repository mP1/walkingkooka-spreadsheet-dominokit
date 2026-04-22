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
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class LengthComponentTest implements ValueTextBoxComponentLikeTesting<LengthComponent, Length<?>> {

    private final static Length<?> LENGTH = Length.pixel(1.0);

    @Test
    public void testWithNullTextStylePropertyNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> LengthComponent.with(null)
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testClearValueOptional() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue()
                .optional(),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(LENGTH)
                ),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1px] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValuePixel() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        LENGTH.text()
                    )
                ),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1px] icons=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueNoneLength() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        Length.none()
                            .text()
                    )
                ),
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [none] icons=mdi-close-circle REQUIRED\n"
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
            "LengthComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!] icons=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character 'I' at 0\n"
        );
    }

    @Test
    public void testAddValueWatcher() {
        this.fired = null;

        final LengthComponent component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<Length<?>>() {
                @Override
                public void onValue(Optional<Length<?>> value) {
                    LengthComponentTest.this.fired = value.orElse(null);
                }
            }
        );

        component.setValue(
            Optional.of(LENGTH)
        );

        this.checkEquals(
            LENGTH,
            this.fired,
            "fired value"
        );
    }

    private Length<?> fired;

    // ValueComponent...................................................................................................

    @Override
    public LengthComponent createComponent() {
        return LengthComponent.with(
            TextStylePropertyName.HEIGHT
        );
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
