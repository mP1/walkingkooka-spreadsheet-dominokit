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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.Lists;
import walkingkooka.predicate.PredicateTesting2;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.LineEnding;

import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class MultiGlobPatternPredicateTest implements PredicateTesting2<MultiGlobPatternPredicate, CharSequence> {

    @Test
    public void testTestStarTextMatch() {
        this.testTrue(
                MultiGlobPatternPredicate.with(
                        Lists.of(
                                CaseSensitivity.INSENSITIVE.globPattern("Starts*", '\\')
                        )
                ),
                "Starts with matched"
        );
    }

    @Test
    public void testTestQuestionMarksMatch() {
        this.testTrue(
                MultiGlobPatternPredicate.with(
                        Lists.of(
                                CaseSensitivity.INSENSITIVE.globPattern("?2?4?6?", '\\')
                        )
                ),
                "A2C4E6G"
        );
    }

    @Test
    public void testTestStarTextStarMatch() {
        this.testTrue(
                "HELlo"
        );
    }

    @Test
    public void testTestStarTextStarMatch2() {
        this.testTrue(
                "123 HELlo 456"
        );
    }

    @Test
    public void testTestStarTextStarNotMatch() {
        this.testFalse(
                "Not-a-match"
        );
    }

    @Test
    public void testTestMultiplePatternsMatch() {
        this.testTrue(
                MultiGlobPatternPredicate.with(
                        Lists.of(
                                CaseSensitivity.INSENSITIVE.globPattern("*First*", '\\'),
                                CaseSensitivity.INSENSITIVE.globPattern("*Second*", '\\'),
                                CaseSensitivity.INSENSITIVE.globPattern("*Third", '\\')
                        )
                ),
                "Third"
        );
    }

    @Test
    public void testTestListOfTextExample() {
        final String abc = "abc";
        final String def = "def";
        final String ghi = "ghi";
        final String jkl = "jkl";
        final String mnop = "mnop";

        final Predicate<CharSequence> predicate = MultiGlobPatternPredicate.with(
                Lists.of(
                        CaseSensitivity.INSENSITIVE.globPattern("*bc*", '\\'),
                        CaseSensitivity.INSENSITIVE.globPattern("j*", '\\')
                )
        );

        this.checkEquals(
                "*** abc ***\n" +
                        "def\n" +
                        "ghi\n" +
                        "*** jkl ***\n" +
                        "mnop",
                Lists.of(
                                abc,
                                def,
                                ghi,
                                jkl,
                                mnop
                        ).stream()
                        .map(t -> predicate.test(t) ?
                                "*** " + t + " ***" :
                                t
                        ).collect(Collectors.joining(LineEnding.NL))
        );
    }

    @Override
    public MultiGlobPatternPredicate createPredicate() {
        return MultiGlobPatternPredicate.with(
                Lists.of(
                        CaseSensitivity.INSENSITIVE.globPattern("*Hello*", '\\')
                )
        );
    }

    // class............................................................................................................

    @Override
    public Class<MultiGlobPatternPredicate> type() {
        return MultiGlobPatternPredicate.class;
    }
}
