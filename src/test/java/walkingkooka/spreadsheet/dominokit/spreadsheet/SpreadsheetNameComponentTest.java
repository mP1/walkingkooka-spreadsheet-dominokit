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

package walkingkooka.spreadsheet.dominokit.spreadsheet;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.meta.SpreadsheetName;

import java.util.Optional;

public final class SpreadsheetNameComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetName, SpreadsheetNameComponent> {

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            SpreadsheetNameComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Spreadsheet123"
                    )
                ),
            "SpreadsheetNameComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Spreadsheet123]\n"
        );
    }

    @Test
    public void testSetValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetNameComponent.empty()
                .clearValue(),
            "SpreadsheetNameComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n" +
                "      Errors\n" +
                "        Empty \"name\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetNameComponent createComponent() {
        return SpreadsheetNameComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetNameComponent> type() {
        return SpreadsheetNameComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
