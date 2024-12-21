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
import walkingkooka.reflect.JavaVisibility;

import java.util.Optional;

public final class SpreadsheetIntegerBoxTest implements ValueComponentTesting<HTMLFieldSetElement, Integer, SpreadsheetIntegerBox> {

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
                SpreadsheetIntegerBox.empty()
                        .setId("id123")
                        .setValue(
                                Optional.of(
                                        123
                                )
                        ),
                "SpreadsheetIntegerBox\n" +
                        "  [123] id=id123\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public SpreadsheetIntegerBox createComponent() {
        return SpreadsheetIntegerBox.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<SpreadsheetIntegerBox> type() {
        return SpreadsheetIntegerBox.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
