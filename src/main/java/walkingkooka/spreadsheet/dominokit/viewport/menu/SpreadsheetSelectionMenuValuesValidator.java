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

package walkingkooka.spreadsheet.dominokit.viewport.menu;

import org.dominokit.domino.ui.icons.Icon;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.HistoryToken;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

final class SpreadsheetSelectionMenuValuesValidator extends SpreadsheetSelectionMenuValues<ValidatorSelector> {

    static SpreadsheetSelectionMenuValuesValidator with(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                        final SpreadsheetContextMenu menu,
                                                        final SpreadsheetSelectionMenuContext context) {
        return new SpreadsheetSelectionMenuValuesValidator(
            historyToken,
            menu,
            context
        );
    }

    private SpreadsheetSelectionMenuValuesValidator(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                    final SpreadsheetContextMenu menu,
                                                    final SpreadsheetSelectionMenuContext context) {
        super(historyToken, menu, context);
    }

    @Override
    void values() {
        final SpreadsheetSelectionMenuContext context = this.context;
        final HistoryToken historyToken = this.historyToken;
        final SpreadsheetContextMenu menu = this.menu;

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
                    selectorToMenuItemText(validatorSelector)
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

    @Override
    Optional<Icon<?>> clearIcon() {
        return Optional.of(
            SpreadsheetIcons.validatorRemove()
        );
    }

    @Override //
    Collection<ValidatorSelector> recentValues() {
        return this.context.recentValidatorSelectors();
    }

    @Override //
    String recentText(final ValidatorSelector validator) {
        return selectorToMenuItemText(validator);
    }

    @Override
    Optional<ValidatorSelector> spreadsheetCellValue(final SpreadsheetCell cell) {
        return cell.validator();
    }

    @Override //
    Class<ValidatorSelector> type() {
        return ValidatorSelector.class;
    }
}
