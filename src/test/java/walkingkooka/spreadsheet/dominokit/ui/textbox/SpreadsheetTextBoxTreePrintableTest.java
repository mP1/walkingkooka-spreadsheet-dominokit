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

package walkingkooka.spreadsheet.dominokit.ui.textbox;

import elemental2.dom.EventListener;
import elemental2.dom.HTMLFieldSetElement;
import org.dominokit.domino.ui.utils.HasChangeListeners.ChangeListener;
import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.dominokit.ui.ValueComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetTextBoxTreePrintableTest implements TreePrintableTesting {

    @Test
    public void testLabel() {
        this.treePrintAndCheck(
                SpreadsheetTextBox.empty()
                        .setLabel("Label123"),
                "SpreadsheetTextBox\n" +
                        "  Label123 []\n"
        );
    }

    @Test
    public void testLabelAndValue() {
        this.treePrintAndCheck(
                SpreadsheetTextBox.empty()
                        .setLabel("Label123")
                        .setValue(Optional.of("Value456")),
                "SpreadsheetTextBox\n" +
                        "  Label123 [Value456]\n"
        );
    }

    @Test
    public void testValue() {
        this.treePrintAndCheck(
                SpreadsheetTextBox.empty()
                        .setValue(Optional.of("Value456")),
                "SpreadsheetTextBox\n" +
                        "  [Value456]\n"
        );
    }

    @Test
    public void testTreePrintAlternateValues() {
        class TestValueComponent implements ValueComponent<HTMLFieldSetElement, String, walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox>,
                SpreadsheetTextBoxTreePrintable<walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox, String> {
            @Override
            public SpreadsheetTextBox setId(String id) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String id() {
                return "id123";
            }

            @Override
            public SpreadsheetTextBox setLabel(String label) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String label() {
                return "Label123";
            }

            @Override
            public SpreadsheetTextBox setValue(final Optional<String> value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox focus() {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox optional() {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox required() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isRequired() {
                return true;
            }

            @Override
            public SpreadsheetTextBox validate() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isDisabled() {
                return true;
            }

            @Override
            public SpreadsheetTextBox setDisabled(final boolean disabled) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox addChangeListener(final ChangeListener<Optional<String>> listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox addFocusListener(final EventListener listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox addKeydownListener(final EventListener listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox addKeyupListener(final EventListener listener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox alwaysShowHelperText() {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox setHelperText(final Optional<String> text) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<String> helperText() {
                return Optional.of("HelperText123");
            }

            @Override
            public SpreadsheetTextBox hideMarginBottom() {
                throw new UnsupportedOperationException();
            }

            @Override
            public SpreadsheetTextBox removeBorders() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void treePrintAlternateValues(final IndentingPrinter printer) {
                printer.indent();
                {
                    printer.println("Value1a");
                    printer.println("Value2b");
                    printer.println("Value3c");
                }
                printer.outdent();
            }

            @Override
            public Optional<String> value() {
                return Optional.of("Value123");
            }

            @Override
            public HTMLFieldSetElement element() {
                throw new UnsupportedOperationException();
            }
        }

        this.treePrintAndCheck(
                new TestValueComponent(),
                "TestValueComponent\n" +
                        "  Label123 [Value123] id=id123 helperText=\"HelperText123\" DISABLED REQUIRED\n" +
                        "    Value1a\n" +
                        "    Value2b\n" +
                        "    Value3c\n"
        );
    }

    @Test
    public void testLabelValueIdDisabledRequiredHelperText() {
        this.treePrintAndCheck(
                SpreadsheetTextBox.empty()
                        .setLabel("Label123")
                        .setValue(Optional.of("Value456"))
                        .setId("id987")
                        .setDisabled(true)
                        .required()
                        .setHelperText(Optional.of("HelperText789"))
                ,
                "SpreadsheetTextBox\n" +
                        "  Label123 [Value456] id=id987 helperText=\"HelperText789\" DISABLED REQUIRED\n"
        );
    }
}
