package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellDeleteHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    static SpreadsheetCellDeleteHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellDeleteHistoryHashToken(viewportSelection);
    }

    private SpreadsheetCellDeleteHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment cellUrlFragment() {
        return DELETE;
    }
}
