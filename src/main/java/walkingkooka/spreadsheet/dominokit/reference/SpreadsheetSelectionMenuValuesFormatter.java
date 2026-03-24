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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.CaseKind;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

final class SpreadsheetSelectionMenuValuesFormatter extends SpreadsheetSelectionMenuValues<SpreadsheetFormatterSelector> {

    static SpreadsheetSelectionMenuValuesFormatter with(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                        final SpreadsheetContextMenu menu,
                                                        final SpreadsheetSelectionMenuContext context) {
        return new SpreadsheetSelectionMenuValuesFormatter(
            historyToken,
            menu,
            context
        );
    }

    private SpreadsheetSelectionMenuValuesFormatter(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                    final SpreadsheetContextMenu menu,
                                                    final SpreadsheetSelectionMenuContext context) {
        super(historyToken, menu, context);
    }

    @Override
    void values() {
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = this.historyToken;
        final SpreadsheetContextMenu menu = this.menu;
        final SpreadsheetSelectionMenuContext context = this.context;

        final Map<SpreadsheetFormatterName, List<SpreadsheetFormatterMenu>> nameToMenus = context.spreadsheetFormatterMenus()
            .stream()
            .collect(
                Collectors.toMap(
                    (SpreadsheetFormatterMenu m) -> m.selector().name(),
                    (SpreadsheetFormatterMenu m) -> {
                        final List<SpreadsheetFormatterMenu> first = Lists.array();
                        first.add(m);
                        return first;
                    },
                    (List<SpreadsheetFormatterMenu> before,
                     List<SpreadsheetFormatterMenu> merge) -> {
                        before.addAll(merge);
                        return before;
                    }
                )
            );

        final SpreadsheetFormatterSelector cellFormatter = context.selectionSummary()
            .flatMap(SpreadsheetCell::formatter)
            .orElse(null);

        // sort SpreadsheetFormatterName
        for (final Entry<SpreadsheetFormatterName, List<SpreadsheetFormatterMenu>> nameAndMenus : new TreeMap<>(nameToMenus).entrySet()) {
            final SpreadsheetFormatterName name = nameAndMenus.getKey();
            final String nameMenuId = this.idPrefix + name.value();

            final SpreadsheetContextMenu nameMenu = menu.subMenu(
                nameMenuId + SpreadsheetElementIds.SUB_MENU,
                CaseKind.kebabToTitle(
                    name.value()
                )
            );

            for (final SpreadsheetFormatterMenu selectorMenu : nameAndMenus.getValue()) {
                final SpreadsheetFormatterSelector selector = selectorMenu.selector();

                nameMenu.item(
                    SpreadsheetContextMenuItem.with(
                        nameMenuId + SpreadsheetElementIds.MENU_ITEM,
                        selectorMenu.label()
                    ).historyToken(
                        Optional.of(
                            historyToken.setSaveStringValue(
                                selector.toString()
                            )
                        )
                    ).checked(
                        selector.equals(cellFormatter)
                    )
                );
            }
        }
    }

    @Override
    Optional<Icon<?>> clearIcon() {
        return Optional.of(
            SpreadsheetIcons.formatterRemove()
        );
    }

    @Override //
    Collection<SpreadsheetFormatterSelector> recentValues() {
        return this.context.recentSpreadsheetFormatterSelectors();
    }

    @Override //
    String recentText(final SpreadsheetFormatterSelector formatter) {
        return selectorToMenuItemText(formatter);
    }

    @Override
    Optional<SpreadsheetFormatterSelector> spreadsheetCellValue(final SpreadsheetCell cell) {
        return cell.formatter();
    }

    @Override //
    Class<SpreadsheetFormatterSelector> type() {
        return SpreadsheetFormatterSelector.class;
    }
}
