package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellFormulaSelectHistoryHashTokenTest extends SpreadsheetCellFormulaHistoryHashTokenTestCase<SpreadsheetCellFormulaSelectHistoryHashToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/formula");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/formula"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/formula"
        );
    }

    @Override
    SpreadsheetCellFormulaSelectHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellFormulaSelectHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetCellFormulaSelectHistoryHashToken> type() {
        return SpreadsheetCellFormulaSelectHistoryHashToken.class;
    }
}
