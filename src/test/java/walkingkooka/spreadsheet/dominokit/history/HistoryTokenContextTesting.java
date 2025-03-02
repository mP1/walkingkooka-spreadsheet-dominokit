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

package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.ContextTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface HistoryTokenContextTesting<C extends HistoryTokenContext> extends ContextTesting<C> {

    // addHistoryTokenWatcher...........................................................................................

    @Test
    default void testAddHistoryTokenWatcherWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .addHistoryTokenWatcher(null)
        );
    }

    // addHistoryTokenWatcherOnce.......................................................................................

    @Test
    default void testAddHistoryTokenWatcherOnceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .addHistoryTokenWatcherOnce(null)
        );
    }

    // historyToken.....................................................................................................

    default void historyTokenAndCheck(final HistoryTokenContext context,
                                      final HistoryToken expected) {
        this.checkEquals(
            expected,
            context.historyToken()
        );
    }

    // pushHistoryToken.................................................................................................

    @Test
    default void testPushHistoryTokenWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .addHistoryTokenWatcherOnce(null)
        );
    }
}
