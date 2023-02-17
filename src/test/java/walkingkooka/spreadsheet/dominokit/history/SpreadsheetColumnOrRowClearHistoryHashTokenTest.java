package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetColumnOrRowClearHistoryHashTokenTest extends SpreadsheetColumnOrRowHistoryHashTokenTestCase<SpreadsheetColumnOrRowClearHistoryHashToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/column/A/clear");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/column/B:C/right/clear"
        );
    }

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck(
                ROW,
                "/row/1/clear"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
                ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM),
                "/row/2:3/bottom/clear"
        );
    }

    @Override
    SpreadsheetColumnOrRowClearHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowClearHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetColumnOrRowClearHistoryHashToken> type() {
        return SpreadsheetColumnOrRowClearHistoryHashToken.class;
    }
}
