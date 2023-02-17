package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellMenuHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    static SpreadsheetCellMenuHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellMenuHistoryHashToken(viewportSelection);
    }

    private SpreadsheetCellMenuHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment cellUrlFragment() {
        return MENU;
    }
}
