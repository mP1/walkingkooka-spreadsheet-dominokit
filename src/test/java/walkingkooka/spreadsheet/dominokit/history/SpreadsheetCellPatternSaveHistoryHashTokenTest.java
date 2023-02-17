package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.format.pattern.SpreadsheetPattern;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;

public final class SpreadsheetCellPatternSaveHistoryHashTokenTest extends SpreadsheetCellPatternHistoryHashTokenTestCase<SpreadsheetCellPatternSaveHistoryHashToken> {

    private final static SpreadsheetPattern PATTERN = SpreadsheetPattern.parseDateFormatPattern("yyyy-mm-dd");

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/pattern/date-format/save/yyyy-mm-dd");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/pattern/date-format/save/yyyy-mm-dd"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/pattern/date-format/save/yyyy-mm-dd"
        );
    }

    @Override
    SpreadsheetCellPatternSaveHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return SpreadsheetCellPatternSaveHistoryHashToken.with(
                viewportSelection,
                PATTERN
        );
    }

    @Override
    public Class<SpreadsheetCellPatternSaveHistoryHashToken> type() {
        return SpreadsheetCellPatternSaveHistoryHashToken.class;
    }
}
