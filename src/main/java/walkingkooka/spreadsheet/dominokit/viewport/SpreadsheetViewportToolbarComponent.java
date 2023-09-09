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

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.IsElement;
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.ComponentLifecycle;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetCellHistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetViewportSelectionHistoryToken;
import walkingkooka.spreadsheet.dominokit.layout.FlexLayout;
import walkingkooka.spreadsheet.dominokit.net.NopFetcherWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaFetcherWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A toolbar that contains icons that trigger an action for the viewport selection.
 */
public final class SpreadsheetViewportToolbarComponent implements ComponentLifecycle,
        IsElement<HTMLDivElement>,
        NopFetcherWatcher,
        SpreadsheetDeltaFetcherWatcher {

    public static SpreadsheetViewportToolbarComponent with(final AppContext context) {
        Objects.requireNonNull(context, "context");

        return new SpreadsheetViewportToolbarComponent(context);
    }

    private SpreadsheetViewportToolbarComponent(final AppContext context) {
        this.components = this.components(context);
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

        for (final SpreadsheetViewportToolbarComponentItem component : this.components) {
            flexLayout.appendChild(
                    component.element()
            );
        }

        return flexLayout;
    }

    private List<SpreadsheetViewportToolbarComponentItem> components(final HistoryTokenContext context) {
        return Lists.of(
                SpreadsheetViewportToolbarComponentItem.bold(context),
                SpreadsheetViewportToolbarComponentItem.italics(context),
                SpreadsheetViewportToolbarComponentItem.strikeThru(context),
                SpreadsheetViewportToolbarComponentItem.underline(context),
                SpreadsheetViewportToolbarComponentItem.textAlignLeft(context),
                SpreadsheetViewportToolbarComponentItem.textAlignCenter(context),
                SpreadsheetViewportToolbarComponentItem.textAlignRight(context),
                SpreadsheetViewportToolbarComponentItem.textAlignJustify(context),
                SpreadsheetViewportToolbarComponentItem.verticalAlignTop(context),
                SpreadsheetViewportToolbarComponentItem.verticalAlignMiddle(context),
                SpreadsheetViewportToolbarComponentItem.verticalAlignBottom(context),
                SpreadsheetViewportToolbarComponentItem.clear(context),
                SpreadsheetViewportToolbarComponentItem.pattern(context)
        );
    }

    /**
     * The UI components within the toolbar thqt react to selection changes and also support updates.
     */
    private final List<SpreadsheetViewportToolbarComponentItem> components;

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
        return token instanceof SpreadsheetCellHistoryToken;
    }

    @Override
    public boolean isOpen() {
        return false == "hidden".equals(
                this.element()
                        .style
                        .visibility
        );
    }

    @Override
    public void open(final AppContext context) {
        this.setVisibility("visible");
    }

    @Override
    public void close(final AppContext context) {
        this.setVisibility("hidden");
    }

    private void setVisibility(final String visibility) {
        this.element()
                .style.set(
                        "visibility",
                        visibility
                );
    }

    @Override
    public void refresh(final AppContext context) {
        context.debug("SpreadsheetViewportToolbarComponent.refresh BEGIN");

        final SpreadsheetViewportSelectionHistoryToken historyToken = context.historyToken()
                .cast(SpreadsheetViewportSelectionHistoryToken.class);

        final Optional<SpreadsheetSelection> maybeSelection = context.viewportCache()
                .nonLabelSelection(
                        historyToken.viewportSelection()
                                .selection()
                );

        if (maybeSelection.isPresent()) {
            final SpreadsheetSelection selection = maybeSelection.get();
            if (selection.isCellReference() || selection.isCellRange()) {
                this.refreshItems(
                        selection,
                        context
                );
            }
        }

        context.debug("SpreadsheetViewportToolbarComponent.refresh END");
    }

    /**
     * Refreshes the individual components aka icons in the toolbar.
     */
    private void refreshItems(final SpreadsheetSelection selection,
                              final AppContext context) {
        final SpreadsheetViewportCache cache = context.viewportCache();
        final SpreadsheetViewportWindows window = cache.windows();
        context.debug("SpreadsheetViewportToolbarComponent.refreshItems begin " + selection + " window: " + window);

        final List<SpreadsheetViewportToolbarComponentItem> components = this.components;
        for (final SpreadsheetViewportToolbarComponentItem component : components) {
            component.onToolbarRefreshBegin();
        }

        int cellCount = 0;

        for (final SpreadsheetCellReference cellReference : Iterables.iterator(window.cells(selection))) {
            final Optional<SpreadsheetCell> maybeCell = context.viewportCell(cellReference);
            context.debug("SpreadsheetViewportToolbarComponent.refreshItems " + cellReference, maybeCell.orElse(null));
            if (maybeCell.isPresent()) {
                final SpreadsheetCell cell = maybeCell.get();

                for (final SpreadsheetViewportToolbarComponentItem component : components) {
                    component.onToolbarRefreshSelectedCell(
                            cell,
                            context
                    );
                }
            }

            cellCount++;
        }

        for (final SpreadsheetViewportToolbarComponentItem component : components) {
            component.onToolbarRefreshEnd(
                    cellCount,
                    context
            );
        }

        context.debug("SpreadsheetViewportToolbarComponent.refreshItems end " + selection);
    }

    // element..........................................................................................................

    // viewport-column-A
    public static <T> String id(final TextStylePropertyName<T> propertyName,
                                final Optional<T> value) {
        return VIEWPORT_TOOLBAR_ID_PREFIX +
                propertyName.constantName().toLowerCase() +
                value.map(
                        v -> '-' + v.toString().toUpperCase()
                ).orElse("");
    }

    public static String pattern() {
        return VIEWPORT_TOOLBAR_ID_PREFIX + "-pattern";
    }

    final static String VIEWPORT_TOOLBAR_ID_PREFIX = "viewport-toolbar-";
}
