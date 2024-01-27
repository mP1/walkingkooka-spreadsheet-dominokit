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

import walkingkooka.spreadsheet.reference.AnchoredSpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelectionVisitor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportAnchor;

import java.util.function.Function;

/**
 * This visitor is used to create a {@link SpreadsheetHistoryToken} that selects the given {@link SpreadsheetSelection}.
 */
final class HistoryTokenSelectionSpreadsheetSelectionVisitor extends SpreadsheetSelectionVisitor {

    static HistoryToken selectionToken(final SpreadsheetHistoryToken token,
                                       final AnchoredSpreadsheetSelection anchoredSelection) {
        final HistoryTokenSelectionSpreadsheetSelectionVisitor visitor = new HistoryTokenSelectionSpreadsheetSelectionVisitor(
                token,
                anchoredSelection.anchor()
        );
        visitor.accept(
                anchoredSelection.selection()
        );
        return visitor.selectionToken;
    }

    HistoryTokenSelectionSpreadsheetSelectionVisitor(final SpreadsheetHistoryToken token,
                                                     final SpreadsheetViewportAnchor anchor) {
        super();
        this.token = token;
        this.anchor = anchor;
    }

    @Override
    protected void visit(final SpreadsheetCellRange cell) {
        this.setSelectionToken(
                cell,
                this.token::setCell
        );
    }

    @Override
    protected void visit(final SpreadsheetCellReference cell) {
        this.setSelectionToken(
                cell,
                this.token::setCell
        );
    }

    @Override
    protected void visit(final SpreadsheetColumnReference column) {
        this.setSelectionToken(
                column,
                this.token::setColumn
        );
    }

    @Override
    protected void visit(final SpreadsheetColumnReferenceRange column) {
        this.setSelectionToken(
                column,
                this.token::setColumn
        );
    }

    @Override
    protected void visit(final SpreadsheetLabelName label) {
        this.setSelectionToken(
                label,
                this.token::setCell
        );
    }

    @Override
    protected void visit(final SpreadsheetRowReference row) {
        this.setSelectionToken(
                row,
                this.token::setRow
        );
    }

    @Override
    protected void visit(final SpreadsheetRowReferenceRange range) {
        this.setSelectionToken(
                range,
                this.token::setRow
        );
    }

    private final HistoryToken token;

    private void setSelectionToken(final SpreadsheetSelection selection,
                                   final Function<SpreadsheetSelection, HistoryToken> factory) {
        this.selectionToken = factory.apply(selection)
                .setAnchor(this.anchor);
    }

    private HistoryToken selectionToken;

    private final SpreadsheetViewportAnchor anchor;

    @Override
    public String toString() {
        return this.token.toString();
    }
}
