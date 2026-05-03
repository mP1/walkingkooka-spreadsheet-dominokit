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

package walkingkooka.spreadsheet.dominokit.value.textstyle;

import elemental2.dom.HTMLElement;
import org.junit.jupiter.api.Test;
import walkingkooka.naming.HasNameTesting;
import walkingkooka.spreadsheet.dominokit.value.FormValueComponentTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.tree.text.TextStylePropertyName;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface TextStylePropertyComponentTesting<E extends HTMLElement, V, C extends TextStylePropertyComponent<E, V, C>>
    extends FormValueComponentTesting<E, V, C>,
    HasNameTesting<TextStylePropertyName<V>> {

    // filterTest.......................................................................................................

    @Test
    default void testFilterTestWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent()
                .filterTest(null)
        );
    }

    @Test
    default void testFilterTestWithName() {
        final C component = this.createComponent();

        this.filterTestAndCheck(
            component,
            CharSequences.subSequence(
                component.name()
                    .value(),
                0,
                -1
            ).toString(),
            true
        );
    }

    @Test
    default void testFilterTestWithNoValue() {
        final C component = this.createComponent();

        this.filterTestAndCheck(
            component,
            "ZZZ",
            false
        );
    }

    default void filterTestAndCheck(final C component,
                                    final String filter,
                                    final boolean expected) {
        this.filterTestAndCheck(
            component,
            TextStyleDialogComponentFilter.with(filter),
            expected
        );
    }

    default void filterTestAndCheck(final C component,
                                    final TextStyleDialogComponentFilter filter,
                                    final boolean expected) {
        this.checkEquals(
            expected,
            component.filterTest(filter),
            () -> filter + "\n" + component
        );
    }
}
