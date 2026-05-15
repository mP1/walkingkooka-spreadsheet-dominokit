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

public final class BorderComponentTest extends BorderSharedComponentTestCase<BorderComponent> {

    @Test
    public void testSetLabelFromPropertyName() {
        this.treePrintAndCheck(
            this.createComponent()
                .setLabelFromPropertyName(),
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      Border [] icons=mdi-close-circle id=TestIdPrefix123-border-TextBox REQUIRED\n" +
                "        innerRight\n" +
                "          BorderBoxComponent\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-border-TextBox REQUIRED\n" +
                "        innerRight\n" +
                "          BorderBoxComponent\n"
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
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [black SOLID 1px] icons=mdi-close-circle id=TestIdPrefix123-border-TextBox REQUIRED\n" +
                "        innerRight\n" +
                "          BorderBoxComponent\n" +
                "            Border\n" +
                "              ALL\n" +
                "                TextStyle\n" +
                "                  border-bottom-color=black\n" +
                "                  border-bottom-style=SOLID\n" +
                "                  border-bottom-width=1px\n" +
                "                  border-left-color=black\n" +
                "                  border-left-style=SOLID\n" +
                "                  border-left-width=1px\n" +
                "                  border-right-color=black\n" +
                "                  border-right-style=SOLID\n" +
                "                  border-right-width=1px\n" +
                "                  border-top-color=black\n" +
                "                  border-top-style=SOLID\n" +
                "                  border-top-width=1px\n"
        );
    }

    @Test
    public void testSetValueWithBorderTop() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        BoxEdge.RIGHT.parseBorder("BLACK SOLID 1px")
                    )
                ).setValue(
                    Optional.of(
                        BoxEdge.TOP.parseBorder("WHITE DASHED 2px")
                    )
                ),
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [top-color: white; top-style: DASHED; top-width: 2px;] icons=mdi-close-circle id=TestIdPrefix123-border-TextBox REQUIRED\n" +
                "        innerRight\n" +
                "          BorderBoxComponent\n" +
                "            Border\n" +
                "              ALL\n" +
                "                TextStyle\n" +
                "                  border-top-color=white\n" +
                "                  border-top-style=DASHED\n" +
                "                  border-top-width=2px\n"
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
            "BorderComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid] icons=mdi-close-circle id=TestIdPrefix123-border-TextBox REQUIRED\n" +
                "        innerRight\n" +
                "          BorderBoxComponent\n" +
                "      Errors\n" +
                "        Unknown color name\n"
        );
    }

    // addValueWatcher..................................................................................................

    @Test
    public void testAddValueWatcher() {
        this.fired = null;

        final BorderComponent component = this.createComponent();
        component.addValueWatcher(
            new ValueWatcher<>() {
                @Override
                public void onValue(final Optional<Border> value) {
                    BorderComponentTest.this.fired = value.orElse(null);
                }
            }
        );

        final Border value = BoxEdge.ALL.parseBorder("black solid 1px");

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
    public BorderComponent createComponent() {
        return BorderComponent.with("TestIdPrefix123-");
    }

    // class............................................................................................................

    @Override
    public Class<BorderComponent> type() {
        return BorderComponent.class;
    }
}
