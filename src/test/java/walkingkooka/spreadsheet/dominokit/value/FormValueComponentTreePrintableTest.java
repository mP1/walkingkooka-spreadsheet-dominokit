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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.TestHtmlElementComponent;
import walkingkooka.spreadsheet.dominokit.text.TextBoxComponent;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class FormValueComponentTreePrintableTest implements TreePrintableTesting {

    @Test
    public void testLabel() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123"),
            "TextBoxComponent\n" +
                "  Label123 []\n"
        );
    }

    @Test
    public void testLabelAndValue() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValue(Optional.of("Value456")),
            "TextBoxComponent\n" +
                "  Label123 [Value456]\n"
        );
    }

    @Test
    public void testValue() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setValue(Optional.of("Value456")),
            "TextBoxComponent\n" +
                "  [Value456]\n"
        );
    }

    @Test
    public void testSetCssText() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setValue(Optional.of("Value456"))
                .setCssText("background-color: lavender;"),
            "TextBoxComponent\n" +
                "  [Value456]\n"
        );
    }

    @Test
    public void testTreePrintAlternateValuesAndErrors() {
        class TestValueComponent implements FormValueComponent<HTMLFieldSetElement, String, TestValueComponent>,
            FormValueComponentTreePrintable<HTMLFieldSetElement, TestValueComponent, String>,
            TestHtmlElementComponent<HTMLFieldSetElement, TestValueComponent> {
            @Override
            public TestValueComponent setId(String id) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String id() {
                return "id123";
            }

            @Override
            public TestValueComponent setLabel(String label) {
                throw new UnsupportedOperationException();
            }

            @Override
            public String label() {
                return "Label123";
            }

            @Override
            public TestValueComponent setValue(final Optional<String> value) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Runnable addValueWatcher(final ValueWatcher<String> watcher) {
                Objects.requireNonNull(watcher, "watcher");

                throw new UnsupportedOperationException();
            }

            @Override
            public TestValueComponent focus() {
                throw new UnsupportedOperationException();
            }

            @Override
            public TestValueComponent blur() {
                throw new UnsupportedOperationException();
            }

            @Override
            public TestValueComponent optional() {
                throw new UnsupportedOperationException();
            }

            @Override
            public TestValueComponent required() {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isRequired() {
                return true;
            }

            @Override
            public TestValueComponent validate() {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<String> errors() {
                return Lists.of(
                    "Error1a",
                    "Error2b"
                );
            }

            @Override
            public TestValueComponent setErrors(final List<String> errors) {
                throw new UnsupportedOperationException();
            }

            @Override
            public boolean isDisabled() {
                return true;
            }

            @Override
            public TestValueComponent setDisabled(final boolean disabled) {
                throw new UnsupportedOperationException();
            }

            @Override
            public TestValueComponent alwaysShowHelperText() {
                throw new UnsupportedOperationException();
            }

            @Override
            public TestValueComponent setHelperText(final Optional<String> text) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<String> helperText() {
                return Optional.of("HelperText123");
            }

            @Override
            public TestValueComponent hideMarginBottom() {
                throw new UnsupportedOperationException();
            }

            @Override
            public TestValueComponent removeBorders() {
                throw new UnsupportedOperationException();
            }

            @Override
            public TestValueComponent removePadding() {
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
                "    Value3c\n" +
                "  Errors\n" +
                "    Error1a\n" +
                "    Error2b\n"
        );
    }

    @Test
    public void testLabelValueIdDisabledRequiredHelperText() {
        this.treePrintAndCheck(
            TextBoxComponent.empty()
                .setLabel("Label123")
                .setValue(Optional.of("Value456"))
                .setId("id987")
                .disabled()
                .required()
                .setHelperText(Optional.of("HelperText789"))
            ,
            "TextBoxComponent\n" +
                "  Label123 [Value456] id=id987 helperText=\"HelperText789\" DISABLED REQUIRED\n"
        );
    }
}
