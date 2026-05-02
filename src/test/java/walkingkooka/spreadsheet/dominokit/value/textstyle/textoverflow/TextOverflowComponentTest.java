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

package walkingkooka.spreadsheet.dominokit.value.textstyle.textoverflow;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLikeTesting;
import walkingkooka.tree.text.TextOverflow;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class TextOverflowComponentTest implements TextStylePropertyValueTextBoxComponentLikeTesting<TextOverflowComponent, TextOverflow> {
    
    private final static TextOverflow TEXT_OVERFLOW = TextOverflow.string("Custom");

    @Test
    public void testWithNullIdPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> TextOverflowComponent.with(null)
        );
    }

    @Test
    public void testWithEmptyIdPrefixFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> TextOverflowComponent.with("")
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "TextOverflowComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testClearValueTextOverflow() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .clearValue(),
            "TextOverflowComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(TEXT_OVERFLOW)
                ),
            "TextOverflowComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [\"Custom\"] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueClip() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(TextOverflow.CLIP)
                ),
            "TextOverflowComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [clip] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueEllipsis() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(TextOverflow.ELLIPSIS)
                ),
            "TextOverflowComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [ellipsis] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n"
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
            "TextOverflowComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n" +
                "      Errors\n" +
                "        Invalid text\n"
        );
    }

    // addValueWatcher..................................................................................................

    @Test
    public void testAddValueWatcher() {
        this.fired = null;

        final TextOverflowComponent component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<>() {
                @Override
                public void onValue(final Optional<TextOverflow> value) {
                    TextOverflowComponentTest.this.fired = value.orElse(null);
                }
            }
        );

        component.setValue(
            Optional.of(TEXT_OVERFLOW)
        );

        this.checkEquals(
            TEXT_OVERFLOW,
            this.fired,
            "fired value"
        );
    }

    private TextOverflow fired;

    // ValueComponent...................................................................................................

    @Override
    public TextOverflowComponent createComponent() {
        return TextOverflowComponent.with("TestIdPrefix123-");
    }

    // HasName..........................................................................................................

    @Test
    public void testName() {
        this.nameAndCheck(
            this.createComponent(),
            TextStylePropertyName.TEXT_OVERFLOW
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextOverflowComponent> type() {
        return TextOverflowComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
