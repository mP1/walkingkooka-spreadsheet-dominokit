package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPatternKind;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellPatternSelectHistoryHashTokenTest extends SpreadsheetCellPatternHistoryHashTokenTestCase<SpreadsheetCellPatternSelectHistoryHashToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/pattern/date-format");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/pattern/date-format"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/pattern/date-format"
        );
    }

    @Override
    SpreadsheetCellPatternSelectHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellPatternSelectHistoryHashToken.with(
                viewportSelection,
                SpreadsheetPatternKind.DATE_FORMAT_PATTERN
        );
    }

    @Override
    public Class<SpreadsheetCellPatternSelectHistoryHashToken> type() {
        return SpreadsheetCellPatternSelectHistoryHashToken.class;
    }
}
