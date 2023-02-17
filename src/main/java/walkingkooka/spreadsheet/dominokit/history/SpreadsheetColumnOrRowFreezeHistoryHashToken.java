package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public class SpreadsheetColumnOrRowFreezeHistoryHashToken extends SpreadsheetColumnOrRowHistoryHashToken {

    static SpreadsheetColumnOrRowFreezeHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetColumnOrRowFreezeHistoryHashToken(viewportSelection);
    }

    private SpreadsheetColumnOrRowFreezeHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }


    @Override
    UrlFragment columnOrRowUrlFragment() {
        return FREEZE;
    }
}
