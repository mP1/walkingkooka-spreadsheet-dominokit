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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.viewport.SpreadsheetViewportRectangleNavigationList;

import java.util.Objects;


/**
 * This {@link HistoryToken} is used by viewport scrollbar left/right arrows.
 * <pre>
 * http://localhost:12345/index.html#/2/Untitled/cell/A1/navigate/home/B2/width/200/height/300/navigations/right 1500px
 * </pre>
 */
public final class SpreadsheetColumnNavigateHistoryToken extends SpreadsheetColumnHistoryToken {

    static SpreadsheetColumnNavigateHistoryToken with(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection,
                                                      final SpreadsheetViewportRectangleNavigationList navigation) {
        return new SpreadsheetColumnNavigateHistoryToken(
            id,
            name,
            anchoredSelection,
            navigation
        );
    }

    private SpreadsheetColumnNavigateHistoryToken(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection anchoredSelection,
                                                  final SpreadsheetViewportRectangleNavigationList navigation) {
        super(
            id,
            name,
            anchoredSelection
        );
        this.navigation = Objects.requireNonNull(navigation, "navigation");
    }

    public SpreadsheetViewportRectangleNavigationList navigation() {
        return this.navigation;
    }

    private final SpreadsheetViewportRectangleNavigationList navigation;

    // /1/SpreadsheetName/column/A/navigate/home/Z9/width/200/height/300/navigation/right 400
    @Override
    UrlFragment columnUrlFragment() {
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
        // http://localhost:12345/api/spreadsheet/1/cell/*/force-recompute?home=A1&width=1568&height=463&includeFrozenColumnsRows=true&selection=F1&selectionType=cell&navigation=right+1567px
    }
}
