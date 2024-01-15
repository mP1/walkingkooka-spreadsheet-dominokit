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

package walkingkooka.spreadsheet.dominokit.ui.find;

import elemental2.dom.Event;
import elemental2.dom.EventListener;
import elemental2.dom.Node;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.datatable.CellRenderer;
import org.dominokit.domino.ui.datatable.CellRenderer.CellInfo;
import org.dominokit.domino.ui.datatable.CellTextAlign;
import org.dominokit.domino.ui.datatable.ColumnConfig;
import org.dominokit.domino.ui.datatable.DataTable;
import org.dominokit.domino.ui.datatable.TableConfig;
import org.dominokit.domino.ui.datatable.plugins.pagination.BodyScrollPlugin;
import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
import org.dominokit.domino.ui.events.EventType;
import org.dominokit.domino.ui.style.Elevation;
import org.dominokit.domino.ui.style.StyleType;
import org.dominokit.domino.ui.utils.ElementsFactory;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.dom.Doms;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFindHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.Anchor;
import walkingkooka.spreadsheet.dominokit.ui.SpreadsheetIds;
import walkingkooka.spreadsheet.dominokit.ui.cellrange.SpreadsheetCellRangeComponent;
import walkingkooka.spreadsheet.dominokit.ui.cellrangepath.SpreadsheetCellRangePathComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.ui.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.formula.SpreadsheetFormulaComponent;
import walkingkooka.spreadsheet.dominokit.ui.spreadsheetvaluetype.SpreadsheetValueTypeComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.expression.Expression;
import walkingkooka.tree.text.TextNode;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;

import static org.dominokit.domino.ui.utils.Domino.div;
import static org.dominokit.domino.ui.utils.Domino.dui_p_2;

/**
 * A modal dialog that provides form elements to perform a find with a table showing the matching cells.
 */
