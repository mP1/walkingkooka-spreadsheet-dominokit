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

import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenu;
import walkingkooka.text.CaseKind;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Creates a sub menu for each {@link SpreadsheetFormatterName} in the {@link SpreadsheetSelectionMenuContext#spreadsheetFormatterSelectorsMenus()},
 * with items for each {@link SpreadsheetFormatterSelectorMenu} using the labels and {@link SpreadsheetFormatterSelector}.
 */
final class SpreadsheetSelectionMenuFormatter {

    static void build(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                      final SpreadsheetContextMenu menu,
                      final SpreadsheetSelectionMenuContext context) {
        build0(
                historyToken.setFormatter(),
                menu,
                context.idPrefix() + "formatter-",
                context
        );
    }

    static void build0(final HistoryToken historyToken,
                       final SpreadsheetContextMenu menu,
                       final String idPrefix,
                       final SpreadsheetSelectionMenuContext context) {
        buildSpreadsheetFormatterSelectorsMenus(
                historyToken,
                menu,
                idPrefix,
                context.spreadsheetFormatterSelectorsMenus()
        );

        buildEdit(
                historyToken,
                menu,
                idPrefix
        );
    }

    private static void buildSpreadsheetFormatterSelectorsMenus(final HistoryToken historyToken,
                                                                final SpreadsheetContextMenu menu,
                                                                final String idPrefix,
                                                                final List<SpreadsheetFormatterSelectorMenu> menus) {
        final Map<SpreadsheetFormatterName, List<SpreadsheetFormatterSelectorMenu>> nameToMenus = menus.stream()
                .collect(
                        Collectors.toMap(
                                (SpreadsheetFormatterSelectorMenu m) -> m.selector().name(),
                                (SpreadsheetFormatterSelectorMenu m) -> {
                                    final List<SpreadsheetFormatterSelectorMenu> first = Lists.array();
                                    first.add(m);
                                    return first;
                                },
                                (List<SpreadsheetFormatterSelectorMenu> before, List<SpreadsheetFormatterSelectorMenu> merge) -> {
                                    before.addAll(merge);
                                    return before;
                                }
                        )
                );

        // sort SpreadsheetFormatterName
        for (final Entry<SpreadsheetFormatterName, List<SpreadsheetFormatterSelectorMenu>> nameAndMenus : new TreeMap<>(nameToMenus).entrySet()) {
            final String name = nameAndMenus.getKey().value();

            final String nameMenuId = idPrefix + name;

            final SpreadsheetContextMenu nameMenu = menu.subMenu(
                    nameMenuId + SpreadsheetElementIds.SUB_MENU,
                    CaseKind.KEBAB.change(
                            name,
                            CaseKind.TITLE
                    )
            );

            for (final SpreadsheetFormatterSelectorMenu spreadsheetFormatterSelectorMenu : nameAndMenus.getValue()) {
                nameMenu.item(
                        SpreadsheetContextMenuItem.with(
                                nameMenuId + SpreadsheetElementIds.MENU_ITEM,
                                spreadsheetFormatterSelectorMenu.label()
                        ).historyToken(
                                Optional.of(
                                        historyToken.setSave(
                                                spreadsheetFormatterSelectorMenu.selector()
                                                        .toString()
                                        )
                                )
                        )
                );
            }
        }
    }

    private static void buildEdit(final HistoryToken historyToken,
                                  final SpreadsheetContextMenu menu,
                                  final String idPrefix) {
        menu.item(
                SpreadsheetContextMenuItem.with(
                        idPrefix + "edit" + SpreadsheetElementIds.MENU_ITEM,
                        "Edit..."
                ).historyToken(
                        Optional.of(
                                historyToken
                        )
                )
        );
    }

    /**
     * Stop creation
     */
    private SpreadsheetSelectionMenuFormatter() {
        throw new UnsupportedOperationException();
    }
}
