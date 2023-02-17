package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellSelectHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    static SpreadsheetCellSelectHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellSelectHistoryHashToken(viewportSelection);
    }

    private SpreadsheetCellSelectHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment cellUrlFragment() {
        return SELECT;
    }
}
