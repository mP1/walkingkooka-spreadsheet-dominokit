package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public class SpreadsheetColumnOrRowUnfreezeHistoryHashToken extends SpreadsheetColumnOrRowHistoryHashToken {

    static SpreadsheetColumnOrRowUnfreezeHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetColumnOrRowUnfreezeHistoryHashToken(viewportSelection);
    }

    private SpreadsheetColumnOrRowUnfreezeHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }


    @Override
    UrlFragment columnOrRowUrlFragment() {
        return UNFREEZE;
    }
}
