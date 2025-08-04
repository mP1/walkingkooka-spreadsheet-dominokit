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

package walkingkooka.spreadsheet.dominokit.value;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.HasText;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ValueSpreadsheetTextBoxTest implements FormValueComponentTesting<HTMLFieldSetElement, SpreadsheetCellReference, ValueSpreadsheetTextBox<SpreadsheetCellReference>> {

    @Test
    public void testWithNullParserFunctionFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueSpreadsheetTextBox.with(
                null,
                Object::toString
            )
        );
    }

    @Test
    public void testWithNullFormatterFunctionFails() {
        assertThrows(
            NullPointerException.class,
            () -> ValueSpreadsheetTextBox.with(
                (s) -> {
                    throw new UnsupportedOperationException();
                },
                null
            )
        );
    }

    @Test
    public void testSetValue() {
        this.checkEquals(
            Optional.of("AB12"),
            this.createComponent()
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseCell("AB12")
                    )
                ).stringValue()
        );
    }

    @Test
    public void testPrintTreeSetStringValueInvalidCharacter() {
        this.treePrintAndCheck(
            this.createComponent()
                .setId("id123")
                .setStringValue(
                    Optional.of(
                        "AB!12"
                    )
                ),
            "ValueSpreadsheetTextBox\n" +
                "  TextBoxComponent\n" +
                "    [AB!12] id=id123\n" +
                "    Errors\n" +
                "      Invalid character '!' at 2\n"
        );
    }

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createComponent()
                .setId("id123")
                .setValue(
                    Optional.of(
                        SpreadsheetSelection.parseCell("AB12")
                    )
                ),
            "ValueSpreadsheetTextBox\n" +
                "  TextBoxComponent\n" +
                "    [AB12] id=id123\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public ValueSpreadsheetTextBox<SpreadsheetCellReference> createComponent() {
        return ValueSpreadsheetTextBox.with(
            SpreadsheetSelection::parseCell,
            HasText::text
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ValueSpreadsheetTextBox<SpreadsheetCellReference>> type() {
        return Cast.to(ValueSpreadsheetTextBox.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
