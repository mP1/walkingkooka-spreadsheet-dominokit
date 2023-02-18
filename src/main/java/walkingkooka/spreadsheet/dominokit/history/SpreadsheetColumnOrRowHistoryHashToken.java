package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

abstract public class SpreadsheetColumnOrRowHistoryHashToken extends SpreadsheetViewportSelectionHistoryHashToken {

    SpreadsheetColumnOrRowHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    final UrlFragment selectionUrlFragment() {
        return this.columnOrRowUrlFragment();
    }

    abstract UrlFragment columnOrRowUrlFragment();
}
