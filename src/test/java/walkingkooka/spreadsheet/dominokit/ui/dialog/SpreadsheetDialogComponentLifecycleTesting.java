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

package walkingkooka.spreadsheet.dominokit.ui.dialog;

import walkingkooka.reflect.ClassTesting;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.text.printer.TreePrintableTesting;

public interface SpreadsheetDialogComponentLifecycleTesting<T extends SpreadsheetDialogComponentLifecycle> extends ClassTesting<T>,
        TreePrintableTesting {

    default void onHistoryTokenChangeAndCheck(final T dialog,
                                              final HistoryToken previous,
                                              final AppContext context,
                                              final String expected) {
        dialog.onHistoryTokenChange(
                previous,
                context
        );
        this.checkEquals(
                true,
                dialog.isOpen(),
                () -> "dialog must be open for " + context.historyToken()
        );
        this.treePrintAndCheck(
                dialog,
                expected
        );
    }
}
