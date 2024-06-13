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
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;

/**
 * Freezes the selected row or rows.
 * <pre>
 * /123/SpreadsheetName456/row/1/freeze
 *
 * /spreadsheet-id/spreadsheet-name/row/row or row-range/freeze
 * </pre>
 */
public class SpreadsheetRowFreezeHistoryToken extends SpreadsheetRowHistoryToken {

    static SpreadsheetRowFreezeHistoryToken with(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetRowFreezeHistoryToken(
                id,
                name,
                anchoredSelection
        );
    }

    private SpreadsheetRowFreezeHistoryToken(final SpreadsheetId id,
                                             final SpreadsheetName name,
                                             final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );

        // validate selection
        SpreadsheetMetadata.EMPTY.set(
                SpreadsheetMetadataPropertyName.FROZEN_ROWS,
                anchoredSelection.selection()
                        .toRowRange()
        );
    }

    @Override
    UrlFragment rowUrlFragment() {
        return FREEZE;
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                id,
                name,
                anchoredSelection
        ).setFreeze();
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        this.patchMetadataAndPushSelectionHistoryToken(
                SpreadsheetMetadataPropertyName.FROZEN_ROWS,
                this.anchoredSelection()
                        .selection()
                        .toRowRange(),
                context
        );
    }
}
