package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellUnfreezeHistoryHashTokenTest extends SpreadsheetCellHistoryHashTokenTestCase<SpreadsheetCellUnfreezeHistoryHashToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/unfreeze");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/unfreeze"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/unfreeze"
        );
    }

    @Override
    SpreadsheetCellUnfreezeHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellUnfreezeHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetCellUnfreezeHistoryHashToken> type() {
        return SpreadsheetCellUnfreezeHistoryHashToken.class;
    }
}
