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

package walkingkooka.spreadsheet.dominokit.form;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueTextBoxComponentLikeTesting;
import walkingkooka.spreadsheet.validation.SpreadsheetValidationReference;

import java.util.Optional;

public final class SpreadsheetValidationReferenceComponentTest implements ValueTextBoxComponentLikeTesting<SpreadsheetValidationReferenceComponent, SpreadsheetValidationReference> {

    @Test
    public void testSetStringValueWithCell() {
        this.treePrintAndCheck(
            SpreadsheetValidationReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "A1"
                    )
                ),
            "SpreadsheetValidationReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A1] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithAbsoluteCell() {
        this.treePrintAndCheck(
            SpreadsheetValidationReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "$A$1"
                    )
                ),
            "SpreadsheetValidationReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [$A$1] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithCellRange() {
        this.treePrintAndCheck(
            SpreadsheetValidationReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "A1:B2"
                    )
                ),
            "SpreadsheetValidationReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A1:B2] icon=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character ':' at 2\n"
        );
    }

    @Test
    public void testSetStringValueWithLabel() {
        this.treePrintAndCheck(
            SpreadsheetValidationReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Label123"
                    )
                ),
            "SpreadsheetValidationReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Label123] icon=mdi-close-circle REQUIRED\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetValidationReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "SpreadsheetValidationReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!] icon=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character '!' at 10\n"
        );
    }

    @Test
    public void testSetStringValueWithColumn() {
        this.treePrintAndCheck(
            SpreadsheetValidationReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "$A"
                    )
                ),
            "SpreadsheetValidationReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [$A] icon=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character '$' at 0\n"
        );
    }

    @Test
    public void testSetStringValueWithRow() {
        this.treePrintAndCheck(
            SpreadsheetValidationReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "123"
                    )
                ),
            "SpreadsheetValidationReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [123] icon=mdi-close-circle REQUIRED\n" +
                "      Errors\n" +
                "        Invalid character '1' at 0\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetValidationReferenceComponent createComponent() {
        return SpreadsheetValidationReferenceComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetValidationReferenceComponent> type() {
        return SpreadsheetValidationReferenceComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
