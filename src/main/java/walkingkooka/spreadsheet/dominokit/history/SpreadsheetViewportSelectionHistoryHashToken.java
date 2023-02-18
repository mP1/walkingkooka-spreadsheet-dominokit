package walkingkooka.spreadsheet.dominokit.history;

import walkingkooka.net.UrlFragment;
import walkingkooka.spreadsheet.reference.SpreadsheetViewportSelection;

import java.util.Objects;

public abstract class SpreadsheetViewportSelectionHistoryHashToken extends SpreadsheetSelectionHistoryHashToken {

    SpreadsheetViewportSelectionHistoryHashToken(final SpreadsheetViewportSelection viewportSelection) {
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
