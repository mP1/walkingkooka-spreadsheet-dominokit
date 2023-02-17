package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.spreadsheet.reference.SpreadsheetColumnOrRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReference;
import walkingkooka.spreadsheet.reference.SpreadsheetColumnReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReference;
import walkingkooka.spreadsheet.reference.SpreadsheetRowReferenceRange;
import walkingkooka.spreadsheet.reference.SpreadsheetSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public abstract class SpreadsheetColumnOrRowHistoryHashTokenTestCase<T extends SpreadsheetColumnOrRowHistoryHashToken> extends SpreadsheetSelectionHistoryHashTokenTestCase<T> {

    final static SpreadsheetColumnReference COLUMN = SpreadsheetSelection.parseColumn("A");

    final static SpreadsheetColumnReferenceRange COLUMN_RANGE = SpreadsheetSelection.parseColumnRange("B:C");

    final static SpreadsheetRowReference ROW = SpreadsheetSelection.parseRow("1");

    final static SpreadsheetRowReferenceRange ROW_RANGE = SpreadsheetSelection.parseRowRange("2:3");

    SpreadsheetColumnOrRowHistoryHashTokenTestCase() {
        super();
    }

    final void urlFragmentAndCheck(final SpreadsheetColumnOrRowReference reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createSpreadsheetHistoryHashToken(
                        reference.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
                ),
                expected
        );
    }

    final void urlFragmentAndCheck(final SpreadsheetColumnReferenceRange reference,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createSpreadsheetHistoryHashToken(
                        reference.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
                ),
                expected
        );
    }

    final void urlFragmentAndCheck(final SpreadsheetRowReferenceRange reference,
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
                COLUMN.setAnchor(SpreadsheetViewportSelectionAnchor.NONE)
        );
    }
}
