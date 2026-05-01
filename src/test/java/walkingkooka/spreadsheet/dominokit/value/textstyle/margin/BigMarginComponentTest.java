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
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Margin;

import java.util.Optional;

public final class BigMarginComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, Margin, BigMarginComponent> {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createComponent(),
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Margin.parse("top: 1px; right: 2px; bottom: 3px; left: 4px;")
                    )
                ),
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n" +
                "                    Margin\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          margin-bottom=3px\n" +
                "                          margin-left=4px\n" +
                "                          margin-right=2px\n" +
                "                          margin-top=1px\n"
        );
    }

    @Test
    public void testTopSetStringValueFails() {
        final BigMarginComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.top.setStringValue(
            Optional.of(
                "!invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [!invalid] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n" +
                "                    Margin\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          margin-bottom=3px\n" +
                "                          margin-left=4px\n" +
                "                          margin-right=2px\n" +
                "                          margin-top=1px\n"
        );
    }

    @Test
    public void testRightSetStringValueFails() {
        final BigMarginComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.right.setStringValue(
            Optional.of(
                "!invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [!invalid] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n" +
                "                    Margin\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          margin-bottom=3px\n" +
                "                          margin-left=4px\n" +
                "                          margin-right=2px\n" +
                "                          margin-top=1px\n"
        );
    }

    @Test
    public void testBottomSetStringValueFails() {
        final BigMarginComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.bottom.setStringValue(
            Optional.of(
                "!invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [!invalid] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n" +
                "                    Margin\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          margin-bottom=3px\n" +
                "                          margin-left=4px\n" +
                "                          margin-right=2px\n" +
                "                          margin-top=1px\n"
        );
    }

    @Test
    public void testLeftSetStringValueFails() {
        final BigMarginComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.left.setStringValue(
            Optional.of(
                "!invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [!invalid] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n" +
                "                    Margin\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          margin-bottom=3px\n" +
                "                          margin-left=4px\n" +
                "                          margin-right=2px\n" +
                "                          margin-top=1px\n"
        );
    }

    @Test
    public void testAllSetStringValueFails() {
        final BigMarginComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.margin.setStringValue(
            Optional.of(
                "!invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [!invalid] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n" +
                "              Errors\n" +
                "                Invalid character '!' at 0\n"
        );
    }

    @Override
    public BigMarginComponent createComponent() {
        return BigMarginComponent.with("testIdPrefix123-");
    }

    private BigMarginComponent createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue() {
        final BigMarginComponent component = this.createComponent();
        component.top.setValue(
            Optional.of(
                Length.parse("1px")
            )
        );
        component.right.setValue(
            Optional.of(
                Length.parse("2px")
            )
        );
        component.bottom.setValue(
            Optional.of(
                Length.parse("3px")
            )
        );
        component.left.setValue(
            Optional.of(
                Length.parse("4px")
            )
        );

        this.treePrintAndCheck(
            component,
            "BigMarginComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Margin\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        MarginTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-marginTop-TextBox\n" +
                "        MarginRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-marginRight-TextBox\n" +
                "        MarginBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-marginBottom-TextBox\n" +
                "        MarginLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-marginLeft-TextBox\n" +
                "        MarginComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-margin-TextBox\n" +
                "                innerRight\n" +
                "                  MarginBoxComponent\n" +
                "                    Margin\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          margin-bottom=3px\n" +
                "                          margin-left=4px\n" +
                "                          margin-right=2px\n" +
                "                          margin-top=1px\n"
        );

        return component;
    }

    @Override
    public Class<BigMarginComponent> type() {
        return BigMarginComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}