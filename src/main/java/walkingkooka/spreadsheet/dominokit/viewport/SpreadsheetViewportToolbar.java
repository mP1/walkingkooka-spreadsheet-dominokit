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
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.jboss.elemento.IsElement;
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.SpreadsheetViewportWindows;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenWatcher;
import walkingkooka.spreadsheet.dominokit.net.SpreadsheetDeltaWatcher;
import walkingkooka.spreadsheet.engine.SpreadsheetDelta;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Optional;

/**
 * A toolbar that contains icons that trigger an action for the viewport selection.
 */
public final class SpreadsheetViewportToolbar implements HistoryTokenWatcher,
        IsElement<HTMLDivElement>,
        SpreadsheetDeltaWatcher {

    public static SpreadsheetViewportToolbar create(final AppContext context) {
        return new SpreadsheetViewportToolbar(context);
    }

    private SpreadsheetViewportToolbar(final AppContext context) {
        this.components = this.components(context);
        this.flexLayout = this.createFlexLayout();

        context.addHistoryWatcher(this);
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
        final FlexItem<HTMLDivElement> flexItem = FlexItem.create();

        for (final SpreadsheetViewportToolbarComponent component : this.components) {
            flexItem.appendChild(
                    component.element()
            );
        }

        return FlexLayout.create()
                .appendChild(flexItem);
    }

    private List<SpreadsheetViewportToolbarComponent> components(final HistoryTokenContext context) {
        return Lists.of(
                SpreadsheetViewportToolbarComponent.bold(context),
                SpreadsheetViewportToolbarComponent.italics(context),
                SpreadsheetViewportToolbarComponent.strikeThru(context),
                SpreadsheetViewportToolbarComponent.underline(context),
                SpreadsheetViewportToolbarComponent.textAlignLeft(context),
                SpreadsheetViewportToolbarComponent.textAlignCenter(context),
                SpreadsheetViewportToolbarComponent.textAlignRight(context),
                SpreadsheetViewportToolbarComponent.textAlignJustify(context),
                SpreadsheetViewportToolbarComponent.verticalAlignTop(context),
                SpreadsheetViewportToolbarComponent.verticalAlignMiddle(context),
                SpreadsheetViewportToolbarComponent.verticalAlignBottom(context),
                SpreadsheetViewportToolbarComponent.clear(context),
                SpreadsheetViewportToolbarComponent.pattern(context)
        );
    }

    /**
     * The UI components within the toolbar thqt react to selection changes and also support updates.
     */
    private final List<SpreadsheetViewportToolbarComponent> components;

    // HistoryTokenWatcher..............................................................................................

    @Override
    public void onHistoryTokenChange(final HistoryToken previous,
                                     final AppContext context) {
        this.refresh(context);
    }

    // SpreadsheetDeltaWatcher..........................................................................................

    @Override
    public void onSpreadsheetDelta(final SpreadsheetDelta delta,
                                   final AppContext context) {
        this.refresh(context);
    }

    // refresh..........................................................................................................

    private void refresh(final AppContext context) {
        context.debug("SpreadsheetViewportToolbar.refresh");

        String visibility = "hidden";

        final Optional<SpreadsheetSelection> maybeSelection = context.viewportNonLabelSelection();
        if (maybeSelection.isPresent()) {
            final SpreadsheetSelection selection = maybeSelection.get();
            if (selection.isCellReference() || selection.isCellRange()) {
                visibility = "visible";

                // if window is missing might be browser refresh, and refreshComponents is happening before loadViewportCells etc
                if (false == context.viewportCache()
                        .windows()
                        .isEmpty()) {
                    refreshComponents(selection, context);
                }
            }
        }

        this.element()
                .style.set(
                        "visibility",
                        visibility
                );
    }

    /**
     * Refreshes the individual components aka icons in the toolbar.
     */
    private void refreshComponents(final SpreadsheetSelection selection,
                                   final AppContext context) {
        final SpreadsheetViewportCache cache = context.viewportCache();
        final SpreadsheetViewportWindows window = cache.windows();
        context.debug("SpreadsheetViewportToolbar.refreshComponents begin " + selection + " window: " + window);

        final List<SpreadsheetViewportToolbarComponent> components = this.components;
        for (final SpreadsheetViewportToolbarComponent component : components) {
            component.onToolbarRefreshBegin();
        }

        int cellCount = 0;

        for (final SpreadsheetCellReference cellReference : Iterables.iterator(window.cells(selection))) {
            final Optional<SpreadsheetCell> maybeCell = context.viewportCell(cellReference);
            context.debug("SpreadsheetViewportToolbar.refreshComponents " + cellReference, maybeCell.orElse(null));
            if (maybeCell.isPresent()) {
                final SpreadsheetCell cell = maybeCell.get();

                for (final SpreadsheetViewportToolbarComponent component : components) {
                    component.onToolbarRefreshSelectedCell(
                            cell,
                            context
                    );
                }
            }

            cellCount++;
        }

        for (final SpreadsheetViewportToolbarComponent component : components) {
            component.onToolbarRefreshEnd(
                    cellCount,
                    context
            );
        }

        context.debug("SpreadsheetViewportToolbar.refreshComponents end " + selection);
    }

    // element..........................................................................................................

    // viewport-column-A
    public static <T> String id(final TextStylePropertyName<T> propertyName,
                                final Optional<T> value) {
        return VIEWPORT_TOOLBAR_ID_PREFIX +
                propertyName.constantName().toLowerCase() +
                value.map(
                        v -> '-' +
                                value.toString()
                                        .toUpperCase()
                ).orElse("");
    }

    public static String pattern() {
        return VIEWPORT_TOOLBAR_ID_PREFIX + "-pattern";
    }

    final static String VIEWPORT_TOOLBAR_ID_PREFIX = "viewport-toolbar-";
}