public final class SpreadsheetFindComponent implements SpreadsheetDialogComponentLifecycle,
        NopFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher {

    /**
     * Creates a new {@link SpreadsheetFindComponent}.
     */
    public static SpreadsheetFindComponent with(final SpreadsheetFindComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetFindComponent(context);
    }

    private SpreadsheetFindComponent(final SpreadsheetFindComponentContext context) {
        this.context = context;

        this.cellRange = this.cellRange();
        this.path = this.path();
        this.query = this.query();
        this.valueType = this.valueType();

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, with a form to perform a FIND.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetDialogComponent dialog = SpreadsheetDialogComponent.create(this.context);
        dialog.setTitle("Find");
        dialog.id(ID);

        dialog.appendChild(this.cellRange);
        dialog.appendChild(this.path);
        dialog.appendChild(this.valueType);
        dialog.appendChild(this.query);

        dialog.appendChild(
                ElementsFactory.elements.div()
                        .appendChild(this.findButton())
                        .appendChild(this.resetButton())
                        .appendChild(this.closeButton())
        );

        dialog.appendChild(
                div()
                        .addCss(dui_p_2)
                        .appendChild(
                                this.dataTable()
                        )
        );

        return dialog;
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetFindComponentContext context;

    private DataTable<SpreadsheetCell> dataTable() {
        return new DataTable<>(
                this.tableConfig(),
                this.cellDataStore
        );
    }

    /**
     * The table showing matching cells will have four columns.
     * <pre>
     * cell | formula | value | formatted
     * </pre>
     */
    private TableConfig<SpreadsheetCell> tableConfig() {
        return new TableConfig<SpreadsheetCell>()
                .addColumn(
                        columnConfig(
                                "Cell",
                                CellTextAlign.LEFT,
                                this::renderCellReference
                        )
                ).addColumn(
                        columnConfig(
                                "Formula",
                                CellTextAlign.LEFT,
                                this::renderCellFormula
                        )
                ).addColumn(
                        columnConfig(
                                "Formatted",
                                CellTextAlign.LEFT,
                                this::renderCellFormatted
                        )
                ).addColumn(
                        columnConfig(
                                "Value",
                                CellTextAlign.LEFT,
                                this::renderCellValue
                        )
                ).addPlugin(new BodyScrollPlugin<>());
    }

    private ColumnConfig<SpreadsheetCell> columnConfig(final String title,
                                                       final CellTextAlign cellTextAlign,
                                                       final CellRenderer<SpreadsheetCell> renderer) {
        return ColumnConfig.<SpreadsheetCell>create(title.toLowerCase(), title)
                .setTextAlign(cellTextAlign)
                .setCellRenderer(renderer);
    }

    private Node renderCellReference(final CellInfo<SpreadsheetCell> info) {
        final SpreadsheetCell cell = info.getRecord();
        final HistoryToken historyToken = this.context.historyToken();

        return Anchor.empty()
                .setTextContent(cell.reference().text())
                .setHistoryToken(
                        Optional.of(
                                historyToken.clearSelection()
                                        .setCell(cell.reference())
                        )
                ).element();
    }

    private Node renderCellFormula(final CellInfo<SpreadsheetCell> info) {
        final SpreadsheetCell cell = info.getRecord();
        final HistoryToken historyToken = this.context.historyToken();

        return Anchor.empty()
                .setTextContent(cell.formula().text())
                .setHistoryToken(
                        Optional.of(
                                historyToken.clearSelection()
                                        .setCell(cell.reference())
                                        .setFormula()
                        )
                ).element();
    }

    private Node renderCellFormatted(final CellInfo<SpreadsheetCell> cell) {
        return Doms.node(
                cell.getRecord()
                        .formatted()
                        .orElse(TextNode.EMPTY_TEXT)
        );
    }

    private Node renderCellValue(final CellInfo<SpreadsheetCell> cell) {
        return Doms.textNode(
                        cell.getRecord()
                                .formula()
                                .value()
                                .map(Object::toString)
                                .orElse("")
        );
    }

    /**
     * Holds all the cells from the last search
     */
    private final LocalListDataStore<SpreadsheetCell> cellDataStore = new LocalListDataStore<>();

    // SpreadsheetDeltaWatcher.........................................................................................

    /**
     * Replaces the cells in the {@link #cellDataStore}.
     */
    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        final List<SpreadsheetCell> cells = Lists.array();
        cells.addAll(delta.cells());
        this.cellDataStore.setData(cells);
    }

    // path.....................................................................................................

    private SpreadsheetCellRangeComponent cellRange() {
        return SpreadsheetCellRangeComponent.empty()
                .setId(ID_PREFIX + "-cell-range")
                .setLabel("Cell Range")
                .addChangeListener(this::onCellRangeValueChange)
                .required();
    }

    /**
     * Push the new {@link SpreadsheetCellRange}.
     */
    private void onCellRangeValueChange(final Optional<SpreadsheetCellRange> oldCellRange,
                                        final Optional<SpreadsheetCellRange> newCellRange) {
        this.historyTokenSetAndPush(
                t -> t.setSelection(
                        newCellRange.map(
                                SpreadsheetSelection::setDefaultAnchor
                        )
                ).cast(SpreadsheetCellFindHistoryToken.class)
        );
    }

    private final SpreadsheetCellRangeComponent cellRange;

    // path.............................................................................................................

    private SpreadsheetCellRangePathComponent path() {
        return SpreadsheetCellRangePathComponent.empty()
                .setId(ID_PREFIX + "-cell-range-path-Select")
                .setLabel("Cell Range Path")
                .addChangeListener(this::onCellRangePathValueChange);
    }

    private void onCellRangePathValueChange(final Optional<SpreadsheetCellRangePath> oldPath,
                                            final Optional<SpreadsheetCellRangePath> newPath) {
        this.historyTokenSetAndPush(
                t -> t.setPath(newPath)
        );
    }

    private final SpreadsheetCellRangePathComponent path;

    // queryTextBox.....................................................................................................

    /**
     * Creates a text box that will accept the query.
     */
    private SpreadsheetFormulaComponent query() {
        return SpreadsheetFormulaComponent.empty(
                        SpreadsheetFindComponentSpreadsheetFormulaComponentParserFunction.with(this.context)
                ).setId("query-TextBox")
                .setLabel("Query")
                .addChangeListener(this::onQueryChange);
    }

    private void onQueryChange(final Optional<SpreadsheetFormula> oldFormula,
                               final Optional<SpreadsheetFormula> newFormula) {
        this.historyTokenSetAndPush(
                t -> t.setQuery(newFormula.map(SpreadsheetFormula::text))
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
        this.historyTokenSetAndPush(
                t -> t.setValueType(newValue)
        );
    }

    private final SpreadsheetValueTypeComponent valueType;

    // buttons..........................................................................................................

    /**
     * When clicked the CLOSE button invokes {@link SpreadsheetFindComponentContext#close}.
     */
    private Button closeButton() {
        return this.button(
                "Close",
                StyleType.DANGER,
                this::onCloseButtonClick
        );
    }

    private void onCloseButtonClick(final Event event) {
        final SpreadsheetFindComponentContext context = this.context;
        context.debug("SpreadsheetFindComponent.onCloseButtonClick");
        context.close();
    }

    /**
     * When clicked initiates a find cells using the given parameters.
     */
    private Button findButton() {
        return this.button(
                "Find",
                StyleType.DEFAULT,
                this::onFindButtonClick
        );
    }

    private void onFindButtonClick(final Event event) {
        this.find();
    }

    /**
     * Copies the parameters from the current {@link HistoryToken} assuming its a {@link SpreadsheetCellFindHistoryToken}
     * and performs a {@link walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher#findCells(SpreadsheetId, SpreadsheetCellRange, Optional, OptionalInt, OptionalInt, Optional, Optional)}.
     */
    private void find() {
        final SpreadsheetFindComponentContext context = this.context;

        final SpreadsheetCellFindHistoryToken historyToken = context.historyToken()
                .cast(SpreadsheetCellFindHistoryToken.class);

        final SpreadsheetId id = historyToken.id();
        final SpreadsheetCellRange cells = historyToken.selection()
                .selection()
                .toCellRange();
        final Optional<SpreadsheetCellRangePath> path = historyToken.path();
        final OptionalInt offset = historyToken.offset();
        final OptionalInt max = historyToken.max();
        final Optional<String> valueType = historyToken.valueType();
        final Optional<String> query = historyToken.query();

        context.spreadsheetDeltaFetcher()
                .findCells(
                        id,
                        cells,
                        path,
                        offset,
                        max,
                        valueType,
                        query
                );
    }

    /**
     * When clicked the RESET button invokes {@link #onResetButtonClick}.
     */
    private Button resetButton() {
        return this.button(
                "reset",
                StyleType.PRIMARY,
                this::onResetButtonClick
        );
    }

    /**
     * Resets the form.
     */
    private void onResetButtonClick(final Event event) {
        this.resetForm();
    }

    private void resetForm() {
        this.path.setValue(Optional.empty());
        this.valueType.setValue(Optional.empty());
    }

    /**
     * Creates one of the modal action buttons that appear at the bottom of the modal dialog.
     */
    private Button button(final String text,
                          final StyleType type,
                          final EventListener listener) {
        final Button button = new Button(text);

        button.id(ID_PREFIX + text.toLowerCase() + SpreadsheetIds.BUTTON);
        button.addCss("dui-" + type.getStyle());
        button.elevate(Elevation.LEVEL_1);

        button.addEventListener(
                EventType.click,
                listener
        );

        return button;
    }

    // SpreadsheetDialogComponentLifecycle..............................................................................

    @Override
    public SpreadsheetDialogComponent dialog() {
        return this.dialog;
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
        context.debug("SpreadsheetFindComponent.refresh " + context.historyToken());

        final SpreadsheetCellFindHistoryToken token = context.historyToken()
                .cast(SpreadsheetCellFindHistoryToken.class);
        context.debug("SpreadsheetFindComponent.refresh " + token);

        this.cellRange.setValue(
                Optional.of(
                        token.selection()
                                .selection()
                                .toCellRange()
                )
        );
        this.path.setValue(
                token.path()
        );
        this.valueType.setValue(
                token.valueType()
        );
        this.query.setStringValue(
            token.query()
        );

        this.find();
    }

    // History.........................................................................................................

    private void historyTokenSetAndPush(final Function<SpreadsheetCellFindHistoryToken, HistoryToken> updater) {
        final SpreadsheetFindComponentContext context = this.context;

        // if setter failed ignore, validation will eventually show an error for the field.
        HistoryToken token = null;
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

    // UI...............................................................................................................

    private final static String ID = "find";

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
