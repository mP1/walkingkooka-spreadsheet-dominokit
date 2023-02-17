package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public class SpreadsheetColumnOrRowDeleteHistoryHashToken extends SpreadsheetColumnOrRowHistoryHashToken {

    static SpreadsheetColumnOrRowDeleteHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetColumnOrRowDeleteHistoryHashToken(viewportSelection);
    }

    private SpreadsheetColumnOrRowDeleteHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }


    @Override
    UrlFragment columnOrRowUrlFragment() {
        return DELETE;
    }
}
