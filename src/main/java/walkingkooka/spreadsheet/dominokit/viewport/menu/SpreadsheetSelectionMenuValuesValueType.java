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
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.SpreadsheetElementIds;
import walkingkooka.spreadsheet.dominokit.SpreadsheetIcons;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenuItem;
import walkingkooka.spreadsheet.dominokit.history.SpreadsheetAnchoredSelectionHistoryToken;
import walkingkooka.spreadsheet.value.SpreadsheetCell;
import walkingkooka.spreadsheet.value.SpreadsheetValueType;
import walkingkooka.text.CaseKind;
import walkingkooka.validation.ValueType;

import java.util.Collection;
import java.util.Optional;

final class SpreadsheetSelectionMenuValuesValueType extends SpreadsheetSelectionMenuValues<ValueType> {

    static SpreadsheetSelectionMenuValuesValueType with(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                        final SpreadsheetContextMenu menu,
                                                        final SpreadsheetSelectionMenuContext context) {
        return new SpreadsheetSelectionMenuValuesValueType(
            historyToken,
            menu,
            context
        );
    }

    private SpreadsheetSelectionMenuValuesValueType(final SpreadsheetAnchoredSelectionHistoryToken historyToken,
                                                    final SpreadsheetContextMenu menu,
                                                    final SpreadsheetSelectionMenuContext context) {
        super(historyToken, menu, context);
    }

    @Override
    void values() {
        final SpreadsheetAnchoredSelectionHistoryToken historyToken = this.historyToken;
        final SpreadsheetContextMenu menu = this.menu;
        final SpreadsheetSelectionMenuContext context = this.context;

        final String idPrefix = context.idPrefix() + "ValueTypes-";

        final SpreadsheetCell summary = context.selectionSummary()
            .orElse(null);

        for (final ValueType type : SpreadsheetValueType.ALL) {
            final String typeMenuId = idPrefix + type.value();

            menu.item(
                SpreadsheetContextMenuItem.with(
                    typeMenuId + SpreadsheetElementIds.MENU_ITEM,
                    CaseKind.KEBAB.change(
                        type.text(),
                        CaseKind.TITLE
                    )
                ).historyToken(
                    Optional.of(
                        historyToken.setValueType()
                            .setSaveValue(
                                Optional.of(type)
                            )
                    )
                ).checked(
                    type.equals(
                        null == summary ?
                            null :
                            summary.formula()
                                .valueType()
                                .orElse(null)
                    )
                )
            );
        }
    }

    @Override
    Optional<Icon<?>> clearIcon() {
        return Optional.of(
            SpreadsheetIcons.valueTypeRemove()
        );
    }

    @Override //
    Collection<ValueType> recentValues() {
        return Lists.empty();
    }

    @Override //
    String recentText(final ValueType valueType) {
        return CaseKind.KEBAB.change(
            valueType.text(),
            CaseKind.TITLE
        );
    }

    @Override
    Optional<ValueType> spreadsheetCellValue(final SpreadsheetCell cell) {
        return cell.formula()
            .valueType();
    }

    @Override //
    Class<ValueType> type() {
        return ValueType.class;
    }
}
