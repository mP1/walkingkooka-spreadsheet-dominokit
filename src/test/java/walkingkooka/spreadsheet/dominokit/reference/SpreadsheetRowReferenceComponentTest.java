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
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;

import java.util.Optional;

public final class SpreadsheetRowReferenceComponentTest implements ValueComponentTesting<HTMLFieldSetElement, SpreadsheetRowReference, SpreadsheetRowReferenceComponent> {

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
                SpreadsheetRowReferenceComponent.empty()
                        .setStringValue(
                                Optional.of(
                                        "Hello"
                                )
                        ),
                "SpreadsheetRowReferenceComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [Hello]\n" +
                        "      Errors\n" +
                        "        Invalid character 'H' at 0\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
                SpreadsheetRowReferenceComponent.empty()
                        .setStringValue(
                                Optional.of(
                                        "Invalid123!"
                                )
                        ),
                "SpreadsheetRowReferenceComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [Invalid123!]\n" +
                        "      Errors\n" +
                        "        Invalid character 'I' at 0\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetRowReferenceComponent> type() {
        return SpreadsheetRowReferenceComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
