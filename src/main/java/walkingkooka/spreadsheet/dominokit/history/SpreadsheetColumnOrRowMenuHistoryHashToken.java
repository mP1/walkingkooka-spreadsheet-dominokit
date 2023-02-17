package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public class SpreadsheetColumnOrRowMenuHistoryHashToken extends SpreadsheetColumnOrRowHistoryHashToken {

    static SpreadsheetColumnOrRowMenuHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetColumnOrRowMenuHistoryHashToken(viewportSelection);
    }

    private SpreadsheetColumnOrRowMenuHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }


    @Override
    UrlFragment columnOrRowUrlFragment() {
        return MENU;
    }
}
