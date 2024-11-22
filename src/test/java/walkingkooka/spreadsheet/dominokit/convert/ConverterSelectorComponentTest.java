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

package walkingkooka.spreadsheet.dominokit.convert;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.convert.provider.ConverterSelector;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;

import java.util.Optional;

public final class ConverterSelectorComponentTest implements ValueComponentTesting<HTMLFieldSetElement, ConverterSelector, ConverterSelectorComponent> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
                ConverterSelectorComponent.empty()
                        .setStringValue(
                                Optional.of("hello")
                        ),
                "ConverterSelectorComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [hello]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
                ConverterSelectorComponent.empty()
                        .setStringValue(
                                Optional.of("!@#")
                        ),
                "ConverterSelectorComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [!@#]\n" +
                        "      Errors\n" +
                        "        Invalid character '!' at 0\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<ConverterSelectorComponent> type() {
        return ConverterSelectorComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
