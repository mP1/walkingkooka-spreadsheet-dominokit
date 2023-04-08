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

import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetSelectionVisitor;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

import java.util.function.Function;

/**
 * This visitor is used to create a {@link SpreadsheetNameHistoryToken} that selects the given {@link SpreadsheetSelection}.
 */
final class SpreadsheetNameHistoryTokenSelectionSpreadsheetSelectionVisitor extends SpreadsheetSelectionVisitor {

    static SpreadsheetNameHistoryToken selectionToken(final SpreadsheetNameHistoryToken token,
                                                      final SpreadsheetViewportSelection viewportSelection) {
        final SpreadsheetNameHistoryTokenSelectionSpreadsheetSelectionVisitor visitor = new SpreadsheetNameHistoryTokenSelectionSpreadsheetSelectionVisitor(
                token,
                viewportSelection.anchor()
        );
        visitor.accept(viewportSelection.selection());
        return visitor.selectionToken;
    }

    SpreadsheetNameHistoryTokenSelectionSpreadsheetSelectionVisitor(final SpreadsheetNameHistoryToken token,
                                                                    final SpreadsheetViewportSelectionAnchor anchor) {
        super();
        this.token = token;
        this.anchor = anchor;
    }

    @Override
    protected void visit(final SpreadsheetCellRange cell) {
        this.setSelectionToken(
                cell,
                this.token::cell
        );
    }

    @Override
    protected void visit(final SpreadsheetCellReference cell) {
        this.setSelectionToken(
                cell,
                this.token::cell
        );
    }

    @Override
    protected void visit(final SpreadsheetColumnReference column) {
        this.setSelectionToken(
                column,
                this.token::column
        );
    }

    @Override
    protected void visit(final SpreadsheetColumnReferenceRange column) {
        this.setSelectionToken(
                column,
                this.token::column
        );
    }

    @Override
    protected void visit(final SpreadsheetLabelName label) {
        this.setSelectionToken(
                label,
                this.token::cell
        );
    }

    @Override
    protected void visit(final SpreadsheetRowReference row) {
        this.setSelectionToken(
                row,
                this.token::row
        );
    }

    @Override
    protected void visit(final SpreadsheetRowReferenceRange range) {
        this.setSelectionToken(
                range,
                this.token::row
        );
    }

    private final SpreadsheetNameHistoryToken token;

    private void setSelectionToken(final SpreadsheetSelection selection,
                                   final Function<SpreadsheetViewportSelection, SpreadsheetNameHistoryToken> factory) {
        this.selectionToken = factory.apply(
                selection.setAnchor(this.anchor)
        );
    }

    private SpreadsheetNameHistoryToken selectionToken;

    private final SpreadsheetViewportSelectionAnchor anchor;

    @Override
    public String toString() {
        return this.token.toString();
    }
}
