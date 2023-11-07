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
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadata;
import walkingkooka.spreadsheet.meta.SpreadsheetMetadataPropertyName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;

import java.util.Optional;

public final class SpreadsheetCellFreezeHistoryToken extends SpreadsheetCellHistoryToken {

    static SpreadsheetCellFreezeHistoryToken with(final SpreadsheetId id,
                                                  final SpreadsheetName name,
                                                  final AnchoredSpreadsheetSelection selection) {
        return new SpreadsheetCellFreezeHistoryToken(
                id,
                name,
                selection
        );
    }

    private SpreadsheetCellFreezeHistoryToken(final SpreadsheetId id,
                                              final SpreadsheetName name,
                                              final AnchoredSpreadsheetSelection selection) {
        super(
                id,
                name,
                selection
        );

        final SpreadsheetSelection spreadsheetSelection = selection.selection();

        if (false == spreadsheetSelection.isLabelName()) {
            SpreadsheetMetadata.EMPTY.set(
                    SpreadsheetMetadataPropertyName.FROZEN_COLUMNS,
                    spreadsheetSelection.toColumnRange()
            ).set(
                    SpreadsheetMetadataPropertyName.FROZEN_ROWS,
                    spreadsheetSelection.toRowRange()
            );
        }
    }

    @Override
    UrlFragment cellUrlFragment() {
        return FREEZE;
    }

    @Override
    public HistoryToken clearAction() {
        return this.selectionSelect();
    }

    @Override
    public HistoryToken setFormula() {
        return setFormula0();
    }

    @Override
    public HistoryToken setIdAndName(final SpreadsheetId id,
                                     final SpreadsheetName name) {
        return with(
                id,
                name,
                this.selection()
        );
    }

    @Override
    HistoryToken setPatternKind0(final Optional<SpreadsheetPatternKind> patternKind) {
        return this;
    }

    @Override
    HistoryToken setSave0(final String value) {
        return this;
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        final SpreadsheetSelection selection = this.selection()
                .selection();
        this.patchMetadataAndPushSelectionHistoryToken(
                SpreadsheetMetadataPropertyName.FROZEN_COLUMNS,
                selection.toColumnRange(),
                SpreadsheetMetadataPropertyName.FROZEN_ROWS,
                selection.toRowRange(),
                context
        );
    }
}
