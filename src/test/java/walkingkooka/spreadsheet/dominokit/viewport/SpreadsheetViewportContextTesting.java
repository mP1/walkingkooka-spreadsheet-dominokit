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

package walkingkooka.spreadsheet.dominokit.viewport;

import org.junit.jupiter.api.Test;
import walkingkooka.ContextTesting;
import walkingkooka.tree.text.TextStyle;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface SpreadsheetViewportContextTesting<C extends SpreadsheetViewportContext> extends ContextTesting<C> {

    // hideZeroStyle....................................................................................................

    @Test
    default void testHideZeroStyleWithNullTextStyleFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext().hideZeroStyle(null)
        );
    }

    default void hideZeroStyleAndCheck(final TextStyle textStyle,
                                       final TextStyle expected) {
        this.hideZeroStyleAndCheck(
            this.createContext(),
            textStyle,
            expected
        );
    }

    default void hideZeroStyleAndCheck(final C context,
                                       final TextStyle textStyle,
                                       final TextStyle expected) {
        this.checkEquals(
            expected,
            context.hideZeroStyle(textStyle),
            textStyle::toString
        );
    }
}
