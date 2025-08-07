/*
 * Copydown 2023 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportHomeNavigationList;

import java.util.Objects;
import java.util.Optional;


/**
 * This {@link HistoryToken} is used by viewport scrollbar left/down arrows.
 * <pre>
 * http://localhost:12345/index.html#/2/Untitled/row/1/navigate/B2/down 1500px
 * </pre>
 */
public final class SpreadsheetRowNavigateHistoryToken extends SpreadsheetRowHistoryToken {

    static SpreadsheetRowNavigateHistoryToken with(final SpreadsheetId id,
                                                   final SpreadsheetName name,
                                                   final AnchoredSpreadsheetSelection anchoredSelection,
                                                   final SpreadsheetViewportHomeNavigationList navigation) {
        return new SpreadsheetRowNavigateHistoryToken(
            id,
            name,
            anchoredSelection,
            navigation
        );
    }

    private SpreadsheetRowNavigateHistoryToken(final SpreadsheetId id,
                                               final SpreadsheetName name,
                                               final AnchoredSpreadsheetSelection anchoredSelection,
                                               final SpreadsheetViewportHomeNavigationList navigation) {
        super(
            id,
            name,
            anchoredSelection
        );
        this.navigation = Objects.requireNonNull(navigation, "navigation");
    }

    public SpreadsheetViewportHomeNavigationList navigation() {
        return this.navigation;
    }

    private final SpreadsheetViewportHomeNavigationList navigation;

    // /1/SpreadsheetName/row/1/navigate/Z9/down 400
    @Override
    UrlFragment rowUrlFragment() {
        return NAVIGATE.append(
            this.navigation.urlFragment()
        );
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        ).setNavigation(this.navigation);
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        // load the cells
        // http://localhost:12345/api/spreadsheet/1/cell/*/force-recompute?home=A1&width=1568&height=463&includeFrozenColumnsRows=true&selection=123&selectionType=row&navigation=down+1567px
        context.spreadsheetDeltaFetcher()
            .getCells(
                this.id(),
                context.viewport(
                    this.navigation(),
                    Optional.of(this.anchoredSelection)
                ).setIncludeFrozenColumnsRows(true)
            );

        context.pushHistoryToken(previous);
    }
}
