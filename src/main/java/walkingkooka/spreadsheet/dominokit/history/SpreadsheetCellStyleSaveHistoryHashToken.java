package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

final public class SpreadsheetCellStyleSaveHistoryHashToken<T> extends SpreadsheetCellStyleHistoryHashToken {

    static <T> SpreadsheetCellStyleSaveHistoryHashToken<T> with(final SpreadsheetViewportSelection viewportSelection,
                                                                final TextStylePropertyName<T> propertyName,
                                                                final T propertyValue) {
        return new SpreadsheetCellStyleSaveHistoryHashToken<>(
                viewportSelection,
                propertyName,
                propertyValue
        );
    }

    private SpreadsheetCellStyleSaveHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                                     final TextStylePropertyName<T> propertyName,
                                                     final T propertyValue) {
        super(viewportSelection, propertyName);
        this.propertyValue = propertyValue;
    }

    public T propertyValue() {
        return this.propertyValue;
    }

    private final T propertyValue;

    @Override
    UrlFragment styleUrlFragment() {
        return SAVE.append(
                UrlFragment.with(String.valueOf(this.propertyValue()))
        );
    }
}
