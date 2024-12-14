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
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

/**
 * The inverse of freeze, removes any selected frozen cells.
 * <pre>
 * /123/SpreadsheetName456/cell/Label123/unfreeze
 *
 * /spreadsheet-id/spreadsheet-name/cell/cell or cell-range or label/unfreeze
 * </pre>
 */
public final class SpreadsheetCellUnfreezeHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellUnfreezeHistoryToken with(final SpreadsheetId id,
                                                    final SpreadsheetName name,
                                                    final AnchoredSpreadsheetSelection anchoredSelection) {
        return new SpreadsheetCellUnfreezeHistoryToken(
                id,
                name,
                anchoredSelection
        );
    }

    private SpreadsheetCellUnfreezeHistoryToken(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        super(
                id,
                name,
                anchoredSelection
        );

        final SpreadsheetSelection selection = anchoredSelection.selection();

        if (false == selection.isLabelName()) {
            SpreadsheetMetadata.EMPTY.set(
                    SpreadsheetMetadataPropertyName.FROZEN_COLUMNS,
                    selection.toColumnRange()
            ).set(
                    SpreadsheetMetadataPropertyName.FROZEN_ROWS,
                    selection.toRowRange()
            );
        }
    }

    @Override
    UrlFragment cellUrlFragment() {
        return UNFREEZE;
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override//
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
                id,
                name,
                anchoredSelection
        ).unfreeze();
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        this.patchMetadataAndPushSelectionHistoryToken(
                SpreadsheetMetadataPropertyName.FROZEN_COLUMNS,
                null,
                SpreadsheetMetadataPropertyName.FROZEN_ROWS,
                null,
                context
        );
    }
}
