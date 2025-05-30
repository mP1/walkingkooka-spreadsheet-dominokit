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

package walkingkooka.spreadsheet.dominokit.insert;

import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContext;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentContextDelegator;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnInsertHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowInsertHistoryToken;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

final class AppContextSpreadsheetColumnRowInsertCountDialogComponentContext implements SpreadsheetColumnRowInsertCountDialogComponentContext,
    SpreadsheetDialogComponentContextDelegator {

    static AppContextSpreadsheetColumnRowInsertCountDialogComponentContext with(final AppContext context) {
        return new AppContextSpreadsheetColumnRowInsertCountDialogComponentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private AppContextSpreadsheetColumnRowInsertCountDialogComponentContext(final AppContext context) {
        this.context = context;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return (token instanceof SpreadsheetColumnInsertHistoryToken ||
            token instanceof SpreadsheetRowInsertHistoryToken) &&
            false == token.count().isPresent();
    }

    @Override
    public String dialogTitle() {
        String title = null;

        final HistoryToken token = this.historyToken();
        final Optional<SpreadsheetSelection> maybeSelection = token.anchoredSelectionOrEmpty()
            .map(AnchoredSpreadsheetSelection::selection);
        if (maybeSelection.isPresent()) {
            final SpreadsheetSelection selection = maybeSelection.get();
            final String text = selection.cellColumnOrRowText();

            title = "Insert " +
                (
                    token.getClass().getSimpleName().contains("After") ?
                        "after" :
                        "before"
                ) +
                " " +
                text +
                " " +
                selection.toStringMaybeStar();
        }

        return null != title ?
            title :
            "Insert";
    }

    @Override
    public void save(final int count) {
        this.pushHistoryToken(
            this.historyToken().setCount(
                OptionalInt.of(count)
            )
        );
    }

    // SpreadsheetDialogComponentContext................................................................................

    @Override
    public SpreadsheetDialogComponentContext spreadsheetDialogComponentContext() {
        return this.context;
    }

    private final AppContext context;

    @Override
    public String toString() {
        return this.context.toString();
    }
}
