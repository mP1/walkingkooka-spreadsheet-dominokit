package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellFreezeHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    static SpreadsheetCellFreezeHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellFreezeHistoryHashToken(viewportSelection);
    }

    private SpreadsheetCellFreezeHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment cellUrlFragment() {
        return FREEZE;
    }
}
