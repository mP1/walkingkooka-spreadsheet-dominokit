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

package walkingkooka.spreadsheet.dominokit.reference;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;

import java.util.Optional;

public final class SpreadsheetColumnOrRowReferenceComponentTest implements ValueComponentTesting<HTMLFieldSetElement, SpreadsheetColumnOrRowReference, SpreadsheetColumnOrRowReferenceComponent> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "AB"
                    )
                ),
            "SpreadsheetColumnOrRowReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [AB]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "A1!"
                    )
                ),
            "SpreadsheetColumnOrRowReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    SpreadsheetTextBox\n" +
                "      [A1!]\n" +
                "      Errors\n" +
                "        Invalid character '1' at 1\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetColumnOrRowReferenceComponent createComponent() {
        return SpreadsheetColumnOrRowReferenceComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetColumnOrRowReferenceComponent> type() {
        return SpreadsheetColumnOrRowReferenceComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
