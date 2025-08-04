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

import walkingkooka.locale.LocaleContexts;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.RefreshContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.cell.SpreadsheetCellRangeReferenceComponent;
import walkingkooka.spreadsheet.dominokit.condition.ConditionRightSpreadsheetFormulaParserTokenComponent;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaCellsTableComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaComponent;
import walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaComponentFunctions;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFindHistoryToken;
import walkingkooka.spreadsheet.dominokit.link.AnchorListComponent;
import walkingkooka.spreadsheet.dominokit.row.SpreadsheetRowComponent;
import walkingkooka.spreadsheet.dominokit.textmatch.TextMatchComponent;
import walkingkooka.spreadsheet.dominokit.valuetype.SpreadsheetValueTypeComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.formula.parser.ConditionRightSpreadsheetFormulaParserToken;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetParserContext;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReferencePath;
import walkingkooka.text.CaseKind;
import walkingkooka.tree.expression.Expression;
import walkingkooka.validation.ValidationValueTypeName;

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

        this.formula = this.formula();
        this.dateTimeSymbols = this.dateTimeSymbols();
        this.decimalNumberSymbols = this.decimalNumberSymbols();
        this.formatter = this.formatter();
        this.parser = this.parser();
        this.style = this.style();
        this.value = this.value();
        this.validator = this.validator();
        this.formattedValue = this.formattedValue();

        this.find = this.anchor("Find");
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
    private SpreadsheetDialogComponent dialogCreate() {
        final SpreadsheetFindDialogComponentContext context = this.context;

        return SpreadsheetDialogComponent.largeList(
                ID + SpreadsheetElementIds.DIALOG,
                SpreadsheetDialogComponent.INCLUDE_CLOSE,
                context
            ).appendChild(
                SpreadsheetRowComponent.columnSpan3()
                    .appendChild(this.cellRange)
                    .appendChild(this.path)
                    .appendChild(this.valueType)
                    .appendChild(this.formula)
            ).appendChild(
                SpreadsheetRowComponent.columnSpan3()
                    .appendChild(this.dateTimeSymbols)
                    .appendChild(this.decimalNumberSymbols)
                    .appendChild(this.formatter)
                    .appendChild(this.parser)
            ).appendChild(
                SpreadsheetRowComponent.columnSpan3()
                    .appendChild(this.style)
                    .appendChild(this.value)
                    .appendChild(this.validator)
                    .appendChild(this.formattedValue)
            ).appendChild(this.query)
            .appendChild(
                AnchorListComponent.empty()
                    .setCssProperty("margin-top", "5px")
                    .setCssProperty("margin-left", "-5px")
                    .appendChild(this.find)
                    .appendChild(this.reset)
                    .appendChild(this.loadHighlightingQuery)
                    .appendChild(this.saveAsHighlightingQuery)
                    .appendChild(this.close)
            ).appendChild(this.table);
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetFindDialogComponentContext context;

    // table............................................................................................................

    private void refreshTable(final HistoryToken token) {
        this.table.refresh(token);
    }

    // @VisibleForTesting.
    final SpreadsheetDeltaCellsTableComponent table;

    // path.....................................................................................................

    private SpreadsheetCellRangeReferenceComponent cellRange() {
        return SpreadsheetCellRangeReferenceComponent.with(
                ID_PREFIX + "cell-range" + SpreadsheetElementIds.TEXT_BOX
            ).setLabel("Cell Range")
            .addChangeListener(this::onCellRangeValueChange)
            .required();
    }

    /**
     * Push the new {@link SpreadsheetCellRangeReference} keeping the original {@link SpreadsheetCellFindQuery}.
     */
    private void onCellRangeValueChange(final Optional<SpreadsheetCellRangeReference> oldCellRange,
                                        final Optional<SpreadsheetCellRangeReference> newCellRange) {
        this.setAndRefresh(
            t -> t.setSelection(newCellRange)
                .setQuery(
                    t.query()
                )
        );
    }

    private final SpreadsheetCellRangeReferenceComponent cellRange;

    // path.............................................................................................................

    private SpreadsheetCellRangeReferencePathComponent path() {
        return SpreadsheetCellRangeReferencePathComponent.empty()
            .setId(ID_PREFIX + "cell-range-path" + SpreadsheetElementIds.SELECT)
            .setLabel("Cell Range Path")
            .addChangeListener(this::onCellRangePathValueChange);
    }

    private void onCellRangePathValueChange(final Optional<SpreadsheetCellRangeReferencePath> oldPath,
                                            final Optional<SpreadsheetCellRangeReferencePath> newPath) {
        this.setAndRefresh(
            t -> t.setQuery(
                t.query()
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
                SpreadsheetFormulaComponentFunctions.expressionParser(
                    this::spreadsheetParserContext
                )
            ).setId("query" + SpreadsheetElementIds.TEXT_BOX)
            .setLabel("Query")
            .addChangeListener(this::onQueryChange);
    }

    private void onQueryChange(final Optional<SpreadsheetFormula> oldFormula,
                               final Optional<SpreadsheetFormula> newFormula) {
        this.setAndRefresh(
            t -> t.setQuery(
                t.query()
                    .setQuery(
                        newFormula.map(f -> SpreadsheetCellQuery.parse(f.text()))
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
    private void refreshQueryAndFindFromWizardFieldsAndServerFind(final Optional<?> old,
                                                                  final Optional<?> newAlsoIgnored) {
        final Optional<SpreadsheetFormula> formula = SpreadsheetFindDialogComponentQuery.query(
            this.context.historyToken()
                .cast(SpreadsheetCellFindHistoryToken.class)
                .query()
                .query(),
            this.formula.value(),
            this.dateTimeSymbols.value(),
            this.decimalNumberSymbols.value(),
            this.formatter.value(),
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

        final SpreadsheetCellFindQuery cellFindQuery = SpreadsheetCellFindQuery.empty()
            .setPath(this.path.value())
            .setValueType(this.valueType.value())
            .setQuery(
                Optional.ofNullable(query)
            );

        this.refreshFind(
            this.context.historyToken()
                .cast(SpreadsheetCellFindHistoryToken.class)
                .setSelection(
                    this.cellRange.value()
                ).setQuery(cellFindQuery)
                .cast(SpreadsheetCellFindHistoryToken.class)
        );
        this.findCells(cellFindQuery);
    }

    // valueType........................................................................................................

    private SpreadsheetValueTypeComponent valueType() {
        return SpreadsheetValueTypeComponent.empty()
            .setId(ID_PREFIX + "value-type" + SpreadsheetElementIds.SELECT)
            .setLabel("Value type")
            .addChangeListener(this::onValueTypeChange);
    }

    private void onValueTypeChange(final Optional<ValidationValueTypeName> oldValue,
                                   final Optional<ValidationValueTypeName> newValue) {
        this.setAndRefresh(
            t -> t.setQuery(
                t.query()
                    .setValueType(newValue)
            )
        );
    }

    private final SpreadsheetValueTypeComponent valueType;

    private SpreadsheetParserContext spreadsheetParserContext() {
        final SpreadsheetFindDialogComponentContext context = this.context;
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
    private void setAndRefresh(final Function<SpreadsheetCellFindHistoryToken, HistoryToken> historyTokenSetter) {
        final SpreadsheetFindDialogComponentContext context = this.context;

        // if setter failed ignore, validation will eventually show an error for the field.
        HistoryToken token;
        try {
            token = historyTokenSetter.apply(
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

    // formula..........................................................................................................

    private TextMatchComponent formula() {
        return textMatchComponent(
            "Formula"
        );
    }

    final TextMatchComponent formula;

    // formatter..........................................................................................................

    private TextMatchComponent dateTimeSymbols() {
        return textMatchComponent(
            "DateTimeSymbols"
        );
    }

    final TextMatchComponent dateTimeSymbols;

    // decimalNumberSymbols.............................................................................................

    private TextMatchComponent decimalNumberSymbols() {
        return textMatchComponent(
            "decimalNumberSymbols"
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
            .addChangeListener(this::onValueValueChange);
    }

    private void onValueValueChange(final Optional<ConditionRightSpreadsheetFormulaParserToken> old,
                                    final Optional<ConditionRightSpreadsheetFormulaParserToken> newValue) {
        this.refreshQueryAndFindFromWizardFieldsAndServerFind(
            old,
            newValue
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
                        CaseKind.KEBAB
                    ) +
                    SpreadsheetElementIds.TEXT_BOX
            ).setLabel(label)
            .addChangeListener(this::refreshQueryAndFindFromWizardFieldsAndServerFind);
    }

    // find.............................................................................................................

    private void refreshFind(final SpreadsheetCellFindHistoryToken historyToken) {
        this.find.setHistoryToken(
            Optional.of(historyToken)
        );
    }

    private final HistoryTokenAnchorComponent find;

    // reset............................................................................................................

    private void refreshReset(final SpreadsheetCellFindHistoryToken historyToken) {
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
     * If a {@link SpreadsheetMetadataPropertyName#FIND_QUERY} is present, enable the anchor with the query, otherwise
     * disable it.
     */
    private void refreshLoadHighlightingQuery(final SpreadsheetCellFindHistoryToken token,
                                              final RefreshContext context) {
        final SpreadsheetMetadata metadata = this.context.spreadsheetMetadata();
        final SpreadsheetCellQuery highlightingQuery = metadata.get(SpreadsheetMetadataPropertyName.FIND_QUERY).orElse(null);

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
    private void refreshSaveAsHighlightingQuery(final SpreadsheetCellFindHistoryToken token) {
        final Optional<SpreadsheetCellQuery> query = token.query()
            .query();

        this.saveAsHighlightingQuery.setHistoryToken(
            Optional.ofNullable(
                query.isPresent() ?
                    token.setMetadataPropertyName(SpreadsheetMetadataPropertyName.FIND_QUERY)
                        .setSaveValue(query) :
                    null
            )
        );
    }

    private final HistoryTokenAnchorComponent saveAsHighlightingQuery;

    // close............................................................................................................

    private void refreshClose(final SpreadsheetCellFindHistoryToken token) {
        this.close.setHistoryToken(
            Optional.of(token.close())
        );
    }

    private final HistoryTokenAnchorComponent close;

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
        return token.isSave();
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetCellFindHistoryToken;
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

        final SpreadsheetCellFindHistoryToken historyToken = context.historyToken()
            .cast(SpreadsheetCellFindHistoryToken.class);

        this.refreshTable(historyToken);

        this.cellRange.setValue(
            Optional.of(
                historyToken.anchoredSelection()
                    .selection()
                    .toCellRange()
            )
        );

        final SpreadsheetCellFindQuery findQuery = historyToken.query();

        this.path.setValue(
            findQuery.path()
        );
        this.valueType.setValue(
            findQuery.valueType()
        );

        this.value.validate();

        final Optional<SpreadsheetCellQuery> maybeQuery = findQuery.query();
        this.query.setStringValue(
            maybeQuery.map(SpreadsheetCellQuery::text)
        );
        final SpreadsheetCellQuery query = maybeQuery.orElse(null);
        if (null != query) {
            SpreadsheetFindDialogComponentSpreadsheetFormulaParserTokenVisitor.refresh(
                query.parserToken(),
                this
            );
        }

        this.refreshFind(historyToken);
        this.refreshReset(historyToken);
        this.refreshLoadHighlightingQuery(historyToken, context);
        this.refreshSaveAsHighlightingQuery(historyToken);
        this.refreshClose(historyToken);

        this.findCells(findQuery);
    }

    /**
     * Copies the parameters from the current {@link HistoryToken} assuming its a {@link SpreadsheetCellFindHistoryToken}
     * and performs a {@link walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher#getFindCells(SpreadsheetId, SpreadsheetCellRangeReference, SpreadsheetCellFindQuery)}.
     */
    private void findCells(final SpreadsheetCellFindQuery query) {
        final SpreadsheetFindDialogComponentContext context = this.context;

        final SpreadsheetCellFindHistoryToken historyToken = context.historyToken()
            .cast(SpreadsheetCellFindHistoryToken.class);

        final SpreadsheetId id = historyToken.id();
        final SpreadsheetCellRangeReference cells = historyToken.anchoredSelection()
            .selection()
            .toCellRange();

        context.findCells(
            id,
            cells,
            query
        );
    }

    // UI...............................................................................................................

    private final static String ID = "cellFind";

    private final static String ID_PREFIX = ID + "-";

    // Object..........................................................................................................

    @Override
    public String toString() {
        return this.dialog.toString();
    }
}
