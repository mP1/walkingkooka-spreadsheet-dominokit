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

package walkingkooka.spreadsheet.dominokit.ui.parsertextbox;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ui.viewport.ValueComponentTesting;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ParserSpreadsheetTextBoxTest implements ValueComponentTesting<HTMLFieldSetElement, SpreadsheetCellReference, ParserSpreadsheetTextBox<SpreadsheetCellReference>> {

    @Test
    public void testWithNullFunctionFails() {
        assertThrows(
                NullPointerException.class,
                () -> ParserSpreadsheetTextBox.with(null)
        );
    }

    @Test
    public void testSetValue() {
        this.checkEquals(
                Optional.of("AB12"),
                ParserSpreadsheetTextBox.with(SpreadsheetSelection::parseCell)
                        .setValue(
                                Optional.of(
                                        SpreadsheetSelection.parseCell("AB12")
                                )
                        ).stringValue()
        );
    }

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                ParserSpreadsheetTextBox.with(SpreadsheetSelection::parseCell)
                        .setId("id123")
                        .setValue(
                                Optional.of(
                                        SpreadsheetSelection.parseCell("AB12")
                                )
                        ),
                "ParserSpreadsheetTextBox\n" +
                        "  SpreadsheetTextBox\n" +
                        "    [AB12] id=id123\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<ParserSpreadsheetTextBox<SpreadsheetCellReference>> type() {
        return Cast.to(ParserSpreadsheetTextBox.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
