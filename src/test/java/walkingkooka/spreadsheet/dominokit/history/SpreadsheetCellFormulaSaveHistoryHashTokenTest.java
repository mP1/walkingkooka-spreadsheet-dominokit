package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.SpreadsheetFormula;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellFormulaSaveHistoryHashTokenTest extends SpreadsheetCellFormulaHistoryHashTokenTestCase<SpreadsheetCellFormulaSaveHistoryHashToken> {

    private final static SpreadsheetFormula FORMULA = SpreadsheetFormula.EMPTY.setText("=12+3");

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/formula/save/=12+3");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/formula/save/=12+3"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/formula/save/=12+3"
        );
    }

    @Override
    SpreadsheetCellFormulaSaveHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFormulaSaveHistoryHashToken.with(
                viewportSelection,
                FORMULA
        );
    }

    @Override
    public Class<SpreadsheetCellFormulaSaveHistoryHashToken> type() {
        return SpreadsheetCellFormulaSaveHistoryHashToken.class;
    }
}
