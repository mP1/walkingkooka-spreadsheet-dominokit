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

package walkingkooka.spreadsheet.dominokit;

import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.text.printer.TreePrintable;

/**
 * Note the overloads that take a {@link HistoryTokenWatcher} are intended for tests to provide the {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatchers},
 * also given to the provided {@link AppContext}. This will be necessary for components that have children that also register themselves as {@link HistoryTokenWatcher}.
 */
public interface HistoryTokenAwareComponentLifecycleTesting<T extends HistoryTokenAwareComponentLifecycle & TreePrintable> extends ComponentLifecycleTesting<T> {

    HistoryToken NOT_MATCHED = HistoryToken.parseString("/not-matched-123");

    default void onHistoryTokenChangeAndCheck(final T component,
                                              final AppContext context,
                                              final String expected) {
        onHistoryTokenChangeAndCheck(
                component,
                component,
                context,
                expected
        );
    }

    default void onHistoryTokenChangeAndCheck(final T component,
                                              final HistoryTokenWatcher watcher,
                                              final AppContext context,
                                              final String expected) {
        onHistoryTokenChangeAndCheck(
                component,
                watcher,
                NOT_MATCHED,
                context,
                expected
        );
    }

    default void onHistoryTokenChangeAndCheck(final T component,
                                              final HistoryToken previous,
                                              final AppContext context,
                                              final String expected) {
        this.onHistoryTokenChangeAndCheck(
                component,
                component,
                previous,
                context,
                expected
        );
    }

    default void onHistoryTokenChangeAndCheck(final T component,
                                              final HistoryTokenWatcher watcher,
                                              final HistoryToken previous,
                                              final AppContext context,
                                              final String expected) {
        watcher.onHistoryTokenChange(
                previous,
                context
        );
        this.treePrintAndCheck(
                component,
                expected
        );
    }
}
