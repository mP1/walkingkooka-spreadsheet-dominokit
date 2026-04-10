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

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;

import java.util.Objects;
import java.util.function.BooleanSupplier;

final class DialogAnchorListComponentContextDialogOpenAwareHistoryTokenWatcher implements HistoryTokenWatcher {

    static DialogAnchorListComponentContextDialogOpenAwareHistoryTokenWatcher with(final BooleanSupplier isDialogOpen,
                                                                                   final HistoryTokenWatcher watcher) {
        return new DialogAnchorListComponentContextDialogOpenAwareHistoryTokenWatcher(
            isDialogOpen,
            Objects.requireNonNull(watcher, "watcher")
        );
    }

    private DialogAnchorListComponentContextDialogOpenAwareHistoryTokenWatcher(final BooleanSupplier isDialogOpen,
                                                                               final HistoryTokenWatcher watcher) {
        super();

        this.isDialogOpen = isDialogOpen;
        this.watcher = watcher;
    }

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        if (this.isDialogOpen.getAsBoolean()) {
            this.watcher.onHistoryTokenChange(
                previous,
                context
            );
        }
    }

    private final BooleanSupplier isDialogOpen;

    private final HistoryTokenWatcher watcher;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
