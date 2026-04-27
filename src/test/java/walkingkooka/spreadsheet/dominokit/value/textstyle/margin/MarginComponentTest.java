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

package walkingkooka.spreadsheet.dominokit.value.textstyle.margin;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.tree.text.BoxEdge;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Margin;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MarginComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, Margin, MarginComponent>,
    ValueTextBoxComponentLikeTesting<MarginComponent, Margin> {
    
    private final static Margin MARGIN = Margin.parse("1px 2px -3px 4.5px");

    @Test
    public void testWithNullIdPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> MarginComponent.with(null)
        );
    }

    @Test
    public void testWithEmptyIdPrefixFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> MarginComponent.with("")
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "MarginComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-margin-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testOptionalClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .clearValue(),
            "MarginComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-margin-TextBox\n"
        );
    }

    @Test
    public void testClearValueMargin() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional()
                .clearValue(),
            "MarginComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-margin-TextBox\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(MARGIN)
                ),
            "MarginComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1px 2px -3px 4.5px] icons=mdi-close-circle id=TestIdPrefix123-margin-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithMarginLeft() {
        final MarginComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                BoxEdge.LEFT.setMargin(
                    Optional.of(
                        Length.pixel(12.5)
                    )
                )
            )
        );
        this.valueAndCheck(
            component,
            Margin.parse("left: 12.5px")
        );

        this.treePrintAndCheck(
            component,
            "MarginComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [left: 12.5px] icons=mdi-close-circle id=TestIdPrefix123-margin-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithMarginRight() {
        final MarginComponent component = this.createComponent();

        component.setValue(
            Optional.of(
                BoxEdge.RIGHT.setMargin(
                    Optional.of(
                        Length.pixel(12.5)
                    )
                )
            )
        );
        this.valueAndCheck(
            component,
            Margin.parse("right: 12.5px")
        );

        this.treePrintAndCheck(
            component,
            "MarginComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [right: 12.5px] icons=mdi-close-circle id=TestIdPrefix123-margin-TextBox REQUIRED\n"
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
            "MarginComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!] icons=mdi-close-circle id=TestIdPrefix123-margin-TextBox REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character 'I' at 0\n"
        );
    }

    // addValueWatcher..................................................................................................

    @Test
    public void testAddValueWatcher() {
        this.fired = null;

        final MarginComponent component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<>() {
                @Override
                public void onValue(final Optional<Margin> value) {
                    MarginComponentTest.this.fired = value.orElse(null);
                }
            }
        );

        component.setValue(
            Optional.of(MARGIN)
        );

        this.checkEquals(
            MARGIN,
            this.fired,
            "fired value"
        );
    }

    private Margin fired;

    // ValueComponent...................................................................................................

    @Override
    public MarginComponent createComponent() {
        return MarginComponent.with("TestIdPrefix123-");
    }

    // HasName..........................................................................................................

    @Test
    public void testName() {
        this.nameAndCheck(
            this.createComponent(),
            TextStylePropertyName.MARGIN
        );
    }

    // class............................................................................................................

    @Override
    public Class<MarginComponent> type() {
        return MarginComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
