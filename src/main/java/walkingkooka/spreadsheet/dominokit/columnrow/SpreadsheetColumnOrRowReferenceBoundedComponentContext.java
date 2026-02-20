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

package walkingkooka.spreadsheet.dominokit.columnrow;

import org.dominokit.domino.ui.menu.MenuItem;
import walkingkooka.Context;
import walkingkooka.collect.iterable.Iterables;
import walkingkooka.collect.list.Lists;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponent;
import walkingkooka.spreadsheet.dominokit.suggestbox.SuggestBoxComponentSuggestionsProvider;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReferenceOrRange;

import java.util.List;

public interface SpreadsheetColumnOrRowReferenceBoundedComponentContext extends SuggestBoxComponentSuggestionsProvider<SpreadsheetColumnOrRowReference>,
    Context {

    /**
     * A {@link SpreadsheetColumnOrRowReferenceOrRange} used to determine valid column or rows.
     */
    SpreadsheetColumnOrRowReferenceOrRange columnOrRowRange();

    /**
     * Refreshes the {@link SuggestBoxComponent#setOptions(List)} with all {@link SpreadsheetColumnOrRowReference} that
     * are matched by the startsWith parameter.
     */
    @Override
    default void filter(final String startsWith,
                        final SuggestBoxComponent<SpreadsheetColumnOrRowReference> suggestBox) {
        final List<SpreadsheetColumnOrRowReference> columnsOrRows = Lists.array();

        for (final SpreadsheetColumnOrRowReference columnOrRow : Iterables.iterator(this.columnOrRowRange().columnOrRowsIterator())) {
            columnsOrRows.add(
                columnOrRow
            );
        }

        suggestBox.setOptions(columnsOrRows);
    }

    /**
     * A {@link SpreadsheetColumnOrRowReference} must be within the range of {@link #columnOrRowRange()}.
     */
    @Override
    default void verifyOption(final SpreadsheetColumnOrRowReference value,
                              final SuggestBoxComponent<SpreadsheetColumnOrRowReference> suggestBox) {
        SpreadsheetColumnOrRowReference verified = null;

        if (null != value) {
            if(false == this.columnOrRowRange().test(value.toSelection())) {
                verified = value;
            }
        }

        if (null != verified) {
            suggestBox.setVerifiedOption(verified);
        }
    }

    MenuItem<SpreadsheetColumnOrRowReference> createMenuItem(final SpreadsheetColumnOrRowReference value);

    /**
     * The menuItemKey is simply the {@link SpreadsheetColumnOrRowReference#text()}.
     */
    @Override
    default String menuItemKey(final SpreadsheetColumnOrRowReference value) {
        return value.text();
    }
}
