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

package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

abstract public class SpreadsheetSelectionHistoryHashToken extends SpreadsheetHistoryHashToken {

    SpreadsheetSelectionHistoryHashToken(final SpreadsheetId id,
                                         final SpreadsheetName name) {
        super(id);

        this.name = Objects.requireNonNull(name, "name");
    }

    public final SpreadsheetName name() {
        return this.name;
    }

    private final SpreadsheetName name;

    @Override
    final UrlFragment spreadsheetUrlFragment() {
        return this.name.urlFragment().append(
                this.selectionUrlFragment()
        );
    }

    abstract UrlFragment selectionUrlFragment();

    /**
     * Creates a clear {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken clear();

    /**
     * Creates a delete {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken delete();

    /**
     * Creates a freeze {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken freeze();

    /**
     * Creates a menu {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken menu();

    /**
     * Factory that creates a {@link HistoryHashToken} with the given {@link TextStylePropertyName} property name.
     */
    abstract SpreadsheetSelectionHistoryHashToken style(final TextStylePropertyName<?> propertyName);

    /**
     * Factory that creates a {@link HistoryHashToken} with the given {@link TextStylePropertyName} property name and value.
     */
    abstract <T> SpreadsheetHistoryHashToken styleSave(final TextStylePropertyName<T> propertyName,
                                                       final T propertyValue);

    /**
     * Creates a unfreeze {@link HistoryHashToken}.
     */
    abstract SpreadsheetSelectionHistoryHashToken unfreeze();
}
