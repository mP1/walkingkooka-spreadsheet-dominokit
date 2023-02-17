package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetColumnOrRowFreezeHistoryHashTokenTest extends SpreadsheetColumnOrRowHistoryHashTokenTestCase<SpreadsheetColumnOrRowFreezeHistoryHashToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/column/A/freeze");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/column/B:C/right/freeze"
        );
    }

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck(
                ROW,
                "/row/1/freeze"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
                ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM),
                "/row/2:3/bottom/freeze"
        );
    }

    @Override
    SpreadsheetColumnOrRowFreezeHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowFreezeHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetColumnOrRowFreezeHistoryHashToken> type() {
        return SpreadsheetColumnOrRowFreezeHistoryHashToken.class;
    }
}
