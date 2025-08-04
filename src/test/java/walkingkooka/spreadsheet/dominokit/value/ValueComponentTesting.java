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

import elemental2.dom.HTMLElement;
import walkingkooka.spreadsheet.dominokit.HtmlComponentTesting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface ValueComponentTesting<E extends HTMLElement, V, C extends ValueComponent<E, V, C>>
    extends HtmlComponentTesting<C, E> {

    // value............................................................................................................

    default void valueAndCheck(final C component) {
        this.valueAndCheck(
            component,
            Optional.empty()
        );
    }

    default void valueAndCheck(final C component,
                               final V expected) {
        this.valueAndCheck(
            component,
            Optional.of(expected)
        );
    }

    default void valueAndCheck(final C component,
                               final Optional<V> expected) {
        this.checkEquals(
            expected,
            component.value()
        );
    }

    // setValue.........................................................................................................

    default void setValueWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createComponent().setValue(null)
        );
    }

    // isDisabled.......................................................................................................

    default void isDisabledAndCheck(final C component,
                                    final boolean expected) {
        this.checkEquals(
            expected,
            component.isDisabled(),
            component::toString
        );
    }


    C createComponent();
}
