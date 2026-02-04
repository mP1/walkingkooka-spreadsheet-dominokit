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

import walkingkooka.collect.set.SortedSets;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.text.CaseKind;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Creates a sub menu for each {@link walkingkooka.validation.provider.ValidatorSelector} in the {@link SpreadsheetSelectionMenuContext#validatorSelectors()}
 * <pre>
 * Validators
 *   Validator-1
 *   Validator-2
 *   ---
 *   Clear
 *   ---
 *   Edit
 *   ----
 *   Recent-1
 *   Recent-2
 * </pre>
 */
final class SpreadsheetSelectionMenuValidator {

    static void build(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                      final SpreadsheetContextMenu menu,
                      final SpreadsheetSelectionMenuContext context) {
        buildMenu(
            historyToken.validator(),
            menu,
            context.idPrefix() + "validator-",
            context
        );
    }

    private static void buildMenu(final HistoryToken historyToken,
                                  final SpreadsheetContextMenu menu,
                                  final String idPrefix,
                                  final SpreadsheetSelectionMenuContext context) {
        buildValidatorSelectors(
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
            context.recentValidatorSelectors()
        );
    }

    private static void buildValidatorSelectors(final HistoryToken historyToken,
                                                final SpreadsheetContextMenu menu,
                                                final String idPrefix,
                                                final SpreadsheetSelectionMenuContext context) {

        final Set<ValidatorSelector> sorted = SortedSets.tree(ValidatorSelector.NAME_ONLY_COMPARATOR);
        sorted.addAll(
            context.validatorSelectors()
        );

        final ValidatorSelector selected = context.selectionSummary()
            .flatMap(SpreadsheetCell::validator)
            .orElse(null);

        if (null != selected) {
            sorted.add(selected);
        }

        for (final ValidatorSelector validatorSelector : sorted) {
            final String name = validatorSelector.name()
                .value();

            menu.item(
                SpreadsheetContextMenuItem.with(
                    idPrefix +
                        name +
                        SpreadsheetElementIds.MENU_ITEM,
                    CaseKind.kebabToTitle(name)
                ).historyToken(
                    Optional.of(
                        historyToken.setSaveStringValue(
                            validatorSelector.toString()
                        )
                    )
                ).checked(
                    validatorSelector.equals(selected)
                )
            );
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
                    SpreadsheetIcons.validatorRemove()
                )
            ).historyToken(
                Optional.of(
                    historyToken.validator()
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
                                     final List<ValidatorSelector> selectors) {

        int i = 0;

        for (final ValidatorSelector selector : selectors) {
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
                )
            );

            i++;
        }
    }

    /**
     * Stop creation
     */
    private SpreadsheetSelectionMenuValidator() {
        throw new UnsupportedOperationException();
    }
}
