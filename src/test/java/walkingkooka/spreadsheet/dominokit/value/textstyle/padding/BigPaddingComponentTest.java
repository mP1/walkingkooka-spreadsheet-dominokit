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
import walkingkooka.spreadsheet.dominokit.value.textstyle.BigMarginOrPaddingComponentTesting;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.Padding;

import java.util.Optional;

public final class BigPaddingComponentTest implements BigMarginOrPaddingComponentTesting<Padding, BigPaddingComponent> {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createComponent(),
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Padding.parse("top: 1px; right: 2px; bottom: 3px; left: 4px;")
                    )
                ),
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n" +
                "                    Padding\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          padding-bottom=3px\n" +
                "                          padding-left=4px\n" +
                "                          padding-right=2px\n" +
                "                          padding-top=1px\n"
        );
    }

    @Test
    public void testTopSetStringValueFails() {
        final BigPaddingComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.top()
            .setStringValue(
                Optional.of(
                    "!invalid"
                )
            );

        this.treePrintAndCheck(
            component,
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [!invalid] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n" +
                "                    Padding\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          padding-bottom=3px\n" +
                "                          padding-left=4px\n" +
                "                          padding-right=2px\n" +
                "                          padding-top=1px\n"
        );
    }

    @Test
    public void testRightSetStringValueFails() {
        final BigPaddingComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.right()
            .setStringValue(
                Optional.of(
                    "!invalid"
                )
            );

        this.treePrintAndCheck(
            component,
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [!invalid] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n" +
                "                    Padding\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          padding-bottom=3px\n" +
                "                          padding-left=4px\n" +
                "                          padding-right=2px\n" +
                "                          padding-top=1px\n"
        );
    }

    @Test
    public void testBottomSetStringValueFails() {
        final BigPaddingComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.bottom()
            .setStringValue(
                Optional.of(
                    "!invalid"
                )
            );

        this.treePrintAndCheck(
            component,
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [!invalid] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n" +
                "                    Padding\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          padding-bottom=3px\n" +
                "                          padding-left=4px\n" +
                "                          padding-right=2px\n" +
                "                          padding-top=1px\n"
        );
    }

    @Test
    public void testLeftSetStringValueFails() {
        final BigPaddingComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.left()
            .setStringValue(
                Optional.of(
                    "!invalid"
                )
            );

        this.treePrintAndCheck(
            component,
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [!invalid] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "                Errors\n" +
                "                  Invalid character '!' at 0\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n" +
                "                    Padding\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          padding-bottom=3px\n" +
                "                          padding-left=4px\n" +
                "                          padding-right=2px\n" +
                "                          padding-top=1px\n"
        );
    }

    @Test
    public void testAllSetStringValueFails() {
        final BigPaddingComponent component = this.createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue();
        component.all.setStringValue(
            Optional.of(
                "!invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [!invalid] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n" +
                "              Errors\n" +
                "                Invalid character '!' at 0\n"
        );
    }

    @Override
    public BigPaddingComponent createComponent() {
        return BigPaddingComponent.with("testIdPrefix123-");
    }

    private BigPaddingComponent createComponentAndTopSetValueRightSetValueBottomSetValueLeftSetValue() {
        final BigPaddingComponent component = this.createComponent();

        component.top()
            .setValue(
                Optional.of(
                    Length.parse("1px")
                )
            );
        component.right()
            .setValue(
                Optional.of(
                    Length.parse("2px")
                )
            );
        component.bottom()
            .setValue(
                Optional.of(
                    Length.parse("3px")
                )
            );
        component.left()
            .setValue(
                Optional.of(
                    Length.parse("4px")
                )
            );

        this.treePrintAndCheck(
            component,
            "BigPaddingComponent\n" +
                "  FormElementComponent\n" +
                "    label\n" +
                "      Padding\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        PaddingTopComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Top [1px] icons=mdi-close-circle id=testIdPrefix123-paddingTop-TextBox\n" +
                "        PaddingRightComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Right [2px] icons=mdi-close-circle id=testIdPrefix123-paddingRight-TextBox\n" +
                "        PaddingBottomComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Bottom [3px] icons=mdi-close-circle id=testIdPrefix123-paddingBottom-TextBox\n" +
                "        PaddingLeftComponent\n" +
                "          LengthComponent\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Left [4px] icons=mdi-close-circle id=testIdPrefix123-paddingLeft-TextBox\n" +
                "        PaddingComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              All [1px 2px 3px 4px] icons=mdi-close-circle id=testIdPrefix123-padding-TextBox\n" +
                "                innerRight\n" +
                "                  PaddingBoxComponent\n" +
                "                    Padding\n" +
                "                      ALL\n" +
                "                        TextStyle\n" +
                "                          padding-bottom=3px\n" +
                "                          padding-left=4px\n" +
                "                          padding-right=2px\n" +
                "                          padding-top=1px\n"
        );

        return component;
    }

    @Override
    public Class<BigPaddingComponent> type() {
        return BigPaddingComponent.class;
    }
}