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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

public final class SpreadsheetCellHighlightSaveHistoryToken extends SpreadsheetCellHighlightHistoryToken {

    static SpreadsheetCellHighlightSaveHistoryToken with(final SpreadsheetId id,
                                                         final SpreadsheetName name,
                                                         final AnchoredSpreadsheetSelection selection,
                                                         final Boolean value) {
        return new SpreadsheetCellHighlightSaveHistoryToken(
                id,
                name,
                selection,
                value
        );
    }

    private SpreadsheetCellHighlightSaveHistoryToken(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection selection,
                                                     final Boolean value) {
        super(id, name, selection);
        this.value = value;
    }

    public boolean value() {
        return this.value;
    }

    private final Boolean value;

    @Override
    UrlFragment highlightUrlFragment() {
        return SAVE.append(
                this.value() ?
                        ENABLE_FRAGMENT :
                        DISABLE_FRAGMENT
        );
    }

    private final static UrlFragment DISABLE_FRAGMENT = UrlFragment.with(DISABLE);

    private final static UrlFragment ENABLE_FRAGMENT = UrlFragment.with(ENABLE);

    HistoryToken setDifferentSelection(final AnchoredSpreadsheetSelection selection) {
        return selection(
                this.id(),
                this.name(),
                selection
        ).setHighlight()
                .setSave(
                        this.value ?
                                ENABLE :
                                DISABLE
                );
    }

    @Override
    public HistoryToken clearAction() {
        return cellHighlightSelect(
                this.id(),
                this.name(),
                this.selection()
        );
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return new SpreadsheetCellHighlightSaveHistoryToken(
                id,
                name,
                this.selection(),
                this.value
        );
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.setViewportHighlightEnabled(this.value);

        context.pushHistoryToken(
                previous
        );
    }
}
