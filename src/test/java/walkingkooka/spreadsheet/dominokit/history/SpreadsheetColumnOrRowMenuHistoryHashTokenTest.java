package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetColumnOrRowMenuHistoryHashTokenTest extends SpreadsheetColumnOrRowHistoryHashTokenTestCase<SpreadsheetColumnOrRowMenuHistoryHashToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/column/A/menu");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/column/B:C/right/menu"
        );
    }

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck(
                ROW,
                "/row/1/menu"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
                ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM),
                "/row/2:3/bottom/menu"
        );
    }

    @Override
    SpreadsheetColumnOrRowMenuHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowMenuHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetColumnOrRowMenuHistoryHashToken> type() {
        return SpreadsheetColumnOrRowMenuHistoryHashToken.class;
    }
}
