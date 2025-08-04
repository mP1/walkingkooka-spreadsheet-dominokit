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

package walkingkooka.spreadsheet.dominokit.spreadsheetexpressionreference;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;

import java.util.Optional;

public final class SpreadsheetExpressionReferenceComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetExpressionReference, SpreadsheetExpressionReferenceComponent> {

    @Test
    public void testSetStringValueWithCell() {
        this.treePrintAndCheck(
            SpreadsheetExpressionReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "A1"
                    )
                ),
            "SpreadsheetExpressionReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A1]\n"
        );
    }

    @Test
    public void testSetStringValueWithAbsoluteCell() {
        this.treePrintAndCheck(
            SpreadsheetExpressionReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "$A$1"
                    )
                ),
            "SpreadsheetExpressionReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [$A$1]\n"
        );
    }

    @Test
    public void testSetStringValueWithCellRange() {
        this.treePrintAndCheck(
            SpreadsheetExpressionReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "A1:B2"
                    )
                ),
            "SpreadsheetExpressionReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A1:B2]\n"
        );
    }

    @Test
    public void testSetStringValueWithLabel() {
        this.treePrintAndCheck(
            SpreadsheetExpressionReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Label123"
                    )
                ),
            "SpreadsheetExpressionReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Label123]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            SpreadsheetExpressionReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Invalid123!"
                    )
                ),
            "SpreadsheetExpressionReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [Invalid123!]\n" +
                "      Errors\n" +
                "        Invalid character '!' at 10\n"
        );
    }

    @Test
    public void testSetStringValueWithColumn() {
        this.treePrintAndCheck(
            SpreadsheetExpressionReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "$A"
                    )
                ),
            "SpreadsheetExpressionReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [$A]\n" +
                "      Errors\n" +
                "        Invalid character '$' at 0\n"
        );
    }

    @Test
    public void testSetStringValueWithRow() {
        this.treePrintAndCheck(
            SpreadsheetExpressionReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "123"
                    )
                ),
            "SpreadsheetExpressionReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [123]\n" +
                "      Errors\n" +
                "        Invalid character '1' at 0\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetExpressionReferenceComponent createComponent() {
        return SpreadsheetExpressionReferenceComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetExpressionReferenceComponent> type() {
        return SpreadsheetExpressionReferenceComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
