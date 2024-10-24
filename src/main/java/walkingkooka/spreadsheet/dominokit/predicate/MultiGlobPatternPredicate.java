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

package walkingkooka.spreadsheet.dominokit.predicate;

import walkingkooka.collect.list.Lists;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.GlobPattern;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A {@link Predicate} that supports matching any given text using multiple {@link GlobPattern patterns}.
 * This is intended to be used to as a filter to match plugins with an ADD or REMOVE panel when the user edits a {@link walkingkooka.spreadsheet.meta.SpreadsheetMetadata} property,
 * such as available FUNCTIONS.
 * <pre>
 * Functions
 *
 * Add              [a_____]
 * abs, add, asin
 *
 * Remove           [z_____]
 * zap, zing
 *
 * [_________________________]
 * </pre>
 */
final class MultiGlobPatternPredicate implements Predicate<CharSequence> {

    static MultiGlobPatternPredicate with(final List<GlobPattern> globs) {
        return new MultiGlobPatternPredicate(
                Lists.immutable(
                        Objects.requireNonNull(globs, "globs")
                )
        );
    }

    private MultiGlobPatternPredicate(final List<GlobPattern> globs) {
        this.globs = globs;
    }

    @Override
    public boolean test(final CharSequence chars) {
        Objects.requireNonNull(chars, "chars");

        return this.globs.stream()
                .anyMatch(g -> g.test(chars));
    }

    private final List<GlobPattern> globs;

    // Object..........................................................................................................

    @Override
    public int hashCode() {
        return this.globs.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return other == this || other instanceof GlobPattern && this.equals0((MultiGlobPatternPredicate) other);
    }

    private boolean equals0(final MultiGlobPatternPredicate other) {
        return this.globs.equals(other.globs);
    }

    @Override
    public String toString() {
        return CharacterConstant.with(' ')
                .toSeparatedString(this.globs, Objects::toString);
    }
}
