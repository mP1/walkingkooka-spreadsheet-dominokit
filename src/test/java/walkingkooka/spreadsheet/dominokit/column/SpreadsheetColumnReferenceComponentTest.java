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

package walkingkooka.spreadsheet.dominokit.column;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;

import java.util.Optional;

public final class SpreadsheetColumnReferenceComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetColumnReference, SpreadsheetColumnReferenceComponent> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetColumnReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "AB"
                    )
                ),
            "SpreadsheetColumnReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [AB]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetColumnReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "SpreadsheetColumnReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!]\n" +
                "      Errors\n" +
                "        Invalid column \"Invalid\" not between \"A\" and \"XFE\"\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetColumnReferenceComponent createComponent() {
        return SpreadsheetColumnReferenceComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetColumnReferenceComponent> type() {
        return SpreadsheetColumnReferenceComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
