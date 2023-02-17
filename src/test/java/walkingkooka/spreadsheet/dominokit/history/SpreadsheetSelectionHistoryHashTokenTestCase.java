package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public abstract class SpreadsheetSelectionHistoryHashTokenTestCase<T extends SpreadsheetSelectionHistoryHashToken> extends SpreadsheetHistoryHashTokenTokenTestCase<T> {

    SpreadsheetSelectionHistoryHashTokenTestCase() {
        super();
    }

    final void urlFragmentAndCheck(final SpreadsheetViewportSelection viewportSelection,
                                   final String expected) {
        this.urlFragmentAndCheck(
                this.createSpreadsheetHistoryHashToken(viewportSelection),
                expected
        );
    }

    abstract T createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection);
}
