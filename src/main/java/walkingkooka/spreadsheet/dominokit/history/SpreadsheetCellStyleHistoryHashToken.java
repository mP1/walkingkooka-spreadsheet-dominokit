package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;
import walkingkooka.tree.text.TextStylePropertyName;

import java.util.Objects;

abstract public class SpreadsheetCellStyleHistoryHashToken extends SpreadsheetCellHistoryHashToken {

    SpreadsheetCellStyleHistoryHashToken(final SpreadsheetViewportSelection viewportSelection,
                                         final TextStylePropertyName<?> propertyName) {
        super(viewportSelection);

        this.propertyName = Objects.requireNonNull(propertyName, "propertyName");
    }

    public final TextStylePropertyName<?> propertyName() {
        return this.propertyName;
    }

    private final TextStylePropertyName<?> propertyName;

    @Override
    UrlFragment cellUrlFragment() {
        return STYLE.append(this.propertyName().urlFragment())
                .append(this.styleUrlFragment());
    }

    abstract UrlFragment styleUrlFragment();
}
