package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.color.Color;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

public abstract class SpreadsheetCellStyleHistoryHashTokenTestCase<T extends SpreadsheetCellStyleHistoryHashToken> extends SpreadsheetCellHistoryHashTokenTestCase<T> {

    final static TextStylePropertyName<Color> PROPERTY_NAME = TextStylePropertyName.COLOR;

    final static Color PROPERTY_VALUE = Color.parse("#123456");

    SpreadsheetCellStyleHistoryHashTokenTestCase() {
        super();
    }

    @Override
    T createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        return this.createSpreadsheetHistoryHashToken(
                viewportSelection,
                PROPERTY_NAME
        );
    }

    abstract T createSpreadsheetHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                 final TextStylePropertyName<?> propertyName);
}
