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

package walkingkooka.spreadsheet.dominokit.query;

import walkingkooka.collect.list.Lists;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.anchor.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaCellsTableComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.DialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.grid.FourColumnComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellQueryHistoryToken;
import walkingkooka.spreadsheet.dominokit.query.condition.ConditionRightSpreadsheetFormulaParserTokenComponent;
import walkingkooka.spreadsheet.dominokit.query.textmatch.TextMatchComponent;
import walkingkooka.spreadsheet.dominokit.value.cell.SpreadsheetCellRangeReferenceComponent;
import walkingkooka.spreadsheet.dominokit.value.formula.SpreadsheetFormulaComponent;
import walkingkooka.spreadsheet.dominokit.value.formula.SpreadsheetFormulaComponentFunctions;
import walkingkooka.spreadsheet.dominokit.value.valuetype.ValueTypeEditComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQueryRequest;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.text.CaseKind;
import walkingkooka.tree.expression.Expression;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A modal dialog that provides form elements to perform a query with a table showing the matching cells.
 */
public final class SpreadsheetQueryDialogComponent implements DialogComponentLifecycle,
    LoadedSpreadsheetMetadataRequired {

    /**
     * Creates a new {@link SpreadsheetQueryDialogComponent}.
     */
    public static SpreadsheetQueryDialogComponent with(final SpreadsheetQueryDialogComponentContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetQueryDialogComponent(context);
    }

    private SpreadsheetQueryDialogComponent(final SpreadsheetQueryDialogComponentContext context) {
        this.context = context;

        this.cellRange = this.cellRange();
        this.path = this.path();
        this.query = this.query();
        this.valueType = this.valueType();

        this.formula = this.formula();

        this.currency = this.currency();
        this.dateTimeSymbols = this.dateTimeSymbols();
        this.decimalNumberSymbols = this.decimalNumberSymbols();
        this.formatter = this.formatter();
        this.locale = this.locale();
        this.parser = this.parser();
        this.style = this.style();
        this.value = this.value();
        this.validator = this.validator();
        this.formattedValue = this.formattedValue();

        this.execute = this.anchor("Execute");
        this.reset = this.anchor("Reset");
        this.loadHighlightingQuery = this.anchor("Load Highlighting Query");
        this.saveAsHighlightingQuery = this.anchor("Save as Highlighting Query");
        this.close = this.closeAnchor();

        this.table = SpreadsheetDeltaCellsTableComponent.with(
            ID_PREFIX,
            context // SpreadsheetDeltaCellsTableComponentContext
        );

        this.dialog = this.dialogCreate();

        context.addHistoryTokenWatcher(this);
    }

    // dialog...........................................................................................................

    /**
     * Creates the modal dialog, which holds first the form including the query field, links and then the table showing
     * the matching cells.
     */
    private DialogComponent dialogCreate() {
        final SpreadsheetQueryDialogComponentContext context = this.context;

        return DialogComponent.largeList(
                ID + SpreadsheetElementIds.DIALOG,
                DialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(
                FourColumnComponent.empty()
                    .appendChildren(
                        Lists.of(
                            this.cellRange,
                            this.path,
                            this.valueType,
                            this.formula,
                            this.currency,
                            this.dateTimeSymbols,
                            this.decimalNumberSymbols,
                            this.formatter,
                            this.locale,
                            this.parser,
                            this.style,
                            this.value,
                            this.validator,
                            this.formattedValue
                        )
                    )
            ).appendChild(this.query)
            .appendChild(
                AnchorListComponent.empty()
                    .setCssProperty("margin-top", "5px")
                    .setCssProperty("margin-left", "-5px")
                    .appendChild(this.execute)
                    .appendChild(this.reset)
                    .appendChild(this.loadHighlightingQuery)
                    .appendChild(this.saveAsHighlightingQuery)
                    .appendChild(this.close)
            ).appendChild(this.table);
    }

    private final DialogComponent dialog;

    private final SpreadsheetQueryDialogComponentContext context;

    // table............................................................................................................

    private void refreshTable(final HistoryToken token) {
        this.table.refresh(token);
    }

    // @VisibleForTesting.
    final SpreadsheetDeltaCellsTableComponent table;

    // path.....................................................................................................

    private SpreadsheetCellRangeReferenceComponent cellRange() {
        return SpreadsheetCellRangeReferenceComponent.with(
                ID_PREFIX + "cellRange" + SpreadsheetElementIds.TEXT_BOX
            ).setLabel("Cell Range")
            .addValueWatcher2(value -> this.setAndPushHistoryToken(
                t -> t.setSelection(value)
                    .setQuery(
                        t.query()
                    )
            ))
            .required();
    }

    private final SpreadsheetCellRangeReferenceComponent cellRange;

    // path.............................................................................................................

    private SpreadsheetCellRangeReferencePathComponent path() {
        return SpreadsheetCellRangeReferencePathComponent.empty(
                ID_PREFIX + "cellRangePath-",
                this.context
            ).setLabel("Cell Range Path")
            .addValueWatcher2(
                (Optional<SpreadsheetCellRangeReferencePath> value) -> this.setAndPushHistoryToken(
                    t -> t.setQuery(
                        t.query()
                            .setPath(value)
                    )
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
                SpreadsheetFormulaComponentFunctions.expressionParser(
                    this::spreadsheetParserContext
                )
            ).setId(ID_PREFIX + "query" + SpreadsheetElementIds.TEXT_BOX)
            .setLabel("Query")
            .addValueWatcher2(
                (v) -> this.setAndPushHistoryToken(
                    t -> t.setQuery(
                        t.query()
                            .setQuery(
                                v.map(f -> SpreadsheetCellQuery.parse(f.text()))
                            )
                    )
                )
            );
    }

    /**
     * The {@link SpreadsheetFormulaComponent} that holds the edited {@link Expression}.
     */
    // VisibleForTesting
    final SpreadsheetFormulaComponent query;

    /**
     * Reconstructs the query from the other fields in the form, then updates the FIND link and performs a FIND.
     */
    private void refreshQueryAndFindFromWizardFieldsAndServerFind(final Optional<?> ignored) {
        final Optional<SpreadsheetFormula> formula = SpreadsheetQueryDialogComponentQuery.query(
            this.context.historyToken()
                .cast(SpreadsheetCellQueryHistoryToken.class)
                .query()
                .query(),
            this.formula.value(),
            this.currency.value(),
            this.dateTimeSymbols.value(),
            this.decimalNumberSymbols.value(),
            this.formatter.value(),
            this.locale.value(),
            this.parser.value(),
            this.style.value(),
            this.value.value(),
            this.validator.value(),
            this.formattedValue.value()
        );

        this.query.setValue(formula);

        SpreadsheetCellQuery query = null;
        if (formula.isPresent()) {
            query = formula.get()
                .token()
                .map(SpreadsheetCellQuery::with)
                .orElse(null);
        }

        final SpreadsheetCellQueryRequest cellFindQuery = SpreadsheetCellQueryRequest.empty()
            .setPath(this.path.value())
            .setValueType(this.valueType.value())
            .setQuery(
                Optional.ofNullable(query)
            );

        this.refreshExecute(
            this.context.historyToken()
                .cast(SpreadsheetCellQueryHistoryToken.class)
                .setSelection(
                    this.cellRange.value()
                        .map(SpreadsheetSelection::toScalarIfUnit)
                ).setQuery(cellFindQuery)
                .cast(SpreadsheetCellQueryHistoryToken.class)
        );
        this.findCells(cellFindQuery);
    }

    // valueType........................................................................................................

    private ValueTypeEditComponent valueType() {
        return ValueTypeEditComponent.empty(
                ID_PREFIX + "valueType-",
                this.context
            ).setLabel("Value type")
            .addValueWatcher2(
                this::refreshQueryAndFindFromWizardFieldsAndServerFind
            );
    }

    private final ValueTypeEditComponent valueType;

    private SpreadsheetParserContext spreadsheetParserContext() {
        final SpreadsheetQueryDialogComponentContext context = this.context;
        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();

        return metadata.spreadsheetParserContext(
            SpreadsheetMetadata.NO_CELL,
            LocaleContexts.fake(),
            context
        );
    }

    /**
     * Each time a component of the find is updated, a new {@link HistoryToken} is pushed, which will cause a search
     * and refresh of the UI.
     */
    private void setAndPushHistoryToken(final Function<SpreadsheetCellQueryHistoryToken, HistoryToken> historyTokenSetter) {
        final SpreadsheetQueryDialogComponentContext context = this.context;

        // if setter failed ignore, validation will eventually show an error for the field.
        HistoryToken token;
        try {
            token = historyTokenSetter.apply(
                context.historyToken()
                    .cast(SpreadsheetCellQueryHistoryToken.class)
            );
        } catch (final UnsupportedOperationException rethrow) {
            throw rethrow;
        } catch (final RuntimeException ignore) {
            token = null;
        }

        // only update history token if setter was successful.
        if (token instanceof SpreadsheetCellQueryHistoryToken) {
            context.pushHistoryToken(token);
        }
    }

    // formula..........................................................................................................

    private TextMatchComponent formula() {
        return textMatchComponent(
            "Formula"
        );
    }

    final TextMatchComponent formula;

    // currency.........................................................................................................

    private TextMatchComponent currency() {
        return textMatchComponent(
            "Currency"
        );
    }

    final TextMatchComponent currency;

    // formatter..........................................................................................................

    private TextMatchComponent dateTimeSymbols() {
        return textMatchComponent(
            "Date Time Symbols"
        );
    }

    final TextMatchComponent dateTimeSymbols;

    // decimalNumberSymbols.............................................................................................

    private TextMatchComponent decimalNumberSymbols() {
        return textMatchComponent(
            "Decimal Number Symbols"
        );
    }

    final TextMatchComponent decimalNumberSymbols;

    // formatter..........................................................................................................

    private TextMatchComponent formatter() {
        return textMatchComponent(
            "Formatter"
        );
    }

    final TextMatchComponent formatter;

    // locale...........................................................................................................

    private TextMatchComponent locale() {
        return textMatchComponent(
            "Locale"
        );
    }

    final TextMatchComponent locale;
    
    // parser...........................................................................................................

    private TextMatchComponent parser() {
        return textMatchComponent(
            "Parser"
        );
    }

    final TextMatchComponent parser;

    // style............................................................................................................

    private TextMatchComponent style() {
        return textMatchComponent(
            "Style"
        );
    }

    final TextMatchComponent style;

    // value............................................................................................................

    private ConditionRightSpreadsheetFormulaParserTokenComponent value() {
        return ConditionRightSpreadsheetFormulaParserTokenComponent.empty(
                this::spreadsheetParserContext
            ).setId(ID_PREFIX + "value" + SpreadsheetElementIds.TEXT_BOX)
            .setLabel("Value")
            .optional()
            .addValueWatcher2(
                this::refreshQueryAndFindFromWizardFieldsAndServerFind
            );
    }

    final ConditionRightSpreadsheetFormulaParserTokenComponent value;

    // validator........................................................................................................

    private TextMatchComponent validator() {
        return textMatchComponent(
            "Validator"
        );
    }

    final TextMatchComponent validator;

    // formattedValue...................................................................................................

    private TextMatchComponent formattedValue() {
        return textMatchComponent(
            "Formatted"
        );
    }

    final TextMatchComponent formattedValue;

    // TextMatchComponent...............................................................................................

    /**
     * Factory that creates a {@link TextMatchComponent} with the given label and listener.
     * This is intended to only be used by Wizard fields that contribute to the query by some transformation
     */
    private TextMatchComponent textMatchComponent(final String label) {
        return TextMatchComponent.empty()
            .setId(
                ID_PREFIX +
                    CaseKind.TITLE.change(
                        label,
                        CaseKind.CAMEL
                    ) +
                    SpreadsheetElementIds.TEXT_BOX
            ).setLabel(label)
            .optional()
            .addValueWatcher2(this::refreshQueryAndFindFromWizardFieldsAndServerFind);
    }

    // find.............................................................................................................

    private void refreshExecute(final SpreadsheetCellQueryHistoryToken historyToken) {
        this.execute.setHistoryToken(
            Optional.of(historyToken)
        );
    }

    private final HistoryTokenAnchorComponent execute;

    // reset............................................................................................................

    private void refreshReset(final SpreadsheetCellQueryHistoryToken historyToken) {
        this.reset.setHistoryToken(
            Optional.of(
                historyToken.setQuery(
                    historyToken.query()
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

    // loadHighlightingQuery............................................................................................

    /**
     * If a {@link SpreadsheetMetadataPropertyName#QUERY} is present, enable the anchor with the query, otherwise
     * disable it.
     */
    private void refreshLoadHighlightingQuery(final SpreadsheetCellQueryHistoryToken token) {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();
        final SpreadsheetCellQuery highlightingQuery = metadata.get(SpreadsheetMetadataPropertyName.QUERY)
            .orElse(null);

        this.loadHighlightingQuery.setHistoryToken(
            Optional.ofNullable(
                null != highlightingQuery ?
                    token.setQuery(
                        token.query()
                            .setQuery(
                                Optional.of(highlightingQuery)
                            )
                    ) :
                    null
            )
        );
    }

    private final HistoryTokenAnchorComponent loadHighlightingQuery;

    // saveAsHighlightingQuery..........................................................................................

    /**
     * Creates a link which will save the QUERY text box value as it will be used as the highlighting query.
     */
    private void refreshSaveAsHighlightingQuery(final SpreadsheetCellQueryHistoryToken token) {
        final Optional<SpreadsheetCellQuery> query = token.query()
            .query();

        this.saveAsHighlightingQuery.setHistoryToken(
            Optional.ofNullable(
                query.isPresent() ?
                    token.setMetadataPropertyName(SpreadsheetMetadataPropertyName.QUERY)
                        .setSaveValue(query) :
                    null
            )
        );
    }

    private final HistoryTokenAnchorComponent saveAsHighlightingQuery;

    // close............................................................................................................

    private void refreshClose(final SpreadsheetCellQueryHistoryToken token) {
        this.close.setHistoryToken(
            Optional.of(token.close())
        );
    }

    private final HistoryTokenAnchorComponent close;

    // DialogComponentLifecycle..............................................................................

    @Override
    public DialogComponent dialog() {
        return this.dialog;
    }

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return token.isSave();
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellQueryHistoryToken;
    }

    @Override
    public void dialogReset() {
        this.table.clear();
    }

    @Override
    public void openGiveFocus(final RefreshContext context) {
        context.giveFocus(
            this.cellRange::focus
        );
    }

    /**
     * Refreshes the widget, typically done when the history token changes.
     */
    @Override
    public void refresh(final RefreshContext context) {
        this.context.refreshDialogTitle(this);

        final SpreadsheetCellQueryHistoryToken historyToken = context.historyToken()
            .cast(SpreadsheetCellQueryHistoryToken.class);

        this.refreshTable(historyToken);

        this.cellRange.setValue(
            Optional.of(
                historyToken.anchoredSelection()
                    .selection()
                    .toCellRange()
            )
        );

        final SpreadsheetCellQueryRequest queryRequest = historyToken.query();

        this.path.setValue(
            queryRequest.path()
        );
        this.valueType.setValue(
            queryRequest.valueType()
        );

        this.value.validate();

        final Optional<SpreadsheetCellQuery> query = queryRequest.query();
        this.query.setStringValue(
            query.map(SpreadsheetCellQuery::text)
        );
        final SpreadsheetCellQuery queryOrNull = query.orElse(null);
        if (null != queryOrNull) {
            SpreadsheetQueryDialogComponentSpreadsheetFormulaParserTokenVisitor.refresh(
                queryOrNull.parserToken(),
                this
            );
        }

        this.refreshExecute(historyToken);
        this.refreshReset(historyToken);
        this.refreshLoadHighlightingQuery(historyToken);
        this.refreshSaveAsHighlightingQuery(historyToken);
        this.refreshClose(historyToken);

        this.findCells(queryRequest);
    }

    /**
     * Copies the parameters from the current {@link HistoryToken} assuming its a {@link SpreadsheetCellQueryHistoryToken}
     * and performs a {@link walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher#getFindCells(SpreadsheetId, SpreadsheetCellRangeReference, SpreadsheetCellQueryRequest)}.
     */
    private void findCells(final SpreadsheetCellQueryRequest query) {
        final SpreadsheetQueryDialogComponentContext context = this.context;

        final SpreadsheetCellQueryHistoryToken historyToken = context.historyToken()
            .cast(SpreadsheetCellQueryHistoryToken.class);

        final SpreadsheetId id = historyToken.spreadsheetId();
        final SpreadsheetCellRangeReference cells = historyToken.anchoredSelection()
            .selection()
            .toCellRange();

        context.findCells(
            id,
            cells,
            query
        );
    }

    @Override
    public boolean shouldLogLifecycleChanges() {
        return SPREADSHEET_CELL_QUERY_DIALOG_COMPONENT;
    }

    // UI...............................................................................................................

    private final static String ID = SpreadsheetCellQuery.class.getSimpleName();

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
