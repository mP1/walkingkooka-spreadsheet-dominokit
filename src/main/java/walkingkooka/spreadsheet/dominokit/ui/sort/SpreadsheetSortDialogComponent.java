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

import org.dominokit.domino.ui.IsElement;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSortEditHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSortEditHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetColumnSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSortEditHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSortHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetRowSortSaveHistoryToken;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.columnorrowcomparatornames.SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent;
import walkingkooka.spreadsheet.dominokit.ui.columnorrowcomparatornameslist.SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.flexlayout.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.historytokenanchor.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

        this.columnOrRowComparatorNamesList = this.columnOrRowComparatorNamesList();

        this.columnOrRowComparatorNamesParent = SpreadsheetFlexLayout.emptyRow();

        this.sort = this.anchor("Sort")
                .setDisabled(true);
        this.close = this.closeAnchor(
                context.historyToken()
        );

        this.dialog = this.dialogCreate();
    }

    // dialog..........................................................................................................

    private SpreadsheetDialogComponent dialogCreate() {
        return SpreadsheetDialogComponent.with(
                        ID,
                        "Sort",
                        true, // includeClose
                        this.context
                ).appendChild(this.columnOrRowComparatorNamesList)
                .appendChild(this.columnOrRowComparatorNamesParent)
                .appendChild(
                        SpreadsheetFlexLayout.emptyRow()
                                .appendChild(this.sort)
                                .appendChild(this.close)
                );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private SpreadsheetDialogComponent dialog;

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = "sort";

    final static String ID_PREFIX = ID + "-";

    static String comparatorIdPrefix(final int index) {
        return COMPARATOR_ID_PREFIX + index + '-';
    }

    private final static String COMPARATOR_ID_PREFIX = ID_PREFIX + "comparator-";

    // lifecycle........................................................................................................

    /**
     * Synchronize the {@link #columnOrRowComparatorNamesList} and components for each {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}
     * from the history token and then give it focus.
     */
    @Override
    public void openGiveFocus(final AppContext context) {
        this.refreshColumnOrRowComparatorNamesList(context);
        this.columnOrRowComparatorNamesListHistoryTokenEdit = null;
        this.columnOrRowComparatorNamesList.focus();
    }

    // refresh..........................................................................................................

    @Override
    public void refresh(final AppContext context) {
        this.refreshSelection(context);
        this.refreshColumnOrRowComparatorNamesList(context); // maybe sync from history token
        this.refreshColumnOrRowComparatorNamesList(); // sync everything from the namesList component
        this.refreshSort();
        this.refreshClose();
    }

    /**
     * Extract the {@link SpreadsheetSelection} from the current {@link HistoryToken}.
     * This will be used to validate any entered columns or rows.
     */
    private void refreshSelection(final AppContext context) {
        this.selectionNotLabel = context.spreadsheetViewportCache()
                .resolveIfLabel(
                        context.historyToken()
                                .cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                                .anchoredSelection()
                                .selection()
                );
    }

    /**
     * The selection (aka range) to be sorted but never a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName}.
     */
    private SpreadsheetSelection selectionNotLabel;

    /**
     * Only the {@link #columnOrRowComparatorNamesList} if the history token has changed since this dialog appeared.
     */
    private void refreshColumnOrRowComparatorNamesList(final AppContext context) {
        final Optional<String> list = this.historyTokenComparatorNamesList(context.historyToken());
        if (false == list.equals(this.columnOrRowComparatorNamesListHistoryTokenEdit)) {
            this.columnOrRowComparatorNamesListHistoryTokenEdit = list;
            this.setColumnOrRowComparatorNamesListString(list);
        }
    }

    /**
     * Local copy from the EDIT HISTORY TOKEN value after /edit/ holding the hopefully valid {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesList}.
     */
    private Optional<String> columnOrRowComparatorNamesListHistoryTokenEdit;

    /**
     * Returns the {@link String} comparator names list from the {@link HistoryToken}.
     */
    private Optional<String> historyTokenComparatorNamesList(final HistoryToken historyToken) {
        // try and sync columnOrRowComparatorNamesList from historyToken.
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

    // columnOrRowComparatorNamesList...................................................................................

    private SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent columnOrRowComparatorNamesList() {
        return SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent.empty()
                .setId(ID_PREFIX + "columnOrRowComparatorNamesList" + SpreadsheetIds.TEXT_BOX)
                .addKeyupListener(
                        (e) -> this.refreshColumnOrRowComparatorNamesList()
                ).addChangeListener(
                        (oldValue, newValue) -> this.refreshColumnOrRowComparatorNamesList()
                );
    }

    private void setColumnOrRowComparatorNamesListString(final Optional<String> list) {
        this.columnOrRowComparatorNamesList.setStringValue(list);
    }

    private final SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent columnOrRowComparatorNamesList;

    /**
     * Creates/refreshes a {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent} for each token within the text / string value of the
     * {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent}.
     */
    private void refreshColumnOrRowComparatorNamesList() {
        this.columnOrRowComparatorNamesList.validate();

        final String text = this.columnOrRowComparatorNamesList.stringValue()
                .orElse("");

        final Optional<SpreadsheetColumnOrRowReference> firstColumnOrRow = SpreadsheetColumnOrRowSpreadsheetComparatorNames.tryParseColumnOrRow(text);

        final String[] tokens = text.isEmpty() ?
                new String[0] :
                text.split(
                        SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string()
                );

        final List<String> names = Lists.array();
        names.addAll(
                Lists.of(tokens)
        );
        names.add("");

        clearIfMax(
                names,
                firstColumnOrRow
        );

        final Set<SpreadsheetColumnOrRowReference> previousColumnOrRows = Sets.sorted();
        int i = 0;
        for (String name : names) {
            this.getOrCreateColumnOrRowComparatorNamesComponent(
                    i,
                    name,
                    previousColumnOrRows
            );
            i++;
        }

        // remove extra names
        final SpreadsheetFlexLayout parent = this.columnOrRowComparatorNamesParent;
        while (names.size() < parent.children().size()) {
            parent.removeChild(names.size());
        }

        this.refreshSort();
    }

    private void clearIfMax(final List<String> names,
                            final Optional<SpreadsheetColumnOrRowReference> firstColumnOrRow) {
        // remove if too many columns/rows for the selection.
        final int namesCount = names.size();
        final int max = maxColumnsOrRows(
                this.selectionNotLabel,
                firstColumnOrRow
        );

        if (namesCount > max) {
            names.subList(
                    max,
                    namesCount

            ).clear();
        }
    }

    /**
     * Uses the column or row to select the "length" of the selection. If there is no first column/row and the selection is
     * a cell/cell-range return the greater length.
     */
    private int maxColumnsOrRows(final SpreadsheetSelection selectionNotLabel,
                                 final Optional<SpreadsheetColumnOrRowReference> firstColumnOrRow) {
        final long max;

        SpreadsheetSelection selectionRange = selectionNotLabel.toRange();

        // if the first column/row is a COLUMN convert selection to a COLUMN.
        if (firstColumnOrRow.isPresent()) {
            SpreadsheetColumnOrRowReference first = firstColumnOrRow.get();
            if (first.isColumnReference()) {
                selectionRange = selectionRange.toColumnRange();
            } else {
                if (first.isRowReference()) {
                    selectionRange = selectionRange.toRowRange();
                } else {
                    throw new NeverError("Got " + selectionRange + " expected column or row");
                }
            }
            max = selectionRange.count();
        } else {
            if (selectionRange.isCellRangeReference()) {
                final long maxColumn = selectionRange.toColumnRange()
                        .count();
                final long maxRow = selectionRange.toRowRange()
                        .count();
                max = Math.max(
                        maxColumn,
                        maxRow
                );
            } else {
                max = selectionRange.count();
            }
        }

        // just to be sure there is no overflow.
        return max > Integer.MAX_VALUE ?
                Integer.MAX_VALUE :
                (int) max;
    }

    // List<SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent>..................................................

    private void getOrCreateColumnOrRowComparatorNamesComponent(final int i,
                                                                final String text,
                                                                final Set<SpreadsheetColumnOrRowReference> previousColumnOrRows) {
        final SpreadsheetFlexLayout parent = this.columnOrRowComparatorNamesParent;
        final List<IsElement<?>> children = parent.children();

        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent names;
        if (i < children.size()) {
            names = (SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent) children.get(i);
        } else {
            names = SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.empty()
                    .setId(ID_PREFIX + "comparatorNames-" + i + SpreadsheetIds.TEXT_BOX);

            names.addKeyupListener((e) -> this.refreshColumnOrRowComparatorNames(names))
                    .addChangeListener((o, n) -> this.refreshColumnOrRowComparatorNames(names));
            parent.appendChild(names);
        }

        names.setStringValue(
                Optional.of(text)
        );

        // dont add more errors if it already has one.
        if (false == names.hasErrors()) {
            // columnOrRow might be out of selection range,
            // the wrong kind(sorting columns but got a row)
            // a duplicate
            final Optional<SpreadsheetColumnOrRowReference> maybeColumnOrRow = SpreadsheetColumnOrRowSpreadsheetComparatorNames.tryParseColumnOrRow(text);
            if (maybeColumnOrRow.isPresent()) {
                String errorMessage = null;

                final SpreadsheetColumnOrRowReference columnOrRow = maybeColumnOrRow.get();
                if (false == previousColumnOrRows.isEmpty()) {
                    try {
                        previousColumnOrRows.iterator()
                                .next()
                                .ifDifferentReferenceTypeFail(columnOrRow);
                        if (previousColumnOrRows.contains(columnOrRow)) {
                            errorMessage = "Duplicate " + columnOrRow.textLabel() + " " + columnOrRow;
                        }
                    } catch (final IllegalArgumentException fail) {
                        errorMessage = fail.getMessage();
                    }
                }

                if (null == errorMessage) {
                    final SpreadsheetSelection selectionNotLabel = this.selectionNotLabel;
                    if (false == selectionNotLabel.test(columnOrRow)) {
                        errorMessage = "Invalid " + columnOrRow.textLabel() + " " + columnOrRow + " is not within " + selectionNotLabel;
                    }
                }

                if (null != errorMessage) {
                    names.addError(errorMessage);
                } else {
                    previousColumnOrRows.add(columnOrRow);
                }
            }
        }
    }

    /**
     * Concatenate the string value of all {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent} and update {@link #columnOrRowComparatorNamesList}.
     */
    private void refreshColumnOrRowComparatorNames(final SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent component) {
        component.validate();

        String text = this.columnOrRowComparatorNamesParent.children()
                .stream()
                .map(c -> ((SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent) c).stringValue().orElse(""))
                .collect(
                        Collectors.joining(
                                SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string()
                        )
                );
        if (text.endsWith(SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string())) {
            text = CharSequences.subSequence(
                    text,
                    0,
                    -SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string()
                            .length()
            ).toString();
        }

        this.columnOrRowComparatorNamesList.setStringValue(
                Optional.of(text)
        );
        this.refreshSort();
    }

    /**
     * Holds all the many {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent}.
     */
    private SpreadsheetFlexLayout columnOrRowComparatorNamesParent;

    // sort.............................................................................................................

    private void refreshSort() {
        final SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent listComponent = this.columnOrRowComparatorNamesList;

        // try and parse columnOrRowComparatorNamesList into a List.
        Optional<HistoryToken> historyToken;

        try {
            historyToken = listComponent.stringValue()
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
                    ).map(
                            l -> this.context.historyToken()
                                    .setSave(l.text())
                    );
        } catch (final IllegalArgumentException cause) {
            // some columns/rows could be out of the selection range.
            historyToken = Optional.empty();

            if (false == listComponent.hasErrors()) {
                listComponent.addError(cause.getMessage());
            }
        }
        this.sort.setHistoryToken(historyToken);
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

    // SpreadsheetDialogComponentLifecycle..............................................................................

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
}
