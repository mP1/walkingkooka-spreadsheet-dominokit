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

package walkingkooka.spreadsheet.dominokit.viewport;

import walkingkooka.ToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.AbsoluteOrRelativeUrl;
import walkingkooka.net.http.HttpMethod;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetCellRange;
import walkingkooka.spreadsheet.SpreadsheetColumn;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetRow;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.fetcher.NopEmptyResponseFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcher;
import walkingkooka.spreadsheet.dominokit.fetcher.SpreadsheetMetadataFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetIdHistoryToken;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.formula.SpreadsheetFormula;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReferenceOrRange;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportWindows;
import walkingkooka.tree.text.Length;
import walkingkooka.tree.text.TextStyle;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.ValidationValueTypeName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.SortedMap;
import java.util.stream.Collectors;

/**
 * A cache of the cells and labels for a viewport. This is mostly used during the rendering phase to provide content
 * for the cells in the spreadsheet viewport TABLE.
 */
public final class SpreadsheetViewportCache implements NopFetcherWatcher,
    NopEmptyResponseFetcherWatcher,
    HistoryTokenWatcher,
    SpreadsheetDeltaFetcherWatcher,
    SpreadsheetMetadataFetcherWatcher,
    SpreadsheetLabelNameResolver {

    /**
     * Creates a new cache with no cells or labels present.
     */
    public static SpreadsheetViewportCache empty(final SpreadsheetViewportCacheContext context) {
        return new SpreadsheetViewportCache(
            Objects.requireNonNull(context, "context")
        );
    }

    /**
     * Private ctor use static factory
     */
    private SpreadsheetViewportCache(final SpreadsheetViewportCacheContext context) {
        super();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaFetcherWatcher(this);
        context.addSpreadsheetMetadataFetcherWatcher(this);

        this.context = context;

        this.spreadsheetId = null;
        this.selectionSummary = null;
    }

    /**
     * Clearing all caches.
     */
    public void clear() {
        this.cells.clear();

        this.labelMappings.clear();
        this.cellToLabels.clear();
        this.cellToReferences.clear();
        this.matchedCells.clear();
        this.labelToNonLabel.clear();

        this.columns.clear();
        this.rows.clear();

        this.columnWidths.clear();
        this.rowHeights.clear();

        this.columnCount = OptionalInt.empty();
        this.rowCount = OptionalInt.empty();

        this.windows = SpreadsheetViewportWindows.EMPTY;
    }

    /**
     * Getter that returns the {@link SpreadsheetCell} using the {@link SpreadsheetSelection} found in the current
     * {@link HistoryToken}.
     */
    public Optional<SpreadsheetCell> historyTokenCell() {
        return this.context.historyToken()
            .selection()
            .flatMap(this::cell);
    }

    private final SpreadsheetViewportCacheContext context;

    public Optional<SpreadsheetCell> cell(final SpreadsheetSelection selection) {
        return selection.isExternalReference() ?
            this.resolveIfLabel(selection)
                .flatMap(
                    (SpreadsheetSelection notLabel) -> this.cell(notLabel.toCell())
                ) :
            Optional.empty();
    }

    public Optional<SpreadsheetCell> cell(final SpreadsheetCellReference cell) {
        return Optional.ofNullable(
            this.cells.get(cell)
        );
    }

    /**
     * Returns an {@link Iterator} that will return all cells in the given {@link SpreadsheetCellRangeReference}.
     */
    public Iterator<SpreadsheetCell> cells(final SpreadsheetCellRangeReference range) {
        Objects.requireNonNull(range, "range");
        return range.cellsIterator(this.cells);
    }

    /**
     * Creates a {@link SpreadsheetCellRange} filled with cells from this {@link SpreadsheetViewportCache}.
     */
    public SpreadsheetCellRange cellRange(final SpreadsheetCellRangeReference range) {
        Objects.requireNonNull(range, "range");

        final Set<SpreadsheetCell> cells = SortedSets.tree(SpreadsheetCell.REFERENCE_COMPARATOR);
        final Iterator<SpreadsheetCell> iterator = this.cells(range);

        while (iterator.hasNext()) {
            cells.add(
                iterator.next()
            );
        }

        return range.setValue(cells);
    }

    /**
     * A cache of cells, this allows partial updates such as a single cell and still be able to render a complete viewport.
     */
    // VisibleForTesting
    final SortedMap<SpreadsheetCellReference, SpreadsheetCell> cells = Maps.sorted(SpreadsheetSelection.IGNORES_REFERENCE_KIND_COMPARATOR);

    /**
     * Returns a {@link Set} with all the labels for the given {@link SpreadsheetCellReference}.
     */
    public Set<SpreadsheetLabelName> cellLabels(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.resolveIfLabelAndGetAll(
            spreadsheetExpressionReference,
            this.cellToLabels
        );
    }

    /**
     * A cache of cell references and their one or more labels.
     */
    // VisibleForTesting
    final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels = Maps.sorted(SpreadsheetSelection.IGNORES_REFERENCE_KIND_COMPARATOR);

    /**
     * Returns a {@link Set} with all the cell references for the given {@link SpreadsheetCellReference}.
     */
    public Set<SpreadsheetExpressionReference> cellReferences(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.resolveIfLabelAndGetAll(
            spreadsheetExpressionReference,
            this.cellToReferences
        );
    }

    /**
     * Tries to return the formula text for a given {@link SpreadsheetExpressionReference} which may include
     * resolving labels to a {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReference}.
     */
    public Optional<String> formulaText(final SpreadsheetExpressionReference spreadsheetExpressionReference) {
        return this.cell(spreadsheetExpressionReference)
            .flatMap(c -> {
                final String text = c.formula()
                    .text();

                return Optional.ofNullable(
                    text.isEmpty() ?
                        null :
                        text
                );
            });
    }

    /**
     * A cache of cell references to their cell references.
     */
    // VisibleForTesting
    final Map<SpreadsheetCellReference, Set<SpreadsheetExpressionReference>> cellToReferences = Maps.sorted();

    /**
     * Helper for {@link #cellLabels(SpreadsheetExpressionReference)}, resolving the {@link SpreadsheetLabelName} into a
     * {@link walkingkooka.spreadsheet.reference.SpreadsheetCellReferenceOrRange} and returning all labels for that.
     */
    private <T> Set<T> resolveIfLabelAndGetAll(final SpreadsheetExpressionReference spreadsheetExpressionReference,
                                               final Map<SpreadsheetCellReference, Set<T>> cellToTarget) {
        Objects.requireNonNull(spreadsheetExpressionReference, "spreadsheetExpressionReference");

        SpreadsheetCellReferenceOrRange cellOrCellRange = null;

        if (spreadsheetExpressionReference.isLabelName()) {
            final SpreadsheetSelection nonLabel = this.labelToNonLabel.get(spreadsheetExpressionReference.toLabelName());
            if (null != nonLabel) {
                cellOrCellRange = nonLabel.toCellOrCellRange();
            }
        } else {
            cellOrCellRange = spreadsheetExpressionReference.toCellOrCellRange();
        }

        Set<T> result;

        if (null != cellOrCellRange) {
            if (cellOrCellRange.isCell()) {
                result = cellToTarget.getOrDefault(
                    cellOrCellRange,
                    Sets.<T>empty()
                );
            } else {
                result = SortedSets.tree();

                for (final SpreadsheetCellReference in : cellOrCellRange.toCellRange()) {
                    final Set<T> targets = cellToTarget.get(in);
                    if (null != targets) {
                        result.addAll(targets);
                    }
                }

                result = Sets.immutable(result);
            }
        } else {
            result = Sets.empty();
        }

        return result;
    }

    public boolean isMatchedCell(final SpreadsheetCellReference cell) {
        return this.matchedCells.contains(cell);
    }

    /**
     * A cache of all matched cells. Anytime the window or {link SpreadsheetCellQuery} changes this entire cache needs
     * to be cleared and all cells in the viewport reloaded.
     */
    final Set<SpreadsheetCellReference> matchedCells = SortedSets.tree();

    Optional<SpreadsheetColumn> column(final SpreadsheetColumnReference column) {
        return Optional.ofNullable(this.columns.get(column));
    }

    /**
     * Returns true only if the column is present and hidden.
     */
    boolean isColumnHidden(final SpreadsheetColumnReference column) {
        Objects.requireNonNull(column, "column");

        final SpreadsheetColumn spreadsheetColumn = this.columns.get(column);
        return null != spreadsheetColumn && spreadsheetColumn.hidden();
    }

    /**
     * A cache of columns, this is used mostly to track hidden columns.
     */
    // VisibleForTesting
    final Map<SpreadsheetColumnReference, SpreadsheetColumn> columns = Maps.sorted(SpreadsheetSelection.IGNORES_REFERENCE_KIND_COMPARATOR);

    /**
     * Retrieves the width for the given {@link SpreadsheetColumnReference} using the default if none is available.
     */
    Length<?> columnWidth(final SpreadsheetColumnReference column) {
        Objects.requireNonNull(column, "column");

        Length<?> width = this.columnWidths.get(column);
        if (null == width) {
            width = this.defaultWidth;
        }
        return width;
    }

    /**
     * A cache holding the max width for interesting columns. If the column is hidden it will have a width of zero.
     */
    // VisibleForTesting
    final Map<SpreadsheetColumnReference, Length<?>> columnWidths = Maps.sorted(SpreadsheetSelection.IGNORES_REFERENCE_KIND_COMPARATOR);

    // @VisibleForTesting
    Length<?> defaultWidth;

    /**
     * Returns all {@link SpreadsheetLabelMapping} within the active window.
     */
    public Set<SpreadsheetLabelMapping> labelMappings() {
        return this.labelMappings;
    }

    private final Set<SpreadsheetLabelMapping> labelMappings = SortedSets.tree();

    /**
     * Returns all {@link SpreadsheetLabelMapping} for the given {@link SpreadsheetSelection}.
     * If the labels is unknown not mappings will be returned.
     * <br>
     * This will be useful for creating a context menu item holding all the labels for current selection.
     */
    public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
        Objects.requireNonNull(selection, "selection");

        final SpreadsheetSelection nonLabelSelection = this.resolveIfLabel(selection)
            .orElse(null);

        return null == nonLabelSelection ?
            Sets.empty() :
            this.labelMappings()
                .stream()
                .filter((SpreadsheetLabelMapping m) ->
                    this.resolveIfLabel((SpreadsheetSelection) m.reference())
                        .map((SpreadsheetSelection s) -> s.test(nonLabelSelection))
                        .orElse(false)
                ).collect(Collectors.toCollection(SortedSets::tree));
    }

    /**
     * Attempts to resolve any labels to a non label {@link SpreadsheetSelection}.
     * This is useful when trying to show selected cells for a label.
     */
    @Override
    public Optional<SpreadsheetSelection> resolveLabel(final SpreadsheetLabelName labelName) {
        Objects.requireNonNull(labelName, "labelName");

        return Optional.ofNullable(
            this.labelToNonLabel.get(labelName)
        );
    }

    /**
     * A cache of {@link SpreadsheetLabelMapping} expanded to {@link SpreadsheetLabelName} to its {@link walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference}.
     */
    final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel = Maps.sorted(SpreadsheetSelection.IGNORES_REFERENCE_KIND_COMPARATOR);

    Optional<SpreadsheetRow> row(final SpreadsheetRowReference row) {
        return Optional.ofNullable(this.rows.get(row));
    }

    /**
     * Returns true only if the row is present and hidden.
     */
    boolean isRowHidden(final SpreadsheetRowReference row) {
        Objects.requireNonNull(row, "row");

        final SpreadsheetRow spreadsheetRow = this.rows.get(row);
        return null != spreadsheetRow && spreadsheetRow.hidden();
    }

    /**
     * A cache of rows, this is used mostly to track hidden rows.
     */
    // VisibleForTesting
    final Map<SpreadsheetRowReference, SpreadsheetRow> rows = Maps.sorted(SpreadsheetSelection.IGNORES_REFERENCE_KIND_COMPARATOR);

    /**
     * Retrieves the height for the given {@link SpreadsheetRowReference} using the default if none is available.
     */
    Length<?> rowHeight(final SpreadsheetRowReference row) {
        Objects.requireNonNull(row, "row");

        Length<?> height = this.rowHeights.get(row);
        if (null == height) {
            height = this.defaultHeight;
        }
        return height;
    }

    /**
     * A cache holding the max height for interesting rows. If the row is hidden it will have a height of zero.
     */
    // VisibleForTesting
    final Map<SpreadsheetRowReference, Length<?>> rowHeights = Maps.sorted(SpreadsheetSelection.IGNORES_REFERENCE_KIND_COMPARATOR);

    // @VisibleForTesting
    Length<?> defaultHeight;

    public OptionalInt columnCount() {
        return this.columnCount;
    }

    private OptionalInt columnCount = OptionalInt.empty();

    public OptionalInt rowCount() {
        return this.rowCount;
    }

    private OptionalInt rowCount = OptionalInt.empty();

    /**
     * Returns a {@link SpreadsheetCell} which merges all properties from matching cells. Note if any properties
     * are different that will be cleared.
     */
    public Optional<SpreadsheetCell> selectionSummary() {
        if (null == this.selectionSummary) {
            SpreadsheetCell selectionSummary = null;

            final SpreadsheetSelection selectionNotLabel = this.selectionNotLabel.orElse(null);

            if (null != selectionNotLabel) {
                Optional<SpreadsheetFormatterSelector> formatter = null;
                Optional<SpreadsheetParserSelector> parser = null;
                Map<TextStylePropertyName<?>, Object> styleNameToValues = null;
                Optional<ValidatorSelector> validator = null;
                Optional<ValidationValueTypeName> valueType = null;

                for (final SpreadsheetCell cell : this.cells.values()) {
                    if (selectionNotLabel.test(cell.reference())) {

                        if (null == selectionSummary) {
                            selectionSummary = cell;

                            formatter = cell.formatter();
                            parser = cell.parser();
                            validator = cell.validator();
                            valueType = cell.formula()
                                .valueType();

                            styleNameToValues = Maps.sorted();
                            styleNameToValues.putAll(
                                cell.style()
                                    .value()
                            );
                        } else {
                            if (false == formatter.equals(cell.formatter())) {
                                formatter = SpreadsheetCell.NO_FORMATTER;
                            }
                            if (false == parser.equals(cell.parser())) {
                                parser = SpreadsheetCell.NO_PARSER;
                            }
                            if (false == validator.equals(cell.validator())) {
                                validator = SpreadsheetCell.NO_VALIDATOR;
                            }
                            if (false == valueType.equals(cell.formula().valueType())) {
                                valueType = SpreadsheetFormula.NO_VALUE_TYPE;
                            }

                            // clear any properties that have different values.
                            final TextStyle style = cell.style();

                            for (final Entry<TextStylePropertyName<?>, Object> styleNameAndValue : styleNameToValues.entrySet()) {
                                final TextStylePropertyName<?> styleName = styleNameAndValue.getKey();
                                if (false == Objects.equals(
                                    styleNameAndValue.getValue(), style.get(styleName).orElse(null)
                                )) {
                                    styleNameAndValue.setValue(null);
                                }
                            }
                        }
                    }
                }

                if (null != selectionSummary) {
                    for (Iterator<Entry<TextStylePropertyName<?>, Object>> i = styleNameToValues.entrySet().iterator(); i.hasNext(); ) {
                        final Entry<TextStylePropertyName<?>, Object> nameAndValue = i.next();
                        if (null == nameAndValue.getValue()) {
                            i.remove();
                        }
                    }

                    selectionSummary = selectionSummary.setFormatter(formatter)
                        .setParser(parser)
                        .setStyle(
                            TextStyle.EMPTY.setValues(styleNameToValues)
                        ).setFormula(
                            selectionSummary.formula()
                                .setValueType(valueType)
                        ).setValidator(validator);
                }
            }

            this.selectionSummary = Optional.ofNullable(
                null == selectionSummary || selectionSummary.isEmpty() ?
                    null :
                    selectionSummary
            );
        }

        return this.selectionSummary;
    }

    /**
     * This field will be cleared or made null whenever the selection changes or new data arrives.
     */
    private Optional<SpreadsheetCell> selectionSummary;

    /**
     * The {@link SpreadsheetSelection} for the given currently cached {@link #selectionSummary}.
     */
    private Optional<SpreadsheetSelection> selectionNotLabel = Optional.empty();

    /**
     * Sets a new {@link SpreadsheetViewportWindows}.
     */
    public void setWindows(final SpreadsheetViewportWindows windows) {
        Objects.requireNonNull(windows, "windows");

        if (false == windows.isEmpty()) {
            if (false == this.windows.equals(windows)) {
                // no window clear caches
                this.cells.clear();
                this.matchedCells.clear();

                this.labelMappings.clear();
                this.cellToLabels.clear();
                this.cellToReferences.clear();
                this.labelToNonLabel.clear();

                this.columns.clear();
                this.rows.clear();

                this.columnWidths.clear();
                this.rowHeights.clear();
            }

            this.windows = windows;
        }
    }

    /**
     * The viewport window.
     */
    public SpreadsheetViewportWindows windows() {
        return this.windows;
    }

    /**
     * The viewport. This is used to filter cells and labels in the cache.
     */
    private SpreadsheetViewportWindows windows = SpreadsheetViewportWindows.EMPTY;

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        final HistoryToken historyToken = context.historyToken();

        SpreadsheetId id = null;

        if (historyToken instanceof SpreadsheetHistoryToken) {
            final SpreadsheetId currentId = this.spreadsheetId;

            if (historyToken instanceof SpreadsheetIdHistoryToken) {
                final SpreadsheetId newId = historyToken.cast(SpreadsheetIdHistoryToken.class).id();

                if (false == Objects.equals(currentId, newId)) {
                    this.clear();

                    context.debug(
                        "SpreadsheetViewportCache.onHistoryTokenChange id changed from " +
                            currentId +
                            " to " +
                            newId
                    );
                }

                final Optional<SpreadsheetSelection> maybeSelectionNotLabel = historyToken.anchoredSelectionOrEmpty()
                    .map(AnchoredSpreadsheetSelection::selection)
                    .filter(s -> false == s.isLabelName() || false == this.labelMappings.isEmpty())
                    .flatMap(this::resolveIfLabel);

                // clear the cached #selectionSummary if there is no active selection or it changed.
                if (maybeSelectionNotLabel.isPresent()) {
                    final SpreadsheetSelection selectionNotLabel = maybeSelectionNotLabel.get();

                    if (false == selectionNotLabel.equals(this.selectionNotLabel.orElse(null))) {
                        this.selectionSummary = null;
                    }
                } else {
                    this.selectionSummary = null;
                }

                this.selectionNotLabel = maybeSelectionNotLabel;

                id = newId;
            } else {
                this.clear();

                context.debug("SpreadsheetViewportCache.onHistoryTokenChange clearing cache was " + currentId);

                id = null;
            }
        }

        this.spreadsheetId = id;
    }

    // SpreadsheetMetadataFetcherWatcher................................................................................

    /**
     * Captures the default width and height which will be used when rendering
     */
    @Override
    public void onSpreadsheetMetadata(final SpreadsheetMetadata metadata,
                                      final AppContext context) {
        // clear cache if id changed.
        final SpreadsheetId oldId = this.spreadsheetId;
        final SpreadsheetId id = metadata.id().orElse(null);
        if (false == Objects.equals(oldId, id)) {
            // history is probably a create spreadsheet id so clear cache
            this.clear();
            context.debug(this.getClass().getSimpleName() + ".onSpreadsheetMetadata id changed from " + oldId + " to " + id + " clearing cache");
        }

        this.spreadsheetId = id;
        this.defaultWidth = metadata.getEffectiveStylePropertyOrFail(TextStylePropertyName.WIDTH);
        this.defaultHeight = metadata.getEffectiveStylePropertyOrFail(TextStylePropertyName.HEIGHT);
    }

    // @VisibleTesting
    SpreadsheetId spreadsheetId;

    @Override
    public void onSpreadsheetMetadataSet(final Set<SpreadsheetMetadata> metadatas,
                                         final AppContext context) {
        // ignore
    }

    // SpreadsheetDeltaFetcherWatcher...................................................................................

    /**
     * Basically adds/removes cells, labels, column, rows from the fields in this cache.
     * Note the cache is only cleared when the windows changes, as {@link SpreadsheetDelta} are mostly changes and not
     * all values for a window.
     */
    @Override
    public void onSpreadsheetDelta(final HttpMethod method,
                                   final AbsoluteOrRelativeUrl url,
                                   final SpreadsheetDelta delta,
                                   final AppContext context) {
        final Optional<SpreadsheetId> maybeSpreadsheetId = SpreadsheetMetadataFetcher.extractSpreadsheetId(url);
        if (maybeSpreadsheetId.isPresent() &&
            maybeSpreadsheetId.get()
                .equals(this.spreadsheetId)) {

            // GET https://server/api/spreadsheet/1/cell/*
            if (SpreadsheetDeltaFetcher.isGetAllCells(method, url)) {
                this.clear();
            }

            this.setWindows(delta.window());

            {
                final Map<SpreadsheetCellReference, SpreadsheetCell> cells = this.cells;
                final Set<SpreadsheetCellReference> matchedCells = this.matchedCells;

                final Map<SpreadsheetCellReference, Set<SpreadsheetLabelName>> cellToLabels = this.cellToLabels;
                final Map<SpreadsheetCellReference, Set<SpreadsheetExpressionReference>> cellToReferences = this.cellToReferences;

                final Map<SpreadsheetLabelName, SpreadsheetSelection> labelToNonLabel = this.labelToNonLabel;

                // while removing deleted cells also remove cell-> labels, any labels will be (re)-added a few lines below.
                for (final SpreadsheetCellReference cell : delta.deletedCells()) {
                    cells.remove(cell);
                    matchedCells.remove(cell);
                    cellToLabels.remove(cell);
                    cellToReferences.remove(cell);
                }

                // while adding cells also remove cell -> label, ditto.
                for (final SpreadsheetCell cell : delta.cells()) {
                    final SpreadsheetCellReference cellReference = cell.reference();
                    cells.put(
                        cellReference,
                        cell
                    );
                    cellToLabels.remove(cellReference);
                    cellToReferences.remove(cellReference);
                }

                matchedCells.addAll(delta.matchedCells());

                this.labelMappings.clear();
                this.labelMappings.addAll(
                    delta.labels()
                );

                // expands a Map holding cells to labels, the visitor is mostly used to add cell -> labels for all cells in a range,
                // as well as handling multiple label to label mappings eventually to cells.
                SpreadsheetViewportCacheUpdatingSpreadsheetSelectionVisitor.accept(
                    delta.labels(),
                    cellToLabels,
                    labelToNonLabel,
                    this.windows
                );

                cellToReferences.putAll(
                    delta.references()
                );
            }

            // columns.....................................................................................................
            {
                final Map<SpreadsheetColumnReference, SpreadsheetColumn> columns = this.columns;
                final Map<SpreadsheetColumnReference, Length<?>> columnWidths = this.columnWidths;

                for (final SpreadsheetColumnReference column : delta.deletedColumns()) {
                    columns.remove(column);
                    columnWidths.remove(column);
                }

                for (final SpreadsheetColumn column : delta.columns()) {
                    columns.put(
                        column.reference(),
                        column
                    );
                }

                for (final Entry<SpreadsheetColumnReference, Double> width : delta.columnWidths().entrySet()) {
                    columnWidths.put(
                        width.getKey(),
                        Length.pixel(width.getValue())
                    );
                }

                final OptionalInt columnCount = delta.columnCount();
                if (columnCount.isPresent()) {
                    this.columnCount = columnCount;
                }
            }

            // rows.........................................................................................................

            final Map<SpreadsheetRowReference, SpreadsheetRow> rows = this.rows;
            final Map<SpreadsheetRowReference, Length<?>> rowHeights = this.rowHeights;

            for (final SpreadsheetRowReference row : delta.deletedRows()) {
                rows.remove(row);
                rowHeights.remove(row);
            }

            for (final SpreadsheetRow row : delta.rows()) {
                rows.put(
                    row.reference(),
                    row
                );
            }

            for (final Entry<SpreadsheetRowReference, Double> height : delta.rowHeights().entrySet()) {
                rowHeights.put(
                    height.getKey(),
                    Length.pixel(height.getValue())
                );
            }

            final OptionalInt rowCount = delta.rowCount();
            if (rowCount.isPresent()) {
                this.rowCount = rowCount;
            }

            this.selectionSummary = null; // clear cache force recompute
        }
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.empty()
            .value(this.cells)
            .value(this.matchedCells)
            .value(this.columns)
            .value(this.columnWidths)
            .value(this.cellToLabels)
            .value(this.labelMappings)
            .value(this.rows)
            .value(this.rowHeights)
            .value(this.windows)
            .build();
    }
}
