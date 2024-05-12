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
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSortEditHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSortEditHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSortEditHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.columnorrowcomparatornameslist.SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;

import java.util.Objects;
import java.util.Optional;

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

        this.comparatorNamesList = this.comparatorNamesList();

        this.sort = this.anchor("Sort")
                .setDisabled(true);
        this.close = this.closeAnchor(
                context.historyToken()
        );

        this.dialog = this.dialogCreate();
    }

    // dialog..........................................................................................................

    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.with(
                ID,
                "Sort",
                true, // includeClose
                this.context
        );

        dialog.appendChild(this.comparatorNamesList);

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

    // lifecycle........................................................................................................

    @Override
    public void openGiveFocus(final AppContext context) {
        this.comparatorNamesListHistoryToken = null;
        this.refreshComparatorNameList(context);
        this.comparatorNamesList.focus();
    }

    @Override
    public void refresh(final AppContext context) {
        this.refreshComparatorNameList(context);
        this.refreshLinks();
    }

    /**
     * Only the {@link #comparatorNamesList} if the history token has changed since this dialog appeared.
     */
    private void refreshComparatorNameList(final AppContext context) {
        final Optional<String> list = this.historyTokenComparatorNameList(context.historyToken());
        if (false == list.equals(this.comparatorNamesListHistoryToken)) {
            this.comparatorNamesListHistoryToken = list;
            this.setComparatorNamesListString(list);
        }
    }

    private Optional<String> comparatorNamesListHistoryToken;

    private Optional<String> historyTokenComparatorNameList(final HistoryToken historyToken) {
        // try and sync comparatorNamesList from historyToken.
        String list = null;
        if (historyToken instanceof SpreadsheetCellSortEditHistoryToken) {
            list = historyToken.cast(SpreadsheetCellSortEditHistoryToken.class)
                    .comparatorNames();
        }
        if (historyToken instanceof SpreadsheetColumnSortEditHistoryToken) {
            list = historyToken.cast(SpreadsheetColumnSortEditHistoryToken.class)
                    .comparatorNames();
        }
        if (historyToken instanceof SpreadsheetRowSortEditHistoryToken) {
            list = historyToken.cast(SpreadsheetRowSortEditHistoryToken.class)
                    .comparatorNames();
        }

        return Optional.ofNullable(list);
    }

    private void refreshLinks() {
        // try and parse comparatorNamesList into a List.
        this.setSort(
                this.comparatorNamesList.stringValue()
                        .map(
                                ls -> {
                                    SpreadsheetColumnOrRowSpreadsheetComparatorNamesList list;

                                    try {
                                        list = SpreadsheetColumnOrRowSpreadsheetComparatorNamesList.parse(ls);
                                    } catch (final RuntimeException ignore) {
                                        list = null;
                                    }

                                    return list;
                                }
                        )
        );

        this.refreshClose();
    }

    // comparatorNamesList..............................................................................................

    private SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent comparatorNamesList() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.empty()
                .setId(ID_PREFIX + "comparatorNamesList")
                .addKeyupListener(
                        (e) -> this.refreshLinks()
                ).addChangeListener(
                        (oldValue, newValue) -> this.refreshLinks()
                );
    }

    private void setComparatorNamesListString(final Optional<String> list) {
        this.comparatorNamesList.setStringValue(list);
    }

    private final SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent comparatorNamesList;

    // sort.............................................................................................................

    /**
     * Updates the SORT link. If the {@link Optional} is empty the link will be disabled otherwise it will be enabled with
     * a SAVE link which executes the sort.
     */
    private void setSort(final Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNamesList> list) {
        this.sort.setHistoryToken(
                list.map(
                        l -> this.context.historyToken()
                                .setSave(l.text())
                )
        );
    }

    /**
     * A SORT link which will execute the SORT.
     */
    private final HistoryTokenAnchorComponent sort;

    // close.............................................................................................................

    private void refreshClose() {
        this.close.setHistoryToken(
                Optional.of(
                        this.context.historyToken()
                                .close()
                )
        );
    }

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
