package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public class SpreadsheetColumnOrRowClearHistoryHashToken extends SpreadsheetColumnOrRowHistoryHashToken {

    static SpreadsheetColumnOrRowClearHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetColumnOrRowClearHistoryHashToken(viewportSelection);
    }

    private SpreadsheetColumnOrRowClearHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment columnOrRowUrlFragment() {
        return CLEAR;
    }
}
