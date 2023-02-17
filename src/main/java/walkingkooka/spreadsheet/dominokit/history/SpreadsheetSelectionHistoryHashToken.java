package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

abstract public class SpreadsheetSelectionHistoryHashToken extends SpreadsheetHistoryHashToken {

    SpreadsheetSelectionHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
        super();
        this.viewportSelection = Objects.requireNonNull(viewportSelection, "viewportSelection");
    }

    private final SpreadsheetViewportSelection viewportSelection;

    @Override
    public final UrlFragment urlFragment() {
        return this.viewportSelection.urlFragment()
                .append(this.selectionUrlFragment());
    }

    abstract UrlFragment selectionUrlFragment();
}
