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

package walkingkooka.spreadsheet.dominokit.reference;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.plugin.PluginSelectorLike;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.value.SpreadsheetCell;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

final class SpreadsheetSelectionMenuValuesParser extends SpreadsheetSelectionMenuValues<SpreadsheetParserSelector> {

    static SpreadsheetSelectionMenuValuesParser with(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                     final SpreadsheetContextMenu menu,
                                                     final SpreadsheetSelectionMenuContext context) {
        return new SpreadsheetSelectionMenuValuesParser(
            historyToken,
            menu,
            context
        );
    }

    private SpreadsheetSelectionMenuValuesParser(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                 final SpreadsheetContextMenu menu,
                                                 final SpreadsheetSelectionMenuContext context) {
        super(historyToken, menu, context);
    }

    @Override
    void values() {
        final SpreadsheetSelectionMenuContext context = this.context;
        final HistoryToken historyToken = this.historyToken;
        final SpreadsheetContextMenu menu = this.menu;

        final Set<SpreadsheetParserSelector> sorted = SortedSets.tree(
            PluginSelectorLike.nameOnlyComparator()
        );
        sorted.addAll(
            context.spreadsheetParserSelectors()
        );

        final SpreadsheetParserSelector selected = context.selectionSummary()
            .flatMap(SpreadsheetCell::parser)
            .orElse(null);

        if (null != selected) {
            sorted.add(selected);
        }

        for (final SpreadsheetParserSelector spreadsheetParserSelector : sorted) {
            final String name = spreadsheetParserSelector.name()
                .value();

            menu.item(
                SpreadsheetContextMenuItem.with(
                    idPrefix +
                        name +
                        SpreadsheetElementIds.MENU_ITEM,
                    selectorToMenuItemText(spreadsheetParserSelector)
                ).historyToken(
                    Optional.of(
                        historyToken.setSaveStringValue(
                            spreadsheetParserSelector.toString()
                        )
                    )
                ).checked(
                    spreadsheetParserSelector.equals(selected)
                )
            );
        }
    }

    @Override
    Optional<Icon<?>> clearIcon() {
        return Optional.of(
            SpreadsheetIcons.parserRemove()
        );
    }

    @Override //
    Collection<SpreadsheetParserSelector> recentValues() {
        return this.context.recentSpreadsheetParserSelectors();
    }

    @Override //
    String recentText(final SpreadsheetParserSelector parser) {
        return selectorToMenuItemText(parser);
    }

    @Override
    Optional<SpreadsheetParserSelector> spreadsheetCellValue(final SpreadsheetCell cell) {
        return cell.parser();
    }

    @Override //
    Class<SpreadsheetParserSelector> type() {
        return SpreadsheetParserSelector.class;
    }
}
