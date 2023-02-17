package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetColumnOrRowUnfreezeHistoryHashTokenTest extends SpreadsheetColumnOrRowHistoryHashTokenTestCase<SpreadsheetColumnOrRowUnfreezeHistoryHashToken> {

    @Test
    public void testUrlFragmentColumn() {
        this.urlFragmentAndCheck(
                COLUMN,
                "/column/A/unfreeze");
    }

    @Test
    public void testUrlFragmentColumnRange() {
        this.urlFragmentAndCheck(
                COLUMN_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.RIGHT),
                "/column/B:C/right/unfreeze"
        );
    }

    @Test
    public void testUrlFragmentRow() {
        this.urlFragmentAndCheck(
                ROW,
                "/row/1/unfreeze"
        );
    }

    @Test
    public void testUrlFragmentRowRange() {
        this.urlFragmentAndCheck(
                ROW_RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.BOTTOM),
                "/row/2:3/bottom/unfreeze"
        );
    }

    @Override
    SpreadsheetColumnOrRowUnfreezeHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetColumnOrRowUnfreezeHistoryHashToken.with(viewportSelection);
    }

    @Override
    public Class<SpreadsheetColumnOrRowUnfreezeHistoryHashToken> type() {
        return SpreadsheetColumnOrRowUnfreezeHistoryHashToken.class;
    }
}
