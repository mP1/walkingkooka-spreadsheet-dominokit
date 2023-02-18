package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public abstract class SpreadsheetViewportSelectionHistoryHashTokenTestCase<T extends SpreadsheetViewportSelectionHistoryHashToken> extends SpreadsheetSelectionHistoryHashTokenTestCase<T> {

    SpreadsheetViewportSelectionHistoryHashTokenTestCase() {
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
