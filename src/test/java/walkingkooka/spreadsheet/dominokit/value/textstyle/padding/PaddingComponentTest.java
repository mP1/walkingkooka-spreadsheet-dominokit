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

package walkingkooka.spreadsheet.dominokit.value.textstyle.padding;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyValueTextBoxComponentLikeTesting;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Padding;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PaddingComponentTest implements TextStylePropertyValueTextBoxComponentLikeTesting<PaddingComponent, Padding> {

    private final static Padding PADDING = Padding.parse("1px 2px -3px 4.5px");

    @Test
    public void testWithNullIdPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> PaddingComponent.with(null)
        );
    }

    @Test
    public void testWithEmptyIdPrefixFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> PaddingComponent.with("")
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-padding-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testOptionalClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .clearValue(),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-padding-TextBox\n"
        );
    }

    @Test
    public void testClearValuePadding() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .clearValue(),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-padding-TextBox\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(PADDING)
                ),
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1px 2px -3px 4.5px] icons=mdi-close-circle id=TestIdPrefix123-padding-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithPaddingLeft() {
        final PaddingComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                BoxEdge.LEFT.setPadding(
                    Optional.of(
                        Length.pixel(12.5)
                    )
                )
            )
        );
        this.valueAndCheck(
            component,
            Padding.parse("left: 12.5px")
        );

        this.treePrintAndCheck(
            component,
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [left: 12.5px] icons=mdi-close-circle id=TestIdPrefix123-padding-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithPaddingRight() {
        final PaddingComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                BoxEdge.RIGHT.setPadding(
                    Optional.of(
                        Length.pixel(12.5)
                    )
                )
            )
        );
        this.valueAndCheck(
            component,
            Padding.parse("right: 12.5px")
        );

        this.treePrintAndCheck(
            component,
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [right: 12.5px] icons=mdi-close-circle id=TestIdPrefix123-padding-TextBox REQUIRED\n"
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
            "PaddingComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!] icons=mdi-close-circle id=TestIdPrefix123-padding-TextBox REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character 'I' at 0\n"
        );
    }

    // addValueWatcher..................................................................................................

    @Test
    public void testAddValueWatcher() {
        this.fired = null;

        final PaddingComponent component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<>() {
                @Override
                public void onValue(final Optional<Padding> value) {
                    PaddingComponentTest.this.fired = value.orElse(null);
                }
            }
        );

        component.setValue(
            Optional.of(PADDING)
        );

        this.checkEquals(
            PADDING,
            this.fired,
            "fired value"
        );
    }

    private Padding fired;

    // ValueComponent...................................................................................................

    @Override
    public PaddingComponent createComponent() {
        return PaddingComponent.with("TestIdPrefix123-");
    }

    // HasName..........................................................................................................

    @Test
    public void testName() {
        this.nameAndCheck(
            this.createComponent(),
            TextStylePropertyName.PADDING
        );
    }

    // class............................................................................................................

    @Override
    public Class<PaddingComponent> type() {
        return PaddingComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
