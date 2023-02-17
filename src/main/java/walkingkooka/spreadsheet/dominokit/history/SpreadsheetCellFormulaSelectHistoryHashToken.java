package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

public final class SpreadsheetCellFormulaSelectHistoryHashToken extends SpreadsheetCellFormulaHistoryHashToken {

    static SpreadsheetCellFormulaSelectHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection) {
        return new SpreadsheetCellFormulaSelectHistoryHashToken(viewportSelection);
    }

    private SpreadsheetCellFormulaSelectHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment formulaUrlFragment() {
        return SELECT;
    }
}
