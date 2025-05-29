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

import org.dominokit.domino.ui.utils.HasValidation.Validator;
import walkingkooka.InvalidTextLengthException;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.function.Function;

/**
 * A {@link Validator} which asserts that the text within the parent {@link CharacterComponent} is a single character and
 * that character is matched by the given {@link walkingkooka.predicate.character.CharPredicate}.
 */
final class CharacterComponentParserFunction implements Function<String, Character> {

    static CharacterComponentParserFunction with(final String label,
                                                 final CharPredicate predicate,
                                                 final String message) {
        return new CharacterComponentParserFunction(
            CharSequences.failIfNullOrEmpty(label, "label"),
            Objects.requireNonNull(predicate, "predicate"),
            CharSequences.failIfNullOrEmpty(message, "message")
        );
    }

    private CharacterComponentParserFunction(final String label,
                                             final CharPredicate predicate,
                                             final String message) {
        super();

        this.label = label;
        this.predicate = predicate;
        this.message = message;
    }

    @Override
    public Character apply(final String text) {
        InvalidTextLengthException.throwIfFail(
            this.label,
            text,
            1,
            1
        );

        final char character = text.charAt(0);
        if (false == this.predicate.test(character)) {
            throw new IllegalArgumentException(this.message);
        }
        return character;
    }

    private final String label;
    private final CharPredicate predicate;
    private final String message;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return CharSequences.quoteAndEscape(this.message).toString();
    }
}
