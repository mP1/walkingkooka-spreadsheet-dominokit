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

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers;
import walkingkooka.spreadsheet.dominokit.value.textstyle.TextStylePropertyComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.Border;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Optional;

public final class BigBorderComponentTest implements TextStylePropertyComponentTesting<HTMLFieldSetElement, Border, BigBorderComponent>,
    ComponentLifecycleMatcherTesting {

    @Test
    public void testTreePrintable() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigBorderComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        BorderComponent\n" +
                "          ALL\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Text [] icons=mdi-close-circle id=TestIdPrefix123-border-text-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testOptional() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional(),
            "BigBorderComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        BorderComponent\n" +
                "          ALL\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Text [] icons=mdi-close-circle id=TestIdPrefix123-border-text-TextBox\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "BigBorderComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        BorderComponent\n" +
                "          ALL\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Text [] icons=mdi-close-circle id=TestIdPrefix123-border-text-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        Border.parse("123")
                    )
                ),
            "BigBorderComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        BorderComponent\n" +
                "          ALL\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Text [123] icons=mdi-close-circle id=TestIdPrefix123-border-text-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueInvalid() {
        final BigBorderComponent component = this.createComponent();
        component.border.setStringValue(
            Optional.of(
                "!Invalid"
            )
        );

        this.treePrintAndCheck(
            component,
            "BigBorderComponent\n" +
                "  FormElementComponent\n" +
                "    FlexLayoutComponent\n" +
                "      COLUMN\n" +
                "        BorderComponent\n" +
                "          ALL\n" +
                "            ValueTextBoxComponent\n" +
                "              TextBoxComponent\n" +
                "                Text [!Invalid] icons=mdi-close-circle id=TestIdPrefix123-border-text-TextBox REQUIRED\n" +
                "                Errors\n" +
                "                  Invalid rgb \"!Invalid\"\n"
        );
    }

    @Override
    public BigBorderComponent createComponent() {
        return this.createComponent(
            HistoryToken.cellStyle(
                SPREADSHEET_ID,
                SPREADSHEET_NAME,
                SpreadsheetSelection.A1.setDefaultAnchor(),
                Optional.of(
                    TextStylePropertyName.BORDER
                ),
                NO_FILTER
            )
        );
    }

    private BigBorderComponent createComponent(final HistoryToken historyToken) {
        return BigBorderComponent.with(
            "TestIdPrefix123-border-",
            new FakeBigBorderComponentContext() {

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
    public Class<BigBorderComponent> type() {
        return BigBorderComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
