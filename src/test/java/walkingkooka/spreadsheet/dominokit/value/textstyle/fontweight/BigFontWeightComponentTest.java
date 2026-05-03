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

package walkingkooka.spreadsheet.dominokit.value.textstyle.fontweight;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.FontWeight;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class BigFontWeightComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, FontWeight, BigFontWeightComponent>,
    ComponentLifecycleMatcherTesting {

    @Test
    public void testTreePrintable() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigFontWeightComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Bold\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/BOLD] id=TestIdPrefix123-BOLD-Link\n" +
                "        \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/NORMAL] id=TestIdPrefix123-NORMAL-Link\n" +
                "        FontWeightComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Text [] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n" +
                "              Errors\n" +
                "                Empty \"text\"\n"
        );
    }

    @Test
    public void testOptional() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional(),
            "BigFontWeightComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Bold\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/BOLD] id=TestIdPrefix123-BOLD-Link\n" +
                "        \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/NORMAL] id=TestIdPrefix123-NORMAL-Link\n" +
                "        FontWeightComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Text [] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigFontWeightComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Bold\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/BOLD] id=TestIdPrefix123-BOLD-Link\n" +
                "        \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/NORMAL] id=TestIdPrefix123-NORMAL-Link\n" +
                "        FontWeightComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Text [] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n" +
                "              Errors\n" +
                "                Empty \"text\"\n"
        );
    }

    @Test
    public void testSetValueBold() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        FontWeight.BOLD
                    )
                ),
            "BigFontWeightComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Bold\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/BOLD] id=TestIdPrefix123-BOLD-Link\n" +
                "        \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/NORMAL] id=TestIdPrefix123-NORMAL-Link\n" +
                "        FontWeightComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Text [BOLD] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueNormal() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        FontWeight.NORMAL
                    )
                ),
            "BigFontWeightComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Bold\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/BOLD] CHECKED id=TestIdPrefix123-BOLD-Link\n" +
                "        \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/NORMAL] CHECKED id=TestIdPrefix123-NORMAL-Link\n" +
                "        FontWeightComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Text [NORMAL] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        FontWeight.parse("123")
                    )
                ),
            "BigFontWeightComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Bold\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/BOLD] id=TestIdPrefix123-BOLD-Link\n" +
                "        \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/NORMAL] id=TestIdPrefix123-NORMAL-Link\n" +
                "        FontWeightComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Text [123] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueInvalid() {
        final BigFontWeightComponent component = this.createComponent();
        component.fontWeight.setStringValue(
            Optional.of(
                "!Invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigFontWeightComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      ROW\n" +
                "        \"Bold\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/BOLD] id=TestIdPrefix123-BOLD-Link\n" +
                "        \"Normal\" [#/1/SpreadsheetName1/cell/A1/style/font-weight/save/NORMAL] id=TestIdPrefix123-NORMAL-Link\n" +
                "        FontWeightComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Text [!Invalid] icons=mdi-close-circle id=TestIdPrefix123-fontWeight-TextBox REQUIRED\n" +
                "              Errors\n" +
                "                Invalid character '!' at 0\n"
        );
    }

    @Override
    public BigFontWeightComponent createComponent() {
        return this.createComponent(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.of(
                    TextStylePropertyName.FONT_WEIGHT
                )
            )
        );
    }

    private BigFontWeightComponent createComponent(final HistoryToken historyToken) {
        return BigFontWeightComponent.with(
            "TestIdPrefix123-",
            new FakeBigFontWeightComponentContext() {

                @Override
                public HistoryToken historyToken() {
                    return historyToken;
                }

                @Override
                public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
                    return this.watchers.add(watcher);
                }

                private final HistoryTokenWatchers watchers = HistoryTokenWatchers.empty();
            }
        );
    }

    // class............................................................................................................

    @Override
    public Class<BigFontWeightComponent> type() {
        return BigFontWeightComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
