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

import walkingkooka.Value;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

import java.util.Objects;

/**
 * Base class for clipboard operations for a cell/cell-range.
 */
public abstract class SpreadsheetCellClipboardHistoryToken<V> extends SpreadsheetCellHistoryToken implements Value<V> {
    SpreadsheetCellClipboardHistoryToken(final SpreadsheetId id,
                                         final SpreadsheetName name,
                                         final AnchoredSpreadsheetSelection anchoredSelection,
                                         final SpreadsheetCellPropertySelector propertySelector,
                                         final V value) {
        super(
                id,
                name,
                anchoredSelection
        );
        this.propertySelector = Objects.requireNonNull(propertySelector, "propertySelector");
        this.value = this.checkValue(value);
    }

    /**
     * Sub-classes should make a defensive copy of any Collection instance and also check the elements/values within.
     */
    abstract V checkValue(final V value);

    public final SpreadsheetCellPropertySelector propertySelector() {
        return this.propertySelector;
    }

    private final SpreadsheetCellPropertySelector propertySelector;

    @Override
    public final V value() {
        return this.value;
    }

    private final V value;

    // /cell/a1:A2/cut/style/{color:"#123456"}
    @Override //
    final UrlFragment cellUrlFragment() {
        return this.clipboardUrlFragment();
    }

    // cut | copy | paste SLASH propertySelector SLASH serialized-value
    abstract UrlFragment clipboardUrlFragment();
}
