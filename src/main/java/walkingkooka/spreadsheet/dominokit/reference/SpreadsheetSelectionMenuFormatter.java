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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterName;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.text.CaseKind;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Creates a sub menu for each {@link SpreadsheetFormatterName} in the {@link SpreadsheetSelectionMenuContext#spreadsheetFormatterMenus()},
 * with items for each {@link SpreadsheetFormatterMenu} using the labels and {@link SpreadsheetFormatterSelector}.
 * <pre>
 * Formatter
 *   Date Format Pattern
 *     Short
 *     Medium
 *     Long
 *   ---
 *   Edit
 *   ---
 *   Date Format Pattern dd/mm/yy
 *   Date Format Pattern ddd/mmm/yyyy
 * </pre>
 */
final class SpreadsheetSelectionMenuFormatter {

    static void build(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                      final SpreadsheetContextMenu menu,
                      final SpreadsheetSelectionMenuContext context) {
        build0(
            historyToken.formatter(),
            menu,
            context.idPrefix() + "formatter-",
            context
        );
    }

    static void build0(final HistoryToken historyToken,
                       final SpreadsheetContextMenu menu,
                       final String idPrefix,
                       final SpreadsheetSelectionMenuContext context) {
        buildSpreadsheetFormatterMenus(
            historyToken,
            menu,
            idPrefix,
            context
        );

        menu.separator();

        buildClear(
            historyToken,
            menu,
            idPrefix
        );

        menu.separator();

        buildEdit(
            historyToken,
            menu,
            idPrefix
        );

        menu.separator();

        buildRecents(
            historyToken,
            menu,
            idPrefix,
            context
        );
    }

    private static void buildSpreadsheetFormatterMenus(final HistoryToken historyToken,
                                                       final SpreadsheetContextMenu menu,
                                                       final String idPrefix,
                                                       final SpreadsheetSelectionMenuContext context) {
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
                    (List<SpreadsheetFormatterMenu> before, List<SpreadsheetFormatterMenu> merge) -> {
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
            final String nameMenuId = idPrefix + name.value();

            final SpreadsheetContextMenu nameMenu = menu.subMenu(
                nameMenuId + SpreadsheetElementIds.SUB_MENU,
                CaseKind.kebabToTitle(
                    name.value()
                )
            );

            for (final SpreadsheetFormatterMenu spreadsheetFormatterSelectorMenu : nameAndMenus.getValue()) {
                final SpreadsheetFormatterSelector selector = spreadsheetFormatterSelectorMenu.selector();

                nameMenu.item(
                    SpreadsheetContextMenuItem.with(
                        nameMenuId + SpreadsheetElementIds.MENU_ITEM,
                        spreadsheetFormatterSelectorMenu.label()
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

    private static void buildClear(final HistoryToken historyToken,
                                   final SpreadsheetContextMenu menu,
                                   final String idPrefix) {
        menu.item(
            SpreadsheetContextMenuItem.with(
                idPrefix + "clear" + SpreadsheetElementIds.MENU_ITEM,
                "Clear..."
            ).icon(
                Optional.of(
                    SpreadsheetIcons.formatterRemove()
                )
            ).historyToken(
                Optional.of(
                    historyToken.formatter()
                        .clearSaveValue()
                )
            )
        );
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

    private static void buildRecents(final HistoryToken historyToken,
                                     final SpreadsheetContextMenu menu,
                                     final String idPrefix,
                                     final SpreadsheetSelectionMenuContext context) {

        int i = 0;

        final SpreadsheetFormatterSelector cellFormatter = context.selectionSummary()
            .flatMap(SpreadsheetCell::formatter)
            .orElse(null);

        for (final SpreadsheetFormatterSelector selector : context.recentSpreadsheetFormatterSelectors()) {
            final String label = CaseKind.kebabToTitle(
                selector.name()
                    .value()
            );

            final String text = selector.valueText();

            menu.item(
                SpreadsheetContextMenuItem.with(
                    idPrefix + "recent-" + i + SpreadsheetElementIds.MENU_ITEM,
                    text.isEmpty() ?
                        label :
                        label + " " + text
                ).historyToken(
                    Optional.of(
                        historyToken.setSaveStringValue(
                            selector.text()
                        )
                    )
                ).checked(
                    selector.equals(cellFormatter)
                )
            );

            i++;
        }
    }

    /**
     * Stop creation
     */
    private SpreadsheetSelectionMenuFormatter() {
        throw new UnsupportedOperationException();
    }
}
