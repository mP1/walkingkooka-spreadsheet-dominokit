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

package walkingkooka.spreadsheet.dominokit.format;

import org.gwtproject.core.shared.GWT;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormatterHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormatterSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFormatterSelectHistoryToken;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link SpreadsheetFormatterSelectorDialogComponentContext} for editing a cell {@link SpreadsheetFormatterSelector}.
 */
final class AppContextSpreadsheetFormatterSelectorDialogComponentContextCell extends AppContextSpreadsheetFormatterSelectorDialogComponentContext {

    static AppContextSpreadsheetFormatterSelectorDialogComponentContextCell with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new AppContextSpreadsheetFormatterSelectorDialogComponentContextCell(context);
    }

    private AppContextSpreadsheetFormatterSelectorDialogComponentContextCell(final AppContext context) {
        super(context);
    }

    @Override
    public String dialogTitle() {
        return this.selectionValueDialogTitle(SpreadsheetFormatterSelector.class);
    }

    @Override
    public boolean shouldShowTabs() {
        return false;
    }

    @Override
    public Optional<SpreadsheetFormatterSelector> undo() {
        return this.context.spreadsheetViewportCache()
            .historyTokenCell()
            .flatMap(SpreadsheetCell::formatter);
    }

    @Override
    public void loadSpreadsheetFormattersEdit(final String text) {
        final SpreadsheetCellFormatterHistoryToken spreadsheetCellFormatterHistoryToken = this.context.historyToken()
            .cast(SpreadsheetCellFormatterHistoryToken.class);

        // HACK throttler and fetcher will in JVM
        if (GWT.isScript()) {
            this.throttler.add(
                () -> this.context.spreadsheetFormatterFetcher()
                    .getCellFormatterEdit(
                        spreadsheetCellFormatterHistoryToken.id(), // id
                        spreadsheetCellFormatterHistoryToken.selection()
                            .get()
                            .toExpressionReference(),
                        text
                    )
            );
        }
    }

    // ComponentLifecycleMatcher........................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellFormatterSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellFormatterSelectHistoryToken;
    }
}
