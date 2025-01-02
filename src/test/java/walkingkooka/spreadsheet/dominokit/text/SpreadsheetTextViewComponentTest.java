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

package walkingkooka.spreadsheet.dominokit.text;

import elemental2.dom.HTMLDivElement;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;

import java.util.Optional;

public final class SpreadsheetTextViewComponentTest implements ValueComponentTesting<HTMLDivElement, String, SpreadsheetTextViewComponent> {

    @Test
    public void testClearValueAndTreePrint() {
        this.treePrintAndCheck(
                SpreadsheetTextViewComponent.empty()
                        .clearValue(),
               "SpreadsheetTextViewComponent\n" +
                       "  \"\"\n"
        );
    }

    @Test
    public void testSetValueAndTreePrint() {
        this.treePrintAndCheck(
                SpreadsheetTextViewComponent.empty()
                        .setValue(
                                Optional.of("Hello123")
                        ),
                "SpreadsheetTextViewComponent\n" +
                        "  \"Hello123\"\n"
        );
    }

    @Override
    public SpreadsheetTextViewComponent createComponent() {
        return SpreadsheetTextViewComponent.empty();
    }

    // class............................................................................................................

    @Override
    public Class<SpreadsheetTextViewComponent> type() {
        return SpreadsheetTextViewComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
