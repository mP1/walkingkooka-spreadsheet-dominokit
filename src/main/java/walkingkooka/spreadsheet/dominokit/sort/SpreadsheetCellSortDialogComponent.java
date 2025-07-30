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

package walkingkooka.spreadsheet.dominokit.sort;

import org.dominokit.domino.ui.IsElement;
import walkingkooka.NeverError;
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNames;
import walkingkooka.spreadsheet.compare.SpreadsheetColumnOrRowSpreadsheetComparatorNamesList;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.comparator.SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
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
import walkingkooka.spreadsheet.dominokit.link.SpreadsheetLinkListComponent;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CharSequences;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * A dialog which includes various components allowing the user to enter the sort columns/rows and comparators as
 * text or by clicking on links to build the sort text.
 */
public final class SpreadsheetCellSortDialogComponent implements SpreadsheetDialogComponentLifecycle {

    public static SpreadsheetCellSortDialogComponent with(final SpreadsheetCellSortDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetCellSortDialogComponent(context);
    }

    private SpreadsheetCellSortDialogComponent(final SpreadsheetCellSortDialogComponentContext context) {
        super();

        context.addHistoryTokenWatcher(this);

        this.context = context;

        this.columnOrRowComparatorNamesList = this.columnOrRowComparatorNamesList();

        this.columnOrRowComparatorNamesParent = SpreadsheetFlexLayout.row();

        this.sort = this.anchor("Sort")
            .setDisabled(true);
        this.close = this.closeAnchor();

        this.dialog = this.dialogCreate();
    }

    // dialog..........................................................................................................

    private SpreadsheetDialogComponent dialogCreate() {
        return SpreadsheetDialogComponent.largeEdit(
                ID + SpreadsheetElementIds.DIALOG,
                this.context.dialogTitle(),
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                this.context
            ).appendChild(this.columnOrRowComparatorNamesList)
            .appendChild(this.columnOrRowComparatorNamesParent)
            .appendChild(
                SpreadsheetLinkListComponent.empty()
                    .appendChild(this.sort)
                    .appendChild(this.close)
            );
    }

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    // lifecycle........................................................................................................

    /**
     * Synchronize the {@link #columnOrRowComparatorNamesList} and components for each {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames}
     * from the history token and then give it focus.
     */
    @Override
    public void openGiveFocus(final RefreshContext context) {
        this.refreshColumnOrRowComparatorNamesList(context);
        this.columnOrRowComparatorNamesListHistoryTokenEdit = null;
        this.columnOrRowComparatorNamesList.focus();
    }

    // refresh..........................................................................................................

