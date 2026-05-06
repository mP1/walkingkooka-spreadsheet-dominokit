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

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextOverflow;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class BigTextOverflowComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, TextOverflow, BigTextOverflowComponent>,
    ComponentLifecycleMatcherTesting {

    @Test
    public void testTreePrintable() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigTextOverflowComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextBoxComponent\n" +
                "          Text [] icons=mdi-close-circle id=TestIdPrefix123-text-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/CLIP] id=TestIdPrefix123-clip-Link\n" +
                "              \"Ellipsis\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/ELLIPSIS] id=TestIdPrefix123-ellipsis-Link\n" +
                "        TextOverflowComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n" +
                "              Errors\n" +
                "                Empty \"text\"\n"
        );
    }

    @Test
    public void testOptional() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional(),
            "BigTextOverflowComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextBoxComponent\n" +
                "          Text [] icons=mdi-close-circle id=TestIdPrefix123-text-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/CLIP] id=TestIdPrefix123-clip-Link\n" +
                "              \"Ellipsis\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/ELLIPSIS] id=TestIdPrefix123-ellipsis-Link\n" +
                "        TextOverflowComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigTextOverflowComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextBoxComponent\n" +
                "          Text [] icons=mdi-close-circle id=TestIdPrefix123-text-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/CLIP] id=TestIdPrefix123-clip-Link\n" +
                "              \"Ellipsis\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/ELLIPSIS] id=TestIdPrefix123-ellipsis-Link\n" +
                "        TextOverflowComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n" +
                "              Errors\n" +
                "                Empty \"text\"\n"
        );
    }

    @Test
    public void testSetValueClip() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        TextOverflow.CLIP
                    )
                ),
            "BigTextOverflowComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextBoxComponent\n" +
                "          Text [] icons=mdi-close-circle id=TestIdPrefix123-text-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/CLIP] CHECKED id=TestIdPrefix123-clip-Link\n" +
                "              \"Ellipsis\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/ELLIPSIS] CHECKED id=TestIdPrefix123-ellipsis-Link\n" +
                "        TextOverflowComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [CLIP] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueEllipsis() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        TextOverflow.ELLIPSIS
                    )
                ),
            "BigTextOverflowComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextBoxComponent\n" +
                "          Text [] icons=mdi-close-circle id=TestIdPrefix123-text-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/CLIP] id=TestIdPrefix123-clip-Link\n" +
                "              \"Ellipsis\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/ELLIPSIS] id=TestIdPrefix123-ellipsis-Link\n" +
                "        TextOverflowComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [ELLIPSIS] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        TextOverflow.string("Hello")
                    )
                ),
            "BigTextOverflowComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextBoxComponent\n" +
                "          Text [Hello] icons=mdi-close-circle id=TestIdPrefix123-text-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/CLIP] id=TestIdPrefix123-clip-Link\n" +
                "              \"Ellipsis\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/ELLIPSIS] id=TestIdPrefix123-ellipsis-Link\n" +
                "        TextOverflowComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [\"Hello\"] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueInvalid() {
        final BigTextOverflowComponent component = this.createComponent();
        component.textOverflow.setStringValue(
            Optional.of(
                "!Invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigTextOverflowComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        TextBoxComponent\n" +
                "          Text [] icons=mdi-close-circle id=TestIdPrefix123-text-TextBox\n" +
                "        AnchorListComponent\n" +
                "          FlexLayoutComponent\n" +
                "            ROW\n" +
                "              \"Clip\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/CLIP] id=TestIdPrefix123-clip-Link\n" +
                "              \"Ellipsis\" [#/1/SpreadsheetName1/cell/A1/style/text-overflow/save/ELLIPSIS] id=TestIdPrefix123-ellipsis-Link\n" +
                "        TextOverflowComponent\n" +
                "          ValueTextBoxComponent\n" +
                "            TextBoxComponent\n" +
                "              Value [!Invalid] icons=mdi-close-circle id=TestIdPrefix123-textOverflow-TextBox REQUIRED\n" +
                "              Errors\n" +
                "                Invalid textOverflow: expected \"CLIP\", \"ELLIPSIS\" or quoted text got \"!Invalid\"\n"
        );
    }

    // filterTest.......................................................................................................

    @Test
    public void testFilterTestWithClip() {
        this.filterTestAndCheck(
            this.createComponent(),
            "CLip",
            true
        );
    }

    @Test
    public void testFilterTestWithEllipsis() {
        this.filterTestAndCheck(
            this.createComponent(),
            "ELLIPSis",
            true
        );
    }

    @Test
    public void testFilterTestWithText() {
        final BigTextOverflowComponent component = this.createComponent();
        component.text.setValue(
            Optional.of("Hello")
        );

        this.filterTestAndCheck(
            component,
            "Hello",
            true
        );
    }

    @Override
    public BigTextOverflowComponent createComponent() {
        return this.createComponent(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.of(
                    TextStylePropertyName.TEXT_OVERFLOW
                )
            )
        );
    }

    private BigTextOverflowComponent createComponent(final HistoryToken historyToken) {
        return BigTextOverflowComponent.with(
            "TestIdPrefix123-",
            new FakeBigTextOverflowComponentContext() {

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
    public Class<BigTextOverflowComponent> type() {
        return BigTextOverflowComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
