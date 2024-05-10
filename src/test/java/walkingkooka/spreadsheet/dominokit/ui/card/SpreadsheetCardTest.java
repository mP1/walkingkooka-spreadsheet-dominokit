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

package walkingkooka.spreadsheet.dominokit.ui.card;

import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.ui.textbox.SpreadsheetTextBox;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

public final class SpreadsheetCardTest implements ClassTesting<SpreadsheetCard>,
        TreePrintableTesting {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                SpreadsheetCard.empty()
                        .appendChild(
                                SpreadsheetTextBox.empty()
                                        .setValue(
                                                Optional.of("Value123")
                                        )
                        ),
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [Value123]\n"
        );
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetCard> type() {
        return SpreadsheetCard.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