    @Override
    public void refresh(final RefreshContext context) {
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
    private void refreshSelection(final RefreshContext context) {
        this.selectionNotLabel = this.context.spreadsheetViewportCache()
            .resolveIfLabel(
                context.historyToken()
                    .cast(SpreadsheetAnchoredSelectionHistoryToken.class)
                    .anchoredSelection()
                    .selection()
            ).orElse(null); // @@FIX
    }

    /**
     * The selection (aka range) to be sorted but never a {@link walkingkooka.spreadsheet.reference.SpreadsheetLabelName}.
     */
    private SpreadsheetSelection selectionNotLabel;

    /**
     * Only the {@link #columnOrRowComparatorNamesList} if the history token has changed since this dialog appeared.
     */
    private void refreshColumnOrRowComparatorNamesList(final RefreshContext context) {
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
            .setId(ID_PREFIX + "columnOrRowComparatorNamesList" + SpreadsheetElementIds.TEXT_BOX)
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
     * Creates/refreshes a {@link SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent} for each token within the text / string value of the
     * {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesListComponent}.
     */
    private void refreshColumnOrRowComparatorNamesList() {
        this.columnOrRowComparatorNamesList.validate();

        final String text = this.columnOrRowComparatorNamesList.stringValue()
            .orElse("");

        final Optional<SpreadsheetColumnOrRowReferenceOrRange> firstColumnOrRow = SpreadsheetColumnOrRowSpreadsheetComparatorNames.tryParseColumnOrRow(text);

        final String[] tokens = toSpreadsheetColumnOrRowSpreadsheetComparatorNames(text);

        final List<String> names = Lists.array();
        names.addAll(
            Lists.of(tokens)
        );
        names.add("");

        clearIfMax(
            names,
            firstColumnOrRow
        );

        final Set<SpreadsheetSelection> previousColumnOrRows = SortedSets.tree();
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

    /**
     * Helper that splits any given {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames} text into tokens.
     */
    private String[] toSpreadsheetColumnOrRowSpreadsheetComparatorNames(final String spreadsheetColumnOrRowSpreadsheetComparatorNames) {
        return spreadsheetColumnOrRowSpreadsheetComparatorNames.isEmpty() ?
            new String[0] :
            spreadsheetColumnOrRowSpreadsheetComparatorNames.split(
                SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string()
            );
    }

    private void clearIfMax(final List<String> names,
                            final Optional<SpreadsheetColumnOrRowReferenceOrRange> firstColumnOrRow) {
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
                                 final Optional<SpreadsheetColumnOrRowReferenceOrRange> firstColumnOrRow) {
        final long max;

        SpreadsheetSelection selectionRange = selectionNotLabel.toRange();

        // if the first column/row is a COLUMN convert selection to a COLUMN.
        if (firstColumnOrRow.isPresent()) {
            SpreadsheetSelection first = firstColumnOrRow.get();
            if (first.isColumn()) {
                selectionRange = selectionRange.toColumnRange();
            } else {
                if (first.isRow()) {
                    selectionRange = selectionRange.toRowRange();
                } else {
                    throw new NeverError("Got " + selectionRange + " expected column or row");
                }
            }
            max = selectionRange.count();
        } else {
            if (selectionRange.isCellRange()) {
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

    // SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent........................................................

    /**
     * Get or lazily creates {@link SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent} for the nth token in the
     * {@link SpreadsheetColumnOrRowSpreadsheetComparatorNamesList}.
     */
    private void getOrCreateColumnOrRowComparatorNamesComponent(final int i,
                                                                final String text,
                                                                final Set<SpreadsheetSelection> previousColumnOrRows) {
        final SpreadsheetCellSortDialogComponentContext context = this.context;
        final SpreadsheetFlexLayout parent = this.columnOrRowComparatorNamesParent;
        final List<IsElement<?>> children = parent.children();

        final SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent names;
        if (i < children.size()) {
            names = (SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent) children.get(i);
        } else {
            names = SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent.with(
                ID_PREFIX + "comparatorNames-" + i + "-", // id-prefix
                this.moveUp(i),
                this.moveDown(i),
                this.setter(i),
                context
            );

            names.addKeyupListener((e) -> this.onSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(names))
                .addChangeListener((o, n) -> this.onSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(names));
            parent.appendChild(names);
        }

        names.refresh(
            text,
            context
        );

        // dont add more errors if it already has one.
        if (false == names.hasErrors()) {
            // columnOrRow might be out of selection range,
            // the wrong kind(sorting columns but got a row)
            // a duplicate
            final Optional<SpreadsheetColumnOrRowReferenceOrRange> maybeColumnOrRow = SpreadsheetColumnOrRowSpreadsheetComparatorNames.tryParseColumnOrRow(text);
            if (maybeColumnOrRow.isPresent()) {
                String errorMessage = null;

                final SpreadsheetColumnOrRowReferenceOrRange columnOrRow = maybeColumnOrRow.get();
                if (false == previousColumnOrRows.isEmpty()) {
                    try {
                        previousColumnOrRows.iterator()
                            .next()
                            .ifDifferentColumnOrRowTypeFail(columnOrRow);
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

    // 0 = A swap = index - 1
    // 1 = B index = 2
    // 2 = C
    private Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveUp(final int index) {
        return (names) -> {
            HistoryToken historyToken;

            if (0 == index) {
                historyToken = null;
            } else {
                final String namesList = this.columnOrRowComparatorNamesList.stringValue()
                    .orElse("");
                final String[] tokens = this.toSpreadsheetColumnOrRowSpreadsheetComparatorNames(namesList);

                if (index < tokens.length) {
                    final String down = tokens[index - 1];
                    tokens[index - 1] = names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                        .orElse(""); // move up
                    tokens[index] = down;
                }

                historyToken = this.mergeSpreadsheetColumnOrRowSpreadsheetComparatorNamesAndSetSortEdit(
                    tokens,
                    namesList
                );
            }

            return Optional.ofNullable(historyToken);
        };
    }

    // 0 = A
    // 1 = B index = 2
    // 2 = C swap = index + 1
    private Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, Optional<HistoryToken>> moveDown(final int index) {
        return (names) -> {
            HistoryToken historyToken;

            final String namesList = this.columnOrRowComparatorNamesList.stringValue()
                .orElse("");
            final String[] tokens = this.toSpreadsheetColumnOrRowSpreadsheetComparatorNames(namesList);

            if (index + 1 < tokens.length) {
                final String up = tokens[index + 1];
                tokens[index + 1] = names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                    .orElse(""); // move up
                tokens[index] = up;

                historyToken = this.mergeSpreadsheetColumnOrRowSpreadsheetComparatorNamesAndSetSortEdit(
                    tokens,
                    namesList
                );
            } else {
                historyToken = null;
            }

            return Optional.ofNullable(historyToken);
        };
    }

    private HistoryToken mergeSpreadsheetColumnOrRowSpreadsheetComparatorNamesAndSetSortEdit(final String[] names,
                                                                                             final String previousNameList) {
        final HistoryToken historyToken;

        final String newNamesList = mergeSpreadsheetColumnOrRowSpreadsheetComparatorNames(names);
        if (false == previousNameList.equals(newNamesList)) {
            historyToken = this.setEdit(newNamesList);
        } else {
            historyToken = null;
        }

        return historyToken;
    }

    private Function<Optional<SpreadsheetColumnOrRowSpreadsheetComparatorNames>, HistoryToken> setter(final int index) {
        return (names) -> {
            final String[] tokens = this.toSpreadsheetColumnOrRowSpreadsheetComparatorNames(
                this.columnOrRowComparatorNamesList.stringValue()
                    .orElse("")
            );

            if (index < tokens.length) {
                tokens[index] = names.map(SpreadsheetColumnOrRowSpreadsheetComparatorNames::text)
                    .orElse("");
            }

            return this.setEdit(
                this.mergeSpreadsheetColumnOrRowSpreadsheetComparatorNames(tokens)
            );
        };
    }

    private String mergeSpreadsheetColumnOrRowSpreadsheetComparatorNames(final String[] names) {
        return Arrays.stream(names)
            .filter(NOT_EMPTY_STRING)
            .collect(
                Collectors.joining(
                    SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string()
                )
            );
    }

    private HistoryToken setEdit(final String sortEdit) {
        return this.context.historyToken()
            .setSortEdit(sortEdit);
    }


    /**
     * Concatenate the string value of all {@link SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent} and update {@link #columnOrRowComparatorNamesList}.
     */
    private void onSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent(final SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent component) {
        component.validate();

        String columnOrRowSpreadsheetComparatorNames = this.columnOrRowComparatorNamesParent.children()
            .stream()
            .map(c -> ((SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent) c).stringValue().orElse(""))
            .filter(NOT_EMPTY_STRING)
            .collect(
                Collectors.joining(
                    SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string()
                )
            );
        if (columnOrRowSpreadsheetComparatorNames.endsWith(SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string())) {
            columnOrRowSpreadsheetComparatorNames = CharSequences.subSequence(
                columnOrRowSpreadsheetComparatorNames,
                0,
                -SpreadsheetColumnOrRowSpreadsheetComparatorNames.COLUMN_ROW_COMPARATOR_NAMES_SEPARATOR.string()
                    .length()
            ).toString();
        }

        component.refresh(
            component.stringValue()
                .orElse(""),
            this.context
        );

        this.columnOrRowComparatorNamesList.setStringValue(
            Optional.of(columnOrRowSpreadsheetComparatorNames)
        );
        this.refreshSort();
    }

    /**
     * A predicate that only matches non-empty strings. Used when concatenating {@link SpreadsheetColumnOrRowSpreadsheetComparatorNames} which may include empty entries.
     */
    private final static Predicate<String> NOT_EMPTY_STRING = s -> false == s.isEmpty();

    /**
     * Holds all the many {@link SpreadsheetCellSortDialogComponentSpreadsheetColumnOrRowSpreadsheetComparatorNamesComponent}.
     */
    private final SpreadsheetFlexLayout columnOrRowComparatorNamesParent;

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
                        .setSaveStringValue(l.text())
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
     * {@see SpreadsheetCellSortDialogComponentContext}
     */
    private final SpreadsheetCellSortDialogComponentContext context;

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

    @Override
    public void dialogReset() {
        // NOP
    }

    // id...............................................................................................................

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    private final static String ID = "cellSort";

    final static String ID_PREFIX = ID + "-";
}
