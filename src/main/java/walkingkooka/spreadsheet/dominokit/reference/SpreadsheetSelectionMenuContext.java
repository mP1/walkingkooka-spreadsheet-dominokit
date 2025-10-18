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
import walkingkooka.spreadsheet.SpreadsheetCell;
import walkingkooka.spreadsheet.compare.provider.SpreadsheetComparatorName;
import walkingkooka.spreadsheet.dominokit.contextmenu.SpreadsheetContextMenu;
import walkingkooka.spreadsheet.dominokit.history.HistoryContext;
import walkingkooka.spreadsheet.format.provider.SpreadsheetFormatterSelector;
import walkingkooka.spreadsheet.meta.HasSpreadsheetMetadata;
import walkingkooka.spreadsheet.parser.provider.SpreadsheetParserSelector;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelMapping;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelNameResolver;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.server.formatter.SpreadsheetFormatterMenu;
import walkingkooka.tree.text.TextStyleProperty;
import walkingkooka.tree.text.TextStylePropertyName;
import walkingkooka.validation.provider.ValidatorSelector;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link Context} that provides answers when rendering a {@link SpreadsheetContextMenu}.
 */
public interface SpreadsheetSelectionMenuContext extends Context,
    HasSpreadsheetMetadata,
    HistoryContext,
    SpreadsheetLabelNameResolver {

    /**
     * Returns the names of {@link SpreadsheetComparatorName} that will appear in the SORT menus.
     */
    List<SpreadsheetComparatorName> sortComparatorNames();

    /**
     * Returns recent {@link SpreadsheetFormatterSelector}.
     */
    List<SpreadsheetFormatterSelector> recentSpreadsheetFormatterSelectors();

    /**
     * Returns the {@link List<SpreadsheetFormatterMenu>} for the current {@link SpreadsheetFormatterSelector}.
     */
    List<SpreadsheetFormatterMenu> spreadsheetFormatterMenus();

    /**
     * Returns all {@link SpreadsheetLabelMapping} for the given {@link SpreadsheetSelection}.
     */
    Set<SpreadsheetLabelMapping> labelMappings(final SpreadsheetSelection selection);

    /**
     * Returns recent {@link SpreadsheetParserSelector}.
     */
    List<SpreadsheetParserSelector> recentSpreadsheetParserSelectors();

    /**
     * Returns all the references for the given {@link SpreadsheetSelection}
     */
    Set<SpreadsheetExpressionReference> references(final SpreadsheetSelection selection);

    /**
     * Returns recent {@link TextStyleProperty}
     */
    List<TextStyleProperty<?>> recentTextStyleProperties();

    /**
     * Returns all available {@link ValidatorSelector}
     */
    List<ValidatorSelector> validatorSelectors();

    /**
     * Returns recent {@link ValidatorSelector}.
     */
    List<ValidatorSelector> recentValidatorSelectors();

    /**
     * Returns the id prefix which should be assigned to items in the menu.
     */
    String idPrefix();

    Optional<SpreadsheetCell> NO_SELECTION_SUMMARY = Optional.empty();

    /**
     * Returns the active {@link SpreadsheetCell}
     */
    Optional<SpreadsheetCell> selectionSummary();

    /**
     * Used to test if a style is present with the given value. This will typically be used to include a check mark
     * for a style menu item.
     */
    default <T> boolean isChecked(final TextStylePropertyName<T> stylePropertyName,
                                  final T value) {
        Objects.requireNonNull(stylePropertyName, "stylePropertyName");
        Objects.requireNonNull(value, "value");

        final SpreadsheetCell cell = this.selectionSummary()
            .orElse(null);
        return null != cell &&
            value.equals(
                cell.style()
                    .get(stylePropertyName)
                    .orElse(null)
            );
    }
}
