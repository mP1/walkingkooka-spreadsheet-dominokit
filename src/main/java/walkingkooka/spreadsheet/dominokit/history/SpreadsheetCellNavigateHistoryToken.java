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
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportNavigationList;

import java.util.Objects;


/**
 * This {@link HistoryToken} is used by viewport scrollbar left/right/top/down arrows.
 * <pre>
 * http://localhost:12345/index.html#/2/Untitled/cell/A1/navigate/right 1500px
 * </pre>
 */
public final class SpreadsheetCellNavigateHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellNavigateHistoryToken with(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection,
                                                    final SpreadsheetViewportNavigationList navigation) {
        return new SpreadsheetCellNavigateHistoryToken(
            id,
            name,
            anchoredSelection,
            navigation
        );
    }

    private SpreadsheetCellNavigateHistoryToken(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection,
                                                final SpreadsheetViewportNavigationList navigation) {
        super(
            id,
            name,
            anchoredSelection
        );
        this.navigation = Objects.requireNonNull(navigation, "navigation");
    }

    public SpreadsheetViewportNavigationList navigation() {
        return this.navigation;
    }

    private final SpreadsheetViewportNavigationList navigation;

    @Override
    UrlFragment cellUrlFragment() {
        return NAVIGATE.appendSlashThen(
            this.navigation.urlFragment()
        );
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
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
