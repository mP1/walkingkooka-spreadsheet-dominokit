package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.spreadsheet.reference.SpreadsheetCellRange;
import walkingkooka.spreadsheet.reference.SpreadsheetCellReference;
import walkingkooka.spreadsheet.reference.SpreadsheetExpressionReference;
import walkingkooka.spreadsheet.reference.SpreadsheetLabelName;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public abstract class SpreadsheetCellHistoryHashTokenTestCase<T extends SpreadsheetCellHistoryHashToken> extends SpreadsheetViewportSelectionHistoryHashTokenTestCase<T> {

    final static SpreadsheetCellReference CELL = SpreadsheetSelection.parseCell("A1");

    final static SpreadsheetCellRange RANGE = SpreadsheetSelection.parseCellRange("B2:C3");

    final static SpreadsheetLabelName LABEL = SpreadsheetSelection.labelName("Label123");

    SpreadsheetCellHistoryHashTokenTestCase() {
        super();
    }

    final void urlFragmentAndCheck(final SpreadsheetExpressionReference reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createSpreadsheetHistoryHashToken(
                        reference.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
                ),
                expected
        );
    }

    final T createSpreadsheetHistoryHashToken() {
        return this.createSpreadsheetHistoryHashToken(
                CELL.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
        );
    }
}
