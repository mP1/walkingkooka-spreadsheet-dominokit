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

import walkingkooka.spreadsheet.SpreadsheetId;
import walkingkooka.spreadsheet.SpreadsheetName;
import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowRangeReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelectionVisitor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

/**
 * This visitor is used to create a {@link SpreadsheetHistoryToken} that selects the given {@link SpreadsheetSelection}.
 */
final class HistoryTokenSelectionSpreadsheetSelectionVisitor extends SpreadsheetSelectionVisitor {

    static HistoryToken selectionToken(final SpreadsheetNameHistoryToken historyToken,
                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        final HistoryTokenSelectionSpreadsheetSelectionVisitor visitor = new HistoryTokenSelectionSpreadsheetSelectionVisitor(
                historyToken.id(),
                historyToken.name(),
                anchoredSelection.anchor()
        );
        visitor.accept(
                anchoredSelection.selection()
        );
        return visitor.historyToken;
    }

    HistoryTokenSelectionSpreadsheetSelectionVisitor(final SpreadsheetId id,
                                                     final SpreadsheetName name,
                                                     final SpreadsheetViewportAnchor anchor) {
        super();
        this.id = id;
        this.name = name;
        this.anchor = anchor;
    }

    @Override
    protected void visit(final SpreadsheetCellRangeReference cell) {
        this.setCell(cell);
    }

    @Override
    protected void visit(final SpreadsheetCellReference cell) {
        this.setCell(cell);
    }

    @Override
    protected void visit(final SpreadsheetColumnReference column) {
        this.setColumn(column);
    }

    @Override
    protected void visit(final SpreadsheetColumnRangeReference column) {
        this.setColumn(column);
    }

    private void setColumn(final SpreadsheetSelection selection) {
        this.setSelectionToken(
                HistoryToken.column(
                        this.id,
                        this.name,
                        selection.setAnchor(this.anchor)
                )
        );
    }

    @Override
    protected void visit(final SpreadsheetLabelName label) {
        this.setCell(label);
    }

    private void setCell(final SpreadsheetSelection selection) {
        this.setSelectionToken(
                HistoryToken.cell(
                        this.id,
                        this.name,
                        selection.setAnchor(this.anchor)
                )
        );
    }

    @Override
    protected void visit(final SpreadsheetRowReference row) {
        this.setRow(row);
    }

    @Override
    protected void visit(final SpreadsheetRowRangeReference range) {
        this.setRow(range);
    }

    private void setRow(final SpreadsheetSelection selection) {
        this.setSelectionToken(
                HistoryToken.row(
                        this.id,
                        this.name,
                        selection.setAnchor(this.anchor)
                )
        );
    }

    private final SpreadsheetId id;

    private final SpreadsheetName name;

    private void setSelectionToken(final HistoryToken selection) {
        this.historyToken = selection;
    }

    private HistoryToken historyToken;

    private final SpreadsheetViewportAnchor anchor;

    @Override
    public String toString() {
        return this.historyToken.toString();
    }
}
