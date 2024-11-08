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

package walkingkooka.spreadsheet.dominokit.card;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.HtmlElementComponentTesting;
import walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextBox;

import java.util.Optional;

public final class SpreadsheetCardTest implements HtmlElementComponentTesting<SpreadsheetCard, HTMLDivElement> {

    @Test
    public void testTreePrintWhenEmpty() {
        this.treePrintAndCheck(
                SpreadsheetCard.empty(),
                ""
        );
    }

    @Test
    public void testTreePrintWithoutTitle() {
        this.treePrintAndCheck(
                SpreadsheetCard.empty()
                        .appendChild(
                                walkingkooka.spreadsheet.dominokit.value.SpreadsheetTextBox.empty()
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

    @Test
    public void testTreePrintWithTitle() {
        this.treePrintAndCheck(
                SpreadsheetCard.empty()
                        .setTitle("CardTitle123")
                        .appendChild(
                                SpreadsheetTextBox.empty()
                                        .setValue(
                                                Optional.of("Value123")
                                        )
                        ),
                "SpreadsheetCard\n" +
                        "  Card\n" +
                        "    CardTitle123\n" +
                        "      SpreadsheetTextBox\n" +
                        "        [Value123]\n"
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
