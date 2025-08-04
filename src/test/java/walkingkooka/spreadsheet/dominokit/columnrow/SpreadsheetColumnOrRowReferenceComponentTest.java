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

package walkingkooka.spreadsheet.dominokit.columnrow;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;

import java.util.Optional;

public final class SpreadsheetColumnOrRowReferenceComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetColumnOrRowReferenceOrRange, SpreadsheetColumnOrRowReferenceComponent> {

    @Test
    public void testSetStringValueWithColumn() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "AB"
                    )
                ),
            "SpreadsheetColumnOrRowReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [AB]\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidColumn() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "A1!"
                    )
                ),
            "SpreadsheetColumnOrRowReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [A1!]\n" +
                "      Errors\n" +
                "        Invalid character '1' at 1\n"
        );
    }

    @Test
    public void testSetStringValueWithColumnRangeFails() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "C:D"
                    )
                ),
            "SpreadsheetColumnOrRowReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [C:D]\n" +
                "      Errors\n" +
                "        Invalid character ':' at 1\n"
        );
    }

    @Test
    public void testSetStringValueWithRow() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "1"
                    )
                ),
            "SpreadsheetColumnOrRowReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [1]\n"
        );
    }

    @Test
    public void testSetStringValueWithRowRangeInvalid() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "1:2"
                    )
                ),
            "SpreadsheetColumnOrRowReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [1:2]\n" +
                "      Errors\n" +
                "        Invalid character ':' at 1\n"
        );
    }

    @Test
    public void testSetStringValueWithCellFails() {
        this.treePrintAndCheck(
            SpreadsheetColumnOrRowReferenceComponent.empty()
                .setStringValue(
                    Optional.of(
                        "Z9"
                    )
                ),
            "SpreadsheetColumnOrRowReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [Z9]\n" +
                "      Errors\n" +
                "        Invalid character '9' at 1\n"
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
