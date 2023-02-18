package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

abstract public class SpreadsheetCellHistoryHashToken extends SpreadsheetViewportSelectionHistoryHashToken {

    SpreadsheetCellHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);

        final SpreadsheetSelection selection = viewportSelection.selection();
        if (false == (selection.isCellReference() || selection.isCellRange() || selection.isLabelName())) {
            throw new IllegalArgumentException("Expected cell, cell-range or label but got " + selection);
        }
    }

    final UrlFragment selectionUrlFragment() {
        return this.cellUrlFragment();
    }

    abstract UrlFragment cellUrlFragment();
}
