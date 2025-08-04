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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.spreadsheet.expression.function.TextMatch;

import java.util.Optional;

public final class TextMatchComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, TextMatch, TextMatchComponent> {

    @Test
    public void testEmpty() {
        this.treePrintAndCheck(
            TextMatchComponent.empty(),
            "TextMatchComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n"
        );
    }

    @Test
    public void testSetStringValueWithWhitespace() {
        this.treePrintAndCheck(
            TextMatchComponent.empty()
                .setStringValue(
                    Optional.of("   ")
                ),
            "TextMatchComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [   ]\n"
        );
    }

    @Test
    public void testSetStringNotEmpty() {
        this.treePrintAndCheck(
            TextMatchComponent.empty()
                .setValue(
                    Optional.of(
                        TextMatch.parse(
                            "starts* ends* *contains*"
                        )
                    )
                ),
            "TextMatchComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [starts* ends* *contains*]\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public TextMatchComponent createComponent() {
        return TextMatchComponent.empty();
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
