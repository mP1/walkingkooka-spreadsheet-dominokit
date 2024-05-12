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

package walkingkooka.spreadsheet.dominokit.ui.spreadsheetname;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.ui.viewport.ValueComponentTesting;

import java.util.Optional;

public final class SpreadsheetNameComponentTest implements ValueComponentTesting<HTMLFieldSetElement, SpreadsheetName, SpreadsheetNameComponent> {

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
                SpreadsheetNameComponent.empty()
                        .setValue(
                                Optional.of(
                                        SpreadsheetName.with("Spreadsheet123")
                                )
                        ),
                "SpreadsheetNameComponent\n" +
                        "  ParserSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [Spreadsheet123]\n"
        );
    }

    @Override
    public Class<SpreadsheetNameComponent> type() {
        return SpreadsheetNameComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
