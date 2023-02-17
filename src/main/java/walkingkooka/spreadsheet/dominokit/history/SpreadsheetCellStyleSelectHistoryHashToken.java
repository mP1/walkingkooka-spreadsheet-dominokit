package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

final public class SpreadsheetCellStyleSelectHistoryHashToken extends SpreadsheetCellStyleHistoryHashToken {

    static SpreadsheetCellStyleSelectHistoryHashToken with(final SpreadsheetViewportSelection viewportSelection,
                                                           final TextStylePropertyName<?> propertyName) {
        return new SpreadsheetCellStyleSelectHistoryHashToken(
                viewportSelection,
                propertyName
        );
    }

    private SpreadsheetCellStyleSelectHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                       final TextStylePropertyName<?> propertyName) {
        super(viewportSelection, propertyName);
    }

    @Override
    UrlFragment styleUrlFragment() {
        return SELECT;
    }
}
