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
import walkingkooka.net.HasUrlFragment;
import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.meta.SpreadsheetName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

/**
 * Base {@link HistoryToken} for several tokens that support saving or patching individual properties for a range of cells,
 * including formulas, style and more. This will be useful when PASTE functionality is added to the UI, so the user
 * can PASTE formulas, or style over a range of selected cells.
 */
public abstract class SpreadsheetCellSaveHistoryToken<V> extends SpreadsheetCellHistoryToken implements Value<V> {

    SpreadsheetCellSaveHistoryToken(final SpreadsheetId id,
                                    final SpreadsheetName name,
                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
            id,
            name,
            anchoredSelection
        );
    }

    @Override
    public final HistoryToken clearAction() {
        return HistoryToken.cellSelect(
            this.id,
            this.name,
            this.anchoredSelection()
        );
    }

    @Override //
    final HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                      final SpreadsheetName name,
                                                      final AnchoredSpreadsheetSelection anchoredSelection) {
        return this.replace(
            id,
            name,
            anchoredSelection,
            this.value()
        );
    }

    /**
     * Factory method used by various would be setters when one or more components have changed and a new instance needs
     * to be created.
     */
    abstract SpreadsheetCellSaveHistoryToken<V> replace(final SpreadsheetId id,
                                                        final SpreadsheetName name,
                                                        final AnchoredSpreadsheetSelection anchoredSelection,
                                                        final V value);

    // HasUrlFragment...................................................................................................

    @Override//
    final UrlFragment cellUrlFragment() {
        return saveUrlFragment(
            this.urlFragmentSaveEntity()
        ).appendSlashThen(
            ((HasUrlFragment) this.value())
                .urlFragment()
        );
    }

    /**
     * This is a single word such as formula/cell etc. The values will be converted into JSON and appended.
     */
    abstract UrlFragment urlFragmentSaveEntity();
}
