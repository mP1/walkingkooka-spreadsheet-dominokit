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

package walkingkooka.spreadsheet.dominokit.flex;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextBox;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetFlexLayoutTest implements ClassTesting<SpreadsheetFlexLayout>,
        TreePrintableTesting {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                SpreadsheetFlexLayout.row()
                        .appendChild(
                                SpreadsheetTextBox.empty()
                                        .setValue(
                                                Optional.of("Value111")
                                        )
                        ).appendChild(
                                SpreadsheetTextBox.empty()
                                        .setValue(
                                                Optional.of("Value222")
                                        )
                        ),
                "SpreadsheetFlexLayout\n" +
                        "  ROW\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [Value111]\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [Value222]\n"
        );
    }

    @Test
    public void testTreePrintIncludesId() {
        this.treePrintAndCheck(
                SpreadsheetFlexLayout.row()
                        .setId("Id123")
                        .appendChild(
                                SpreadsheetTextBox.empty()
                                        .setValue(
                                                Optional.of("Value111")
                                        )
                        ).appendChild(
                                SpreadsheetTextBox.empty()
                                        .setValue(
                                                Optional.of("Value222")
                                        )
                        ),
                "SpreadsheetFlexLayout\n" +
                        "  ROW\n" +
                        "    id=Id123\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [Value111]\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [Value222]\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetFlexLayout> type() {
        return SpreadsheetFlexLayout.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
