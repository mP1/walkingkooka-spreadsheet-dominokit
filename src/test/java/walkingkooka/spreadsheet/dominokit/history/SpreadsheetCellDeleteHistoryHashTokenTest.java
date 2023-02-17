package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellDeleteHistoryHashTokenTest extends SpreadsheetCellHistoryHashTokenTestCase<SpreadsheetCellDeleteHistoryHashToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/delete");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/delete"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/delete"
        );
    }

    @Override
    SpreadsheetCellDeleteHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellDeleteHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetCellDeleteHistoryHashToken> type() {
        return SpreadsheetCellDeleteHistoryHashToken.class;
    }
}
