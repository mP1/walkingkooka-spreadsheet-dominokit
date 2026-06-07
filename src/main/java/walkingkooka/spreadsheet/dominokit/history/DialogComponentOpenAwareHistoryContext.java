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

import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;

import java.util.Objects;
import java.util.function.BooleanSupplier;

/**
 * A {@link HistoryContext} that wraps another, filtering (skipping) {@link HistoryWatcher}
 * if the enclosing {@link DialogComponent} is closed.
 */
final class DialogComponentOpenAwareHistoryContext implements HistoryContextDelegator {

    static DialogComponentOpenAwareHistoryContext with(final BooleanSupplier isDialogOpen,
                                                       final HistoryContext context) {
        return new DialogComponentOpenAwareHistoryContext(
            Objects.requireNonNull(isDialogOpen, "isDialogOpen"),
            Objects.requireNonNull(context, "context")
        );
    }

    private DialogComponentOpenAwareHistoryContext(final BooleanSupplier isDialogOpen,
                                                   final HistoryContext context) {
        super();
        this.isDialogOpen = isDialogOpen;
        this.context = context;
    }

    // HistoryContextDelegator..........................................................................................

    @Override
    public Runnable addHistoryWatcher(final HistoryWatcher watcher) {
        return this.context.addHistoryWatcher(
            walkingkooka.spreadsheet.dominokit.history.DialogComponentOpenAwareHistoryContextHistoryWatcher.with(
                this.isDialogOpen,
                watcher
            )
        );
    }

    @Override
    public Runnable addHistoryWatcherOnce(final HistoryWatcher watcher) {
        return this.context.addHistoryWatcherOnce(
            walkingkooka.spreadsheet.dominokit.history.DialogComponentOpenAwareHistoryContextHistoryWatcher.with(
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

    private final HistoryContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
