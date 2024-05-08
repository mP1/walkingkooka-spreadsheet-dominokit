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

package walkingkooka.spreadsheet.dominokit.ui.sort;

import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;

import java.util.Objects;

/**
 * A dialog which includes various components allowing the user to enter the sort columns/rows and comparators as
 * text or by clicking on links to build the sort text.
 */
public final class SpreadsheetSortDialogComponent implements SpreadsheetDialogComponentLifecycle {

    public static SpreadsheetSortDialogComponent with(final SpreadsheetSortDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetSortDialogComponent(context);
    }

    private SpreadsheetSortDialogComponent(final SpreadsheetSortDialogComponentContext context) {
        super();

        context.addHistoryTokenWatcher(this);

        this.context = context;

        this.sort = this.anchor("Sort")
                .setDisabled(true);
        this.close = this.closeAnchor(
                context.historyToken()
        );

        this.dialog = this.dialogCreate();
    }

    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                ID,
                "Sort",
                true, // includeClose
                this.context
        );

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.sort)
                        .appendChild(this.close)
        );

        return dialog;
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private SpreadsheetDialogComponent dialog;

    @Override
    public void openGiveFocus(final AppContext context) {
        // TODO GIVE FOCUS
    }

    @Override
    public void refresh(final AppContext context) {
        // TODO REFRESH COMPONENTS
    }

    /**
     * A SORT link which will execute the SORT.
     */
    private final HistoryTokenAnchorComponent sort;

    /**
     * A CLOSE link which will close the dialog.
     */
    private final HistoryTokenAnchorComponent close;

    /**
     * {@see SpreadsheetSortDialogComponentContext}
     */
    private final SpreadsheetSortDialogComponentContext context;

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token instanceof SpreadsheetCellSortSaveHistoryToken ||
                token instanceof SpreadsheetColumnSortSaveHistoryToken ||
                token instanceof SpreadsheetRowSortSaveHistoryToken;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellSortHistoryToken ||
                token instanceof SpreadsheetColumnSortHistoryToken ||
                token instanceof SpreadsheetRowSortHistoryToken;
    }

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = "sort";

    final static String ID_PREFIX = ID + "-";
}
