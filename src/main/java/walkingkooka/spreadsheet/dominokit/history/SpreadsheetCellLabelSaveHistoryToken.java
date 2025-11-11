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
import walkingkooka.spreadsheet.dominokit.AppContext;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.viewport.AnchoredSpreadsheetSelection;

import java.util.Objects;
import java.util.Optional;

/**
 * A request to save a label for a cell selection.
 * <pre>
 * /1/SpreadsheetName/cell/A1/label/save/Label222
 * </pre>
 */
public final class SpreadsheetCellLabelSaveHistoryToken extends SpreadsheetCellLabelHistoryToken
    implements Value<SpreadsheetLabelName> {

    static SpreadsheetCellLabelSaveHistoryToken with(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final AnchoredSpreadsheetSelection anchoredSelection,
                                                     final SpreadsheetLabelName labelName) {
        return new SpreadsheetCellLabelSaveHistoryToken(
            id,
            name,
            anchoredSelection,
            labelName
        );
    }

    private SpreadsheetCellLabelSaveHistoryToken(final SpreadsheetId id,
                                                 final SpreadsheetName name,
                                                 final AnchoredSpreadsheetSelection anchoredSelection,
                                                 final SpreadsheetLabelName labelName) {
        super(id, name, anchoredSelection);
        this.labelName = Objects.requireNonNull(labelName, "labelName");
    }

    @Override
    public SpreadsheetLabelName value() {
        return this.labelName;
    }

    // HistoryToken.labelName
    final SpreadsheetLabelName labelName;

    // /1/SpreadsheetName/cell/A1/label/save/Label222
    @Override
    UrlFragment cellLabelUrlFragment() {
        return SAVE.appendSlashThen(
            UrlFragment.with(
                this.labelName.value()
            )
        );
    }

    @Override //
    HistoryToken replaceIdNameAnchoredSelection(final SpreadsheetId id,
                                                final SpreadsheetName name,
                                                final AnchoredSpreadsheetSelection anchoredSelection) {
        return selection(
            id,
            name,
            anchoredSelection
        ).labelMapping()
            .setSaveValue(
                Optional.of(this.labelName)
            );
    }

    // /1/SpreadsheetName/cell/A1/label
    @Override
    public HistoryToken clearAction() {
        return HistoryToken.cellLabelSelect(
            this.id,
            this.name,
            this.anchoredSelection()
        );
    }

    @Override
    void onHistoryTokenChange0(final HistoryToken previous,
                               final AppContext context) {
        context.pushHistoryToken(previous);

        context.spreadsheetDeltaFetcher()
            .postLabelMapping(
                this.id,
                this.labelName.setLabelMappingReference(
                    this.anchoredSelection()
                        .selection()
                        .toExpressionReference()
                )
            );
    }

    // HistoryTokenVisitor..............................................................................................

    @Override
    void accept(final HistoryTokenVisitor visitor) {
        visitor.visitCellLabelSave(
            this.id,
            this.name,
            this.anchoredSelection,
            this.labelName
        );
    }
}
