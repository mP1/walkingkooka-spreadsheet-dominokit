package walkingkooka.spreadsheet.dominokit.history;

import org.junit.jupiter.api.Test;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelectionAnchor;
import walkingkooka.tree.text.TextStylePropertyName;

public final class SpreadsheetCellStyleSaveHistoryHashTokenTest extends SpreadsheetCellStyleHistoryHashTokenTestCase<SpreadsheetCellStyleSaveHistoryHashToken> {

    @Test
    public void testUrlFragmentCell() {
        this.urlFragmentAndCheck("/cell/A1/style/color/save/#123456");
    }

    @Test
    public void testUrlFragmentCellRange() {
        this.urlFragmentAndCheck(
                RANGE.setAnchor(SpreadsheetViewportSelectionAnchor.TOP_LEFT),
                "/cell/B2:C3/top-left/style/color/save/#123456"
        );
    }

    @Test
    public void testUrlFragmentLabel() {
        this.urlFragmentAndCheck(
                LABEL,
                "/cell/Label123/style/color/save/#123456"
        );
    }

    @Override
    SpreadsheetCellStyleSaveHistoryHashToken createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                                               final TextStylePropertyName<?> propertyName) {
        return SpreadsheetCellStyleSaveHistoryHashToken.with(
                viewportSelection,
                PROPERTY_NAME,
                PROPERTY_VALUE
        );
    }

    @Override
    public Class<SpreadsheetCellStyleSaveHistoryHashToken> type() {
        return SpreadsheetCellStyleSaveHistoryHashToken.class;
    }
}
