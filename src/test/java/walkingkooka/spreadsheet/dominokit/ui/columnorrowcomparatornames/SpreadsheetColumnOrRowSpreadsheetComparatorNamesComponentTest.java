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

package walkingkooka.spreadsheet.dominokit.ui.columnorrowcomparatornames;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponentTest implements ClassTesting<SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent>,
        TreePrintableTesting {

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
                SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
                        .setValue(
                                Optional.of(
                                        SpreadsheetColumnOrRowSpreadsheetComparatorNames.parse("A=text")
                                )
                        ),
                "SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent\n" +
                        "  ParserSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [A=text]\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent> type() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
