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

package walkingkooka.spreadsheet.dominokit.value.textstyle.filter;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycleMatcherTesting;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class TextStylePropertyFilterComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, TextStylePropertyFilter, TextStylePropertyFilterComponent>,
    ComponentLifecycleMatcherTesting {

    @Test
    public void testTreePrintable() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "TextStylePropertyFilterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-filter-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testOptional() {
        this.treePrintAndCheck(
            this.createComponent()
                .optional(),
            "TextStylePropertyFilterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-filter-TextBox\n"
        );
    }

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "TextStylePropertyFilterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] icons=mdi-close-circle id=TestIdPrefix123-filter-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetValueString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        TextStylePropertyFilter.with("Hello")
                    )
                ),
            "TextStylePropertyFilterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Hello] icons=mdi-close-circle id=TestIdPrefix123-filter-TextBox REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValue() {
        final TextStylePropertyFilterComponent component = this.createComponent();
        component.filter.setStringValue(
            Optional.of(
                "Hello world text"
            )
        );

        this.treePrintAndCheck(
            component,
            "TextStylePropertyFilterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Hello world text] icons=mdi-close-circle id=TestIdPrefix123-filter-TextBox REQUIRED\n"
        );
    }

    @Override
    public TextStylePropertyFilterComponent createComponent() {
        return TextStylePropertyFilterComponent.with(
            "TestIdPrefix123-"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextStylePropertyFilterComponent> type() {
        return TextStylePropertyFilterComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
