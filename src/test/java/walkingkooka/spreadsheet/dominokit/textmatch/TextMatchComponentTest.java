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

package walkingkooka.spreadsheet.dominokit.textmatch;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.predicate.Predicates;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.ValueComponentTesting;
import walkingkooka.text.CaseSensitivity;

import java.util.Optional;
import java.util.function.Predicate;

public final class TextMatchComponentTest implements ValueComponentTesting<HTMLFieldSetElement, Predicate<CharSequence>, TextMatchComponent> {

    @Test
    public void testTreePrintWithoutValue() {
        this.treePrintAndCheck(
                TextMatchComponent.empty(),
                "TextMatchComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      []\n"
        );
    }

    @Test
    public void testTreePrintWithAnyValue() {
        this.treePrintAndCheck(
                TextMatchComponent.empty()
                        .setValue(
                                Optional.of(
                                        Predicates.globPatterns(
                                                "starts* ends* *contains*",
                                                CaseSensitivity.INSENSITIVE,
                                                '\\'
                                        )
                                )
                        ),
                "TextMatchComponent\n" +
                        "  ValueSpreadsheetTextBox\n" +
                        "    SpreadsheetTextBox\n" +
                        "      [starts* ends* *contains*]\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<TextMatchComponent> type() {
        return TextMatchComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}