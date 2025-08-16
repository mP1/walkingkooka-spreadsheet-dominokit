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
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class SpreadsheetCellReferenceComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetCellReference, SpreadsheetCellReferenceComponent> {

    // with.............................................................................................................

    @Test
    public void testWithNullIdFails() {
        assertThrows(
            NullPointerException.class,
            () -> SpreadsheetCellReferenceComponent.with(
                null // id
            )
        );
    }

    // setValue.........................................................................................................

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "SpreadsheetCellReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [] id=cell-id\n" +
                "      Errors\n" +
                "        End of text at (1,1) expected CELL\n"
        );
    }

    @Test
    public void testSetValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.A1
                    )
                ),
            "SpreadsheetCellReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A1] id=cell-id\n"
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
            "SpreadsheetCellReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [!invalid] id=cell-id\n" +
                "      Errors\n" +
                "        Invalid character '!' at 0\n"
        );
    }

    @Test
    public void testSetStringValueWithCellRange() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "B2:C3"
                    )
                ),
            "SpreadsheetCellReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [B2:C3] id=cell-id\n" +
                "      Errors\n" +
                "        Invalid character ':' at 2\n"
        );
    }

    @Test
    public void testSetStringValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of(
                        "C3"
                    )
                ),
            "SpreadsheetCellReferenceComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [C3] id=cell-id\n"
        );
    }

    // createComponent..................................................................................................

    @Override
    public SpreadsheetCellReferenceComponent createComponent() {
        return SpreadsheetCellReferenceComponent.with(
            "cell-id"
        );
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetCellReferenceComponent> type() {
        return SpreadsheetCellReferenceComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
