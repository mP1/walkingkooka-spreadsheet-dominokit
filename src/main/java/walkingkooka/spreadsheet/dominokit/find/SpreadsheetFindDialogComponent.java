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

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.condition.SpreadsheetConditionRightParserTokenComponent;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaMatchedCellsTableComponent;
import walkingkooka.spreadsheet.dominokit.delta.SpreadsheetDeltaMatchedCellsTableComponentContexts;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponent;
import walkingkooka.spreadsheet.dominokit.dialog.SpreadsheetDialogComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.flex.SpreadsheetFlexLayout;
import walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaComponent;
import walkingkooka.spreadsheet.dominokit.formula.SpreadsheetFormulaComponentFunctions;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenAnchorComponent;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.LoadedSpreadsheetMetadataRequired;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellFindHistoryToken;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetCellRangeReferenceComponent;
import walkingkooka.spreadsheet.dominokit.textmatch.TextMatchComponent;
import walkingkooka.spreadsheet.engine.SpreadsheetCellFindQuery;
import walkingkooka.spreadsheet.engine.SpreadsheetCellQuery;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.parser.SpreadsheetConditionRightParserToken;
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
        this.formatter = this.formatter();
        this.parser = this.parser();
        this.style = this.style();
        this.value = this.value();
        this.formattedValue = this.formattedValue();

        this.find = this.anchor("Find");
        this.reset = this.anchor("Reset");
        this.loadHighlightingQuery = this.anchor("Load Highlighting Query");
        this.saveAsHighlightingQuery = this.anchor("Save as Highlighting Query");

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
     * Creates the modal dialog, which includes a 2 panel layout with the find form on the left, and the table
     * holding the matches on the right.
     */
    private SpreadsheetDialogComponent dialogCreate() {
        final HistoryTokenContext context = this.context;

        return SpreadsheetDialogComponent.with(
                ID,
                "Find",
                true, // includeClose
                context
        ).appendChild(
                SpreadsheetFindDialogComponentGridLayout.empty()
                        .setLeft(
                                Lists.of(
                                        this.cellRange,
                                        this.path,
                                        this.valueType,
                                        this.formula,
                                        this.formatter,
                                        this.parser,
                                        this.style,
                                        this.value,
                                        this.formattedValue,
                                        this.query
                                )
                        ).setContent(
                                Lists.of(
                                        this.table.setCssText("margin-left: 5px")
                                )
                        ).setFooter(
                                Lists.of(
                                        SpreadsheetFlexLayout.row()
                                                .appendChild(this.find)
                                                .appendChild(this.reset)
                                                .appendChild(this.loadHighlightingQuery)
                                                .appendChild(this.saveAsHighlightingQuery)
                                                .appendChild(
                                                        this.closeAnchor(
                                                                context.historyToken()
                                                        )
                                                )
                                )
                        )
        );
    }

    private final SpreadsheetDialogComponent dialog;

    private final SpreadsheetFindDialogComponentContext context;

    // @VisibleForTesting.
    final SpreadsheetDeltaMatchedCellsTableComponent table;

    // path.....................................................................................................

    private SpreadsheetCellRangeReferenceComponent cellRange() {
        return SpreadsheetCellRangeReferenceComponent.empty()
                .setId(ID_PREFIX + "cell-range")
                .setLabel("Cell Range")
                .addChangeListener(this::onCellRangeValueChange)
                .required();
    }

    /**
     * Push the new {@link SpreadsheetCellRangeReference} keeping the original {@link SpreadsheetCellFindQuery}.
     */
    private void onCellRangeValueChange(final Optional<SpreadsheetCellRangeReference> oldCellRange,
                                        final Optional<SpreadsheetCellRangeReference> newCellRange) {
        this.setAndRefresh(
                t -> t.setAnchoredSelection(
                        newCellRange.map(
                                SpreadsheetSelection::setDefaultAnchor
                        )
                ).setQuery(
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
                ).setId("query-TextBox")
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

    private void refreshQueryFromWizardFields(final Optional<?> old,
                                              final Optional<?> newAlsoIgnored) {
        this.query.setValue(
                SpreadsheetFindDialogComponentQuery.query(
                        this.context.historyToken()
                                .cast(SpreadsheetCellFindHistoryToken.class)
                                .query()
                                .query(),
                        this.formula.value(),
                        this.formatter.value(),
                        this.parser.value(),
                        this.style.value(),
                        this.value.value(),
                        this.formattedValue.value()
                )
        );
    }

    // valueType........................................................................................................

    private SpreadsheetValueTypeComponent valueType() {
        return SpreadsheetValueTypeComponent.empty()
                .setId(ID_PREFIX + "value-type" + SpreadsheetElementIds.SELECT)
                .setLabel("Value type")
                .addChangeListener(this::onValueTypeChange);
    }

    private void onValueTypeChange(final Optional<String> oldValue,
                                   final Optional<String> newValue) {
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
                context::now
        );
    }

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

    // formula..........................................................................................................

    private TextMatchComponent formula() {
        return textMatchComponent(
                "Formula"
        );
    }

    private final TextMatchComponent formula;

    // formatter..........................................................................................................

    private TextMatchComponent formatter() {
        return textMatchComponent(
                "Formatter"
        );
    }

    private final TextMatchComponent formatter;

    // parser...........................................................................................................

    private TextMatchComponent parser() {
        return textMatchComponent(
                "Parser"
        );
    }

    private final TextMatchComponent parser;

    // style............................................................................................................

    private TextMatchComponent style() {
        return textMatchComponent(
                "Style"
        );
    }

    private final TextMatchComponent style;

    // value............................................................................................................

    private SpreadsheetConditionRightParserTokenComponent value() {
        return SpreadsheetConditionRightParserTokenComponent.empty(
                        this::spreadsheetParserContext
                ).setId(ID_PREFIX + "value" + SpreadsheetElementIds.TEXT_BOX)
                .setLabel("Value")
                .optional()
                .addChangeListener(this::onValueValueChange);
    }

    private void onValueValueChange(final Optional<SpreadsheetConditionRightParserToken> old,
                                    final Optional<SpreadsheetConditionRightParserToken> newTextMatch) {

    }

    private final SpreadsheetConditionRightParserTokenComponent value;

    // formattedValue...................................................................................................

    private TextMatchComponent formattedValue() {
        return textMatchComponent(
                "Formatted"
        );
    }

    private final TextMatchComponent formattedValue;

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
                .addChangeListener(this::refreshQueryFromWizardFields);
    }

    // find.............................................................................................................

    private void refreshFind(final SpreadsheetCellFindHistoryToken token) {
        this.find.setHistoryToken(
                Optional.of(token)
        );
    }

    private final HistoryTokenAnchorComponent find;

    // reset............................................................................................................

    private void refreshReset(final SpreadsheetCellFindHistoryToken token) {
        this.reset.setHistoryToken(
                Optional.of(
                        token.setQuery(
                                token.query()
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
                                              final AppContext context) {
        final SpreadsheetMetadata metadata = context.spreadsheetMetadata();
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
                                        .setSave(query) :
                                null
                )
        );
    }

    private final HistoryTokenAnchorComponent saveAsHighlightingQuery;

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
        return token.getClass()
                .getSimpleName()
                .contains("Save");
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

        final SpreadsheetCellFindQuery find = token.query();

        this.path.setValue(
                find.path()
        );
        this.valueType.setValue(
                find.valueType()
        );

        this.value.validate();

        this.query.setStringValue(
                find.query()
                        .map(SpreadsheetCellQuery::text)
        );

        this.refreshFind(token);
        this.refreshReset(token);
        this.refreshLoadHighlightingQuery(token, context);
        this.refreshSaveAsHighlightingQuery(token);

        this.findCells();
    }

    /**
     * Copies the parameters from the current {@link HistoryToken} assuming its a {@link SpreadsheetCellFindHistoryToken}
     * and performs a {@link walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcher#findCells(SpreadsheetId, SpreadsheetCellRangeReference, SpreadsheetCellFindQuery)}.
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
                        historyToken.query()
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
