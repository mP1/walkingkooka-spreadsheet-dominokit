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

package walkingkooka.spreadsheet.dominokit.character;

import elemental2.dom.HTMLFieldSetElement;
import org.junit.jupiter.api.Test;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;

import java.util.Optional;

public final class CharacterComponentTest implements FormValueComponentTesting<HTMLFieldSetElement, Character, CharacterComponent> {

    @Test
    public void testClearValue() {
        this.treePrintAndCheck(
            this.createComponent()
                .clearValue(),
            "CharacterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n" +
                "      Errors\n" +
                "        Empty \"Letter\"\n"
        );
    }

    @Test
    public void testSetStringValueWithEmptyString() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("")
                ),
            "CharacterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      []\n" +
                "      Errors\n" +
                "        Empty \"Letter\"\n"
        );
    }

    @Test
    public void testSetStringValueWithInvalidCharacter() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("1")
                ),
            "CharacterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [1]\n" +
                "      Errors\n" +
                "        Not a letter\n"
        );
    }

    @Test
    public void testSetStringValueWithLetter() {
        this.treePrintAndCheck(
            this.createComponent()
                .setStringValue(
                    Optional.of("A")
                ),
            "CharacterComponent\n" +
                "  ValueTextBoxComponent\n" +
                "    TextBoxComponent\n" +
                "      [A]\n"
        );
    }

    // ValueComponent...................................................................................................

    @Override
    public CharacterComponent createComponent() {
        return CharacterComponent.empty(
            "Letter",
            CharPredicates.letter(),
            "Not a letter"
        );
    }

    // class............................................................................................................

    @Override
    public Class<CharacterComponent> type() {
        return CharacterComponent.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
