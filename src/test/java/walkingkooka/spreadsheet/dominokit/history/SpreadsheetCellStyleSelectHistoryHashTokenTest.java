package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetCellStyleSelectHistoryHashTokenTest extends SpreadsheetCellStyleHistoryHashTokenTestCase<SpreadsheetCellStyleSelectHistoryHashToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/style/color");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/style/color"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/style/color"
        );
    }

    @Override
    SpreadsheetCellStyleSelectHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                                                 final TextStylePropertyName<?> propertyName) {
        return SpreadsheetCellStyleSelectHistoryHashToken.with(
                viewportSelection,
                PROPERTY_NAME
        );
    }

    @Override
    public Class<SpreadsheetCellStyleSelectHistoryHashToken> type() {
        return SpreadsheetCellStyleSelectHistoryHashToken.class;
    }
}
