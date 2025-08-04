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

package walkingkooka.spreadsheet.dominokit.cell;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellRangeReferenceComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetCellRangeReference, SpreadsheetCellRangeReferenceComponent> {

    // with.............................................................................................................

    @Test
    public void testWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellRangeReferenceComponent.with(
                null // id
            )
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            SpreadsheetCellRangeReferenceComponent.with(
                "cell-range-id"
            ).clearValue(),
            "SpreadsheetCellRangeReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [] id=cell-range-id\n" +
                "      Errors\n" +
                "        Empty \"text\"\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseCellRange("A1:B2")
                    )
                ),
            "SpreadsheetCellRangeReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [A1:B2] id=cell-range-id\n"
        );
    }

    // setStringValue...................................................................................................

    @Test
    public void testSetStringValueWithInvalid() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "!invalid"
                    )
                ),
            "SpreadsheetCellRangeReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [!invalid] id=cell-range-id\n" +
                "      Errors\n" +
                "        Invalid character '!' at 0\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "C3:D4"
                    )
                ),
            "SpreadsheetCellRangeReferenceComponent\n" +
                "  ValueSpreadsheetTextBox\n" +
                "    TextBoxComponent\n" +
                "      [C3:D4] id=cell-range-id\n"
        );
    }

    // createComponent..................................................................................................

    @Override
    public SpreadsheetCellRangeReferenceComponent createComponent() {
        return SpreadsheetCellRangeReferenceComponent.with(
            "cell-range-id"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellRangeReferenceComponent> type() {
        return SpreadsheetCellRangeReferenceComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
