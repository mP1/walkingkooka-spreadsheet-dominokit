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

package walkingkooka.spreadsheet.dominokit.dom;

import org.junit.jupiter.api.Test;
import walkingkooka.text.printer.TreePrintableTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface HasEventListenerTesting<V, C extends HasEventListener<V, C>> extends TreePrintableTesting {

    @Test
    default void testAddBlurListenerWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHasEventListeners()
                .addBlurListener(null)
        );
    }

    @Test
    default void testAddClickListenerWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHasEventListeners()
                .addClickListener(null)
        );
    }

    @Test
    default void testAddChangeListenerWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHasEventListeners()
                .addBlurListener(null)
        );
    }

    @Test
    default void testAddFocusListenerWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHasEventListeners()
                .addFocusListener(null)
        );
    }

    @Test
    default void testAddInputListenerWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHasEventListeners()
                .addInputListener(null)
        );
    }

    @Test
    default void testAddKeyDownListenerWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHasEventListeners()
                .addKeyDownListener(null)
        );
    }

    @Test
    default void testAddKeyUpListenerWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createHasEventListeners()
                .addKeyUpListener(null)
        );
    }

    C createHasEventListeners();
}
