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

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.value.ValueWatcher;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.BoxEdge;

import java.util.Optional;

public final class BorderTopComponentTest extends BorderSharedComponentTestCase<BorderTopComponent> {

    @Test
    public void testSetLabelFromPropertyName() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabelFromPropertyName(),
            "BorderTopComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      Border Top [] icons=mdi-close-circle id=TestIdPrefix123-borderTop-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BorderTopComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-borderTop-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Border.parse("black solid 1px")
                    )
                ),
            "BorderTopComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [black SOLID 1px] icons=mdi-close-circle id=TestIdPrefix123-borderTop-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueWithBorderRight() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        BoxEdge.TOP.parseBorder("BLACK SOLID 1px")
                    )
                ).setValue(
                    Optional.of(
                        BoxEdge.RIGHT.parseBorder("WHITE DASHED 2px")
                    )
                ),
            "BorderTopComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-borderTop-TextBox REQUIRED\n"
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
            "BorderTopComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid] icons=mdi-close-circle id=TestIdPrefix123-borderTop-TextBox REQUIRED\n" +
                "      Errors\n" +
                "        Unknown color name\n"
        );
    }

    // addValueWatcher..................................................................................................

    @Test
    public void testAddValueWatcher() {
        this.fired = null;

        final BorderTopComponent component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<>() {
                @Override
                public void onValue(final Optional<Border> value) {
                    BorderTopComponentTest.this.fired = value.orElse(null);
                }
            }
        );

        final Border value = BoxEdge.TOP.parseBorder("black solid 1px");

        component.setValue(
            Optional.of(value)
        );

        this.checkEquals(
            value,
            this.fired,
            "fired value"
        );
    }

    private Border fired;

    // filterTest.......................................................................................................

    @Test
    public void testFilterTestWithBorder() {
        this.filterTestAndCheck(
            this.createComponent(),
            "BORDER",
            true
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public BorderTopComponent createComponent() {
        return BorderTopComponent.with("TestIdPrefix123-");
    }

    // class............................................................................................................

    @Override
    public Class<BorderTopComponent> type() {
        return BorderTopComponent.class;
    }
}
