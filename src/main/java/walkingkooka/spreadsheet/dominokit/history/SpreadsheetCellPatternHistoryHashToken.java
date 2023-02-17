package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

abstract public class SpreadsheetCellPatternHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    SpreadsheetCellPatternHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment cellUrlFragment() {
        return this.patternUrlFragment();
    }

    abstract UrlFragment patternUrlFragment();
}
