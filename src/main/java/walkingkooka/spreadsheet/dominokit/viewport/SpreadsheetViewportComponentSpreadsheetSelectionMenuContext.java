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

import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProvider;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorProviderDelegator;
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContextDelegator;
import walkingkooka.spreadsheet.dominokit.reference.SpreadsheetSelectionMenuContext;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenu;
import walkingkooka.tree.text.TextStyleProperty;

import java.util.List;
import java.util.Set;

/**
 * A {@link SpreadsheetSelectionMenuContext} used by a {@link SpreadsheetViewportComponent}.
 */
final class SpreadsheetViewportComponentSpreadsheetSelectionMenuContext implements SpreadsheetSelectionMenuContext,
        SpreadsheetComparatorProviderDelegator,
        HistoryTokenContextDelegator {

    static SpreadsheetViewportComponentSpreadsheetSelectionMenuContext with(final List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors,
                                                                            final List<SpreadsheetFormatterSelectorMenu> spreadsheetFormatterSelectorsMenus,
                                                                            final List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors,
                                                                            final List<TextStyleProperty<?>> recentTextStyleProperties,
                                                                            final AppContext context) {
        return new SpreadsheetViewportComponentSpreadsheetSelectionMenuContext(
                recentSpreadsheetFormatterSelectors,
                spreadsheetFormatterSelectorsMenus,
                recentSpreadsheetParserSelectors,
                recentTextStyleProperties,
                context
        );
    }

    private SpreadsheetViewportComponentSpreadsheetSelectionMenuContext(final List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors,
                                                                        final List<SpreadsheetFormatterSelectorMenu> spreadsheetFormatterSelectorsMenus,
                                                                        final List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors,
                                                                        final List<TextStyleProperty<?>> recentTextStyleProperties,
                                                                        final AppContext context) {
        this.recentSpreadsheetFormatterSelectors = recentSpreadsheetFormatterSelectors;
        this.spreadsheetFormatterSelectorsMenus = spreadsheetFormatterSelectorsMenus;

        this.recentSpreadsheetParserSelectors = recentSpreadsheetParserSelectors;

        this.recentTextStyleProperties = recentTextStyleProperties;

        this.context = context;
    }

    @Override
    public Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection) {
        return this.context.spreadsheetViewportCache()
                .labelMappings(selection);
    }

    @Override
    public List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors() {
        return this.recentSpreadsheetFormatterSelectors;
    }

    private final List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors;

    @Override
    public List<SpreadsheetFormatterSelectorMenu> spreadsheetFormatterSelectorsMenus() {
        return this.spreadsheetFormatterSelectorsMenus;
    }

    private final List<SpreadsheetFormatterSelectorMenu> spreadsheetFormatterSelectorsMenus;

    @Override
    public List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors() {
        return this.recentSpreadsheetParserSelectors;
    }

    private final List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors;

    @Override
    public List<TextStyleProperty<?>> recentTextStyleProperties() {
        return this.recentTextStyleProperties;
    }

    private final List<TextStyleProperty<?>> recentTextStyleProperties;

    @Override
    public String idPrefix() {
        return ID_PREFIX;
    }

    final static String ID_PREFIX = SpreadsheetViewportComponent.ID_PREFIX + "context-menu-";

    @Override
    public SpreadsheetMetadata spreadsheetMetadata() {
        return this.context.spreadsheetMetadata();
    }

    @Override
    public SpreadsheetSelectionSummary selectionSummary() {
        return this.context.spreadsheetViewportCache()
                .selectionSummary();
    }

    // SpreadsheetComparatorProvider....................................................................................

    @Override
    public SpreadsheetComparatorProvider spreadsheetComparatorProvider() {
        return this.context;
    }

    // HistoryTokenContext..............................................................................................

    @Override
    public HistoryTokenContext historyTokenContext() {
        return this.context;
    }

    private final AppContext context;

    public String toString() {
        return this.context.toString();
    }
}
