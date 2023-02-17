package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

abstract public class SpreadsheetCellFormulaHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    SpreadsheetCellFormulaHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super(viewportSelection);
    }

    @Override
    UrlFragment cellUrlFragment() {
        return ACTION.append(this.formulaUrlFragment());
    }

    private final static UrlFragment ACTION = UrlFragment.SLASH
            .append(UrlFragment.with("formula"));

    abstract UrlFragment formulaUrlFragment();
}
