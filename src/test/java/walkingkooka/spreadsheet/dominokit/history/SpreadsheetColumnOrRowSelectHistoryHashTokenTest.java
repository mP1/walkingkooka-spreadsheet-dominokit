package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetColumnOrRowSelectHistoryHashTokenTest extends SpreadsheetColumnOrRowHistoryHashTokenTestCase<SpreadsheetColumnOrRowSelectHistoryHashToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/column/A");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/column/B:C/right"
        );
    }

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck(
                ROW,
                "/row/1"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
                ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM),
                "/row/2:3/bottom"
        );
    }

    @Override
    SpreadsheetColumnOrRowSelectHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowSelectHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetColumnOrRowSelectHistoryHashToken> type() {
        return SpreadsheetColumnOrRowSelectHistoryHashToken.class;
    }
}
