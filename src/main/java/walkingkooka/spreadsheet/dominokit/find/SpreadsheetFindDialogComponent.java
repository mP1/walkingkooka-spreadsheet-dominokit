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

package walkingkooka.spreadsheet.dominokit.find;

import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaMatchedCellsTableComponent;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaMatchedCellsTableComponentContexts;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFindHistoryToken;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetCellRangeReferenceComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.Expression;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A modal dialog that provides form elements to perform a find with a table showing the matching cells.
 */
public final class SpreadsheetFindDialogComponent implements SpreadsheetDialogComponentLifecycle,
        LoadedSpreadsheetMetadataRequired {

    /**
     * Creates a new {@link SpreadsheetFindDialogComponent}.
     */
    public static SpreadsheetFindDialogComponent with(final SpreadsheetFindDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetFindDialogComponent(context);
    }

    private SpreadsheetFindDialogComponent(final SpreadsheetFindDialogComponentContext context) {
        this.context = context;

        this.cellRange = this.cellRange();
        this.path = this.path();
        this.query = this.query();
        this.valueType = this.valueType();

        this.find = this.anchor("Find");
        this.reset = this.anchor("Reset");

        this.table = SpreadsheetDeltaMatchedCellsTableComponent.with(
                ID,
                SpreadsheetDeltaMatchedCellsTableComponentContexts.basic(
                        context, // HistoryTokenContext
                        context //
                )
        );

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, with a form to perform a FIND.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final HistoryTokenContext context = this.context;

        return SpreadsheetDialogComponent.with(
                        ID,
                        "Find",
                        true, // includeClose
                        context
                ).appendChild(this.cellRange)
                .appendChild(this.path)
                .appendChild(this.valueType)
                .appendChild(this.query)
                .appendChild(
                        SpreadsheetFlexLayout.row()
                                .appendChild(this.find)
                                .appendChild(this.reset)
                                .appendChild(
                                        this.closeAnchor(
                                                context.historyToken()
                                        )
                                )
                ).appendChild(
                        this.table
                );
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetFindDialogComponentContext context;

    // @VisibleForTesting.
    final SpreadsheetDeltaMatchedCellsTableComponent table;

    // path.....................................................................................................

    private SpreadsheetCellRangeReferenceComponent cellRange() {
        return SpreadsheetCellRangeReferenceComponent.empty()
                .setId(ID_PREFIX + "-cell-range")
                .setLabel("Cell Range")
                .addChangeListener(this::onCellRangeValueChange)
                .required();
    }

    /**
     * Push the new {@link SpreadsheetCellRangeReference}.
     */
    private void onCellRangeValueChange(final Optional<SpreadsheetCellRangeReference> oldCellRange,
                                        final Optional<SpreadsheetCellRangeReference> newCellRange) {
        this.setAndRefresh(
                t -> t.setAnchoredSelection(
                        newCellRange.map(
                                SpreadsheetSelection::setDefaultAnchor
                        )
                ).cast(SpreadsheetCellFindHistoryToken.class)
        );
    }

    private final SpreadsheetCellRangeReferenceComponent cellRange;

    // path.............................................................................................................

    private SpreadsheetCellRangeReferencePathComponent path() {
        return SpreadsheetCellRangeReferencePathComponent.empty()
                .setId(ID_PREFIX + "-cell-range-path-Select")
                .setLabel("Cell Range Path")
                .addChangeListener(this::onCellRangePathValueChange);
    }

    private void onCellRangePathValueChange(final Optional<SpreadsheetCellRangeReferencePath> oldPath,
                                            final Optional<SpreadsheetCellRangeReferencePath> newPath) {
        this.setAndRefresh(
                t -> t.setFind(
                        t.find()
                                .setPath(newPath)
                )
        );
    }

    private final SpreadsheetCellRangeReferencePathComponent path;

    // queryTextBox.....................................................................................................

    /**
     * Creates a text box that will accept the query.
     */
    private SpreadsheetFormulaComponent query() {
        return SpreadsheetFormulaComponent.empty(
                        SpreadsheetFindDialogComponentSpreadsheetFormulaComponentParserFunction.with(this.context)
                ).setId("query-TextBox")
                .setLabel("Query")
                .addChangeListener(this::onQueryChange);
    }

    private void onQueryChange(final Optional<SpreadsheetFormula> oldFormula,
                               final Optional<SpreadsheetFormula> newFormula) {
        this.setAndRefresh(
                t -> t.setFind(
                        t.find()
                                .setQuery(newFormula.map(SpreadsheetFormula::text)
                                )
                )
        );
    }

    /**
     * The {@link SpreadsheetFormulaComponent} that holds the edited {@link Expression}.
     */
    private final SpreadsheetFormulaComponent query;

    // valueType........................................................................................................

    private SpreadsheetValueTypeComponent valueType() {
        return SpreadsheetValueTypeComponent.empty()
                .setId(ID_PREFIX + "value-type-Select")
                .setLabel("Value type")
                .addChangeListener(this::onValueTypeChange);
    }

    private void onValueTypeChange(final Optional<String> oldValue,
                                   final Optional<String> newValue) {
        this.setAndRefresh(
                t -> t.setFind(
                        t.find()
                                .setValueType(newValue)
                )
        );
    }

    private final SpreadsheetValueTypeComponent valueType;

    /**
     * Each time a component of the find is updated, a new {@link HistoryToken} is pushed, which will cause a search
     * and refresh of the UI.
     */
    private void setAndRefresh(final Function<SpreadsheetCellFindHistoryToken, HistoryToken> updater) {
        final SpreadsheetFindDialogComponentContext context = this.context;

        // if setter failed ignore, validation will eventually show an error for the field.
        HistoryToken token;
        try {
            token = updater.apply(
                    context.historyToken()
                            .cast(SpreadsheetCellFindHistoryToken.class)
            );
        } catch (final Exception ignore) {
            token = null;
        }

        // only update history token if setter was successful.
        if (token instanceof SpreadsheetCellFindHistoryToken) {
            context.pushHistoryToken(token);
        }
    }

    private void refreshFind(final SpreadsheetCellFindHistoryToken token) {
        this.find.setHistoryToken(
                Optional.of(token)
        );
    }

    private final HistoryTokenAnchorComponent find;

    private void refreshReset(final SpreadsheetCellFindHistoryToken token) {
        this.reset.setHistoryToken(
                Optional.of(
                        token.setFind(
                                token.find()
                                        .setPath(
                                                Optional.empty()
                                        ).setValueType(
                                                Optional.empty()
                                        )
                        )
                )
        );
    }

    private final HistoryTokenAnchorComponent reset;

    // SpreadsheetDialogComponentLifecycle..............................................................................

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
    }

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellFindHistoryToken;
    }

    @Override
    public void openGiveFocus(final AppContext context) {
        context.giveFocus(
                this.cellRange::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the history token changes.
     */
    @Override
    public void refresh(final AppContext context) {
        final SpreadsheetCellFindHistoryToken token = context.historyToken()
                .cast(SpreadsheetCellFindHistoryToken.class);
        context.debug("SpreadsheetFindDialogComponent.refresh " + token);

        this.cellRange.setValue(
                Optional.of(
                        token.anchoredSelection()
                                .selection()
                                .toCellRange()
                )
        );

        final SpreadsheetCellQuery find = token.find();

        this.path.setValue(
                find.path()
        );
        this.valueType.setValue(
                find.valueType()
        );
        this.query.setStringValue(
                find.query()
        );

        this.refreshFind(token);
        this.refreshReset(token);

        this.findCells();
    }

    /**
     * Copies the parameters from the current {@link HistoryToken} assuming its a {@link SpreadsheetCellFindHistoryToken}
     * and performs a {@link walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher#findCells(SpreadsheetId, SpreadsheetCellRangeReference, SpreadsheetCellQuery)}.
     */
    private void findCells() {
        final SpreadsheetFindDialogComponentContext context = this.context;

        final SpreadsheetCellFindHistoryToken historyToken = context.historyToken()
                .cast(SpreadsheetCellFindHistoryToken.class);

        final SpreadsheetId id = historyToken.id();
        final SpreadsheetCellRangeReference cells = historyToken.anchoredSelection()
                .selection()
                .toCellRange();

        context.spreadsheetDeltaFetcher()
                .findCells(
                        id,
                        cells,
                        historyToken.find()
                );
    }

    // UI...............................................................................................................

    private final static String ID = "find";

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
