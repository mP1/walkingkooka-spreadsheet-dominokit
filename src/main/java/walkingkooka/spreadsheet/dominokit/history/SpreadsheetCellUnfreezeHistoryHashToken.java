package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellUnfreezeHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    static SpreadsheetCellUnfreezeHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellUnfreezeHistoryHashToken(viewportSelection);
    }

    private SpreadsheetCellUnfreezeHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment cellUrlFragment() {
        return UNFREEZE;
    }
}
