package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellSelectHistoryHashTokenTest extends SpreadsheetCellHistoryHashTokenTestCase<SpreadsheetCellSelectHistoryHashToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123"
        );
    }

    @Override
    SpreadsheetCellSelectHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellSelectHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetCellSelectHistoryHashToken> type() {
        return SpreadsheetCellSelectHistoryHashToken.class;
    }
}
