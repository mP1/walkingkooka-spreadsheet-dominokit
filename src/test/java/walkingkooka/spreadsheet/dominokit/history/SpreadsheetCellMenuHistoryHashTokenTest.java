package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellMenuHistoryHashTokenTest extends SpreadsheetCellHistoryHashTokenTestCase<SpreadsheetCellMenuHistoryHashToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/menu");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/menu"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/menu"
        );
    }

    @Override
    SpreadsheetCellMenuHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellMenuHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetCellMenuHistoryHashToken> type() {
        return SpreadsheetCellMenuHistoryHashToken.class;
    }
}
