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

package walkingkooka.spreadsheet.dominokit.format;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;

import java.util.Optional;

public final class SpreadsheetFormatterNameComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetFormatterName, SpreadsheetFormatterNameComponent> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetFormatterNameComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Hello"
                    )
                ),
            "SpreadsheetFormatterNameComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [Hello]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetFormatterNameComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "SpreadsheetFormatterNameComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [Invalid123!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 10\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetFormatterNameComponent createComponent() {
        return SpreadsheetFormatterNameComponent.empty();
    }

    // class............................................................................................................
    @Override
    public Class<SpreadsheetFormatterNameComponent> type() {
        return SpreadsheetFormatterNameComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
