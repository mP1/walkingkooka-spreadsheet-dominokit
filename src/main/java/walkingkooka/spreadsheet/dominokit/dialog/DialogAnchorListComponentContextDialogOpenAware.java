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

package walkingkooka.spreadsheet.dominokit.dialog;

import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;

/**
 * A {@link DialogAnchorListComponentContext} that wraps another, filtering (skipping) {@link walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher}
 * if the enclosing {@link DialogComponent} is closed.
 */
final class DialogAnchorListComponentContextDialogOpenAware<T> implements DialogAnchorListComponentContext<T>,
    HistoryContextDelegator {

    static <T> DialogAnchorListComponentContextDialogOpenAware<T> with(final BooleanSupplier isDialogOpen,
                                                                       final DialogAnchorListComponentContext<T> context) {
        return new DialogAnchorListComponentContextDialogOpenAware<>(
            Objects.requireNonNull(isDialogOpen, "isDialogOpen"),
            Objects.requireNonNull(context, "context")
        );
    }

    private DialogAnchorListComponentContextDialogOpenAware(final BooleanSupplier isDialogOpen,
                                                            final DialogAnchorListComponentContext<T> context) {
        super();
        this.isDialogOpen = isDialogOpen;
        this.context = context;
    }

    @Override
    public Optional<T> undo() {
        return this.context.undo();
    }

    // HistoryContextDelegator..........................................................................................

    @Override
    public Runnable addHistoryTokenWatcher(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcher(
            DialogAnchorListComponentContextDialogOpenAwareHistoryTokenWatcher.with(
                this.isDialogOpen,
                watcher
            )
        );
    }

    @Override
    public Runnable addHistoryTokenWatcherOnce(final HistoryTokenWatcher watcher) {
        return this.context.addHistoryTokenWatcherOnce(
            DialogAnchorListComponentContextDialogOpenAwareHistoryTokenWatcher.with(
                this.isDialogOpen,
                watcher
            )
        );
    }

    private final BooleanSupplier isDialogOpen;

    @Override
    public HistoryContext historyContext() {
        return this.context;
    }

    private final DialogAnchorListComponentContext<T> context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
