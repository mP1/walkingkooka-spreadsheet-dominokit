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

import walkingkooka.Context;
import walkingkooka.spreadsheet.compare.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.history.HistoryTokenContext;
import walkingkooka.spreadsheet.dominokit.viewport.SpreadsheetSelectionSummary;
import walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterSelectorMenu;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A {@link Context} that provides answers when rendering a {@link SpreadsheetContextMenu}.
 */
public interface SpreadsheetSelectionMenuContext extends Context,
        HasSpreadsheetMetadata,
        HistoryTokenContext {

    /**
     * Returns the names of {@link SpreadsheetComparatorName} that will appear in the SORT menus.
     */
    List<SpreadsheetComparatorName> sortComparatorNames();

    /**
     * Returns all {@link SpreadsheetLabelMapping} for the given {@link SpreadsheetSelection}.
     */
    Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection);

    /**
     * Returns recent {@link SpreadsheetFormatterSelector}.
     */
    List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors();

    /**
     * Returns the {@link List<SpreadsheetFormatterSelectorMenu>} for the current {@link walkingkooka.spreadsheet.format.SpreadsheetFormatterSelector}.
     */
    List<SpreadsheetFormatterSelectorMenu> spreadsheetFormatterSelectorsMenus();

    /**
     * Returns recent {@link SpreadsheetParserSelector}.
     */
    List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors();

    /**
     * Returns recent {@link TextStyleProperty}
     */
    List<TextStyleProperty<?>> recentTextStyleProperties();

    /**
     * Returns the id prefix which should be assigned to items in the menu.
     */
    String idPrefix();

    /**
     * Returns the active {@link SpreadsheetSelectionSummary}
     */
    SpreadsheetSelectionSummary selectionSummary();

    /**
     * Used to test if a style is present with the given value. This will typically be used to include a check mark
     * for a style menu item.
     */
    default <T> boolean isChecked(final TextStylePropertyName<T> stylePropertyName,
                                  final T value) {
        Objects.requireNonNull(stylePropertyName, "stylePropertyName");
        Objects.requireNonNull(value, "value");

        return value.equals(
                this.selectionSummary()
                        .style()
                        .get(stylePropertyName)
                        .orElse(null)
        );
    }
}
