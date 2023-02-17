package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellClearHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    static SpreadsheetCellClearHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellClearHistoryHashToken(viewportSelection);
    }

    private SpreadsheetCellClearHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment cellUrlFragment() {
        return CLEAR;
    }
}
