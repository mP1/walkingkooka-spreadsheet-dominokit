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

package walkingkooka.spreadsheet.dominokit.ui.toolbar;

import elemental2.dom.HTMLDivElement;
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetHistoryToken;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.ui.Component;
import walkingkooka.spreadsheet.dominokit.ui.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.ui.FlexLayout;
import walkingkooka.spreadsheet.dominokit.ui.viewport.SpreadsheetViewportCache;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A toolbar that contains icons that trigger an action.
 */
public final class SpreadsheetToolbarComponent implements Component<HTMLDivElement>,
        ComponentLifecycle,
        NopFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher {

    public static SpreadsheetToolbarComponent with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetToolbarComponent(context);
    }

    private SpreadsheetToolbarComponent(final AppContext context) {
        final List<SpreadsheetToolbarComponentItem> components = this.components(context);
        components.forEach(context::addHistoryTokenWatcher);

        this.components = components;
        this.flexLayout = this.createFlexLayout();

        context.addHistoryTokenWatcher(this);
        context.addSpreadsheetDeltaWatcher(this);
    }

    // isElement........................................................................................................

    @Override
    public HTMLDivElement element() {
        return this.flexLayout.element();
    }

    private final FlexLayout flexLayout;

    /**
     * Creates a {@link FlexLayout} and populates it with the toolbar icons etc.
     */
    private FlexLayout createFlexLayout() {
        final FlexLayout flexLayout = new FlexLayout();

        for (final SpreadsheetToolbarComponentItem component : this.components) {
            flexLayout.appendChild(
                    component.element()
            );
        }

        return flexLayout;
    }

    private List<SpreadsheetToolbarComponentItem> components(final AppContext context) {
        return Lists.of(
                SpreadsheetToolbarComponentItem.bold(context),
                SpreadsheetToolbarComponentItem.italics(context),
                SpreadsheetToolbarComponentItem.strikeThru(context),
                SpreadsheetToolbarComponentItem.underline(context),
                SpreadsheetToolbarComponentItem.textAlignLeft(context),
                SpreadsheetToolbarComponentItem.textAlignCenter(context),
                SpreadsheetToolbarComponentItem.textAlignRight(context),
                SpreadsheetToolbarComponentItem.textAlignJustify(context),
                // case
                SpreadsheetToolbarComponentItem.textCaseCapitalize(context),
                SpreadsheetToolbarComponentItem.textCaseLowercase(context),
                SpreadsheetToolbarComponentItem.textCaseUppercase(context),
                // vertical
                SpreadsheetToolbarComponentItem.verticalAlignTop(context),
                SpreadsheetToolbarComponentItem.verticalAlignMiddle(context),
                SpreadsheetToolbarComponentItem.verticalAlignBottom(context),
                SpreadsheetToolbarComponentItem.clearStyle(context),
                SpreadsheetToolbarComponentItem.formatPattern(context),
                SpreadsheetToolbarComponentItem.parsePattern(context),
                // metadata properties
                SpreadsheetToolbarComponentItem.hideZeroValues(context),
                // FindCells | Highlight
                SpreadsheetToolbarComponentItem.findCells(context),
                SpreadsheetToolbarComponentItem.highlightCells(context)
        );
    }

    /**
     * The UI components within the toolbar thqt react to selection changes and also support updates.
     */
    private final List<SpreadsheetToolbarComponentItem> components;

    // SpreadsheetDeltaFetcherWatcher..........................................................................................

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refreshIfOpen(context);
    }

    // ComponentLifecycle..............................................................................................

    @Override
    public boolean shouldIgnore(final HistoryToken token) {
        return false;
    }

    @Override
    public boolean isMatch(final HistoryToken token) {
        return token instanceof SpreadsheetHistoryToken;
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public void open(final AppContext context) {
        this.open = true;
    }

    @Override
    public void close(final AppContext context) {
        this.open = false;
    }

    private boolean open;

    @Override
    public void refresh(final AppContext context) {
        context.debug("SpreadsheetToolbarComponent.refresh BEGIN");

        final Optional<SpreadsheetSelection> maybeNonLabelSelection = context.historyToken()
                .nonLabelSelection(
                        context.viewportCache()
                );
        if (maybeNonLabelSelection.isPresent()) {
            final SpreadsheetSelection nonLabelSelection = maybeNonLabelSelection.get();
            if (nonLabelSelection.isCellReference() || nonLabelSelection.isCellRange()) {
                this.refreshItems(
                        nonLabelSelection,
                        context
                );
            }
        }

        context.debug("SpreadsheetToolbarComponent.refresh END");
    }

    /**
     * Refreshes the individual components aka icons in the toolbar.
     */
    private void refreshItems(final SpreadsheetSelection selection,
                              final AppContext context) {
        final SpreadsheetViewportCache cache = context.viewportCache();
        final SpreadsheetViewportWindows window = cache.windows();
        context.debug("SpreadsheetToolbarComponent.refreshItems begin " + selection + " window: " + window);

        final List<SpreadsheetToolbarComponentItem> components = this.components;
        for (final SpreadsheetToolbarComponentItem component : components) {
            component.onToolbarRefreshBegin();
        }

        int cellCount = 0;

        for (final SpreadsheetCellReference cellReference : Iterables.iterator(window.cells(selection))) {
            final Optional<SpreadsheetCell> maybeCell = context.viewportCell(cellReference);
            context.debug("SpreadsheetToolbarComponent.refreshItems " + cellReference, maybeCell.orElse(null));
            if (maybeCell.isPresent()) {
                final SpreadsheetCell cell = maybeCell.get();

                for (final SpreadsheetToolbarComponentItem component : components) {
                    component.onToolbarRefreshSelectedCell(
                            cell,
                            context
                    );
                }
            }

            cellCount++;
        }

        for (final SpreadsheetToolbarComponentItem component : components) {
            component.onToolbarRefreshEnd(
                    cellCount,
                    context
            );
        }

        context.debug("SpreadsheetToolbarComponent.refreshItems end " + selection);
    }

    // element..........................................................................................................

    // viewport-column-A
    public static <T> String id(final TextStylePropertyName<T> propertyName,
                                final Optional<T> value) {
        return TOOLBAR_ID_PREFIX +
                propertyName.constantName().toLowerCase() +
                value.map(
                        v -> '-' + v.toString().toUpperCase()
                ).orElse("");
    }

    public static String findCellsId() {
        return TOOLBAR_ID_PREFIX + "find-cells";
    }

    public static String formatPatternId() {
        return TOOLBAR_ID_PREFIX + "format-pattern";
    }

    public static String hideZeroValues() {
        return TOOLBAR_ID_PREFIX + "hide-zero-values";
    }

    public static String highlightId() {
        return TOOLBAR_ID_PREFIX + "highlight";
    }

    public static String parsePatternId() {
        return TOOLBAR_ID_PREFIX + "parse-pattern";
    }

    final static String TOOLBAR_ID_PREFIX = "toolbar-";
}
